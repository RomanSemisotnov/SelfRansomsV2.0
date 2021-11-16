package ru.marketboost.library.common.http.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncommingCallRequest {
    private String event;
    private String call_start;
    private String pbx_call_id;
    private String caller_id;
    private String called_did;
}
