package ru.marketboost.phone.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.marketboost.library.common.http.models.requests.GetLastCodeRequest;
import ru.marketboost.library.common.http.models.requests.IncommingCallRequest;
import ru.marketboost.library.common.http.models.responses.GetLastCodeResponse;
import ru.marketboost.library.common.interfaces.IPhoneCodeService;
import ru.marketboost.phone.services.PhoneCodeService;

import java.util.Objects;

@RestController
public class PhoneCodeEndpoint implements IPhoneCodeService {


    @Autowired
    private PhoneCodeService phoneCodeService;

    @Override
    @GetMapping(value = GET_LAST_CODE, produces = "application/json")
    public GetLastCodeResponse getLastCode(GetLastCodeRequest incommingCallRequest) throws Exception {
        try {
            String code = phoneCodeService.getLastCode(incommingCallRequest.getNumber());
            return GetLastCodeResponse.builder()
                    .id(incommingCallRequest.getId())
                    .number(incommingCallRequest.getNumber())
                    .code(code)
                    .build();
        } catch (Exception e) {
            throw new Exception("");
        }
    }


    @Override
    @PostMapping(value = INCOMING_CALL, produces = "application/json")
    public String incommingCall(@RequestBody IncommingCallRequest ransomRequest) {
        try {
            if (!Objects.equals(ransomRequest.getEvent(), "NOTIFY_START")) {
                return "event must be 'NOTIFY_START'";
            }
            phoneCodeService.setLast4DigitsToPhone(ransomRequest.getCalled_did(), ransomRequest.getCaller_id());

            return "ok";
        } catch (Exception e) {
            return "not ok";
        }
    }

}
