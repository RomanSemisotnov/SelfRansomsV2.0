package ru.marketboost.ransom.selenium.pages;

import com.twocaptcha.captcha.Captcha;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.context.ApplicationContext;
import ru.marketboost.library.common.http.models.requests.GetLastCodeRequest;
import ru.marketboost.library.common.http.models.responses.GetLastCodeResponse;
import ru.marketboost.ransom.exceptions.CantSolveALotTimeCaptchaException;

import java.util.Optional;

public class LoginPage extends BasePage {

    private final By phoneInput = By.xpath("//div[@class='login__phone form-block form-block--phone-dropdown']/div[@class='form-block__dropdown-wrap']/div[@class='form-block__input']/input[@class='input-item']");
    private final By loginByPhoneButton = By.id("requestCode");
    private final By captchaImg = By.className("form-block__captcha-img");
    private final By captchaInput = By.id("smsCaptchaCode");
    private final By submitCaptchaButton = By.className("login__btn");
    private final By captchaIncorrect_P = By.className("form-block__message--error"); // Код указан неверно
    private final String captchaIncorrectText = "Код указан неверно";
    private final By submitPhoneCode = By.className("j-input-confirm-code");


    public LoginPage(WebDriver driver, ApplicationContext applicationContext) {
        super(driver, applicationContext);
    }

    private final By codeWasSentToOtherDevices_P = By.className("login__code-message"); // Код отправлен на другие устройства Wildberries
    private final String patternForCodeToOtherDevices = "Код отправлен на другие устройства Wildberries";

    public HomePage doLogin(String phoneNumber) throws Exception {
        try {
            moveAndClickAndFill(phoneInput, phoneNumber);

            moveAndClick(loginByPhoneButton);

            checkAndPassCaptcha();

            Optional<WebElement> needToSendToOtherDevice = findElemWithText(codeWasSentToOtherDevices_P, patternForCodeToOtherDevices, String::contains, SHORT_WAITING_DURATION);
            if (needToSendToOtherDevice.isPresent()) {
                Thread.sleep(60100);
                moveAndClick(loginByPhoneButton);
                checkAndPassCaptcha();
            }

            GetLastCodeResponse lastCodeResponse = phoneCodeHttpService.getLastCode(
                    GetLastCodeRequest.builder()
                            .number(phoneNumber)
                            .build()
            );

            moveAndClickAndFill(submitPhoneCode, lastCodeResponse.getCode());

            return new HomePage(driver, applicationContext);
        } catch (Exception e) {
            throw e;
        }
    }

    private void checkAndPassCaptcha() throws Exception {
        checkAndPassCaptcha(0);
    }

    private void checkAndPassCaptcha(int retry) throws Exception {
        if (retry > 4) {
            throw new CantSolveALotTimeCaptchaException(5);
        }
        Optional<WebElement> captcha = checkElementExist(captchaImg, SHORT_WAITING_DURATION);
        if (captcha.isPresent()) {
            Captcha solvedCaptcha = passCaptcha(captcha.get());

            Optional<WebElement> incorrectCaptchaMessage = findElemWithText(captchaIncorrect_P, captchaIncorrectText, String::contains, SHORT_WAITING_DURATION);
            if (incorrectCaptchaMessage.isPresent()) {
                solveCaptchaService.reportIncorrect(solvedCaptcha.getId());
                checkAndPassCaptcha(++retry);
            }
        }
    }

    private Captcha passCaptcha(WebElement captchaEl) throws Exception {
        Captcha captcha = solveCaptcha(captchaEl);

        moveAndClickAndFill(captchaInput, captcha.getCode());

        moveAndClick(submitCaptchaButton);

        return captcha;
    }

    private Captcha solveCaptcha(WebElement captchaImage) throws Exception {
        Captcha captcha = solveCaptchaService.buildCaptcha(captchaImage.getScreenshotAs(OutputType.BASE64));
        String requestId = solveCaptchaService.sendToSolve(captcha);
        captcha.setId(requestId);
        solveCaptchaService.waitingResult(captcha);
        return captcha;
    }

}
