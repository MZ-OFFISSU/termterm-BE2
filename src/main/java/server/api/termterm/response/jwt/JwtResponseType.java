package server.api.termterm.response.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.termterm.response.BaseResponseType;

@Getter
@AllArgsConstructor
public enum JwtResponseType implements BaseResponseType {

    // Exception
    INVALID_REFRESH_TOKEN(40001, "유효하지 않은 리프레시 토큰입니다.", HttpStatus.BAD_REQUEST),
    INVALID_ACCESS_TOKEN(40002, "유효하지 않은 엑세스 토큰입니다.", HttpStatus.BAD_REQUEST),
    ACCESS_TOKEN_EXPIRED(40003, "엑세스 토큰의 유효기간이 만료되었습니다.", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_EXPIRED(40004, "리프레시 토큰의 유효기간이 만료되었습니다.", HttpStatus.BAD_REQUEST),
    BAD_TOKEN(40005, "잘못된 토큰 값입니다.", HttpStatus.BAD_REQUEST),
    EMPTY_TOKEN(40006, "토큰 값이 비어있습니다.", HttpStatus.BAD_REQUEST),
    MALFORMED_JWT(40007, "잘못된 JWT 서명입니다.", HttpStatus.BAD_REQUEST),
    UNSUPPORTED_JWT(40008, "지원되지 않는 JWT 토큰입니다.", HttpStatus.BAD_REQUEST),
    NO_BEARER(40009, "Bearer를 앞에 붙여 요청해주세요.", HttpStatus.BAD_REQUEST),

    // SUCCESS
    TOKEN_REISSUED(20001, "토큰 재발급 완료", HttpStatus.OK),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
