package openapi.openapitest.domain.stock.repository.jpa;

import openapi.openapitest.domain.stock.entity.jpa.StockCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockCodeRepository extends JpaRepository<StockCode, String> {

}
