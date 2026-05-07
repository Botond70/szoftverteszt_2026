package deik.pti.pageObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import deik.pti.factory.WebDriverFactory;

public class CommunitiesPage extends CommonPage {
    public CommunitiesPage(final WebDriverFactory factory) {
        super(factory);
    }
  
  private static final String COMMUNITIES_PAGE_URL = "https://wearecommunity.io/communities";
  private static final By COMMUNITIES_SECTION = By.cssSelector(".evnt-panel .evnt-communities-panel");
  private static final By COMMUNITY_CARDS = By.cssSelector(".evnt-communities-column .evnt-community-card");
  private static final By COMMUNITY_CARD_LINKS = By.cssSelector(".evnt-community-card a[href*='/communities']");
  private static final By CARD_TITLE = By.cssSelector(".evnt-name-wrapper > h2 > span");
  private static final By JOIN_BUTTON = By.cssSelector(".evnt-button-wrapper > .evnt-reg-wrapper > button");

  private static final By MORE_FILTERS_BUTTON = By.cssSelector(".evnt-toggle-filters-button");
  private static final By LANGUAGE_DROPDOWN = By.cssSelector("button#filter_language");
  private static final By LANGUAGE_OPTIONS = By.cssSelector(".evnt-filter-menu-scroll .form-check-label");

  public void open() {
      driver.get(COMMUNITIES_PAGE_URL);
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".evnt-communities-column")));
  }

  public String getFirstLanguageFilterOption() {
    WebElement moreFilters = wait.until(ExpectedConditions.elementToBeClickable(MORE_FILTERS_BUTTON));
    scrollIntoView(moreFilters);
    moreFilters.click();
    
    WebElement langDropdown = wait.until(ExpectedConditions.elementToBeClickable(LANGUAGE_DROPDOWN));
    scrollIntoView(langDropdown);
    langDropdown.click();
    
    // 1. Megvárjuk, hogy betöltődjön legalább egy opció valahol az oldalon
    wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(LANGUAGE_OPTIONS, 0));
    
    // 2. Tiszta Selenium megoldás a JS 'closest()' helyett:
    // Megkeressük a Language gomb legközelebbi olyan szülőjét, ami egy szűrő doboz
    WebElement parentContainer = langDropdown.findElement(
        By.xpath("./ancestor::*[contains(@class, 'evnt-filter-col') or contains(@class, 'dropdown') or contains(@class, 'evnt-filter-item')][1]")
    );
    
    // 3. Ezen a szülő dobozon BELÜL keressük meg az első labelt
    WebElement firstLabel = parentContainer.findElement(By.cssSelector(".form-check-label"));
    
    // 4. A getText() helyett getAttribute("textContent")-et használunk, ami beolvassa a rejtett/animáló szöveget is
    String text = firstLabel.getAttribute("textContent");
    
    return text != null ? text.trim() : "";
  }

  private void scrollIntoView(WebElement element) {
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
  }

  private List<WebElement> waitForCommunityCards() {
    WebElement section = wait.until(ExpectedConditions.visibilityOfElementLocated(COMMUNITIES_SECTION));
    scrollIntoView(section);

    List<WebElement> cards = waitForCards(COMMUNITY_CARDS);
    if (cards.isEmpty()) {
      cards = waitForCards(COMMUNITY_CARD_LINKS);
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
  
  public List<String> getJoinButtonHeightIssues() {
    List<WebElement> cards = waitForCommunityCards();
    List<String> violations = new ArrayList<>();

    if (cards.isEmpty()) {
      violations.add("No community cards found to check join button heights");
      return violations;
    }

    int expectedRelativeHeight = -1;
    for (WebElement card : cards) {
      WebElement joinButton = card.findElement(JOIN_BUTTON);
      int height = joinButton.getSize().getHeight();
      int cardHeight = card.getSize().getHeight();
      int relativeHeight = (int) ((height / (double) cardHeight) * 100); // relative height as percentage of card height
      if (expectedRelativeHeight == -1) {
        expectedRelativeHeight = relativeHeight;
      } else if (relativeHeight != expectedRelativeHeight) {
        String title = card.findElement(CARD_TITLE).getText();
        violations.add("Card '" + title + "' has join button height " + relativeHeight + "%, expected " + expectedRelativeHeight + "%");
      }
    }

    return violations;
  }

  public List<String> getCommunitiesDisplayed(){
    List<WebElement> cards = waitForCommunityCards();
    List<String> violations = new ArrayList<>();
    switch (cards.size()) {
      case 0:
        violations.add("No community cards found in the Communities section");
        break;
      case 1:
        violations.add("Only one community card found in the Communities section");
        break;
      default:
        //2 or more
        break;
    }
    return violations;
  }


}
