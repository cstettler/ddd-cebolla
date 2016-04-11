package com.github.cstettler.cebolla.event.infrastructure;

import com.github.cstettler.cebolla.event.domain.BaseDomainEvent;
import com.github.cstettler.cebolla.event.domain.DomainEventPublisher;

public class NoOpDomainEventPublisher implements DomainEventPublisher {

  @Override
  public void publish(BaseDomainEvent domainEvent) {
  }

}
