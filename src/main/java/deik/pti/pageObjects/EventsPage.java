package deik.pti.pageObjects;

import deik.pti.factory.WebDriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventsPage extends CommonPage {

    private static final String EVENTS_PAGE_URL = "https://wearecommunity.io/events";

    private static final By EVENTS_TAB_LIST = By.cssSelector(".evnt-tabs-list");
    private static final By ACTIVE_TAB = By.cssSelector("a.evnt-tab-link.nav-link.active");
    private static final By ACTIVE_TAB_COUNTER = By.cssSelector("a.evnt-tab-link.nav-link.active .evnt-tab-counter");

    public EventsPage(final WebDriverFactory factory) {
        super(factory);
    }

    public void open() {
        driver.get(EVENTS_PAGE_URL);
        wait.until(ExpectedConditions.urlContains("/events"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(EVENTS_TAB_LIST));
    }

    public String getActiveTabText() {
        WebElement activeTab = wait.until(ExpectedConditions.visibilityOfElementLocated(ACTIVE_TAB));
        String fullText = activeTab.getText().trim();
        List<WebElement> counters = activeTab.findElements(By.cssSelector(".evnt-tab-counter"));
        if (!counters.isEmpty()) {
            String counterText = counters.getFirst().getText().trim();
            return fullText.replace(counterText, "").trim();
        }
        return fullText;
    }

    public int getActiveTabEventCount() {
        List<WebElement> counters = driver.findElements(ACTIVE_TAB_COUNTER);
        if (counters.isEmpty()) {
            return -1;
        }
        String countText = counters.getFirst().getText().trim().replaceAll("[^0-9]", "");
        return countText.isEmpty() ? -1 : Integer.parseInt(countText);
    }
}
