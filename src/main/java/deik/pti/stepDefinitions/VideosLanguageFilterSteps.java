package deik.pti.stepDefinitions;

import deik.pti.factory.WebDriverFactory;
import deik.pti.pageObjects.VideosPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.List;

public class VideosLanguageFilterSteps {
    private final WebDriverFactory factory = new WebDriverFactory();
    private final VideosPage videosPage = new VideosPage(factory);

    @When("the Videos page is opened")
    public void theVideosPageIsOpened() {
        videosPage.open();
    }

    @Then("the Language filter shows {string}")
    public void theLanguageFilterShows(String expectedLanguage) {
        String actual = videosPage.getLanguageFilterSelection();
        Assert.assertEquals(
            "Expected Language filter to show '" + expectedLanguage + "' but was '" + actual + "'",
            expectedLanguage,
            actual
        );
    }

    @Then("all Popular Talks are marked as English")
    public void allPopularTalksAreMarkedAsEnglish() {
        List<String> violations = videosPage.getPopularTalksWithNonEnglishIndicator();
        Assert.assertTrue(
            "Non-English language indicators found in Popular Talks: " + String.join("; ", violations),
            violations.isEmpty()
        );
    }
}

