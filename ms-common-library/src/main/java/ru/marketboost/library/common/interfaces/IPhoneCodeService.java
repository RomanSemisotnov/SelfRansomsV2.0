package ru.marketboost.library.common.interfaces;

import ru.marketboost.library.common.http.models.requests.GetLastCodeRequest;
import ru.marketboost.library.common.http.models.requests.IncommingCallRequest;
import ru.marketboost.library.common.http.models.responses.GetLastCodeResponse;

public interface IPhoneCodeService {



    String GET_LAST_CODE = "/api/v1/code/last";
    String INCOMING_CALL = "/api/v1/code/incomming";


    GetLastCodeResponse getLastCode(GetLastCodeRequest incommingCallRequest) throws Exception;
    String incommingCall(IncommingCallRequest incommingCallRequest);



}