package server.api.termterm.response.foo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.termterm.response.BaseResponseType;

@Getter
@AllArgsConstructor
public enum FooResponseType implements BaseResponseType {
    FOO_CREATE_SUCCESS(20001, "foo 성공", HttpStatus.OK),

    INVALID_PARAMETER(40001, "parameter 에러", HttpStatus.BAD_REQUEST),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
