package openapi.openapitest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableR2dbcAuditing
@EnableR2dbcRepositories(basePackages = "openapi.openapitest.domain.stock.repository.r2dbc")

@EnableJpaRepositories(basePackages = "openapi.openapitest.domain.stock.repository.jpa")
@Import(DataSourceAutoConfiguration.class)
public class OpenApiTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenApiTestApplication.class, args);
    }

}
