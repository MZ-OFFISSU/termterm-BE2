package server.api.termterm.response.inquiry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.termterm.response.BaseResponseType;

@Getter
@AllArgsConstructor
public enum InquiryResponseType implements BaseResponseType {
    INQUIRY_ACCEPTED(2041, "문의 접수 완료", HttpStatus.OK),
    INQUIRY_COMPLETED(2042, "문의 답변 처리 완료", HttpStatus.OK),

    INVALID_INQUIRY_TYPE(4044, "문의 유형이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_INQUIRY_ID(4045, "문의사항이 존재하지 않거나 삭제되었습니다.", HttpStatus.NOT_FOUND),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
