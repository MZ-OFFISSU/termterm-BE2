package server.api.termterm.response.point;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.termterm.response.base.BaseResponseType;

@Getter
@AllArgsConstructor
public enum PointResponseType implements BaseResponseType {

    CURATION_PAY_SUCCESS(2110, "큐레이션 구매 성공", HttpStatus.OK),

    ALREADY_PAID(4110, "이미 포인트를 지불하였습니다.", HttpStatus.BAD_REQUEST),
    NOT_ENOUGH_POINT(4111, "포인트가 부족합니다.", HttpStatus.BAD_REQUEST),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
