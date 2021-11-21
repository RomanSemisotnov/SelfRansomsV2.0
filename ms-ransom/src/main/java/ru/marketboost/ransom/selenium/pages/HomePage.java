package ru.marketboost.ransom.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.context.ApplicationContext;
import ru.marketboost.ransom.exceptions.MoreThan10PageException;
import ru.marketboost.ransom.exceptions.UnknownSituationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class HomePage extends BasePage {

    public HomePage(WebDriver driver, ApplicationContext applicationContext, UUID sessionId) {
        super(driver, applicationContext, sessionId);
    }

    private final By loginLink = By.className("j-main-login");
    private final By profileLink_A = By.className("navbar-pc__link");
    private final By searchField = By.xpath("//input[@id='searchInput']");
    private final By productCardElements = By.className("product-card__main"); //j-card-link


    public LoginPage goToLoginPage() {
        if (!driver.getCurrentUrl().equals(WILDBERRIES_URL)) {
            driver.get(WILDBERRIES_URL);
        }

        moveAndClick(loginLink);

        return new LoginPage(driver, applicationContext, sessionId);
    }

    public ProfilePage goToProfilePage() throws UnknownSituationException {
        if (!driver.getCurrentUrl().equals(WILDBERRIES_URL)) {
            driver.get(WILDBERRIES_URL);
        }

        moveAndClick(findElemWithAttribute(profileLink_A, "href", "/lk", String::endsWith)
                .orElseThrow(() -> new UnknownSituationException("Cant find link to profile from home page, sessionId: " + sessionId))
        );

        return new ProfilePage(driver, applicationContext, sessionId);
    }

    public void findArticle(String search, String vendorCode) throws Exception {
        if (!driver.getCurrentUrl().equals(WILDBERRIES_URL)) {
            driver.get(WILDBERRIES_URL);
        }

        moveAndClickAndFill(searchField, search, true);

        findAndScrollAndClickToProduct(driver, vendorCode);

        System.out.println(123);
    }

    private void findAndScrollAndClickToProduct(WebDriver driver, String vendorCode) throws Exception {
        int maxPageNum = 10;
        List<WebElement> productList = findElements(productCardElements);
        randomDelay();

        Optional<WebElement> neededProduct = productList.stream()
                .filter(el -> el.getAttribute("href").contains("catalog/" + vendorCode + "/detail"))
                .findAny();

        int pageNum = 0;
        if (neededProduct.isPresent()) {
            List<WebElement> fakeViews = getRandomElements(productList, 2, 4);
            for (WebElement fakeEl : fakeViews) {
                scrollTo(fakeEl);
                randomDelay();

                moveAndClick(fakeEl);
                randomDelay();

                (new ProductCardPage(driver, applicationContext, sessionId)).fakeViewCard();
                randomDelay();

                driver.navigate().back();
            }
        } else {
            if (pageNum++ > maxPageNum) {
                throw new MoreThan10PageException();// потом переделать
            }
        }
    }

}
