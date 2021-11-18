package ru.marketboost.library.common.http.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneResponse {
    private Long id;
    private String number;
    private Optional<LastCodeResponse> lastCode;
    private Instant createdAt;
}
