package openapi.openapitest.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
public class RequestTuzaAccessTokenDto {

    private String grantType;
    private String appkey;
    private String appsecret;
}
