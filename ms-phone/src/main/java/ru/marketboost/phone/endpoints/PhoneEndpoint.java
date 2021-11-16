package ru.marketboost.phone.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.marketboost.library.common.http.models.requests.DeletePhoneRequest;
import ru.marketboost.library.common.http.models.requests.StorePhoneRequest;
import ru.marketboost.library.common.http.models.responses.MultiplyPhoneResponse;
import ru.marketboost.library.common.http.models.responses.PhoneResponse;
import ru.marketboost.library.common.interfaces.IPhoneService;
import ru.marketboost.phone.services.PhoneService;

import java.util.stream.Collectors;

@RestController
public class PhoneEndpoint implements IPhoneService {

    @Autowired
    private PhoneService phoneService;

    @Override
    @GetMapping(value = PHONE, produces = "application/json")
    public MultiplyPhoneResponse findAll() {
        return MultiplyPhoneResponse.builder()
                .phones(phoneService.findAll()
                        .stream()
                        .map(phone -> PhoneResponse.builder().number(phone.getNumber()).build())
                        .collect(Collectors.toList())
                )
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
