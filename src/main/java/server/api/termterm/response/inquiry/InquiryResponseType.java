package server.api.termterm.response.inquiry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.termterm.response.BaseResponseType;

@Getter
@AllArgsConstructor
public enum InquiryResponseType implements BaseResponseType {
    INQUIRY_ACCEPTED(2041, "문의 접수 완료", HttpStatus.OK),

    INVALID_INQUIRY_TYPE(4044, "문의 유형이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
