package server.api.termterm.response.quiz;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.termterm.response.base.BaseResponseType;

@Getter
@AllArgsConstructor
public enum QuizResponseType implements BaseResponseType {
    QUIZ_DAILY_SUCCESS(2201, "데일리 퀴즈를 생성하였습니다.", HttpStatus.OK),
    ;
    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
