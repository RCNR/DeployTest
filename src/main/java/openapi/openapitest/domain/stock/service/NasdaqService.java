package openapi.openapitest.domain.stock.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openapi.openapitest.domain.stock.entity.NasdaqStockCode;
import openapi.openapitest.domain.stock.entity.NasdaqStockInfo;
import openapi.openapitest.domain.stock.repository.NasdaqCodeRepository;
import openapi.openapitest.domain.stock.repository.NasdaqStocksInfoRepository;
import openapi.openapitest.dto.StockInfoDto;
import openapi.openapitest.dto.nasdaqDto.NasdaqDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NasdaqService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final NasdaqCodeRepository nasdaqCodeRepository;
    private final NasdaqStocksInfoRepository nasdaqStocksInfoRepository;


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
        httpHeaders.set("tr_id", "HHDFS76200200");
        httpHeaders.set("custtype", "P");

        return httpHeaders;
    }

    private NasdaqDto.NasdaqInfoDto parsingCurrentNasdaqInfo(String response, String stockCode) {
        NasdaqDto.NasdaqInfoDto data = new NasdaqDto.NasdaqInfoDto();

        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode node = rootNode.path("output");

            if (node != null) {
                NasdaqDto.NasdaqInfoDto outputDto = new NasdaqDto.NasdaqInfoDto();

                outputDto.setCode(stockCode);
                outputDto.setPerx(node.path("perx").asText());
                outputDto.setPbrx(node.path("pbrx").asText());
                outputDto.setEpsx(node.path("epsx").asText());
                if (node.path("e_icod").asText().isEmpty()) outputDto.setE_icod(null);
                else outputDto.setE_icod(node.path("e_icod").asText());

                data = outputDto;
            }
            return data;
        } catch (Exception e) {
            log.error("error is : {}", e.getMessage());
            throw new RuntimeException();
        }
    }

    public NasdaqDto.NasdaqInfoDto getCurrentNasdaqInfo(String stockCode) {

        HttpHeaders header = createHeaders();
        String url = "https://openapi.koreainvestment.com:9443/uapi/overseas-price/v1/quotations/price-detail";

        HttpEntity<?> httpEntity = new HttpEntity<>(header);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("AUTH", "")
                .queryParam("EXCD", "NAS")
                .queryParam("SYMB", stockCode);

        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                httpEntity,
                String.class
        );

        return parsingCurrentNasdaqInfo(response.getBody(), stockCode);

    }

    public void createNasdaqStocksInfo() {
        List<NasdaqStockCode> stockCodeList = nasdaqCodeRepository.findAll();

        nasdaqStocksInfoRepository.deleteAll();

        for (NasdaqStockCode stockCode : stockCodeList) {
            try {

                Thread.sleep(100);

                NasdaqDto.NasdaqInfoDto currentNasdaqInfo = getCurrentNasdaqInfo(stockCode.getCode());

                log.info("PER: {}", currentNasdaqInfo.getPerx());
                log.info("EPS: {}", currentNasdaqInfo.getEpsx());

                NasdaqStockInfo entity = currentNasdaqInfo.toEntity(currentNasdaqInfo);

                nasdaqStocksInfoRepository.save(entity);

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
