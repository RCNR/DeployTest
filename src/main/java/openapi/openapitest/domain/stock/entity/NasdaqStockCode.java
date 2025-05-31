package openapi.openapitest.domain.stock.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NasdaqStockCode {

    @Id
    private String code;

    public NasdaqStockCode(String code) {
        this.code = code;
    }

}
