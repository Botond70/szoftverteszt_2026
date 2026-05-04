package deik.pti.config;

import deik.pti.factory.WebDriverFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("deik.pti")
public class TestConfig {
    @Bean(destroyMethod = "closeWebDriver")
    public WebDriverFactory getWebDriverFactory() {
        return new WebDriverFactory();
    }
}
