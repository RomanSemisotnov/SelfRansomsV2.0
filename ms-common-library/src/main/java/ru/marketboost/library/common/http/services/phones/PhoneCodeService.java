package ru.marketboost.library.common.http.services.phones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import ru.marketboost.library.common.http.models.requests.GetLastCodeRequest;
import ru.marketboost.library.common.http.models.requests.IncommingCallRequest;
import ru.marketboost.library.common.http.models.responses.GetLastCodeResponse;
import ru.marketboost.library.common.http.services.BaseMicroService;
import ru.marketboost.library.common.interfaces.IPhoneCodeService;

public class PhoneCodeService extends BaseMicroService implements IPhoneCodeService {


    @Autowired
    protected PhoneCodeService(RestTemplate restTemplate) {
        super("ms-phone", restTemplate);
    }

    @Override
    public GetLastCodeResponse getLastCode(GetLastCodeRequest incommingCallRequest) {
        return null;
    }

    @Override
    public String incommingCall(IncommingCallRequest incommingCallRequest) {
        return null;
    }

}
