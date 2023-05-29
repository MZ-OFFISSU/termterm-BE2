package server.api.termterm.response.base;

import org.springframework.http.HttpStatus;

public interface BaseResponseType {
    Integer getCode();
    String getMessage();
    HttpStatus getHttpStatus();
}
