package server.api.termterm.response.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.termterm.response.BaseResponseType;

@Getter
@AllArgsConstructor
public enum ReportResponseType implements BaseResponseType {
    INVALID_REPORT_TYPE(4071, "신고 유형이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    DOES_NOT_EXIST(4072, "신고가 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    REPORT_WAITING(2071, "신고 접수 완료", HttpStatus.OK),
    REPORT_DELETED(2072, "신고 삭제 완료", HttpStatus.OK),
    REPORT_COMPLETED(2073, "신고 처리 완료", HttpStatus.OK),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}