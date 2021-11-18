package ru.marketboost.phone.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.marketboost.library.common.exceptions.MsModelNotFoundException;
import ru.marketboost.library.common.http.models.requests.IncommingCallRequest;
import ru.marketboost.library.common.interfaces.IPhoneCodeService;
import ru.marketboost.phone.services.PhoneCodeService;

import java.util.Objects;

@RestController
public class PhoneCodeEndpoint implements IPhoneCodeService {


    @Autowired
    private PhoneCodeService phoneCodeService;

    @Override
    @GetMapping(value = INCOMING_CALL, produces = "application/json")
    public String testForZadarma(@RequestParam(name = "zd_echo") String zd_echo) {
        return zd_echo;
    }

    @Override
    @PostMapping(value = INCOMING_CALL, produces = "application/json")
    public String incommingCall(@RequestBody IncommingCallRequest ransomRequest) throws MsModelNotFoundException {
        if (!Objects.equals(ransomRequest.getEvent(), "NOTIFY_START")) {
            return "event must be 'NOTIFY_START'";
        }
        phoneCodeService.setLast4DigitsToPhone(ransomRequest.getCalled_did(), ransomRequest.getCaller_id());

        return "ok";
    }

}
