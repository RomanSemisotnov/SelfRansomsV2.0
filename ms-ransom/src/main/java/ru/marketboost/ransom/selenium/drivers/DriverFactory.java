package ru.marketboost.ransom.selenium.drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

@Service
public class DriverFactory {

    public WebDriver getDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-single-click-autofill");
        options.addArguments("--disable-autofill-keyboard-accessory-view[8]");

        ChromeDriver chromeDriver = new ChromeDriver(options);
        chromeDriver.manage().window().maximize();
        return chromeDriver;
    }

}
