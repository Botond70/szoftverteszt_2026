package deik.pti.stepDefinitions;

import deik.pti.factory.WebDriverFactory;
import deik.pti.pageObjects.EventsPage;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;

public class EventsUpcomingTabSteps {

    private final WebDriverFactory factory = new WebDriverFactory();
    private final EventsPage eventsPage = new EventsPage(factory);

    @Given("the Events page is opened")
    public void theEventsPageIsOpened() {
        eventsPage.open();
    }

    @Then("the Upcoming events tab is active")
    public void theUpcomingEventsTabIsActive() {
        String activeTab = eventsPage.getActiveTabText();
        Assert.assertTrue(
            "Expected the active tab to contain 'Upcoming' but was: '" + activeTab + "'",
            activeTab.toLowerCase().contains("upcoming")
        );
    }

    @Then("the Upcoming events tab shows a non-negative event count")
    public void theUpcomingEventsTabShowsNonNegativeCount() {
        int count = eventsPage.getActiveTabEventCount();
        Assert.assertTrue(
            "Expected the Upcoming events tab count to be non-negative, but count was: " + count,
            count >= 0
        );
    }

    @After
    public void tearDown() {
        factory.closeWebDriver();
    }
}
