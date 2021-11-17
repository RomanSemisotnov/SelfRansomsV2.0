package ru.marketboost.library.common.interfaces;

import ru.marketboost.library.common.exceptions.MicroServiceException;
import ru.marketboost.library.common.http.models.requests.DeletePhoneRequest;
import ru.marketboost.library.common.http.models.requests.StorePhoneRequest;
import ru.marketboost.library.common.http.models.responses.MultiplyPhoneResponse;
import ru.marketboost.library.common.http.models.responses.PhoneResponse;

public interface IPhoneService {

    String PHONE_GET = "/api/v1/phone/get";
    String PHONE_PUT = "/api/v1/phone/add";
    String DELETE_PHONE_BY_ID = "/api/v1/phone/delete";

    MultiplyPhoneResponse findAll() throws MicroServiceException;
    PhoneResponse storePhone(StorePhoneRequest ransomRequest) throws MicroServiceException;
    String deletePhoneById(DeletePhoneRequest incommingCallRequest) throws MicroServiceException;

}
