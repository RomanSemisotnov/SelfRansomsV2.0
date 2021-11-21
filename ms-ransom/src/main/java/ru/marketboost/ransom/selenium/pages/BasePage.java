package ru.marketboost.ransom.selenium.pages;

import io.vavr.control.Either;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.ApplicationContext;
import ru.marketboost.library.common.http.services.phones.PhoneHttpService;
import ru.marketboost.ransom.exceptions.OneOfTwoElementNotFoundException;
import ru.marketboost.ransom.services.captcha.SolveCaptchaService;
import ru.marketboost.library.common.utils.Randomizer;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;


public abstract class BasePage {

    protected static final String WILDBERRIES_URL = "https://wildberries.ru";

    protected final WebDriver driver;
    protected ApplicationContext applicationContext;
    protected SolveCaptchaService solveCaptchaService;
    protected PhoneHttpService phoneHttpService;
    protected UUID sessionId;

    public BasePage(WebDriver driver, ApplicationContext applicationContext, UUID sessionId) {
        this.driver = driver;
        this.applicationContext = applicationContext;
        this.sessionId = sessionId;
        init();
    }

    private void init() {
        solveCaptchaService = applicationContext.getBean(SolveCaptchaService.class);
        phoneHttpService = applicationContext.getBean(PhoneHttpService.class);
    }

    protected Optional<WebElement> waitLoadingElem(By location) {
        return waitLoadingElem(location, randomBaseDuration());
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

    protected WebElement findElement(By location) {
        return new WebDriverWait(driver, randomBaseDuration())
                .until(ExpectedConditions.elementToBeClickable(location));
    }

    protected WebElement moveAndClick(By location) {
        WebElement element = new WebDriverWait(driver, randomBaseDuration())
                .until(ExpectedConditions.elementToBeClickable(location));
        moveAndClick(element);
        return element;
    }

    protected WebElement moveAndClickAndFill(By location, String str) {
        return moveAndClickAndFill(location, str, false);
    }

    protected WebElement moveAndClickAndFill(By location, String str, boolean enterInTheEnd) {
        WebElement element = moveAndClick(location);
        fillInput(element, str, enterInTheEnd);
        return element;
    }

    protected void moveAndClick(WebElement element) {
        new Actions(driver).moveToElement(element).click().perform();
    }

    private void fillInput(WebElement field, String text, boolean enterInTheEnd) {
        try {
            for (int i = 0; i < text.length(); i++) {
                field.sendKeys(String.valueOf(text.charAt(i)));
                Thread.sleep(Randomizer.randomBetween(70, 160));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("interrupting in filling input with text: " + text, e);
        }
        if (enterInTheEnd)
            field.sendKeys(Keys.ENTER);
    }

    protected Optional<WebElement> checkElementExist(By locator, int duration) {
        return waitLoadingElem(locator, duration);
    }

    protected Either<WebElement, WebElement> findOneOfTwoElements(By first, By second) throws OneOfTwoElementNotFoundException {
        boolean result = new WebDriverWait(driver, randomBaseDuration())
                .until(ExpectedConditions.or(
                                ExpectedConditions.elementToBeClickable(first),
                                ExpectedConditions.elementToBeClickable(second)
                        )
                );

        if (!result)
            throw new OneOfTwoElementNotFoundException(String.format(
                    "cant find for %s seconds %s or %s",
                    randomBaseDuration(),
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
        return findElemWithText(locator, match, pr, randomBaseDuration());
    }

    protected Optional<WebElement> findElemWithText(By locator, String match, BiPredicate<String, String> pr, int duration) {
        waitLoadingElem(locator, duration);
        return driver.findElements(locator)
                .stream()
                .filter(el -> pr.test(el.getText(), match))
                .findAny();
    }

    protected List<WebElement> findElements(By locator) {
        waitLoadingElem(locator);
        return driver.findElements(locator);
    }

    protected Optional<WebElement> findElemWithAttribute(By locator, String attribute, String attValue) {
        return findElemWithAttribute(locator, attribute, attValue, Objects::equals, randomBaseDuration());
    }

    protected Optional<WebElement> findElemWithAttribute(By locator, String attribute, String attValue, BiPredicate<String, String> pr) {
        return findElemWithAttribute(locator, attribute, attValue, pr, randomBaseDuration());
    }

    protected Optional<WebElement> findElemWithAttribute(By locator, String attribute, String attValue, BiPredicate<String, String> pr, int duration) {
        waitLoadingElem(locator, duration);
        return driver.findElements(locator)
                .stream()
                .filter(el -> pr.test(el.getAttribute(attribute), attValue))
                .findAny();
    }

    protected int randomShortDuration() {
        return (int) (Math.random() * (3 - 1)) + 1;
    }

    protected int randomBaseDuration() {
        return (int) (Math.random() * (6 - 3)) + 3;
    }

    protected <T> List<T> getRandomElements(List<T> elements, int min, int max) {
        Random random = new Random();
        List<T> randomElements = new ArrayList<>();

        int fakeProductView = Randomizer.randomBetween(min, max);
        for (int i = 0; i < fakeProductView; i++) {
            randomElements.add(elements.get(random.nextInt(elements.size())));
        }
        return randomElements;
    }

    protected void scrollToAndClick(WebElement element) {
        scrollTo(element);
        moveAndClick(element);
    }

    protected void scrollTo(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected WebElement scrollToAndClick(By by) {
        WebElement el = findElement(by);
        scrollToAndClick(el);
        return el;
    }

    protected WebElement scrollTo(By by) {
        WebElement el = findElement(by);
        scrollTo(el);
        return el;
    }

    protected void randomDelay() throws InterruptedException {
        randomDelay(0.3, 1.20);
    }

    protected void randomDelay(double min, double max) throws InterruptedException {
        long t = (long) (Randomizer.randomBetween(min, max) * 1000);
        Thread.sleep(t);
    }

}
