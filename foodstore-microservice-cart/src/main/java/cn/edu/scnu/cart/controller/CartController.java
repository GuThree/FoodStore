package cn.edu.scnu.cart.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easymall.common.pojo.Cart;
import com.easymall.common.vo.SysResult;

import cn.edu.scnu.cart.service.CartService;

@RestController
@RequestMapping("cart/manage")
public class CartController {
	@Autowired
	private CartService cartService;
	
	@RequestMapping("query")
	public List<Cart> queryMyCart(String userId){
		if(!StringUtils.isNotEmpty(userId)){
			return null;
		}
		return this.cartService.queryMyCart(userId);
	}
	@RequestMapping("save")
	public SysResult cartSave(Cart cart){
//		System.out.println(cart);
		try{
			this.cartService.cartSave(cart);
			return SysResult.ok();
		}catch(Exception e){
			e.printStackTrace();
			return SysResult.build(201, "", null);
		}
	}
	@RequestMapping("update")
	public SysResult updateNum(Cart cart){
		try{
			this.cartService.updateNum(cart);
			return SysResult.ok();
		}catch(Exception e){
			e.printStackTrace();
			return SysResult.build(201, "", null);
		}
	}
	@RequestMapping("delete")
	public SysResult deleteCart(Cart cart){
		try{
			this.cartService.deleteCart(cart);
			return SysResult.ok();
		}catch(Exception e){
			e.printStackTrace();
			return SysResult.build(201, "", null);
		}
	}
	
}
