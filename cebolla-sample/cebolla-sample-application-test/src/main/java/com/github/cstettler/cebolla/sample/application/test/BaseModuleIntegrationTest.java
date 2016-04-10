package com.github.cstettler.cebolla.sample.application.test;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerPropertiesAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringApplicationConfiguration(classes = {
    EmbeddedServletContainerAutoConfiguration.class,
    ServerPropertiesAutoConfiguration.class,
    PropertyPlaceholderAutoConfiguration.class
})
@WebIntegrationTest(randomPort = true)
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class BaseModuleIntegrationTest {

  @Inject
  private WebApplicationContext webApplicationContext;

  @Value("${local.server.port}")
  private int serverPort;

  private MockMvc mockMvc;

  @Before
  public void setupMockMvc() {
    this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
  }

  protected MockMvc mockMvc() {
    return this.mockMvc;
  }

  protected int serverPort() {
    return this.serverPort;
  }

}
