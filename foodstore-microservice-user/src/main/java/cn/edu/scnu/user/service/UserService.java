package cn.edu.scnu.user.service;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easymall.common.pojo.User;
import com.easymall.common.utils.MD5Util;
import com.easymall.common.utils.MapperUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.edu.scnu.user.mapper.UserMapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;
//	@Autowired
//	private ShardedJedisPool pool;
	private ObjectMapper mapper = MapperUtil.MP;
	@Autowired
	private JedisCluster jedis;

	public Integer checkUsername(String userName) {

		return this.userMapper.queryUsername(userName);
	}

	public void userSave(User user) {
		user.setUserId(UUID.randomUUID().toString());
		// 给密码加密
		user.setUserPassword(MD5Util.md5(user.getUserPassword()));
		this.userMapper.userSave(user);
	}

	public String doLogin(User user) {
		// 获取连接池对象
		// ShardedJedis jedis = pool.getResource();
		try {
			// 对password做加密操作
			user.setUserPassword(MD5Util.md5(user.getUserPassword()));
			// 利用user数据查询数据库，判断登录是否合法
			User exist = this.userMapper.queryUserByNameAndPassword(user);

			if (exist == null) {
				return "";
			} else {
				String ticket = UUID.randomUUID().toString();
				String userJson;
				userJson = mapper.writeValueAsString(exist);
				//判断当前用户是否曾经有人登录过
//				String existTicket=jedis.get("user_logined_"+exist.getUserId());
				//顶替实现.不允许前一个登录的人ticket存在
//				if(StringUtils.isNotEmpty(existTicket)){
//					jedis.del(existTicket);
//				}
				//定义当前客户端登录的信息 userId:ticket
//				jedis.setex("user_logined_"+exist.getUserId(), 60*30,ticket);
				//实现一个账户最多3个用户同时登录
				List<String> existTicketList = jedis.lrange("user_logined_"+exist.getUserId(), 0, -1);
				System.out.println("user_logined_"+exist.getUserId());
				if(existTicketList.size() >= 2){
					System.out.println(existTicketList.size());
					jedis.del(jedis.rpop("user_logined_"+exist.getUserId()));
					
				}
				
				jedis.lpush("user_logined_"+exist.getUserId(), ticket);

				// 用户登录超时分钟
				jedis.setex(ticket, 30, userJson);
				return ticket;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 从redis中获取userJson
	public String queryUserJson(String ticket) {
		// ShardedJedis jedis = pool.getResource();
		String userJson = "";
		try {
			// 首先判断超时剩余时间
			Long leftTime = jedis.pttl(ticket);
			// 少于10分钟,延长5分钟
			if (leftTime < 1000 * 60 * 10l) {
				jedis.pexpire(ticket, leftTime + 1000 * 60 * 5);
			}
			userJson = jedis.get(ticket);
			return userJson;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public void delUserRedis(String key){
//		System.out.println(key);
		this.jedis.del(key);
		
	}

}
