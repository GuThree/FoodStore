package cn.edu.scnu.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.easymall.common.pojo.Product;



public interface ProductMapper {
	//查询商品总数量
	Integer queryTotal();

	List<Product> queryByPage(@Param("start")Integer start, @Param("rows")Integer rows);

	Product queryById(String prodId);

	void productSave(Product product);

	void productUpdate(Product product);

	List<Product> queryByPageAndCategory(@Param("start")Integer start, @Param("rows")Integer rows, @Param("category")String category);

	void productDelete(Product product);



}
