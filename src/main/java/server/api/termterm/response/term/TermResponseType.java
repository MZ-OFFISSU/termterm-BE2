package server.api.termterm.response.term;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.termterm.response.BaseResponseType;

@Getter
@AllArgsConstructor
public enum TermResponseType implements BaseResponseType {
    TERM_SEARCH_SUCCESS(2051, "용어 검색 성공", HttpStatus.OK),
    TERM_DETAIL_SUCCESS(2052, "용어 상세 정보 조회 성공", HttpStatus.OK),

    TERM_SEARCH_NO_RESULT(4051, "검색어에 대한 검색 결과가 없습니다.", HttpStatus.BAD_REQUEST),
    TERM_ID_NO_RESULT(4052, "단어가 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}