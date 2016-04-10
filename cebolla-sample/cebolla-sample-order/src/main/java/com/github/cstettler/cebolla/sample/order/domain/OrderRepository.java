package com.github.cstettler.cebolla.sample.order.domain;

public interface OrderRepository {

  void addOrder(Order order);

  Order retrieveBy(String orderId);

}
