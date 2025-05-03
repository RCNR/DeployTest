package openapi.openapitest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class callStockInformation {

    private final AuthenticationService authenticationService;
    private final RestTemplate restTemplate;
}
