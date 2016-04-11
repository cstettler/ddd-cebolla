package com.github.cstettler.cebolla.sample.order.domain;

import com.github.cstettler.cebolla.event.domain.BaseDomainEvent;

public class OrderPlaced extends BaseDomainEvent {

  private final String userId;
  private final String orderId;

  private OrderPlaced(String userId, String orderId) {
    this.userId = userId;
    this.orderId = orderId;
  }

  public String userId() {
    return userId;
  }

  public String orderId() {
    return orderId;
  }

  public static OrderPlaced orderPlaced(String userId, String orderId) {
    return new OrderPlaced(userId, orderId);
  }

}
