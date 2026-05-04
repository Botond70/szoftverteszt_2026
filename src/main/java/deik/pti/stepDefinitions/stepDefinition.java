package deik.pti.stepDefinitions;

import deik.pti.factory.WebDriverFactory;
import deik.pti.pageObjects.HomePage;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class stepDefinition {
    private final WebDriverFactory factory = new WebDriverFactory();
    private final HomePage homePage = new HomePage(factory);

    @Given("the Home page is opened")
    public void theHomePageIsOpened() {
        homePage.open();
    }

    @Then("a {string} gomb látható")
    public void isButtonVisible(String buttonText) {
        WebElement element = factory.getWebDriver().findElement(
            By.xpath("//button[contains(text(), '" + buttonText + "')] | //a[contains(text(), '" + buttonText + "')]")
        );
        Assert.assertTrue("Button/Link should be visible", element.isDisplayed());
    }

    @When("the language selector shows {string}")
    public void theLanguageSelectorShows(String expectedLanguage) {
        String actual = homePage.getLanguageSelectorText();
        Assert.assertTrue(
            "Expected language selector to show '" + expectedLanguage + "' but was '" + actual + "'",
            actual.contains(expectedLanguage)
        );
    }

    @Then("all Popular events are marked as English")
    public void allPopularEventsAreMarkedAsEnglish() {
        List<String> violations = homePage.getPopularCardsWithNonEnglishIndicator();
        Assert.assertTrue(
            "Non-English language indicators found in Popular section: " + String.join("; ", violations),
            violations.isEmpty()
        );
    }

    @After
    public void tearDown() {
        factory.closeWebDriver();
    }
}