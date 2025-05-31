package openapi.openapitest.domain.stock.repository;

import openapi.openapitest.domain.stock.entity.StockCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockCodeRepository extends JpaRepository<StockCode, String> {

}
