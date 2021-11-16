package ru.marketboost.client.endpoints;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RansomEndpoint {


    @GetMapping(value = "/api/v1/ransom")
    public String makeRansom() {
        return "ok";
    }


}
