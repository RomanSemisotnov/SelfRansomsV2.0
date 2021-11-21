package ru.marketboost.ransom.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RansomRequest {
    private String phoneNumber;
    private String search;
    private String proxyIp;
    private String proxyPort;
    private String proxyLogin;
    private String proxyPass;
    private String vendorCode;
}
