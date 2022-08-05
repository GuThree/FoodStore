package cn.edu.scnu.product.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.easymall.common.pojo.Product;
import com.easymall.common.utils.MapperUtil;
import com.easymall.common.vo.EasyUIResult;

import cn.edu.scnu.product.mapper.ProductMapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;


@Service
public class ProductService {

	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private JedisCluster jedis;

	
	public EasyUIResult productPageQuery(Integer page, Integer rows){
		EasyUIResult result = new EasyUIResult();
		Integer total = this.productMapper.queryTotal();
		Integer start = (page - 1) * rows;
		List<Product> pList = this.productMapper.queryByPage(start, rows);
		result.setTotal(total);
		result.setRows(pList);
		return result;
	}
	
	public EasyUIResult productPageOtherQuery(Integer page, Integer rows, String category){
		EasyUIResult result = new EasyUIResult();
		Integer total = this.productMapper.queryTotal();
		Integer start = (page - 1) * rows;
		List<Product> pList = this.productMapper.queryByPageAndCategory(start, rows, category);
		result.setTotal(total);
		result.setRows(pList);
		return result;
	}

//	public String queryById(String prodId) {
////		return this.productMapper.queryById(prodId);
//		
//		String redisServerIP = "192.168.126.151";
//		Integer redisPort = 6379;
//		System.out.println("用户请求查询商品详情，productId：" + prodId);	
//		String productKey = "product_" + prodId;
//		
//		Jedis jedis = new Jedis(redisServerIP, redisPort, 10000);
//		if(jedis.exists(productKey)){
//			System.out.println("获取缓存中的数据：" + jedis.get(productKey));
//			return jedis.get(productKey);
//		}else{
//			
//			Product product = this.productMapper.queryById(prodId);
//			// jackson代码.将product对象转化成json
//			ObjectMapper mapper = new ObjectMapper();
//			String productJson;
//			try {
//				productJson = mapper.writeValueAsString(product);
//			} catch (Exception e) {
//				e.printStackTrace();
//				return "";
//			}
//			
//			System.out.println("到数据库查询数据，productId=" + prodId);
//			System.out.println("将数据存入缓存，商品信息为：" + productJson);
//			jedis.set(productKey, productJson);
//			return jedis.get(productKey);
//		}	
//	}
	public Product queryById(String productId){
		String productKey = "product_query_" + productId;
		System.out.println(productKey);
		String lock = "product_update" + productId + ".lock";
		try{
			if(jedis.exists(lock)){
				return this.productMapper.queryById(productId);
			}
			System.out.println("是否存在： " + jedis.exists(productKey));
			if(jedis.exists(productKey)){
				String productJson = jedis.get(productKey);
				Product product = MapperUtil.MP.readValue(productJson, Product.class);
				return product;
			}else{
				
				Product product = this.productMapper.queryById(productId);
				System.out.println("是否存在shangp： " + product);
				String productJson = MapperUtil.MP.writeValueAsString(product);
				jedis.setex(productKey, 60*60*24*2, productJson);
				return product;
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public void productSave(Product product) {
		product.setProductId(UUID.randomUUID().toString());
		this.productMapper.productSave(product);
		
	}

	public void productUpdate(Product product) {
		String lock = "product_update" + product.getProductId() + ".lock";
		Long leftTime = jedis.ttl("product_query_" + product.getProductId());
		jedis.setex(lock, Integer.parseInt(leftTime+""), "");
		jedis.del("product_query_" + product.getProductId());
		this.productMapper.productUpdate(product);
		jedis.del(lock);
	}

	public void productDelete(Product product) {
		// TODO Auto-generated method stub
		this.productMapper.productDelete(product);
	}
}
