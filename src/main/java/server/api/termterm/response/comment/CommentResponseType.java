package server.api.termterm.response.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.termterm.response.BaseResponseType;

@Getter
@AllArgsConstructor
public enum CommentResponseType implements BaseResponseType {
    POST_SUCCESS(2061, "나만의 용어 설명 등록 완료", HttpStatus.CREATED),
    LIKE_SUCCESS(2062, "용어 설명 좋아요 성공", HttpStatus.OK),
    DISLIKE_SUCCESS(2063, "용어 설명 좋아요 취소 성공", HttpStatus.OK),
    ACCEPT_SUCCESS(2064, "나만의 용어 설명 승인 완료", HttpStatus.OK),

    NO_RESULT(4061, "해당하는 나만의 용어 설명이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    DID_LIKED(4062, "사용자가 용어 설명에 이미 좋아요를 눌렀습니다.", HttpStatus.BAD_REQUEST),
    DID_NOT_LIKED(4063, "사용자가 용어 설명에 좋아요를 누르지 않았습니다.", HttpStatus.BAD_REQUEST),
    ;


    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
