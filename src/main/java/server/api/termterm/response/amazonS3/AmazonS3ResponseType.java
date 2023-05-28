package server.api.termterm.response.amazonS3;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.termterm.response.BaseResponseType;

@Getter
@AllArgsConstructor
public enum AmazonS3ResponseType implements BaseResponseType {
    PRESIGNED_URL_ISSUANCE_SUCCESS(2081, "presigned-url 발급에 성공하였습니다.", HttpStatus.OK),

    CANNOT_GET_PRESIGNED_URL(4081, "presigned-url 발급에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ;


    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
