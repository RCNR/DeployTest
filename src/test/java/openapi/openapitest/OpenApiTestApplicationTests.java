package openapi.openapitest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "APP_KEY=test-app-key",
        "APP_SECRET_KEY=test-secret-key",
        "URL=test_url"
})
class OpenApiTestApplicationTests {

    @Test
    void contextLoads() {

    }

}
