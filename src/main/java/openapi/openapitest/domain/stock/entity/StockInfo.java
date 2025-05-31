package openapi.openapitest.domain.stock.entity;

import jakarta.persistence.*;
import lombok.*;

//@Table("stock_info")

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class StockInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stck_shrn_iscd;     // 주식 단축 종목코드

    private String per;

    private String pbr;

    private String stck_prpr;     // 주식 현재가

    private String bstp_kor_isnm; // 업종 한글 종목명




    private String bsop_prfi_inrt; // 영업 이익 증가율

    private String roe_val; // ROE 값

    private String eps;


}
