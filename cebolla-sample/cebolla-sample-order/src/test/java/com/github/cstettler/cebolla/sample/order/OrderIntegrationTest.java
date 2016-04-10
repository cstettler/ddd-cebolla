package com.github.cstettler.cebolla.sample.order;

import com.github.cstettler.cebolla.sample.application.test.BaseModuleIntegrationTest;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import static com.github.cstettler.cebolla.sample.application.test.RegexpMatcher.matches;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = OrderModuleConfiguration.class)
public class OrderIntegrationTest extends BaseModuleIntegrationTest {

  private static final String UUID_REGEXP_PATTERN = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

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

}
