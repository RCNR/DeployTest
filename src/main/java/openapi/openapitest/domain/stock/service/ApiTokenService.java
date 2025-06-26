package openapi.openapitest.domain.stock.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openapi.openapitest.domain.stock.repository.ApiTokenStore;
import openapi.openapitest.dto.ResponseTuzaAccessTokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 한투 API에서 AccessToken을 발급받고 저장
 */
@Service
@Slf4j
public class ApiTokenService {

    private final ApiTokenStore apiTokenStore;
    private final RestTemplate restTemplate;

//    @Value("${tuza.api.APP_KEY}")
    private final String appKey;

//    @Value("${tuza.api.APP_SECRET_KEY}")
    private final String appSecret;

//    @Value("${tuza.api.URL}")
    private final String tokenUrl;

    public ApiTokenService(ApiTokenStore apiTokenStore, RestTemplate restTemplate, @Value("${tuza.api.APP_KEY}") String appKey, @Value("${tuza.api.APP_SECRET_KEY}") String appSecret,  @Value("${tuza.api.URL}") String tokenUrl) {
        this.apiTokenStore = apiTokenStore;
        this.restTemplate = restTemplate;
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.tokenUrl = tokenUrl;
    }

    public boolean refreshAccessToken() {

        try {
            log.info("HanTu Access Token 갱신 시작");

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = new HashMap<>();
            body.put("grant_type", "client_credentials");
            body.put("appkey", appKey);
            body.put("appsecret", appSecret);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, httpHeaders);

            ResponseEntity<ResponseTuzaAccessTokenDto> response = restTemplate.postForEntity(tokenUrl, request, ResponseTuzaAccessTokenDto.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                apiTokenStore.saveAccessToken(response.getBody().getAccess_token());
                log.info("HanTU Access Token 갱신 완료");
                return true;
            }

            log.error("갱신 실패 - 응답이 잘못됨");
            return false;
        } catch (Exception e) {
            log.error("갱신 중 오류 발생", e);
            return false;
        }
    }

    public String getCurrentAccessToken() {
        return apiTokenStore.getAccessToken();
    }


}
