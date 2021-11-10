package ru.marketboost.phone.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.marketboost.phone.interfaces.IPhoneService;
import ru.marketboost.phone.models.requests.DeletePhoneRequest;
import ru.marketboost.phone.models.requests.IncommingCallRequest;
import ru.marketboost.phone.models.requests.StorePhoneRequest;
import ru.marketboost.phone.models.responses.PhoneResponse;
import ru.marketboost.phone.services.PhoneService;

import java.util.Objects;

@RestController
public class PhoneEndpoint implements IPhoneService {

    @Autowired
    private PhoneService phoneService;

    @Override
    @PostMapping(value = INCOMING_CALL, produces = "application/json")
    public String incommingCall(@RequestBody IncommingCallRequest ransomRequest) {
        if(!Objects.equals(ransomRequest.getEvent(), "NOTIFY_START")) {
            return "event must be 'NOTIFY_START'";
        }
        phoneService.setLast4DigitsToPhone(ransomRequest.getCalled_did(), ransomRequest.getCaller_id());

        return "ok";
    }

    @Override
    @GetMapping(value = PHONE, produces = "application/json")
    public PhoneResponse findAll() {
        return PhoneResponse.builder()
                .phones(phoneService.findAll())
                .build();
    }

    @Override
    @PostMapping(value = PHONE, produces = "application/json")
    public String storePhone(@RequestBody StorePhoneRequest ransomRequest) {
        phoneService.storePhone(ransomRequest.getPhoneNumber());

        return "ok";
    }

    @Override
    @PostMapping(value = DELETE_PHONE_BY_ID, produces = "application/json")
    public String deletePhoneById(@RequestBody DeletePhoneRequest incommingCallRequest) {
        phoneService.deleteByIds(incommingCallRequest.getIds());
        return "ok";
    }


}
