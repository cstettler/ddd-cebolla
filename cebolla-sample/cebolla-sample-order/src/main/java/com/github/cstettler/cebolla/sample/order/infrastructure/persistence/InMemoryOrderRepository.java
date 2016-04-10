package com.github.cstettler.cebolla.sample.order.infrastructure.persistence;

import com.github.cstettler.cebolla.sample.order.domain.Order;
import com.github.cstettler.cebolla.sample.order.domain.OrderRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOrderRepository implements OrderRepository {

  private Map<String, Order> orders;

  public InMemoryOrderRepository() {
    this.orders = new ConcurrentHashMap<>();
  }

  @Override
  public void addOrder(Order order) {
    this.orders.put(order.id(), order);
  }

  @Override
  public Order retrieveBy(String orderId) {
    return this.orders.entrySet().stream()
        .filter((entry) -> entry.getKey().equals(orderId))
        .map((entry) -> entry.getValue())
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("no order found with id '" + orderId + "'"));
  }

}
