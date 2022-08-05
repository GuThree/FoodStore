package cn.edu.scnu.order.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easymall.common.pojo.Order;

import cn.edu.scnu.order.mapper.OrderMapper;

@Service
public class OrderService {
	@Autowired
	private OrderMapper orderMapper;

	public void saveOrder(Order order) {
		order.setOrderId(UUID.randomUUID().toString());
		order.setOrderTime(new Date());
		this.orderMapper.saveOrder(order);
		
	}

	public List<Order> queryMyOrder(String userId) {
		// TODO Auto-generated method stub
		return orderMapper.queryOrder(userId);
	}

	public void deleteOrder(String orderId) {
		// TODO Auto-generated method stub
		orderMapper.deleteOrder(orderId);
	}
}
