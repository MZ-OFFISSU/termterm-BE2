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
import server.api.termterm.domain.member.Member;
import server.api.termterm.dto.inquiry.InquiryRequestDto;
import server.api.termterm.response.BizException;
import server.api.termterm.response.ResponseMessage;
import server.api.termterm.response.inquiry.InquiryResponseType;
import server.api.termterm.response.member.MemberResponseType;
import server.api.termterm.service.inquiry.InquiryService;
import server.api.termterm.service.member.MemberService;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.*;


@Api(tags = "Inquiry")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class InquiryController {
    private final InquiryService inquiryService;
    private final MemberService memberService;

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
    @PostMapping("/inquiry")
    public ResponseEntity<ResponseMessage<InquiryRequestDto>> registerInquiry(
            @Parameter(name = "InquiryRequestDto", description = "String: email / type(문의 유형) / content") @RequestBody InquiryRequestDto inquiryRequestDto
    ){
        inquiryService.registerInquiry(inquiryRequestDto.trimAll());

        return new ResponseEntity<>(ResponseMessage.create(InquiryResponseType.INQUIRY_ACCEPTED, inquiryRequestDto), InquiryResponseType.INQUIRY_ACCEPTED.getHttpStatus());
    }

    @ApiOperation(value = "문의사항 답변 완료", notes = "문의사항 답변 완료")
    @ApiResponses({
            @ApiResponse(code = 2042, message = "문의 답변 처리 완료 (200)"),
            @ApiResponse(code = 4045, message = "문의가 존재하지 않거나 삭제되었습니다. (404)"),
    })
    @PutMapping("/inquiry/to-completed/{id}")
    public ResponseEntity<ResponseMessage<String>> completeInquiry(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @Parameter(name = "id", description = "Inquiry Id", in = PATH, required = true) @PathVariable Long id
    ){
        Member member = memberService.getMemberByToken(token);
        if (!memberService.checkAdmin(member))
            throw new BizException(MemberResponseType.NO_AUTHORIZATION);

        inquiryService.completeInquiry(id);

        return new ResponseEntity<>(ResponseMessage.create(InquiryResponseType.INQUIRY_COMPLETED), InquiryResponseType.INQUIRY_COMPLETED.getHttpStatus());
    }

    @ApiOperation(value = "문의사항 대기 중 변환", notes = "문의사항 대기 중 변환")
    @ApiResponses({
            @ApiResponse(code = 2043, message = "문의 대기 중 변환 완료 (200)"),
            @ApiResponse(code = 4045, message = "문의가 존재하지 않거나 삭제되었습니다. (404)"),
    })
    @PutMapping("/inquiry/to-waiting/{id}")
    public ResponseEntity<ResponseMessage<String>> waitInquiry(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @Parameter(name = "id", description = "Inquiry Id", in = PATH, required = true) @PathVariable Long id
    ){
        Member member = memberService.getMemberByToken(token);
        if (!memberService.checkAdmin(member))
            throw new BizException(MemberResponseType.NO_AUTHORIZATION);

        inquiryService.waitInquiry(id);

        return new ResponseEntity<>(ResponseMessage.create(InquiryResponseType.INQUIRY_HOLD), InquiryResponseType.INQUIRY_HOLD.getHttpStatus());
    }
}
