package server.api.termterm.controller.inquiry;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.api.termterm.dto.inquiry.InquiryRequestDto;
import server.api.termterm.response.ResponseMessage;
import server.api.termterm.response.inquiry.InquiryResponseType;
import server.api.termterm.service.inquiry.InquiryService;


@Api(tags = "Inquiry")
@RestController
@RequiredArgsConstructor
@Slf4j
public class InquiryController {
    private final InquiryService inquiryService;

    @ApiOperation(value = "문의사항 접수", notes = "문의사항 접수\n" +
            "문의 유형 : \n" +
            "    USE(\"USER\", \"이용 문의\"),\n" +
            "    AUTH(\"AUTH\", \"로그인/회원가입 문의\"),\n" +
            "    REPORT(\"REPORT\", \"서비스 불편/오류 제보\"),\n" +
            "    SUGGESTION(\"SUGGESTION\", \"서비스 제안\"),\n" +
            "    OTHER(\"OTHER\", \"기타 문의\")")
    @ApiResponses({
            @ApiResponse(code = 2041, message = "문의 접수 완료 (201)"),
            @ApiResponse(code = 4044, message = "문의 유형이 존재하지 않습니다. (400)"),
    })
    @PostMapping("/v1/inquiry")
    public ResponseEntity<ResponseMessage<InquiryRequestDto>> registerInquiry(
            @Parameter(name = "InquiryRequestDto", description = "String: email / type(문의 유형) / content") @RequestBody InquiryRequestDto inquiryRequestDto
    ){
        inquiryService.registerInquiry(inquiryRequestDto.trimAll());

        return new ResponseEntity<>(ResponseMessage.create(InquiryResponseType.INQUIRY_ACCEPTED, inquiryRequestDto), InquiryResponseType.INQUIRY_ACCEPTED.getHttpStatus());
    }
}
