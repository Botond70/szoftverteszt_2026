package deik.pti.factory;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverFactory {
    public static WebDriver webDriver;

    public WebDriver getWebDriver() {
        if (webDriver == null) {
            webDriver = setupWebDriver();
        }
        return webDriver;
    }

    private WebDriver setupWebDriver() {
        WebDriverManager.chromedriver().setup();
        var options = new ChromeOptions()
                .addArguments("--no-sandbox")
                .addArguments("--remote-allow-origins")
                .addArguments("--start-maximized");
        return new ChromeDriver(options);
    }

    public void closeWebDriver() {
        if (webDriver != null) {
            webDriver.close();
            webDriver.quit();
            webDriver = null;
        }
    }
}
