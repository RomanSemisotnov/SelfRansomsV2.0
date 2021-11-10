package ru.marketboost.phone.interfaces;

import ru.marketboost.phone.models.requests.DeletePhoneRequest;
import ru.marketboost.phone.models.requests.IncommingCallRequest;
import ru.marketboost.phone.models.requests.StorePhoneRequest;
import ru.marketboost.phone.models.responses.PhoneResponse;

public interface IPhoneService {

    String PHONE = "/api/v1/phone";
    String DELETE_PHONE_BY_ID = "/api/v1/phone/delete";
    String INCOMING_CALL = "/api/v1/incomming/call";

    PhoneResponse findAll();
    String storePhone(StorePhoneRequest ransomRequest);
    String incommingCall(IncommingCallRequest incommingCallRequest);
    String deletePhoneById(DeletePhoneRequest incommingCallRequest);

}
