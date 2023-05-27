package server.api.termterm.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ResponseMessage<T> {
    private Integer status;
    private String message;
    private T data;

    public ResponseMessage(final Integer status, final String message){
        this.status = status;
        this.message = message;
        this.data = null;
    }

    public static<T> ResponseMessage<T> create(final BaseResponseType response){
        return create(response, null);
    }

    public static<T> ResponseMessage<T> create(final BaseResponseType response, final T t){
        return ResponseMessage.<T>builder()
                .status(response.getCode())
                .message(response.getMessage())
                .data(t)
                .build();
    }
}