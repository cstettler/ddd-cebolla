package com.github.cstettler.cebolla.sample.order.boundary;

import com.github.cstettler.cebolla.event.domain.DomainEventPublisher;
import com.github.cstettler.cebolla.sample.order.domain.Order;
import com.github.cstettler.cebolla.sample.order.domain.OrderRepository;

import static com.github.cstettler.cebolla.sample.order.domain.OrderPlaced.orderPlaced;

public class OrderApplicationService {

  private final DomainEventPublisher domainEventPublisher;
  private final OrderRepository orderRepository;

  public OrderApplicationService(DomainEventPublisher domainEventPublisher, OrderRepository orderRepository) {
    this.domainEventPublisher = domainEventPublisher;
    this.orderRepository = orderRepository;
  }

  public Order placeOrder(String userId, String productId, int quantity) {
    Order order = new Order(userId, productId, quantity);
    this.orderRepository.addOrder(order);
    this.domainEventPublisher.publish(orderPlaced(userId, order.id()));

    return order;
  }

  public Order retrieveOrder(String orderId) {
    return this.orderRepository.retrieveBy(orderId);
  }

}
