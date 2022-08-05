package cn.edu.scnu.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easymall.common.pojo.Order;
import com.easymall.common.vo.SysResult;

import cn.edu.scnu.order.service.OrderService;

@RestController
@RequestMapping("/order/manage")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
	
	@RequestMapping("save")
	public SysResult saveOrder(Order order){
		//订单增加
		try{
			this.orderService.saveOrder(order);
			return SysResult.ok();
		}catch (Exception e){
			e.printStackTrace();
			return SysResult.build(201, e.getMessage(), null);
		}
	}
	@RequestMapping("query/{userId}")
	public List<Order> queryMyOrder(@PathVariable String userId) {
	    return orderService.queryMyOrder(userId);
	}
	@RequestMapping("delete/{orderId}")
	public SysResult deleteOrder(@PathVariable String orderId) {
	    try {
	        orderService.deleteOrder(orderId);
	        return SysResult.ok();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return SysResult.build(201, "", null);
	    }
	}
}
