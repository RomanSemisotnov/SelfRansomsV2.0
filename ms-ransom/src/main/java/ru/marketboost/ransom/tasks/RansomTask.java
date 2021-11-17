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
        try {
            driver = driverFactory.getDriver();

            HomePage homePageWithoutAuth = new HomePage(driver, applicationContext);

            LoginPage loginPage = homePageWithoutAuth.goToLoginPage();

            HomePage homePageWithAuth = loginPage.doLogin(ransomRequest.getPhoneNumber());

            ProfilePage profilePageWithAuth = homePageWithAuth.goToProfilePage();

            /*WebElement searchField = new WebDriverWait(driver, 60)
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='searchInput']")));

            new Actions(driver).moveToElement(searchField).click().perform();

            Thread.sleep(Randomizer.randomBetween(200, 400));

            fillSearchFieldWithInterval(searchField, ransomRequest.getSearch());

            WebElement searchButton = new WebDriverWait(driver, 20)
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@id='applySearchBtn']")));
            new Actions(driver).moveToElement(searchButton).click().perform();

            List<WebElement> goods = new WebDriverWait(driver, 5)
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//span[@class='goods-name']")));

            Optional<WebElement> ourGood = goods
                    .stream()
                    .filter(s -> s.getAttribute("src").equals("https://images.wbstatic.net/c516x688/new/37760000/37769499-1.jpg"))
                    .findFirst();

            if (ourGood.isPresent()) {
                new Actions(driver).moveToElement(ourGood.get()).click().perform();

            } else {

            }*/
            System.out.println(123);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null)
                driver.quit();
        }

    }

}
