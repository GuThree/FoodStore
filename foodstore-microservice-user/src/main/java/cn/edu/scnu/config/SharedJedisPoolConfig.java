//package cn.edu.scnu.config;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import redis.clients.jedis.JedisShardInfo;
//import redis.clients.jedis.ShardedJedisPool;
//
//@Configuration
//@ConfigurationProperties(prefix="spring.redis.shardedpool")
//public class SharedJedisPoolConfig {
//	private List<String> nodes;
//	private Integer maxTotal;
//	private Integer maxIdle;
//	private Integer minIdle;
//	
//	@Bean
//	public ShardedJedisPool sjPoolInit(){
//		//连接池初始化方法
//		List<JedisShardInfo> infoList = new ArrayList<JedisShardInfo>();
//		for(String node : nodes){
//			String ip = node.split(":")[0];
//			int port = Integer.parseInt(node.split(":")[1]);
//			infoList.add(new JedisShardInfo(ip, port));
//		}
//		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
//		config.setMaxTotal(maxTotal);
//		config.setMaxIdle(maxIdle);
//		config.setMinIdle(minIdle);
//		return new ShardedJedisPool(config, infoList);	
//	}
//
//	public List<String> getNodes() {
//		return nodes;
//	}
//
//	public void setNodes(List<String> nodes) {
//		this.nodes = nodes;
//	}
//
//	public Integer getMaxTotal() {
//		return maxTotal;
//	}
//
//	public void setMaxTotal(Integer maxTotal) {
//		this.maxTotal = maxTotal;
//	}
//
//	public Integer getMaxIdle() {
//		return maxIdle;
//	}
//
//	public void setMaxIdle(Integer maxIdle) {
//		this.maxIdle = maxIdle;
//	}
//
//	public Integer getMinIdle() {
//		return minIdle;
//	}
//
//	public void setMinIdle(Integer minIdle) {
//		this.minIdle = minIdle;
//	}
//	
//	
//	
//}
