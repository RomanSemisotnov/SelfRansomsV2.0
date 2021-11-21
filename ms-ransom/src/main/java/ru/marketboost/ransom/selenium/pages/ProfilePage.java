package ru.marketboost.ransom.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

public class ProfilePage extends BasePage{
    public ProfilePage(WebDriver driver, ApplicationContext applicationContext, UUID sessionId) {
        super(driver, applicationContext, sessionId);
    }

    public void changeName() {

    }
}
