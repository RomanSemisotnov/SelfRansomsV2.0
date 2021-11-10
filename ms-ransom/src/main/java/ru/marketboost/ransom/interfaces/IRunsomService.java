package ru.marketboost.ransom.interfaces;

import ru.marketboost.ransom.models.requests.RansomRequest;

public interface IRunsomService {

    String MAKE_RANSOM = "/api/v1/ransom";

    String make(RansomRequest ransomRequest);

}
