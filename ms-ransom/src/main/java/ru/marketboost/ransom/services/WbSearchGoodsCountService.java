package ru.marketboost.ransom.services;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import ru.marketboost.ransom.models.Stat;
import ru.marketboost.library.common.utils.Randomizer;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class WbSearchGoodsCountService {


    ChromeDriver driver;

    //  @GetMapping
    public void getStats(Set<String> names, List<Stat> alreadyExistStat, Map<String, String> replacing) throws IOException {
        Map<String, String> newStats = new HashMap<>();
        Map<String, Boolean> isNeedToReckek = new HashMap<>();
        try {
            driver = getChromeDriver();

            driver.get("https://wildberries.ru");

            String prev = null;
            for (String name : names) {
                WebElement searchField = new WebDriverWait(driver, 60)
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='searchInput']")));

                Actions actions = new Actions(driver);
                actions.moveToElement(searchField).click().perform();

                Thread.sleep(8000);

                while (!searchField.getAttribute("value").equals("")) {
                    searchField.sendKeys(Keys.BACK_SPACE);
                }

                searchField.click();

                Thread.sleep(Randomizer.randomBetween(50, 100));

                //ЗАПОЛНЯЕМ
                fillSearchFieldWithInterval(name, searchField);

                WebElement searchButton = new WebDriverWait(driver, 20)
                        .until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@id='applySearchBtn']")));
                searchButton.click();

                Thread.sleep(Randomizer.randomBetween(5800, 6100));

                String currentCount;
                currentCount = driver.findElementByXPath("//span[@class='goods-count']").getText();
                if (currentCount.isBlank()) {
                    currentCount = "0";
                }

                String nameInSearchField = !searchField.getAttribute("value").isBlank() ? searchField.getAttribute("value") : name;
                if (!name.equals(nameInSearchField)) {
                    if (!replacing.containsKey(name)) {
                        FileWriter writer = new FileWriter("from2to.csv", true);
                        writer.append(name).append(",").append(nameInSearchField).append("\n").flush();
                    }
                    newStats.put(nameInSearchField, currentCount);
                    isNeedToReckek.put(nameInSearchField, prev != null && prev.equals(currentCount));
                    prev = currentCount;
                    continue;
                }

                newStats.put(name, currentCount);
                isNeedToReckek.put(name, prev != null && prev.equals(currentCount));
                prev = currentCount;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("interrupting sucka");
        } finally {
            List<Stat> data = newStats.entrySet()
                    .stream()
                    .map(entry -> Stat.builder()
                            .name(entry.getKey())
                            .wbCountGoods(Integer.parseInt(entry.getValue()
                                    .replaceAll("товаров", "")
                                    .replaceAll("товара", "")
                                    .replaceAll("товар", "")
                                    .replaceAll(" ", "").trim()))
                            .isNeedToRecheck(isNeedToReckek.get(entry.getKey()))
                            .build())
                    .collect(Collectors.toList());

            alreadyExistStat.addAll(data);

            alreadyExistStat.sort((s1, s2) -> -1 * s1.getWbCountGoods().compareTo(s2.getWbCountGoods()));

            final CsvMapper mapper = new CsvMapper();
            final CsvSchema schema = mapper.schemaFor(Stat.class);
            final String csv = mapper.writer(schema.withUseHeader(true)).writeValueAsString(alreadyExistStat);
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
            driver.manage().window().maximize();
        }

        return driver;
    }

    RemoteWebDriver getDriver() throws MalformedURLException {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setCapability("browserVersion", "94.0");
        chromeOptions.setCapability("platformName", "Linux");

        RemoteWebDriver remoteWebDriver = new RemoteWebDriver(
                new URL("http://localhost:4444"),
                chromeOptions
        );
        return remoteWebDriver;
    }

}

