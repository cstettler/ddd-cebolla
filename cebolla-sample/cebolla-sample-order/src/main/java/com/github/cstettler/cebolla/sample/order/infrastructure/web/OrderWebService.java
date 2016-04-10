package com.github.cstettler.cebolla.sample.order.infrastructure.web;

import com.github.cstettler.cebolla.sample.order.boundary.OrderApplicationService;
import com.github.cstettler.cebolla.sample.order.domain.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class OrderWebService {

  private final OrderApplicationService orderApplicationService;

  public OrderWebService(OrderApplicationService orderApplicationService) {
    this.orderApplicationService = orderApplicationService;
  }

  @ResponseBody
  @RequestMapping(path = "/place-order", method = GET)
  public String placeOrder(@RequestParam("userId") String userId, @RequestParam("productId") String productId, @RequestParam("quantity") int quantity) {
    Order order = this.orderApplicationService.placeOrder(userId, productId, quantity);
    String orderId = order.id();

    return orderId;
  }

  @ResponseBody
  @RequestMapping(path = "/check-order-status", method = GET)
  public String checkOrderStatus(@RequestParam("orderId") String orderId) {
    Order order = this.orderApplicationService.retrieveOrder(orderId);
    String orderStatus = order.status().name();

    return orderStatus;
  }

}
