package ru.marketboost.ransom.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.UUID;

public class ProductCardPage extends BasePage {

    private final By photoEl = By.id("photo");
    private final By descriptionHeaderEl = By.xpath("/html[@class='adaptive']/body[@class='ru']/div[@class='wrapper']/main[@id='body-layout']/div[@id='mainContainer']/div[@id='app']/div[@id='container']/div[@class='product-detail__section-wrap']/div[@class='product-detail__details-wrap']/section[@class='product-detail__details details']/div[@class='details__header-wrap']/h2[@class='details__header section-header']");
    private final By collapseDescriptionEl = By.className("collapsible__toggle");
    private final By reviewEls = By.className("feedback__text");
    private final By addToCard = By.xpath("/html[@class='adaptive']/body[@class='ru']/div[@class='wrapper']/main[@id='body-layout']/div[@id='mainContainer']/div[@id='app']/div[@id='container']/div[@class='product-detail__same-part-kt same-part-kt']/div[@class='same-part-kt']/div[@id='infoBlockProductCard']/div[@class='same-part-kt__order']/div[@class='same-part-kt__btn-wrap']/button[@class='btn-main']/span[@class='hide-mobile']");

    public ProductCardPage(WebDriver driver, ApplicationContext applicationContext, UUID sessionId) {
        super(driver, applicationContext, sessionId);
    }

    public void fakeViewCard() throws Exception {
        scrollTo(findElement(photoEl));
        randomDelay(1.5, 3.5);

        scrollTo(descriptionHeaderEl);
        randomDelay(0.5, 2);
        WebElement collapse = scrollTo(collapseDescriptionEl);
        randomDelay(1, 3);
        moveAndClick(collapse);
        randomDelay(4, 5);

        List<WebElement> reviews = findElements(reviewEls);
        for (WebElement review : getRandomElements(reviews, 0, Math.min(reviews.size(), 6))) {
            scrollTo(review);
            randomDelay(2, 4);
        }
    }

    public void viewAndAddToCard() throws Exception {
        scrollTo(findElement(photoEl));
        randomDelay(1.5, 3.5);

        scrollTo(descriptionHeaderEl);
        randomDelay(0.5, 2);
        WebElement collapse = scrollTo(collapseDescriptionEl);
        randomDelay(1, 3);
        moveAndClick(collapse);
        randomDelay(4, 5);

        List<WebElement> reviews = findElements(reviewEls);
        for (WebElement review : getRandomElements(reviews, 0, Math.min(reviews.size(), 6))) {
            scrollTo(review);
            randomDelay(2, 4);
        }

        scrollTo(findElement(photoEl));
        randomDelay(1.5, 3.5);

        scrollTo(collapseDescriptionEl);
        randomDelay(1, 3);

        scrollTo(findElement(photoEl));
        randomDelay(1.5, 3.5);

        moveAndClick(addToCard);
    }


}
