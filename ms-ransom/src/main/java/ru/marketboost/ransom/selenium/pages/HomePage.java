package ru.marketboost.ransom.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.context.ApplicationContext;
import ru.marketboost.ransom.exceptions.UnknownSituationException;

public class HomePage extends BasePage {

    public HomePage(WebDriver driver, ApplicationContext applicationContext) {
        super(driver, applicationContext);
    }

    private final By loginLink = By.className("j-main-login");
    private final By profileLink_A = By.className("navbar-pc__link");


    public LoginPage goToLoginPage() {
        if (!driver.getCurrentUrl().equals(WILDBERRIES_URL)) {
            driver.get(WILDBERRIES_URL);
        }

        moveAndClick(loginLink);

        return new LoginPage(driver, applicationContext);
    }

    public ProfilePage goToProfilePage() throws  UnknownSituationException {
        if (!driver.getCurrentUrl().equals(WILDBERRIES_URL)) {
            driver.get(WILDBERRIES_URL);
        }

        moveAndClick(findElemWithAttribute(profileLink_A, "href", "/lk", String::endsWith)
                .orElseThrow(() -> new UnknownSituationException("Cant find link to profile from home page"))
        );

        return new ProfilePage(driver, applicationContext);
    }

}
