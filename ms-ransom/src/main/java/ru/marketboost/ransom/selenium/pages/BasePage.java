package ru.marketboost.ransom.selenium.pages;

import io.vavr.control.Either;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.ApplicationContext;
import ru.marketboost.library.common.http.services.phones.PhoneCodeHttpService;
import ru.marketboost.library.common.http.services.phones.PhoneHttpService;
import ru.marketboost.ransom.exceptions.OneOfTwoElementNotFoundException;
import ru.marketboost.ransom.services.captcha.SolveCaptchaService;
import ru.marketboost.ransom.utils.Randomizer;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;


public abstract class BasePage {

    protected static final String WILDBERRIES_URL = "https://wildberries.ru";
    protected static final int SHORTEST_WAITING_DURATION = 1;
    protected static final int SHORT_WAITING_DURATION = 2;
    protected static final int BASE_WAITING_DURATION = 6;

    protected final WebDriver driver;
    protected ApplicationContext applicationContext;
    protected SolveCaptchaService solveCaptchaService;
    protected PhoneCodeHttpService phoneCodeHttpService;

    public BasePage(WebDriver driver, ApplicationContext applicationContext) {
        this.driver = driver;
        this.applicationContext = applicationContext;
        init();
    }

    private void init() {
        solveCaptchaService = applicationContext.getBean(SolveCaptchaService.class);
        phoneCodeHttpService = applicationContext.getBean(PhoneCodeHttpService.class);
    }

    protected Optional<WebElement> waitLoadingElem(By location) {
        return waitLoadingElem(location, BASE_WAITING_DURATION);
    }

    protected Optional<WebElement> waitLoadingElem(By location, int duration) {
        try {
            return Optional.ofNullable(new FluentWait<>(driver) //
                    .withTimeout(duration, TimeUnit.SECONDS) //
                    .pollingEvery(1, TimeUnit.SECONDS)
                    .ignoring(NoSuchElementException.class, TimeoutException.class)
                    .until(ExpectedConditions.elementToBeClickable(location)));
        } catch (NoSuchElementException | TimeoutException e) {
            return Optional.empty();
        }
    }

    protected WebElement moveAndClick(By location) {
        WebElement element = new WebDriverWait(driver, BASE_WAITING_DURATION)
                .until(ExpectedConditions.elementToBeClickable(location));
        moveAndClick(element);
        return element;
    }

    protected WebElement moveAndClickAndFill(By location, String str) {
        WebElement element = moveAndClick(location);
        fillInput(element, str);
        return element;
    }

    protected void moveAndClick(WebElement element) {
        new Actions(driver).moveToElement(element).click().perform();
    }

    private void fillInput(WebElement field, String text) {
        try {
            for (int i = 0; i < text.length(); i++) {
                field.sendKeys(String.valueOf(text.charAt(i)));
                Thread.sleep(Randomizer.randomBetween(70, 160));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("interrupting in filling input with text: " + text, e);
        }
    }

    protected Optional<WebElement> checkElementExist(By locator, int duration) {
        return waitLoadingElem(locator, duration);
    }

    protected Optional<WebElement> checkElementExist(By locator) {
        return waitLoadingElem(locator);
    }

    protected Either<WebElement, WebElement> findOneOfTwoElements(By first, By second) throws OneOfTwoElementNotFoundException {
        boolean result = new WebDriverWait(driver, BASE_WAITING_DURATION)
                .until(ExpectedConditions.or(
                                ExpectedConditions.elementToBeClickable(first),
                                ExpectedConditions.elementToBeClickable(second)
                        )
                );

        if (!result)
            throw new OneOfTwoElementNotFoundException(String.format(
                    "cant find for %s seconds %s or %s",
                    BASE_WAITING_DURATION,
                    first,
                    second
            ));

        try {
            return Either.left(driver.findElement(first));
        } catch (Exception e) {
            return Either.right(driver.findElement(second));
        }
    }

    protected Optional<WebElement> findElemWithText(By locator, String match) {
        return findElemWithText(locator, match, Objects::equals);
    }

    protected Optional<WebElement> findElemWithText(By locator, String match, BiPredicate<String, String> pr) {
        return findElemWithText(locator, match, pr, BASE_WAITING_DURATION);
    }

    protected Optional<WebElement> findElemWithText(By locator, String match, BiPredicate<String, String> pr, int duration) {
        waitLoadingElem(locator, duration);
        return driver.findElements(locator)
                .stream()
                .filter(el -> pr.test(el.getText(), match))
                .findAny();
    }

    protected Optional<WebElement> findElemWithAttribute(By locator, String attribute, String attValue) {
        return findElemWithAttribute(locator, attribute, attValue, Objects::equals, BASE_WAITING_DURATION);
    }

    protected Optional<WebElement> findElemWithAttribute(By locator, String attribute, String attValue, BiPredicate<String, String> pr) {
        return findElemWithAttribute(locator, attribute, attValue, pr, BASE_WAITING_DURATION);
    }

    protected Optional<WebElement> findElemWithAttribute(By locator, String attribute, String attValue, BiPredicate<String, String> pr, int duration) {
        waitLoadingElem(locator, duration);
        return driver.findElements(locator)
                .stream()
                .filter(el -> pr.test(el.getAttribute(attribute), attValue))
                .findAny();
    }

}
