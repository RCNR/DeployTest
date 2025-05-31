package openapi.openapitest.dto;

import lombok.Getter;
import lombok.Setter;
import openapi.openapitest.domain.stock.entity.StockInfo;

@Getter
@Setter
public class ResponseCurrentPerPbrOutputDto {

    private String stck_prpr;     // 주식 현재가
    private String per;     // PER
    private String pbr;     // PBR
    private String stck_shrn_iscd;     // 주식 단축 종목코드
    private String bstp_kor_isnm; // 업종 한글 종목명


    public StockInfo toEntity() {
        return StockInfo.builder()
                .stck_shrn_iscd(this.stck_shrn_iscd)
                .per(this.per)
                .pbr(this.pbr)
                .stck_prpr(this.stck_prpr)
                .bstp_kor_isnm(this.bstp_kor_isnm)
                .build();
    }
}
