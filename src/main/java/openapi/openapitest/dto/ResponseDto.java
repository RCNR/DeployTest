package openapi.openapitest.dto;

import java.util.List;

public class ResponseDto {

    // 성공 실패 여부
    private String rt_cd;

    // 응답 코드
    private String msg_cd;

    // 응답메세지
    private String msg1;

    // 응답상세
    private List<ResponseRankingOutputDto> output;


}
