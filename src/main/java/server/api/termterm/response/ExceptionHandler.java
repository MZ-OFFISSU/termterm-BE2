package server.api.termterm.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(BizException.class)
    public ResponseEntity<ResponseMessage<String>> bizException(BizException e){
        log.error("[{}] - {}", e.getStackTrace()[0], e.getMessage());
        return new ResponseEntity<>(ResponseMessage.create(e.getBaseExceptionType()), e.getBaseExceptionType().getHttpStatus());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessage<String>> notResolvedException(Exception e){
        log.error("[{}] - {}", e.getStackTrace()[0], e.getMessage());
        return new ResponseEntity<>(ResponseMessage.create(InternalServerExceptionType.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
