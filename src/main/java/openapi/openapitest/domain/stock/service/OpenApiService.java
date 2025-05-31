package openapi.openapitest.domain.stock.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openapi.openapitest.domain.stock.entity.StockInfo;
import openapi.openapitest.domain.stock.repository.StocksInfoRepository;
import openapi.openapitest.domain.stock.repository.StockCodeRepository;
import openapi.openapitest.dto.RequestTuzaAccessTokenDto;
import openapi.openapitest.dto.ResponseCurrentPerPbrOutputDto;
import openapi.openapitest.dto.ResponseRankingOutputDto;
import openapi.openapitest.dto.ResponseTuzaAccessTokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OpenApiService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final StockCodeRepository stockCodeRepository;
    private final StocksInfoRepository stocksInfoRepository;
    private final RestTemplate restTemplate;


    @Value("${APP_KEY}")
    private String appKey;

    @Value("${APP_SECRET_KEY}")
    private String appSecret;

    @Value("${ACCESS_TOKEN}")
    private String accessToken;

    @Autowired
    public OpenApiService(WebClient.Builder webClient, ObjectMapper objectMapper, StockCodeRepository stockCodeRepository, StocksInfoRepository stocksInfoRepository, RestTemplate restTemplate) {
        this.webClient = webClient.baseUrl("https://openapi.koreainvestment.com:9443").build();
        this.objectMapper = objectMapper;
        this.stockCodeRepository = stockCodeRepository;
        this.stocksInfoRepository = stocksInfoRepository;
        this.restTemplate = restTemplate;
    }

    public ResponseTuzaAccessTokenDto getTuzaAcessToken(@RequestBody RequestTuzaAccessTokenDto dto) {

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://openapi.koreainvestment.com:9443/oauth2/tokenP";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("grant_type", dto.getGrantType());
        body.put("appkey", dto.getAppkey());
        body.put("appsecret", dto.getAppsecret());

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<ResponseTuzaAccessTokenDto> response = restTemplate.postForEntity(url, request, ResponseTuzaAccessTokenDto.class);
//        log.info(response.toString());
        ResponseTuzaAccessTokenDto tokenResponse = response.getBody();
        return tokenResponse;
    }

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

    private Mono<List<ResponseRankingOutputDto>> parsingTradingRank(String response) {
        try {
            List<ResponseRankingOutputDto> dataList = new ArrayList<>();
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode outputNode = rootNode.path("output");
            if (outputNode != null) {
                for (JsonNode node : outputNode) {
                    ResponseRankingOutputDto outputDto = new ResponseRankingOutputDto();
                    outputDto.setHtsKorIsnm(node.path("hts_kor_isnm").asText());
                    outputDto.setMkscShrnIscd(node.path("mksc_shrn_iscd").asText());
                    outputDto.setDataRank(node.path("data_rank").asText());
                    outputDto.setStckPrpr(node.path("stck_prpr").asText());
                    outputDto.setPrdyVrssSign(node.path("prdy_vrss_sign").asText());
                    outputDto.setPrdyVrss(node.path("prdy_vrss").asText());
                    outputDto.setPrdyCtrt(node.path("prdy_ctrt").asText());
                    outputDto.setAcmlVol(node.path("acml_vol").asText());
                    outputDto.setPrdyVol(node.path("prdy_vol").asText());
                    outputDto.setLstnStcn(node.path("lstn_stcn").asText());
                    outputDto.setAvrgVol(node.path("avrg_vol").asText());
                    outputDto.setNBefrClprVrssPrprRate(node.path("n_befr_clpr_vrss_prpr_rate").asText());
                    outputDto.setVolInrt(node.path("vol_inrt").asText());
                    outputDto.setNdayVolTnrt(node.path("nday_vol_tnrt").asText());
                    outputDto.setAvrgTrPbmn(node.path("avrg_tr_pbmn").asText());
                    outputDto.setTrPbmnTnrt(node.path("tr_pbmn_tnrt").asText());
                    outputDto.setNdayTrPbmnTnrt(node.path("nday_tr_pbmn_tnrt").asText());
                    outputDto.setAcmlTrPbmn(node.path("acml_tr_pbmn").asText());
                    dataList.add(outputDto);
                }
            }
            return Mono.just(dataList);

        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    public Mono<List<ResponseRankingOutputDto>> getTradingRank() {
        HttpHeaders headers = createHeaders();

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/uapi/domestic-stock/v1/quotations/volume-rank")
                        .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                        .queryParam("FID_COND_SCR_DIV_CODE", "20171")
                        .queryParam("FID_INPUT_ISCD", "0002")
                        .queryParam("FID_DIV_CLS_CODE", "0")
                        .queryParam("FID_BLNG_CLS_CODE", "0")
                        .queryParam("FID_TRGT_CLS_CODE", "111111111")
                        .queryParam("FID_TRGT_EXLS_CLS_CODE", "000000")
                        .queryParam("FID_INPUT_PRICE_1", "0")
                        .queryParam("FID_INPUT_PRICE_2", "0")
                        .queryParam("FID_VOL_CNT", "0")
                        .queryParam("FID_INPUT_DATE_1", "0")
                        .build())
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> parsingTradingRank(response));
    }

}