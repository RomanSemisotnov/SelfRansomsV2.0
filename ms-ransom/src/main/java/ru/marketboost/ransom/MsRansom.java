package ru.marketboost.ransom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
		scanBasePackages = {
				"ru.marketboost.ransom",
				"ru.marketboost.library.common.http",
				"ru.marketboost.library.common.configs"
		}
)
public class MsRansom {

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "ms-ransom/selenium_drivers/chromedriver.exe");
		System.setProperty("webdriver.gecko.driver", "ms-ransom/selenium_drivers/geckodriver.exe");
		SpringApplication.run(MsRansom.class, args);
	}

}
