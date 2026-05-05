package deik.pti.pageObjects;

import deik.pti.factory.WebDriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class VideosPage extends CommonPage {
    private static final String VIDEOS_PAGE_URL = "https://wearecommunity.io/videos";

    private static final By TALKS_ROW = By.cssSelector(".evnt-talks-row");
    private static final By TALK_CARDS = By.cssSelector(".evnt-talks-row .evnt-talk-card");
    private static final By TALK_CARD_LINKS = By.cssSelector(".evnt-talks-row .evnt-talk-card a[href*='/talks']");
    private static final By CARD_TITLE = By.cssSelector(".evnt-talk-name h2, .evnt-talk-name h3");
    private static final By CARD_LANGUAGE_BADGE = By.cssSelector(".evnt-details-cell.language-cell .language");
    private static final By LANGUAGE_FILTER_TEXT = By.cssSelector("#filter_language .evnt-filter-text");

    public VideosPage(final WebDriverFactory factory) {
        super(factory);
    }

    public void open() {
        driver.get(VIDEOS_PAGE_URL);
        wait.until(ExpectedConditions.urlContains("/videos"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(TALKS_ROW));
    }

    public String getLanguageFilterSelection() {
        WebElement label = wait.until(ExpectedConditions.visibilityOfElementLocated(LANGUAGE_FILTER_TEXT));
        String text = label.getText().trim();
        if (!text.isEmpty() && !"Language".equalsIgnoreCase(text)) {
            return text;
        }

        WebElement container = label.findElement(By.xpath("./ancestor::div[contains(@class,'evnt-dropdown-filter')][1]"));
        List<WebElement> selectedLabels = container.findElements(
            By.cssSelector(".evnt-filter-menu input:checked + label")
        );
        if (!selectedLabels.isEmpty()) {
            String selected = selectedLabels.getFirst().getAttribute("data-value");
            return selected == null || selected.isBlank() ? selectedLabels.getFirst().getText().trim() : selected.trim();
        }

        return "";
    }

    public List<String> getPopularTalksWithNonEnglishIndicator() {
        List<WebElement> cards = waitForPopularTalkCards();
        List<String> violations = new ArrayList<>();

        if (cards.isEmpty()) {
            violations.add("No talk cards found in the Popular Talks section");
            return violations;
        }

        for (WebElement card : cards) {
            String indicator = findLanguageIndicator(card);
            String title = findCardTitle(card);

            if (!"En".equalsIgnoreCase(indicator)) {
                violations.add("Found non-English talk: '" + title + "' with language: " + indicator);
            }
        }

        return violations;
    }

    private List<WebElement> waitForPopularTalkCards() {
        WebElement section = wait.until(ExpectedConditions.visibilityOfElementLocated(TALKS_ROW));
        scrollIntoView(section);

        List<WebElement> cards = waitForCards(TALK_CARDS);
        if (cards.isEmpty()) {
            cards = waitForCards(TALK_CARD_LINKS);
        }
        return cards;
    }

    private List<WebElement> waitForCards(By locator) {
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        } catch (TimeoutException ignored) {
            return List.of();
        }
        return driver.findElements(locator).stream().filter(WebElement::isDisplayed).collect(Collectors.toList());
    }

    private String findLanguageIndicator(WebElement card) {
        for (WebElement badge : card.findElements(CARD_LANGUAGE_BADGE)) {
            String text = badge.getText().trim();
            if (isLanguageCode(text)) {
                return normalizeLanguage(text);
            }
        }

        return extractIndicatorFromText(card.getText());
    }

    private boolean isLanguageCode(String text) {
        return text != null && text.matches("(?i)^[a-z]{2,3}$");
    }

    private String normalizeLanguage(String text) {
        return text == null ? "" : text.substring(0, 1).toUpperCase(Locale.ROOT) + text.substring(1).toLowerCase(Locale.ROOT);
    }

    private String extractIndicatorFromText(String text) {
        String normalized = text == null ? "" : text;
        if (normalized.matches("(?s).*\\bESP\\b.*")) {
            return "ESP";
        }
        if (normalized.matches("(?s).*\\bRu\\b.*")) {
            return "Ru";
        }
        if (normalized.matches("(?s).*\\bEn\\b.*")) {
            return "En";
        }
        return "MISSING";
    }

    private String findCardTitle(WebElement card) {
        List<WebElement> titles = card.findElements(CARD_TITLE);
        if (!titles.isEmpty()) {
            return titles.getFirst().getText().trim();
        }

        String text = card.getText().trim();
        return text.isEmpty() ? "(untitled talk)" : text.split("\\R", 2)[0].trim();
    }

    private void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }
}
