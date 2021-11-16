package ru.marketboost.library.common.interfaces;

import ru.marketboost.library.common.http.models.requests.DeletePhoneRequest;
import ru.marketboost.library.common.http.models.requests.StorePhoneRequest;
import ru.marketboost.library.common.http.models.responses.MultiplyPhoneResponse;

public interface IPhoneService {

    String PHONE = "/api/v1/phone";
    String DELETE_PHONE_BY_ID = "/api/v1/phone/delete";

    MultiplyPhoneResponse findAll();
    String storePhone(StorePhoneRequest ransomRequest);
    String deletePhoneById(DeletePhoneRequest incommingCallRequest);

}
