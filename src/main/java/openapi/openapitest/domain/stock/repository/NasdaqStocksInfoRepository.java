package openapi.openapitest.domain.stock.repository;

import openapi.openapitest.domain.stock.entity.NasdaqStockInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NasdaqStocksInfoRepository extends JpaRepository<NasdaqStockInfo, Long> {
}
