package deik.pti.stepDefinitions;

import deik.pti.factory.WebDriverFactory;
import deik.pti.pageObjects.CommunitiesPage;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class CommunitiesLanguageFilterSteps {

    private final WebDriverFactory factory = new WebDriverFactory();
    private final CommunitiesPage communitiesPage = new CommunitiesPage(factory);

    @Given("the Communities page is opened")
    public void theCommunitiesPageIsOpened() {
        communitiesPage.open();
    }

    @When("the user opens the language filter dropdown")
    public void theUserOpensTheLanguageFilterDropdown() {
        // az oldal betöltése után a CommunitiesPage kezeli a dropdown megnyitást
    }

    @Then("English is the first option in the language filter list")
    public void englishIsTheFirstOptionInTheLanguageFilterList() {
        String firstOption = communitiesPage.getFirstLanguageFilterOption();
        Assert.assertEquals(
            "Expected 'English' to be the first language option when UI is in English, but was: '" + firstOption + "'",
            "English",
            firstOption
        );
    }

    @After
    public void tearDown() {
        factory.closeWebDriver();
    }
}
