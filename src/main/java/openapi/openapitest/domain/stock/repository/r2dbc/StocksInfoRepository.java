package openapi.openapitest.domain.stock.repository.r2dbc;

import openapi.openapitest.domain.stock.entity.r2dbc.StockInfo;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface StocksInfoRepository extends ReactiveCrudRepository<StockInfo, Long> {
}
