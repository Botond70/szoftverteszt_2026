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
import java.util.stream.Collectors;

@Component
public class HomePage extends CommonPage {
    public HomePage(final WebDriverFactory factory) {
        super(factory);
    }

    private static final String HOME_PAGE_URL = "https://wearecommunity.io/";
    private static final By POPULAR_SECTION = By.cssSelector(".evnt-simple-promo-cards-container");
    private static final By POPULAR_CARDS = By.cssSelector(".evnt-simple-promo-cards-container .evnt-promo-card");
    private static final By POPULAR_CARD_LINKS = By.cssSelector(".evnt-simple-promo-cards-container a[href*='/events']");
    private static final By CARD_TITLE = By.cssSelector(".evnt-event-name h2");
    private static final By CARD_LANGUAGE_INDICATOR = By.cssSelector(".evnt-language");

    public void open() {
        driver.get(HOME_PAGE_URL);
    }

    public String getLanguageSelectorText() {
        WebElement element = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#languageDropdown span"))
        );
        String text = element.getText().trim();
        return text.isEmpty() ? element.getAttribute("textContent").trim() : text;
    }

    public List<String> getPopularCardsWithNonEnglishIndicator() {
        List<WebElement> cards = waitForPopularCards();
        List<String> violations = new ArrayList<>();

        if (cards.isEmpty()) {
            violations.add("No event cards found in the Popular section");
            return violations;
        }

        for (WebElement card : cards) {
            String indicator = findLanguageIndicator(card);
            String title = findCardTitle(card);

            if (!"En".equalsIgnoreCase(indicator)) {
                violations.add("Card '" + title + "' has language indicator '" + indicator + "'");
            }
        }

        return violations;
    }

    private List<WebElement> waitForPopularCards() {
        WebElement section = wait.until(ExpectedConditions.visibilityOfElementLocated(POPULAR_SECTION));
        scrollIntoView(section);

        List<WebElement> cards = waitForCards(POPULAR_CARDS);
        if (cards.isEmpty()) {
            cards = waitForCards(POPULAR_CARD_LINKS);
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
        List<WebElement> exactIndicators = card.findElements(CARD_LANGUAGE_INDICATOR);
        if (!exactIndicators.isEmpty()) {
            return exactIndicators.getFirst().getText().trim();
        }

        List<WebElement> badgeCandidates = card.findElements(
            By.xpath(".//*[contains(@class,'badge') or contains(@class,'lang') or contains(@class,'language')]")
        );
        for (WebElement badge : badgeCandidates) {
            String text = badge.getText().trim();
            if (!text.isEmpty()) {
                return text;
            }
        }

        return extractIndicatorFromText(card.getText());
    }

    private String extractIndicatorFromText(String text) {
        String normalized = text == null ? "" : text;
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
        return text.isEmpty() ? "(untitled card)" : text.split("\\R", 2)[0].trim();
    }

    private void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }
}
