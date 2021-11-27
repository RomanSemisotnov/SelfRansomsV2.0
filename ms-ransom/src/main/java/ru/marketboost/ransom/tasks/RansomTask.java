package ru.marketboost.ransom.tasks;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.marketboost.ransom.models.requests.RansomRequest;
import ru.marketboost.ransom.selenium.drivers.DriverFactory;
import ru.marketboost.ransom.selenium.pages.HomePage;
import ru.marketboost.ransom.selenium.pages.LoginPage;
import ru.marketboost.ransom.selenium.pages.ProfilePage;

import java.util.UUID;

@Service
@Scope(value = "prototype")
public class RansomTask extends BaseTask implements Runnable {

    @Autowired
    private DriverFactory driverFactory;

    @Autowired
    private ApplicationContext applicationContext;

    private RansomRequest ransomRequest;

    public void init(RansomRequest ransomRequest) {
        this.ransomRequest = ransomRequest;
    }

    @Override
    public void run() {
        WebDriver driver = null;
        UUID sessionId = UUID.randomUUID();
        try {
            driver = driverFactory.getDriver(ransomRequest.getProxyIp(), ransomRequest.getProxyPort(), ransomRequest.getProxyLogin(), ransomRequest.getProxyPass());

            // driver = driverFactory.getDriver();

            HomePage homePageWithoutAuth = new HomePage(driver, applicationContext, sessionId);

       //     homePageWithoutAuth.findArticle(ransomRequest.getSearch(), ransomRequest.getVendorCode());

            LoginPage loginPage = homePageWithoutAuth.goToLoginPage();

            HomePage homePageWithAuth = loginPage.doLogin(ransomRequest.getPhoneNumber());

            ProfilePage profilePageWithAuth = homePageWithAuth.goToProfilePage();

            System.out.println(123);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null)
                driver.quit();
        }
    }

}
