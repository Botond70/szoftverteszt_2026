package deik.pti.config;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@CucumberOptions(glue = "deik.pti")
@ContextConfiguration(classes = TestConfig.class)
public class CucumberSpringConfiguration {
}
