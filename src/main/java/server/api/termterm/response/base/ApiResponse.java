package server.api.termterm.response.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ApiResponse<T> {
    private Integer status;
    private String message;
    private T data;

    public ApiResponse(final Integer status, final String message){
        this.status = status;
        this.message = message;
        this.data = null;
    }

    public static<T> ApiResponse<T> of(final BaseResponseType response){
        return of(response, null);
    }

    public static<T> ApiResponse<T> of(final BaseResponseType response, final T t){
        return ApiResponse.<T>builder()
                .status(response.getCode())
                .message(response.getMessage())
                .data(t)
                .build();
    }
}