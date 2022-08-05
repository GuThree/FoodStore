package cn.edu.scnu.jedis.test;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

public class JedisTest {
	
	@Test
	public void test01(){
		Jedis jedis = new Jedis("192.168.126.151", 6379, 10000);
		jedis.set("num1", "100");
		System.out.println(jedis.get("num1"));
		System.out.println(jedis.decr("num1"));
		System.out.println(jedis.decrBy("num1", 10));
	}
	@Test
	public void test02(){
		//存取hash
//		Jedis jedis = new Jedis("192.168.126.151", 6379, 10000);
//		jedis.hset("user", "username", "aa");
//		jedis.hset("user", "password", "123");
//		System.out.println(jedis.hget("user", "username"));
//		System.out.println(jedis.hget("user", "password"));
		
		//存取List
//		Jedis jedis = new Jedis("192.168.126.151", 6379, 10000);
//		jedis.flushDB();
//		jedis.lpush("list01", "10", "20", "30", "40");
//		System.out.println(jedis.lrange("list01", 0, -1));
//		jedis.rpush("list01", "200", "300");
//		System.out.println(jedis.lrange("list01", 0, -1));
//		
//		jedis.linsert("list01", BinaryClient.LIST_POSITION.BEFORE, "200", "100");
//		jedis.linsert("list01", BinaryClient.LIST_POSITION.AFTER, "300", "400");
//		System.out.println(jedis.sort("list01"));
		
		//存取set
//		Jedis jedis = new Jedis("192.168.126.151", 6379, 10000);
//		jedis.sadd("favor", "dancing", "singing", "reading", "swimming");
//		System.out.println(jedis.smembers("favor"));
//		System.out.println(jedis.scard("favor"));
//		jedis.srem("favor", "dancing");
//		System.out.println(jedis.smembers("favor"));
		
		//存取ZSet
		Jedis jedis = new Jedis("192.168.126.151", 6379, 10000);
		jedis.zadd("english", 90, "jason");
		jedis.zadd("english", 70, "tom");
		jedis.zadd("english", 95, "timmy");
		
		System.out.println(jedis.zrange("english", 0, -1));
		System.out.println(jedis.zrangeByScoreWithScores("english", 60, 100));
		
		Set<redis.clients.jedis.Tuple> elements = jedis.zrevrangeWithScores("english", 0, -1);
		for(redis.clients.jedis.Tuple tuple : elements){
			System.out.println(tuple.getElement() + ":" + tuple.getScore());
		}
	}
	
	@Test
	public void test(){
		
//		String productId = "05e20c1a-0401-4c0a-82ab-6fb0f37db397";
//		System.out.println("用户请求查询商品详情，productId：" + productId);	
//		String productKey = "product_" + productId;
//		
//		Jedis jedis = new Jedis("192.168.126.151", 6379, 10000);
//		if(jedis.exists(productKey)){
//			System.out.println("获取缓存中的数据：" + jedis.get(productKey));
//		}else{
//			System.out.println("到数据库查询数据，productId=" + productId);
//			String productJson = "{'name':'荣耀Play5T','price':1199,'category':'手机数码'}";
//			System.out.println("将数据存入缓存，商品信息为：" + productJson);
//			jedis.set(productKey, productJson);
//		}
		Set<String> sentinelSet = new HashSet<String>();
		sentinelSet.add(new HostAndPort("192.168.126.151", 26379).toString());
		sentinelSet.add(new HostAndPort("192.168.126.151", 26380).toString());
		sentinelSet.add(new HostAndPort("192.168.126.151", 26381).toString());
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxTotal(200);
		JedisSentinelPool pool = new JedisSentinelPool("mymaster", sentinelSet, config);
		Jedis jedis = pool.getResource();
		System.out.println("当前正在服役的主节点" + pool.getCurrentHostMaster());
		String name = jedis.get("name");
		System.out.println("name" + name);
	}
}
