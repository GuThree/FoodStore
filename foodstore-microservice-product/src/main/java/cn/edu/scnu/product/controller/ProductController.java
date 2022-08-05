package cn.edu.scnu.product.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easymall.common.pojo.Product;
import com.easymall.common.utils.MapperUtil;
import com.easymall.common.vo.EasyUIResult;
import com.easymall.common.vo.SysResult;
import com.fasterxml.jackson.core.JsonProcessingException;

import cn.edu.scnu.product.service.ProductService;

@RestController
public class ProductController {

	@Autowired
	private ProductService productService;
	
	
	@RequestMapping("/product/manage/pageManage")
	public EasyUIResult productPageQuery(Integer page, Integer rows){
		EasyUIResult result = this.productService.productPageQuery(page, rows);
		return result;
	}
	
	@RequestMapping("/product/manage/pageOtherManage")
	public EasyUIResult productPageOtherQuery(Integer page, Integer rows, String category){
		EasyUIResult result = this.productService.productPageOtherQuery(page, rows, category);
		return result;
	}
	
	@RequestMapping("/product/manage/item/{prodId}")
	public String queryById(@PathVariable String prodId){
		
		Product product = this.productService.queryById(prodId);
		System.out.println("product: " + product);
		String productJson;
		try {
			productJson = MapperUtil.MP.writeValueAsString(product);
			return productJson;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
		
	}
	
	//商品新增
	@RequestMapping("/product/manage/save")
	public SysResult productSave(Product product){
		try{
			this.productService.productSave(product);
			return SysResult.ok();
		}catch (Exception e){
			e.printStackTrace();
			return SysResult.build(201, e.getMessage(), null);
		}
	}
	
	//商品更新
	@RequestMapping("/product/manage/update")
	public SysResult productUpdate(Product product){
		try {
			this.productService.productUpdate(product);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, e.getMessage(), null);
		}
	}
	
	//商品删除
	@RequestMapping("/product/manage/delete")
	public SysResult productDelete(Product product){
		try {
			this.productService.productDelete(product);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, e.getMessage(), null);
		}
	}
	
}
