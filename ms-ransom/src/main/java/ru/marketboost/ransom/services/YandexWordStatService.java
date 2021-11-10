package ru.marketboost.ransom.services;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import ru.marketboost.ransom.models.Stat;
import ru.marketboost.ransom.utils.Randomizer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class YandexWordStatService {

    ChromeDriver driver;

    String WORDSTAT_URL = "https://wordstat.yandex.ru/";

    By userField = new By.ById("b-domik_popup-username");
    By passwordField = new By.ById("b-domik_popup-password");
    By loginButton = By.xpath("//span[@class='b-form-button b-form-button_size_m b-form-button_theme_grey-m b-form-button_valign_middle i-bem b-form-button_js_inited b-form-button_hovered_yes']");

    public void parseStat(List<Stat> stats) throws IOException {
        try {
            driver = getChromeDriver();

            driver.get(WORDSTAT_URL);

            new WebDriverWait(driver, 10000).until(
                    webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

            WebElement loginLink = driver.findElementsByClassName("b-link__inner")
                    .stream()
                    .filter(el -> el.getText().equals("Войти"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("login button wasnt found"));

            new Actions(driver).moveToElement(loginLink).click().perform();

            WebElement userEl = new WebDriverWait(driver, 10)
                    .until(ExpectedConditions.elementToBeClickable(userField));
            new Actions(driver).moveToElement(userEl).click().perform();
            fillSearchFieldWithInterval("kysok24111999@yandex.ru", userEl);

            WebElement passwordEl = new WebDriverWait(driver, 10)
                    .until(ExpectedConditions.elementToBeClickable(passwordField));
            new Actions(driver).moveToElement(passwordEl).click().perform();
            fillSearchFieldWithInterval("6Apon19953", passwordEl);

            // WebElement loginButtonEl = new WebDriverWait(driver, 15)
            //         .until(ExpectedConditions.elementToBeClickable(loginButton));
            // new Actions(driver).moveToElement(loginButtonEl).click().perform();

            WebElement searchField = new WebDriverWait(driver, 15)
                    .until(ExpectedConditions.elementToBeClickable(By.className("b-form-input__input")));

            for (Stat stat : stats) {
                if (stat.getYandexWordstatCount() != null) {
                    continue;
                }
                while (!searchField.getAttribute("value").equals("")) {
                    searchField.sendKeys(Keys.BACK_SPACE);
                }
                Thread.sleep(Randomizer.randomBetween(80, 120));
                fillSearchFieldWithInterval(stat.getName(), searchField);
                searchField.sendKeys(Keys.ENTER);

                Thread.sleep(Randomizer.randomBetween(2000, 2500));

                WebElement yandexResult = new WebDriverWait(driver, 15)
                        .until(ExpectedConditions.elementToBeClickable(By.className("b-word-statistics__info")));

                Thread.sleep(Randomizer.randomBetween(80, 120));

                String frequencyPerMonth = "";
                try {
                    int i = 0;
                    while (!yandexResult.getText().contains(stat.getName()) && i < 20) {
                        Thread.sleep(500);
                        i++;
                    }
                    if (i > 6) {
                        continue;
                    }

                    frequencyPerMonth = yandexResult.getText()
                            .replaceAll("«[\\S\\s]{1,100}»", "")
                            .replaceAll("[^\\d.]", "");
                    stat.setYandexWordstatCount(Integer.parseInt(frequencyPerMonth));
                } catch (StaleElementReferenceException e) {
                    System.out.println("StaleElementReferenceException " + stat.getName());
                    continue;
                } catch (NumberFormatException e) {
                    new FileWriter("log.txt", true)
                            .append(frequencyPerMonth)
                            .append("\n")
                            .flush();
                }
            }

        } catch (InterruptedException e) {
            throw new RuntimeException("fucking interrupting");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stats.sort((s1, s2) -> -1 * s1.getWbCountGoods().compareTo(s2.getWbCountGoods()));

            final CsvMapper mapper = new CsvMapper();
            final CsvSchema schema = mapper.schemaFor(Stat.class);
            final String csv = mapper.writer(schema.withUseHeader(true)).writeValueAsString(stats);
            Files.writeString(Path.of("new_stat.csv"), csv);

            if (driver != null) {
                driver.quit();
                driver = null;
            }
        }
    }

    void fillSearchFieldWithInterval(String text, WebElement field) {
        try {
            for (int i = 0; i < text.length(); i++) {
                field.sendKeys(String.valueOf(text.charAt(i)));
                Thread.sleep(Randomizer.randomBetween(70, 150));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("interrupt suka");
        }
    }

    ChromeDriver getChromeDriver() {
        if (driver == null) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-single-click-autofill");
            options.addArguments("--disable-autofill-keyboard-accessory-view[8]");

            driver = new ChromeDriver(options);
        }
        return driver;
    }

}
