package com.github.cstettler.cebolla.event.domain;

import java.util.UUID;

public abstract class BaseDomainEvent {

  private String id;

  public BaseDomainEvent() {
    this.id = UUID.randomUUID().toString();
  }

  public String id() {
    return id;
  }

}
