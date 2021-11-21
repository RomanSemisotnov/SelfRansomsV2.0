package ru.marketboost.ransom.selenium.pages;

import com.twocaptcha.captcha.Captcha;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.context.ApplicationContext;
import ru.marketboost.library.common.exceptions.MsBadRequestException;
import ru.marketboost.library.common.http.models.requests.GetPhoneRequest;
import ru.marketboost.library.common.http.models.responses.LastCodeResponse;
import ru.marketboost.ransom.exceptions.AttemptsGetCodeExhaustedException;
import ru.marketboost.ransom.exceptions.CantSolveALotTimeCaptchaException;
import ru.marketboost.ransom.exceptions.CouldntGetRightPhoneCodeException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class LoginPage extends BasePage {

    private final int GET_CODE_TRY_NUMBER = 5;
    private final By phoneInput = By.xpath("//div[@class='login__phone form-block form-block--phone-dropdown']/div[@class='form-block__dropdown-wrap']/div[@class='form-block__input']/input[@class='input-item']");
    private final By loginByPhoneButton = By.id("requestCode");
    private final By captchaImg = By.className("form-block__captcha-img");
    private final By captchaInput = By.id("smsCaptchaCode");
    private final By submitCaptchaButton = By.className("login__btn");
    private final By errorAfterCaptcha_P = By.className("form-block__message--error"); // Код указан неверно
    private final By submitPhoneCode = By.className("j-input-confirm-code");

    public LoginPage(WebDriver driver, ApplicationContext applicationContext, UUID sessionId) {
        super(driver, applicationContext, sessionId);
    }

    private final By codeWasSentToOtherDevices_P = By.className("login__code-message"); // Код отправлен на другие устройства Wildberries

    public HomePage  doLogin(String phoneNumber) throws Exception {
        try {
            moveAndClickAndFill(phoneInput, formatNumber(phoneNumber));

            moveAndClick(loginByPhoneButton);

            checkAndPassCaptcha();

            String patternForCodeToOtherDevices = "Код отправлен на другие устройства Wildberries";
            Optional<WebElement> needToSendToOtherDevice = findElemWithText(codeWasSentToOtherDevices_P, patternForCodeToOtherDevices, String::contains, randomShortDuration());
            if (needToSendToOtherDevice.isPresent()) {
                Thread.sleep(60100);
                moveAndClick(loginByPhoneButton);
                checkAndPassCaptcha();
            }

            LastCodeResponse realLastCode = null;
            int tryN = 0;
            while (tryN < GET_CODE_TRY_NUMBER && realLastCode == null) {
                Thread.sleep(2500);

                Optional<LastCodeResponse> lastCodeResponse = phoneHttpService.get(
                        GetPhoneRequest.builder()
                                .number(phoneNumber)
                                .build()
                ).getLastCode();

                if (lastCodeResponse.isPresent() && lastCodeResponse.get().getCreatedAt().isAfter(Instant.now().minusMillis(15 * 1000))) {
                    realLastCode = lastCodeResponse.get();
                }

                tryN++;
            }

            if (realLastCode == null) {
                throw new CouldntGetRightPhoneCodeException(sessionId, GET_CODE_TRY_NUMBER);
            }

            moveAndClickAndFill(submitPhoneCode, realLastCode.getCode());

            return new HomePage(driver, applicationContext, sessionId);
        } catch (Exception e) {
            throw e;
        }
    }

    private void checkAndPassCaptcha() throws Exception {
        checkAndPassCaptcha(0);
    }

    private void checkAndPassCaptcha(int retry) throws Exception {
        if (retry > 4) {
            throw new CantSolveALotTimeCaptchaException(sessionId, 5);
        }
        Optional<WebElement> captcha = checkElementExist(captchaImg, randomShortDuration());
        if (captcha.isPresent()) {
            Captcha solvedCaptcha = passCaptcha(captcha.get());

            String captchaIncorrectText = "Код указан неверно";
            Optional<WebElement> incorrectCaptchaMessage = findElemWithText(errorAfterCaptcha_P, captchaIncorrectText, String::contains, randomShortDuration());
            if (incorrectCaptchaMessage.isPresent()) {
                solveCaptchaService.reportIncorrect(solvedCaptcha.getId());
                checkAndPassCaptcha(++retry);
            }
            String attemptsGetCodeExhausted = "Вы исчерпали все попытки запроса кода";
            Optional<WebElement> attemptsGetCodeExhaustedEl = findElemWithText(errorAfterCaptcha_P, attemptsGetCodeExhausted, String::contains, randomShortDuration());
            if (attemptsGetCodeExhaustedEl.isPresent()) {
                throw new AttemptsGetCodeExhaustedException(sessionId);
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

    private String formatNumber(String need2format) throws MsBadRequestException {
        if (need2format.length() != 10) {
            if (need2format.startsWith("+7")) {
                return need2format.substring(2);
            } else if (need2format.startsWith("8")) {
                return need2format.substring(1);
            } else {
                throw new MsBadRequestException("phone number must be 10 length, start with +7 or 8");
            }
        }
        return need2format;
    }

}
