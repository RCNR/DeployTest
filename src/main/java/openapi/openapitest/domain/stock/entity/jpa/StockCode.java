package openapi.openapitest.domain.stock.entity.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class StockCode {

    @Id
    private String code;

    public StockCode(String code) {
        this.code = code;
    }
}