package openapi.openapitest.domain.stock.repository;

import openapi.openapitest.domain.stock.entity.StockInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StocksInfoRepository extends JpaRepository<StockInfo, Long> {
}
