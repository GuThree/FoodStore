package cn.edu.scnu.cart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.easymall.common.pojo.Cart;
import com.easymall.common.pojo.Product;

import cn.edu.scnu.cart.mapper.CartMapper;

@Service
public class CartService {
	@Autowired
	private CartMapper cartMapper;
	@Autowired
	private RestTemplate restTemplate;
	
	public List<Cart> queryMyCart(String userId) {
		// TODO Auto-generated method stub
		return this.cartMapper.queryMyCart(userId);
	}

	public void cartSave(Cart cart) {
		//判断购物车是否存在该商品
		Cart exist = this.cartMapper.queryOne(cart);
		System.out.println("--------------");
		System.out.println(cart.getProductId());
		
		if(exist != null){
			exist.setNum(exist.getNum() + cart.getNum());
			this.cartMapper.updateNum(exist);
		}else{
			
			Product product = this.restTemplate.getForObject("http://productservice/product/manage/item/" 
					+ cart.getProductId(), Product.class);
			System.out.println(product);
			
//			System.out.println(product.getProductPrice());
			
			cart.setProductPrice(product.getProductPrice());
			
			
			
			cart.setProductName(product.getProductName());
			cart.setProductImage(product.getProductImgurl());
			this.cartMapper.saveCart(cart);
		}
		
	}

	public void updateNum(Cart cart) {
		this.cartMapper.updateNum(cart);
		
	}

	public void deleteCart(Cart cart) {
		this.cartMapper.deleteCart(cart);
		
	}
}
