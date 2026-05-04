package deik.pti.pageObjects;

import deik.pti.factory.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommonPage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    public CommonPage(final WebDriverFactory factory) {
        this.driver = factory.getWebDriver();
        this.wait = new WebDriverWait(driver, 15);
        PageFactory.initElements(driver, this);
    }
}
