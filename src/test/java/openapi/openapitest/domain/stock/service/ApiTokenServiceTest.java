package openapi.openapitest.domain.stock.service;

import openapi.openapitest.domain.stock.ApiTokenInMemoryStore;
import openapi.openapitest.domain.stock.repository.ApiTokenStore;
import openapi.openapitest.dto.ResponseTuzaAccessTokenDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/*@TestPropertySource(properties = {
        "APP_KEY=test-app-key",
        "APP_SECRET_KEY=test-secret-key",
        "URL=test_url"
}) --> @ExtendWith 사용하면 적용이 되지 않는다. 그래서 ApiTokenService에서 생성자 주입을 적용하였다. */
@ExtendWith(MockitoExtension.class)
class ApiTokenServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private ApiTokenService apiTokenService;
    private ApiTokenStore apiTokenStore;

    @BeforeEach
    void before() {
        apiTokenStore = new ApiTokenInMemoryStore();
        apiTokenService = new ApiTokenService(apiTokenStore, restTemplate, "test-app-key","test-secret-key", "test-url");
    }

    @Test
    void 토큰X () {

        String currentAccessToken = apiTokenService.getCurrentAccessToken();

        assertThat(currentAccessToken).isNull();
    }

    @Test
    void 액세스토큰갱신성공 () {

        // given
        ResponseTuzaAccessTokenDto accessTokenResponse = ResponseTuzaAccessTokenDto.builder()
                .access_token("new-access-token").build();

        when(restTemplate.postForEntity(anyString(), any(), eq(ResponseTuzaAccessTokenDto.class)))
                .thenReturn((ResponseEntity<ResponseTuzaAccessTokenDto>) new ResponseEntity<>(accessTokenResponse, HttpStatus.OK));

        // when
        boolean result = apiTokenService.refreshAccessToken();

        // then
        assertThat(result).isTrue();
        assertThat(apiTokenService.getCurrentAccessToken()).isEqualTo("new-access-token");
    }





}