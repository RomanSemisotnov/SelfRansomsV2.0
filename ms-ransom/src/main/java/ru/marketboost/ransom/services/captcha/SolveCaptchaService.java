package ru.marketboost.ransom.services.captcha;

import com.twocaptcha.TwoCaptcha;
import com.twocaptcha.captcha.Captcha;
import com.twocaptcha.captcha.Normal;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
public class SolveCaptchaService {

    private TwoCaptcha solver;

    @PostConstruct
    public void init() {
        String ruCaptchaApiKey = "1d22e019e788ffb647b71f9a28b15dbb";
        solver = new TwoCaptcha(ruCaptchaApiKey);
        solver.setCallback("https://your.site/result-receiver");
        solver.setDefaultTimeout(80);
        solver.setPollingInterval(5);
    }

    public void waitingResult(Captcha captcha) throws Exception {
        solver.waitForResult(captcha, Map.of());
    }

    public String sendToSolve(Captcha captcha) throws Exception {
        return solver.send(captcha);
    }

    public void reportIncorrect(String id) throws Exception {
        solver.report(id, false);
    }

    public double getBalance() throws Exception {
        return solver.balance();
    }

    public Captcha buildCaptcha(String base64) {
        Normal captcha = new Normal();
        captcha.setBase64(base64);
        captcha.setNumeric(4);
        captcha.setMinLen(4);
        captcha.setMaxLen(12);
        captcha.setPhrase(false);
        captcha.setCaseSensitive(false);
        captcha.setCalc(false);
        captcha.setLang("en");
        return captcha;
    }

}
