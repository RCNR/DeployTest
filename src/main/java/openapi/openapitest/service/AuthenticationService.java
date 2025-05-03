package openapi.openapitest.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final String authToken;

    public AuthenticationService(@Value("${INVESTMENT_API_KEY") String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return this.authToken;
    }
}
