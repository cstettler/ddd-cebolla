package com.github.cstettler.cebolla.sample.order;

import com.github.cstettler.cebolla.sample.application.test.BaseModuleIntegrationTest;
import org.junit.After;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import javax.inject.Inject;
import java.time.Instant;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.cstettler.cebolla.sample.application.test.RegexpMatcher.matches;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {
    OrderModuleConfiguration.class,
    OrderIntegrationTest.TestConfiguration.class
})
public class OrderIntegrationTest extends BaseModuleIntegrationTest {

  private static final String UUID_REGEXP_PATTERN = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

  @Inject
  private RecordingDomainEventHandler recordingDomainEventHandler;

  @After
  public void waitForAtLeastOneDomainEventBeingPublishedViaMessageBroker() throws TimeoutException {
    this.recordingDomainEventHandler.waitForRecordCount(1, 1000);
    this.recordingDomainEventHandler.reset();
  }

  @Test
  public void placeOrderReturnsOrderId() throws Exception {
    // arrange
    String userId = "cstettler";
    String productId = "01-2345-9";
    int quantity = 3;

    // act + assert
    mockMvc().perform(get("http://localhost:" + serverPort() + "/place-order?userId={userId}&productId={productId}&quantity={quantity}", userId, productId, quantity))
        .andExpect(status().is(200))
        .andExpect(content().string(matches(UUID_REGEXP_PATTERN)));
  }

  @Test
  public void checkOrderStatusReturnsOrderStatusOfExistingOrder() throws Exception {
    // arrange
    String userId = "cstettler";
    String productId = "01-2345-9";
    int quantity = 3;

    MvcResult result = mockMvc().perform(get("http://localhost:" + serverPort() + "/place-order?userId={userId}&productId={productId}&quantity={quantity}", userId, productId, quantity)).andReturn();
    String orderId = result.getResponse().getContentAsString();

    // act + assert
    mockMvc().perform(get("http://localhost:" + serverPort() + "/check-order-status?orderId={orderId}", orderId))
        .andExpect(status().is(200))
        .andExpect(content().string("PLACED"));
  }


  @Configuration
  static class TestConfiguration {

    @Bean
    public RecordingDomainEventHandler recordingDomainEventHandler() {
      return new RecordingDomainEventHandler();
    }

  }


  static class RecordingDomainEventHandler {

    private final AtomicInteger domainEventCount;

    public RecordingDomainEventHandler() {
      this.domainEventCount = new AtomicInteger(0);
    }

    @JmsListener(destination = "domainEventHandler")
    public void recordDomainEvent(String payload) {
      this.domainEventCount.incrementAndGet();
    }

    public void waitForRecordCount(int expectedRecordCount, long timeout) throws TimeoutException {
      Instant start = Instant.now();

      while (start.plus(timeout, MILLIS).isAfter(Instant.now())) {
        if (domainEventCount.get() >= expectedRecordCount) {
          return;
        }
      }

      throw new TimeoutException("expected number of " + expectedRecordCount + " domain event(s) not received within " + timeout + "ms");
    }

    public void reset() {
      this.domainEventCount.set(0);
    }

  }

}
