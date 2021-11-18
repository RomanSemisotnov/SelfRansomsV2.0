package ru.marketboost.library.common.interfaces;

import ru.marketboost.library.common.exceptions.MicroServiceException;
import ru.marketboost.library.common.http.models.requests.IncommingCallRequest;

public interface IPhoneCodeService {



    String INCOMING_CALL = "/api/v1/code/incomming";


    String testForZadarma(String param);
    String incommingCall(IncommingCallRequest incommingCallRequest) throws MicroServiceException;


}
