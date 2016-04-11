package com.github.cstettler.cebolla.event;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.BridgeFrom;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.jdbc.store.JdbcChannelMessageStore;
import org.springframework.integration.jdbc.store.channel.HsqlChannelMessageStoreQueryProvider;
import org.springframework.integration.jms.JmsSendingMessageHandler;
import org.springframework.integration.store.ChannelMessageStore;
import org.springframework.integration.store.MessageGroupQueue;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageChannel;

import javax.jms.ConnectionFactory;
import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

// TODO add support for properly serializing and deserializing domain events
// TODO add transaction handling using best-effort one-phase commit approach
@Configuration
@EnableIntegration
@EnableJms
public class EventModuleConfiguration {

  @Bean
  public QueueChannel domainEventPublisherChannel(ChannelMessageStore publishedDomainEventMessageStore) {
    return new QueueChannel(new MessageGroupQueue(publishedDomainEventMessageStore, "domainEventGroupId"));
  }

  @Bean
  public ChannelMessageStore domainEventMessageStore(DataSource dataSource) {
    JdbcChannelMessageStore domainEventMessageStore = new JdbcChannelMessageStore(dataSource);
    domainEventMessageStore.setChannelMessageStoreQueryProvider(new HsqlChannelMessageStoreQueryProvider());

    return domainEventMessageStore;
  }

  @Bean
  @BridgeFrom(value = "domainEventPublisherChannel", poller = @Poller(maxMessagesPerPoll = "-1", fixedDelay = "1000"))
  public MessageChannel dispatchBufferedDomainEventsToJmsTopicOutboundChannel(ConnectionFactory connectionFactory) {
    JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
    jmsTemplate.setPubSubDomain(true);

    JmsSendingMessageHandler jmsSendingMessageHandler = new JmsSendingMessageHandler(jmsTemplate);
    jmsSendingMessageHandler.setDestination(new ActiveMQTopic("domainEventHandler"));

    DirectChannel domainEventOutboundChannel = new DirectChannel();
    domainEventOutboundChannel.subscribe(jmsSendingMessageHandler);

    return domainEventOutboundChannel;
  }

  @Bean
  public MessageChannel errorChannel() {
    DirectChannel errorChannel = new DirectChannel();
    errorChannel.subscribe((message) -> {
      throw new IllegalStateException("message handling failed : " + message);
    });

    return errorChannel;
  }

  @Bean
  public JmsListenerContainerFactory<?> jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
    DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
    defaultJmsListenerContainerFactory.setConnectionFactory(connectionFactory);
    defaultJmsListenerContainerFactory.setPubSubDomain(true);

    return defaultJmsListenerContainerFactory;
  }

  @Bean
  public ConnectionFactory connectionFactory() {
    return new ActiveMQConnectionFactory("vm://embedded?broker.persistent=false");
  }

  @Bean
  public MessagingTemplate messagingTemplate() {
    return new MessagingTemplate();
  }

  @Bean
  public DataSource dataSource() {
    return new EmbeddedDatabaseBuilder()
        .setType(HSQL)
        .addScript("classpath:/org/springframework/integration/jdbc/store/channel/schema-hsql.sql")
        .build();
  }

}
