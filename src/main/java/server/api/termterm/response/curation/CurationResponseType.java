package server.api.termterm.response.curation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.termterm.response.base.BaseResponseType;

@Getter
@AllArgsConstructor
public enum CurationResponseType implements BaseResponseType {

    CURATION_REGISTER_SUCCESS(2091, "큐레이션 등록 성공", HttpStatus.OK),
    RECOMMENDED_CURATION_LIST_SUCCESS(2092, "추천 큐레이션 응답 성공", HttpStatus.OK),
    CURATION_BOOKMARK_SUCCESS(2093, "큐레이션 북마크 성공", HttpStatus.OK),
    CURATION_UNBOOKMARK_SUCCESS(2094, "큐레이션 북마크 취소 성공", HttpStatus.OK),
    CURATION_LIST_RETURN_SUCCESS(2095, "큐레이션 리스트 응답 성공", HttpStatus.OK),
    CURATION_DETAIL_RETURN_SUCCESS(2096, "큐레이션 디테일 응답 성공", HttpStatus.OK),

    CURATION_DOESNT_EXIST(4092, "ID와 일치하는 큐레이션이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    CURATION_NOT_BOOKMARKED(4093, "북마크가 되어있지 않아 취소를 할 수 없습니다.", HttpStatus.BAD_REQUEST),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
