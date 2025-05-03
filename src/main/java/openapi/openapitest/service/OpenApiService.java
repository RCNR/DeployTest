//package openapi.openapitest.service;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import openapi.openapitest.dto.ResponseOutputDto;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class OpenApiService {
//
//    private final WebClient webClient;
//    private final ObjectMapper objectMapper;
//
//    @Value("${APP_KEY}")
//    private String appKey;
//
//    @Value("${APP_SECRET_KEY}")
//    private String appSecret;
//
//    @Value("${ACCESS_TOKEN}")
//    private String accessToken;
//
//    @Autowired
//    public OpenApiService(WebClient.Builder webClient, ObjectMapper objectMapper) {
//        this.webClient = webClient.baseUrl("https://openapi.koreainvestment.com:9443").build();
//        this.objectMapper = objectMapper;
//    }
//
//    private HttpHeaders createTradingRankHeaders () {
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        httpHeaders.setBearerAuth(accessToken);
//        httpHeaders.set("appkey", appKey);
//        httpHeaders.set("appsecret", appSecret);
//        httpHeaders.set("tr_id", "FHPST01710000");
//        httpHeaders.set("custtype", "P");
//
//        return httpHeaders;
//    }
//
//    private Mono<List<ResponseOutputDto>> parsingTradingRank(String response) {
//        try {
//            List<ResponseOutputDto> dataList = new ArrayList<>();
//            JsonNode rootNode = objectMapper.readTree(response);
//            JsonNode outputNode = rootNode.get("output");
//            if (outputNode != null) {
//                for (JsonNode node : outputNode) {
//                    ResponseOutputDto outputDto = new ResponseOutputDto();
//                    outputDto.setHtsKorIsnm(node.get("hts_kor_isnm").asText());
//                    outputDto.setMkscShrnIscd(node.get("mksc_shrn_iscd").asText());
//                    outputDto.setDataRank(node.get("data_rank").asText());
//                    outputDto.setStckPrpr(node.get("stck_prpr").asText());
//                    outputDto.setPrdyVrssSign(node.get("prdy_vrss_sign").asText());
//                    outputDto.setPrdyVrss(node.get("prdy_vrss").asText());
//                    outputDto.setPrdyCtrt(node.get("prdy_ctrt").asText());
//                    outputDto.setAcmlVol(node.get("acml_vol").asText());
//                    outputDto.setPrdyVol(node.get("prdy_vol").asText());
//                    outputDto.setLstnStcn(node.get("lstn_stcn").asText());
//                    outputDto.setAvrgVol(node.get("avrg_vol").asText());
//                    outputDto.setNBefrClprVrssPrprRate(node.get("n_befr_clpr_vrss_prpr_rate").asText());
//                    outputDto.setVolInrt(node.get("vol_inrt").asText());
//                    outputDto.setNdayVolTnrt(node.get("nday_vol_tnrt").asText());
//                    outputDto.setAvrgTrPbmn(node.get("avrg_tr_pbmn").asText());
//                    outputDto.setTrPbmnTnrt(node.get("tr_pbmn_tnrt").asText());
//                    outputDto.setNdayTrPbmnTnrt(node.get("nday_tr_pbmn_tnrt").asText());
//                    outputDto.setAcmlTrPbmn(node.get("acml_tr_pbmn").asText());
//                    dataList.add(outputDto);
//                }
//            }
//            return Mono.just(dataList);
//
//        } catch (Exception e) {
//            return Mono.error(e);
//        }
//
//
//    }
//
//    public Mono<List<ResponseOutputDto>> getTradingRank() {
//        HttpHeaders headers = createTradingRankHeaders();
//
//       return webClient.get()
//                .uri(uriBuilder -> uriBuilder.path("/uapi/domestic-stock/v1/quotations/volume-rank")
//                        .queryParam("FID_COND_MRKT_DIV_CODE", "J")
//                        .queryParam("FID_COND_SCR_DIV_CODE", "20171")
//                        .queryParam("FID_INPUT_ISCD", "0002")
//                        .queryParam("FID_DIV_CLS_CODE", "0")
//                        .queryParam("FID_BLNG_CLS_CODE", "0")
//                        .queryParam("FID_TRGT_CLS_CODE", "111111111")
//                        .queryParam("FID_TRGT_EXLS_CLS_CODE", "000000")
//                        .queryParam("FID_INPUT_PRICE_1", "0")
//                        .queryParam("FID_INPUT_PRICE_2", "0")
//                        .queryParam("FID_VOL_CNT", "0")
//                        .queryParam("FID_INPUT_DATE_1", "0")
//                        .build())
//                .headers(httpHeaders -> httpHeaders.addAll(headers))
//                .retrieve()
//                .bodyToMono(String.class)
//                .flatMap(response -> parsingTradingRank(response));
//    }
//}
