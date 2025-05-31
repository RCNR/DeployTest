package openapi.openapitest.domain.stock.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openapi.openapitest.domain.stock.repository.StockCodeRepository;
import openapi.openapitest.domain.stock.repository.StocksInfoRepository;
import openapi.openapitest.dto.ResponseCurrentFinanceOutputDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
@RequiredArgsConstructor
public class FinanceService {

    private final StockCodeRepository stockCodeRepository;
    private final StocksInfoRepository stocksInfoRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


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
        httpHeaders.set("tr_id", "FHKST66430300");
        httpHeaders.set("custtype", "P");

        return httpHeaders;
    }

    public ResponseCurrentFinanceOutputDto parsingCurrentFinanceInfo(String response) {

        ResponseCurrentFinanceOutputDto data = new ResponseCurrentFinanceOutputDto();
//        log.info("resonse: {}", response);


        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode arrayNode = rootNode.path("output");
            /**
             * 한투의 재무비율 관련 API 응답을 보면  output: List[ResponseBodyoutput] 형태
             * output은 ResponseBody가 아닌 배열
             * 한 번더 열어야 한다.
             */

            JsonNode node = arrayNode.get(0);

            if (node != null) {
                ResponseCurrentFinanceOutputDto outputDto = new ResponseCurrentFinanceOutputDto();

                outputDto.setBsop_prfi_inrt(node.path("bsop_prfi_inrt").asText());
                outputDto.setEps(node.path("eps").asText());
                outputDto.setRoe_val(node.path("roe_val").asText());
                log.info("finance data EPS: {}", outputDto.getEps());

                data = outputDto;
            }
            return data;

        } catch (Exception e) {
            log.error("FinanceService Error is : {}", e.getMessage());
            throw new RuntimeException();
        }
    }





    public ResponseCurrentFinanceOutputDto getCurrentFinanceInfo(String stockCode) {

        HttpHeaders headers = createHeaders();
        String url = "https://openapi.koreainvestment.com:9443/uapi/domestic-stock/v1/finance/financial-ratio";

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("FID_DIV_CLS_CODE", "1")
                .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                .queryParam("FID_INPUT_ISCD", stockCode);

        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                httpEntity,
                String.class
        );


        return parsingCurrentFinanceInfo(response.getBody());
    }
}
