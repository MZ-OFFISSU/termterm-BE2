package server.api.termterm.response.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.termterm.response.BaseResponseType;

@Getter
@AllArgsConstructor
public enum CategoryResponseType implements BaseResponseType {
    CATEGORY_NOT_EXISTS(4101, "카테고리가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;

}
