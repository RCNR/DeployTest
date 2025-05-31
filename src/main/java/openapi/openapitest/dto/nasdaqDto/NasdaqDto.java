package openapi.openapitest.dto.nasdaqDto;

import lombok.Getter;
import lombok.Setter;
import openapi.openapitest.domain.stock.entity.NasdaqStockInfo;

public class NasdaqDto {

    @Setter
    @Getter
    public static class NasdaqInfoDto {

        private String code;
        private String perx; // PER
        private String pbrx; // PBR
        private String epsx; // EPS
        private String e_icod; // 업종 섹터

        public NasdaqStockInfo toEntity( NasdaqDto.NasdaqInfoDto dto) {
            return NasdaqStockInfo.builder()
                    .code(dto.code)
                    .perx(dto.perx)
                    .pbrx(dto.pbrx)
                    .epsx(dto.epsx)
                    .eIcod(dto.e_icod)
                    .build();
        }
    }
}
