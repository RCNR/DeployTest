package openapi.openapitest.domain.stock.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import openapi.openapitest.domain.stock.repository.jpa.StockCodeRepository;
import openapi.openapitest.domain.stock.repository.r2dbc.StocksInfoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@Transactional
public class CreateStocksInfoService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final StockCodeRepository stockCodeRepository;
    private final StocksInfoRepository stocksInfoRepository;

    @Autowired
    public CreateStocksInfoService(WebClient.Builder webClient, ObjectMapper objectMapper, StockCodeRepository stockCodeRepository, StocksInfoRepository stocksInfoRepository) {
        this.webClient = webClient.baseUrl("https://openapi.koreainvestment.com:9443").build();
        this.objectMapper = objectMapper;
        this.stockCodeRepository = stockCodeRepository;
        this.stocksInfoRepository = stocksInfoRepository;
    }


}
