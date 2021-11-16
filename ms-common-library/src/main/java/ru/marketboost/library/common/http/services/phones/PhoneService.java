package ru.marketboost.library.common.http.services.phones;

import org.springframework.web.client.RestTemplate;
import ru.marketboost.library.common.http.models.requests.DeletePhoneRequest;
import ru.marketboost.library.common.http.models.requests.StorePhoneRequest;
import ru.marketboost.library.common.http.models.responses.MultiplyPhoneResponse;
import ru.marketboost.library.common.http.services.BaseMicroService;
import ru.marketboost.library.common.interfaces.IPhoneService;

public class PhoneService extends BaseMicroService implements IPhoneService {

    protected PhoneService(RestTemplate restTemplate) {
        super("ms-phone", restTemplate);
    }

    @Override
    public MultiplyPhoneResponse findAll() {
        return null;
    }

    @Override
    public String storePhone(StorePhoneRequest ransomRequest) {
        return null;
    }

    @Override
    public String deletePhoneById(DeletePhoneRequest incommingCallRequest) {
        return null;
    }
}
