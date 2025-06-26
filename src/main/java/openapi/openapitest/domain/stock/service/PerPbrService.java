package openapi.openapitest.domain.stock.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openapi.openapitest.domain.stock.entity.StockCode;
import openapi.openapitest.domain.stock.entity.StockInfo;
import openapi.openapitest.domain.stock.repository.StockCodeRepository;
import openapi.openapitest.domain.stock.repository.StocksInfoRepository;
import openapi.openapitest.dto.ResponseCurrentFinanceOutputDto;
import openapi.openapitest.dto.ResponseCurrentPerPbrOutputDto;
import openapi.openapitest.dto.StockInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PerPbrService {

    private final StockCodeRepository stockCodeRepository;
    private final StocksInfoRepository stocksInfoRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final FinanceService financeService;


    @Value("${tuza.api.APP_KEY}")
    private String appKey;

    @Value("${tuza.api.APP_SECRET_KEY}")
    private String appSecret;

    @Value("${tuza.api.ACCESS_TOKEN}")
    private String accessToken;

    private HttpHeaders createHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(accessToken);
        httpHeaders.set("appkey", appKey);
        httpHeaders.set("appsecret", appSecret);
        httpHeaders.set("tr_id", "FHKST01010100");
        httpHeaders.set("custtype", "P");

        return httpHeaders;
    }

    private ResponseCurrentPerPbrOutputDto parsingCurrentPerPbrInfo(String response) {
        ResponseCurrentPerPbrOutputDto data = new ResponseCurrentPerPbrOutputDto();
        log.info(response);

        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode node = rootNode.path("output");

//            log.info("outputNode : {} ", node);
//            log.info("outputNodeSize : {}", node.size());


            if (node != null) {
                ResponseCurrentPerPbrOutputDto outputDto = new ResponseCurrentPerPbrOutputDto();

                outputDto.setStck_prpr(node.path("stck_prpr").asText());
                outputDto.setPer(node.path("per").asText());
                outputDto.setPbr(node.path("pbr").asText());
                outputDto.setStck_shrn_iscd(node.path("stck_shrn_iscd").asText());
                outputDto.setBstp_kor_isnm(node.path("bstp_kor_isnm").asText());
                data = outputDto;
            }
            return data;
        } catch (Exception e) {
            log.error("error is : {}", e.getMessage());
            throw new RuntimeException();
        }
    }

    public ResponseCurrentPerPbrOutputDto getCurrentPerPbrInfo(String stockCode) {

        HttpHeaders header = createHeaders();
        String url = "https://openapi.koreainvestment.com:9443/uapi/domestic-stock/v1/quotations/inquire-price";

        HttpEntity<?> httpEntity = new HttpEntity<>(header);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                .queryParam("FID_INPUT_ISCD", stockCode);

        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                httpEntity,
                String.class
        );

        return parsingCurrentPerPbrInfo(response.getBody());

    }

    public void createCurrentStocksInfo() {
        List<StockCode> stockCodeList = stockCodeRepository.findAll();

        stocksInfoRepository.deleteAll();

        for (StockCode stockCode : stockCodeList) {
            try {

                Thread.sleep(100);

                ResponseCurrentPerPbrOutputDto currentPerPbrOutputDto = getCurrentPerPbrInfo(stockCode.getCode());
                ResponseCurrentFinanceOutputDto currentFinanceOutputDto = financeService.getCurrentFinanceInfo(stockCode.getCode());

                log.info("PER: {}", currentPerPbrOutputDto.getPer());
                log.info("EPS: {}", currentFinanceOutputDto.getEps());


                StockInfoDto stockInfoDto = new StockInfoDto();
                StockInfo entity = stockInfoDto.toEntity(currentPerPbrOutputDto, currentFinanceOutputDto);

                stocksInfoRepository.save(entity);

                log.info("Saved stocks is : {}", stockCode.getCode());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 현재 스레드 인터럽트 상태 복구
                log.info("Thread Interrupted : {}", e.getMessage());
                break;
            } catch (Exception e) {
                log.info("Error stock code is {} : {} and pass!", stockCode.getCode(), e.getMessage());
            }

        }
    }

}
