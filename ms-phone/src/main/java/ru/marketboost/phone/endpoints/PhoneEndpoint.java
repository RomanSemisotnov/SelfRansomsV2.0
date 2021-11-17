package ru.marketboost.phone.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.marketboost.library.common.exceptions.MsAlreadyExistsException;
import ru.marketboost.library.common.http.models.requests.DeletePhoneRequest;
import ru.marketboost.library.common.http.models.requests.StorePhoneRequest;
import ru.marketboost.library.common.http.models.responses.MultiplyPhoneResponse;
import ru.marketboost.library.common.http.models.responses.PhoneResponse;
import ru.marketboost.library.common.interfaces.IPhoneService;
import ru.marketboost.phone.hibernate.models.Phone;
import ru.marketboost.phone.services.PhoneService;

import java.util.stream.Collectors;

@RestController
public class PhoneEndpoint implements IPhoneService {

    @Autowired
    private PhoneService phoneService;

    @Override
    @PostMapping(value = PHONE_GET, produces = "application/json")
    public MultiplyPhoneResponse findAll() {
        return MultiplyPhoneResponse.builder()
                .phones(phoneService.findAll()
                        .stream()
                        .map(phone -> PhoneResponse.builder()
                                .id(phone.getId())
                                .number(phone.getNumber())
                                .lastCode(phone.getLastCode().orElse(null))
                                .createdAt(phone.getCreatedAt())
                                .build()
                        )
                        .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    @PostMapping(value = PHONE_PUT, produces = "application/json")
    public PhoneResponse storePhone(@RequestBody StorePhoneRequest ransomRequest) throws MsAlreadyExistsException {
        Phone phone = phoneService.storePhone(ransomRequest.getPhoneNumber());
        return PhoneResponse.builder()
                .id(phone.getId())
                .number(phone.getNumber())
                .build();
    }

    @Override
    @PostMapping(value = DELETE_PHONE_BY_ID, produces = "application/json")
    public String deletePhoneById(@RequestBody DeletePhoneRequest incommingCallRequest) {
        phoneService.deleteByIds(incommingCallRequest.getIds());
        return "ok";
    }

}
