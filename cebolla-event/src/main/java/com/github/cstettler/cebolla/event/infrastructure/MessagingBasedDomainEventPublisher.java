package com.github.cstettler.cebolla.event.infrastructure;

import com.github.cstettler.cebolla.event.domain.BaseDomainEvent;
import com.github.cstettler.cebolla.event.domain.DomainEventPublisher;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.support.GenericMessage;

public class MessagingBasedDomainEventPublisher implements DomainEventPublisher {

  private final MessagingTemplate messagingTemplate;

  public MessagingBasedDomainEventPublisher(MessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  @Override
  public void publish(BaseDomainEvent domainEvent) {
    this.messagingTemplate.send("domainEventPublisherChannel", new GenericMessage<>(domainEvent.id()));
  }

}
