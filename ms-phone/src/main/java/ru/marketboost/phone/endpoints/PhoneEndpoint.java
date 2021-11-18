package ru.marketboost.phone.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.marketboost.library.common.exceptions.MsAlreadyExistsException;
import ru.marketboost.library.common.exceptions.MsModelNotFoundException;
import ru.marketboost.library.common.http.models.requests.DeletePhoneRequest;
import ru.marketboost.library.common.http.models.requests.GetPhoneRequest;
import ru.marketboost.library.common.http.models.requests.StorePhoneRequest;
import ru.marketboost.library.common.http.models.responses.LastCodeResponse;
import ru.marketboost.library.common.http.models.responses.MultiplyPhoneResponse;
import ru.marketboost.library.common.http.models.responses.PhoneResponse;
import ru.marketboost.library.common.interfaces.IPhoneService;
import ru.marketboost.phone.hibernate.models.Phone;
import ru.marketboost.phone.hibernate.models.PhoneCode;
import ru.marketboost.phone.services.PhoneService;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class PhoneEndpoint implements IPhoneService {

    @Autowired
    private PhoneService phoneService;

    @Override
    @PostMapping(value = PHONE_ALL, produces = "application/json")
    public MultiplyPhoneResponse findAll() {
        return MultiplyPhoneResponse.builder()
                .phones(phoneService.findAll()
                        .stream()
                        .map(phone -> PhoneResponse.builder()
                                .id(phone.getId())
                                .number(phone.getNumber())
                                .lastCode(lastCode2response(phone.getLastCode()))
                                .createdAt(phone.getCreatedAt())
                                .build()
                        )
                        .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    @PostMapping(value = PHONE_GET, produces = "application/json")
    public PhoneResponse get(@RequestBody GetPhoneRequest request) throws MsModelNotFoundException {
        Phone phone = phoneService.getNumber(request.getNumber());

        return PhoneResponse.builder()
                .id(phone.getId())
                .number(phone.getNumber())
                .lastCode(lastCode2response(phone.getLastCode()))
                .createdAt(phone.getCreatedAt())
                .build();
    }

    @Override
    @PostMapping(value = PHONE_PUT, produces = "application/json")
    public PhoneResponse storePhone(@RequestBody StorePhoneRequest ransomRequest) throws MsAlreadyExistsException {
        Phone phone = phoneService.storePhone(ransomRequest.getPhoneNumber());
        return PhoneResponse.builder()
                .id(phone.getId())
                .number(phone.getNumber())
                .createdAt(phone.getCreatedAt())
                .build();
    }

    @Override
    @PostMapping(value = DELETE_PHONE_BY_ID, produces = "application/json")
    public String deletePhoneById(@RequestBody DeletePhoneRequest incommingCallRequest) {
        phoneService.deleteByIds(incommingCallRequest.getIds());
        return "ok";
    }

    private Optional<LastCodeResponse> lastCode2response(Optional<PhoneCode> phoneCode) {
        return phoneCode.map(code -> LastCodeResponse.builder()
                .id(code.getId())
                .code(code.getCode())
                .createdAt(code.getCreatedAt())
                .build());
    }

}
