package ru.marketboost.ransom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "ms-ransom/selenium_drivers/chromedriver.exe");
		System.setProperty("webdriver.gecko.driver", "ms-ransom/selenium_drivers/geckodriver.exe");
		SpringApplication.run(App.class, args);
	}

}
