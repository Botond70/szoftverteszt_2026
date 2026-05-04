package deik.pti.stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import deik.pti.factory.WebDriverFactory;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

public class stepDefinition {
    @Autowired
    private WebDriverFactory factory;


    @Given("the Home page is opened")
    public void theHomePageIsOpened() {
        factory.getWebDriver().get("https://wearecommunity.io/");
    }

    @Then("a {string} gomb látható")
    public void isButtonVisible(String buttonText) {
        WebElement element = factory.getWebDriver().findElement(
            By.xpath("//button[contains(text(), '" + buttonText + "')] | //a[contains(text(), '" + buttonText + "')]")
        );
        Assert.assertTrue("Button/Link should be visible", element.isDisplayed());
    }
}