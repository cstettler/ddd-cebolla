package com.github.cstettler.cebolla.sample.order.boundary;

import com.github.cstettler.cebolla.sample.order.domain.Order;
import com.github.cstettler.cebolla.sample.order.domain.OrderRepository;

public class OrderApplicationService {

  private final OrderRepository orderRepository;

  public OrderApplicationService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  public Order placeOrder(String userId, String productId, int quantity) {
    Order order = new Order(userId, productId, quantity);
    this.orderRepository.addOrder(order);

    return order;
  }

  public Order retrieveOrder(String orderId) {
    return this.orderRepository.retrieveBy(orderId);
  }

}
