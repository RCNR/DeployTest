package openapi.openapitest.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseTuzaAccessTokenDto {

    private String access_token;
    private String token_type;
    private Long expires_in;
    private String access_token_token_expired;
}
