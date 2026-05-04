package deik.pti.pageObjects;

import deik.pti.factory.WebDriverFactory;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

@Component
public class HomePage extends CommonPage {
    public HomePage(final WebDriverFactory factory) {
        super(factory);
    }

    private static final String HOME_PAGE_URL = "https://wearecommunity.io/";

    @FindBy(id = "popular")
    private WebElement popularButton;
}
