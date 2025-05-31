package openapi.openapitest.dto;

import lombok.Builder;
import lombok.Getter;
import openapi.openapitest.domain.stock.entity.StockInfo;

@Getter
public class StockInfoDto {

    private String stck_prpr;     // 주식 현재가
    private String per;     // PER
    private String pbr;     // PBR
    private String stck_shrn_iscd;     // 주식 단축 종목코드
    private String bstp_kor_isnm; // 업종 한글 종목명
    private String bsop_prfi_inrt; // 영업 이익 증가율
    private String roe_val; // ROE 값
    private String eps; // EPS

    public StockInfo toEntity(ResponseCurrentPerPbrOutputDto perPbrOutputDto,
                              ResponseCurrentFinanceOutputDto financeOutputDto) {
        return StockInfo.builder()
                .stck_shrn_iscd(perPbrOutputDto.getStck_shrn_iscd())
                .per(perPbrOutputDto.getPer())
                .pbr(perPbrOutputDto.getPbr())
                .stck_prpr(perPbrOutputDto.getStck_prpr())
                .bstp_kor_isnm(perPbrOutputDto.getBstp_kor_isnm())
                .bsop_prfi_inrt(financeOutputDto.getBsop_prfi_inrt())
                .roe_val(financeOutputDto.getRoe_val())
                .eps(financeOutputDto.getEps())
                .build();
    }

}
