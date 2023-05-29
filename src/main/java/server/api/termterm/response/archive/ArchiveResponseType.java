package server.api.termterm.response.archive;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.termterm.response.base.BaseResponseType;

@Getter
@AllArgsConstructor
public enum ArchiveResponseType implements BaseResponseType {
    FOLDER_CREATE_SUCCESS(2301, "새로운 폴더를 생성하였습니다.", HttpStatus.OK),
    GET_ARCHIVED_CURATION_SUCCESS(2302, "아카이브한 큐레이션 응답 성공", HttpStatus.OK),
    NO_ARCHIVED_CURATION(2303, "아카이브한 큐레이션이 존재하지 않습니다.", HttpStatus.OK),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
