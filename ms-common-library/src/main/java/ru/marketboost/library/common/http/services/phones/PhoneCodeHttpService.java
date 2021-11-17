package ru.marketboost.library.common.http.services.phones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.marketboost.library.common.exceptions.MicroServiceException;
import ru.marketboost.library.common.http.models.requests.GetLastCodeRequest;
import ru.marketboost.library.common.http.models.requests.IncommingCallRequest;
import ru.marketboost.library.common.http.models.responses.GetLastCodeResponse;
import ru.marketboost.library.common.http.services.BaseMicroService;
import ru.marketboost.library.common.interfaces.IPhoneCodeService;

@Service
public class PhoneCodeHttpService extends BaseMicroService implements IPhoneCodeService {


    @Autowired
    protected PhoneCodeHttpService(RestTemplate restTemplate) {
        super("localhost:8080", restTemplate);
    }

    @Override
    public GetLastCodeResponse getLastCode(GetLastCodeRequest request) throws MicroServiceException {
        return retry(() -> restTemplate.postForEntity(buildUrl(GET_LAST_CODE), request, GetLastCodeResponse.class).getBody());
    }

    @Override
    public String incommingCall(IncommingCallRequest request) throws MicroServiceException {
        return retry(() -> restTemplate.postForEntity(buildUrl(INCOMING_CALL), request, String.class).getBody());
    }

}
