package openapi.openapitest.domain.stock.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import openapi.openapitest.domain.stock.service.StockCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StockController {

    private final StockCodeService stockCodeService;


    @PostMapping("/post-stock-codes")
    @Tag(name = "코스피 주식 코드를 저장")
    public ResponseEntity<String> postStockCodes() {
        try {
            stockCodeService.process();
            return ResponseEntity.ok("completed saving stock codes");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("error about stock codes : " + e.getMessage());
        }
    }
}
