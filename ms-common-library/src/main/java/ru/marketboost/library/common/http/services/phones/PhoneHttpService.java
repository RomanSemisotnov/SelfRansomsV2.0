package ru.marketboost.library.common.http.services.phones;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.marketboost.library.common.exceptions.MicroServiceException;
import ru.marketboost.library.common.http.models.requests.DeletePhoneRequest;
import ru.marketboost.library.common.http.models.requests.StorePhoneRequest;
import ru.marketboost.library.common.http.models.responses.MultiplyPhoneResponse;
import ru.marketboost.library.common.http.models.responses.PhoneResponse;
import ru.marketboost.library.common.http.services.BaseMicroService;
import ru.marketboost.library.common.interfaces.IPhoneService;

@Service
public class PhoneHttpService extends BaseMicroService implements IPhoneService {

    protected PhoneHttpService(RestTemplate restTemplate) {
        super("localhost:8080", restTemplate);
    }

    @Override
    public MultiplyPhoneResponse findAll() throws MicroServiceException {
        return retry(() -> restTemplate.postForEntity(buildUrl(PHONE_GET), new Object(), MultiplyPhoneResponse.class).getBody());
    }

    @Override
    public PhoneResponse storePhone(StorePhoneRequest request) throws MicroServiceException {
        return retry(() -> restTemplate.postForEntity(buildUrl(PHONE_PUT), request, PhoneResponse.class).getBody());
    }

    @Override
    public String deletePhoneById(DeletePhoneRequest request) throws MicroServiceException {
        return retry(() -> restTemplate.postForEntity(buildUrl(DELETE_PHONE_BY_ID), request, String.class).getBody());
    }

}
