package com.github.cstettler.cebolla.sample.order;

import com.github.cstettler.cebolla.event.domain.DomainEventPublisher;
import com.github.cstettler.cebolla.event.infrastructure.NoOpDomainEventPublisher;
import com.github.cstettler.cebolla.sample.order.boundary.OrderApplicationService;
import com.github.cstettler.cebolla.sample.order.domain.OrderRepository;
import com.github.cstettler.cebolla.sample.order.infrastructure.persistence.InMemoryOrderRepository;
import com.github.cstettler.cebolla.sample.order.infrastructure.web.OrderWebService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderModuleConfiguration {

  @Bean
  public DomainEventPublisher domainEventPublisher() {
    return new NoOpDomainEventPublisher();
  }

  @Bean
  public OrderWebService orderWebService(OrderApplicationService orderApplicationService) {
    return new OrderWebService(orderApplicationService);
  }

  @Bean
  public OrderApplicationService orderApplicationService(DomainEventPublisher domainEventPublisher, OrderRepository orderRepository) {
    return new OrderApplicationService(domainEventPublisher, orderRepository);
  }

  @Bean
  public OrderRepository orderRepository() {
    return new InMemoryOrderRepository();
  }

}
