package com.github.cstettler.cebolla.event.domain;

public interface DomainEventPublisher {

  void publish(BaseDomainEvent domainEvent);

}
