package ru.marketboost.ransom.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.context.ApplicationContext;
import ru.marketboost.library.common.utils.Randomizer;

import java.util.List;
import java.util.UUID;

public class ProductCardPage extends BasePage {

    private final By photoEl = By.id("photo");
    private final By descriptionHeaderEl = By.className("details__header-wrap");
    private final By collapseDescriptionEl = By.className("collapsible__toggle");
    private final By reviewEls = By.className("feedback__text");

    public ProductCardPage(WebDriver driver, ApplicationContext applicationContext, UUID sessionId) {
        super(driver, applicationContext, sessionId);
    }

    public void fakeViewCard() throws Exception {
        scrollTo(findElement(photoEl));
        randomDelay(1.5, 3.5);

        scrollTo(descriptionHeaderEl);
        randomDelay(0.3, 0.6);
        WebElement collapse = scrollTo(collapseDescriptionEl);
        randomDelay(1, 2);
        moveAndClick(collapse);
        randomDelay(4, 5);

        List<WebElement> reviews = findElements(reviewEls);
        int want2Read = Randomizer.randomBetween(2, 5);
        int have2Read = Math.min(reviews.size(), want2Read);
        for(WebElement review : reviews.subList(0, have2Read)) {
            scrollTo(review);
            randomDelay(3, 4);
        }
    }


}
