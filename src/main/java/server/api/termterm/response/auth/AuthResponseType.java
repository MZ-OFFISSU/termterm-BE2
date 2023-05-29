package server.api.termterm.response.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.termterm.response.base.BaseResponseType;

@Getter
@AllArgsConstructor
public enum AuthResponseType implements BaseResponseType {
    NO_AUTHORIZATION_CODE(4010, "요청에 인가 코드가 존재하지 않습니다.",HttpStatus.BAD_REQUEST),
    NOT_FOUND_AUTHORITY(4011, "존재하지 않는 권한입니다.", HttpStatus.BAD_REQUEST),
    INVALID_SOCIAL_TYPE(4012, "소셜 타입 경로가 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    GOOGLE_CONNECTION_ERROR(4013, "구글 서버와의 연결에 실패하였습니다.", HttpStatus.GATEWAY_TIMEOUT),
    KAKAO_CONNECTION_ERROR(4014, "카카오 서버와의 연결에 실패하였습니다.", HttpStatus.GATEWAY_TIMEOUT),
    MALFORMED_URL(4015, "URL 오류", HttpStatus.BAD_REQUEST),
    FAIL_PARSE(4016, "json 파싱 실패", HttpStatus.BAD_REQUEST),

    LOGIN_SUCCESS(2011, "로그인 성공", HttpStatus.OK),
    TOKEN_REISSUED(2012, "토큰 재발급 완료", HttpStatus.OK),
    LOGOUT_SUCCESS(2013, "로그아웃 성공", HttpStatus.NO_CONTENT),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;

}
