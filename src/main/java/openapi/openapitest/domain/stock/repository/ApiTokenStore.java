package openapi.openapitest.domain.stock.repository;

public interface ApiTokenStore {

    void saveAccessToken(String accessToken);

    String getAccessToken();

}
