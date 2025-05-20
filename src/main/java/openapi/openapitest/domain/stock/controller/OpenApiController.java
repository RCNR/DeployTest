package openapi.openapitest.domain.stock.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import openapi.openapitest.domain.stock.service.CreateStocksInfoService;
import openapi.openapitest.dto.RequestTuzaAccessTokenDto;
import openapi.openapitest.dto.ResponseCurrentOutputDto;
import openapi.openapitest.dto.ResponseRankingOutputDto;
import openapi.openapitest.dto.ResponseTuzaAccessTokenDto;
import openapi.openapitest.domain.stock.service.OpenApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OpenApiController {

    private final OpenApiService service;
    private final CreateStocksInfoService createStocksInfoService;

    @GetMapping("/home")
    @Tag(name = "테스트")
    public String getHome() {
        return "This is Home";
    }


    @PostMapping("/get-tuza-accessToken")
    @Tag(name = "한국 투자 증권 액세스 키 발급")
    public ResponseEntity<ResponseTuzaAccessTokenDto> getTuzaAccessToken(@RequestBody RequestTuzaAccessTokenDto dto) {

        ResponseTuzaAccessTokenDto tuzaAcessToken = service.getTuzaAcessToken(dto);
        return ResponseEntity.ok(tuzaAcessToken);
    }


    @GetMapping("/trading-rank")
    @Tag(name = "거래량 순위")
    public Mono<List<ResponseRankingOutputDto>> getTradingRank() {
        return service.getTradingRank();
    }

    @GetMapping("/get-one-trading/{stockCode}")
    @Tag(name = "현재가 시세")
    public Mono<ResponseCurrentOutputDto> getTradingCurrentInfo(@PathVariable("stockCode") String stockCode) {
        return service.getCurrentInfo(stockCode);
    }

    @PostMapping("/post-all-trading-info")
    @Tag(name = "주식 현재가 시세 정보 저장")
    public ResponseEntity<?> createTradingInfo() {
        service.createCurrentStocksInfo();
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
