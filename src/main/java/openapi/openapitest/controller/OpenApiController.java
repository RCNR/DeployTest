package openapi.openapitest.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import openapi.openapitest.dto.ResponseOutputDto;
//import openapi.openapitest.service.OpenApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OpenApiController {

//    private final OpenApiService service;
//
//
//    @GetMapping("/trading-rank")
//    public Mono<List<ResponseOutputDto>> getTradingRank() {
//        return service.getTradingRank();
//    }

    @GetMapping("/home")
    public String getHome() {
        return "This is Home";
    }
}
