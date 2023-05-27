package server.api.termterm.controller.foo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.api.termterm.dto.foo.FooDto;
import server.api.termterm.response.BizException;
import server.api.termterm.response.ResponseMessage;
import server.api.termterm.response.foo.FooResponseType;
import server.api.termterm.service.foo.FooService;


import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@Api(tags = "Foo")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class FooController {
    private final FooService fooService;

    /**
     * API 예시
     * @param name
     * @param title
     * @return FooDto
     * @Author Seungwoo, Joo
     */
    @ApiOperation(value = "요청 예시", notes = "요청 예시입니다!\nname에 \"error\" : controller에서 error 처리하는 경우\ntitle에 \"error\" : service에서 error 처리하는 경우")
    @ApiResponses({
            @ApiResponse(code = 20001, message = "Foo 객체 정상 리턴 (200 OK)"),
            @ApiResponse(code = 40001, message = "parameter 누락 (400 BAD_REQUEST)")
    })
    @GetMapping("/foo")
    public ResponseEntity<ResponseMessage> getFoo(
            @Parameter(name = "name", description = "String", in = QUERY, required = false) @RequestParam(required = false) String name,
            @Parameter(name = "title", description = "String", in = QUERY, required = false) @RequestParam String title
    ){
        // 담아줄 데이터가 없으면 create() 함수의 두번째 인자는 쓰지 않아도 됩니다.
        // Exception 던지는 부분엔 log 출력 코드 넣기
        // 주석 처리한 return 문처럼 에러 응답해도 무관
        if (name == null || title == null || name.equals("error")){
//            return new ResponseEntity<>(ResponseMessage.create(FooResponseType.INVALID_PARAMETER), FooResponseType.INVALID_PARAMETER.getHttpStatus());
            log.error(FooResponseType.INVALID_PARAMETER.getMessage());
            throw new BizException(FooResponseType.INVALID_PARAMETER);
        }

        FooDto fooDto = fooService.getFoo(name, title);

        // exception 이외에도 log 넣고 싶으면 넣기
        log.info(FooResponseType.FOO_CREATE_SUCCESS.getMessage());

        // 응답해줄 데이터가 있으면 create() 함수의 두번째 인자에 Dto를 담아준다.
        return new ResponseEntity<>(ResponseMessage.create(FooResponseType.FOO_CREATE_SUCCESS, fooDto), FooResponseType.FOO_CREATE_SUCCESS.getHttpStatus());
    }
}
