package openapi.openapitest.domain.stock.repository;

import openapi.openapitest.domain.stock.entity.NasdaqStockCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NasdaqCodeRepository extends JpaRepository<NasdaqStockCode, Long> {
}
