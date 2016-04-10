package com.github.cstettler.cebolla.sample.order.domain;

import java.util.UUID;

import static com.github.cstettler.cebolla.sample.order.domain.Order.OrderStatus.PLACED;

public class Order {

  private final String id;
  private final String userId;
  private final String productId;
  private final int quantity;
  private final OrderStatus status;

  public Order(String userId, String productId, int quantity) {
    this.userId = userId;
    this.productId = productId;
    this.quantity = quantity;
    this.id = UUID.randomUUID().toString();
    this.status = PLACED;
  }

  public String id() {
    return id;
  }

  public String userId() {
    return userId;
  }

  public String productId() {
    return productId;
  }

  public int quantity() {
    return quantity;
  }

  public OrderStatus status() {
    return status;
  }


  public enum OrderStatus {

    PLACED, SHIPPED
  }

}
