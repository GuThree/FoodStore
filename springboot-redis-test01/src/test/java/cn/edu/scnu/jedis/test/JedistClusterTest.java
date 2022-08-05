package cn.edu.scnu.jedis.test;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class JedistClusterTest {

	@Test
	public void test() {
		Set<HostAndPort> clusterSet = new HashSet<HostAndPort>();
		clusterSet.add(new HostAndPort("192.168.126.151", 8005));
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxTotal(200);
		// 构造一个JedisCluster
		JedisCluster cluster = new JedisCluster(clusterSet, config);
		cluster.set("gender", "male");
		System.out.println(cluster.get("gender"));

	}
}
