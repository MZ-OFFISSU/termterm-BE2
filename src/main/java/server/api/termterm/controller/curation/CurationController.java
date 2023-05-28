package server.api.termterm.controller.curation;

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
import server.api.termterm.dto.curation.CurationRegisterRequestDto;
import server.api.termterm.response.BizException;
import server.api.termterm.response.ResponseMessage;
import server.api.termterm.response.curation.CurationResponseType;
import server.api.termterm.response.member.MemberResponseType;
import server.api.termterm.service.curation.CurationService;
import server.api.termterm.service.member.MemberService;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

@Api(tags = "Curation")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class CurationController {
    private final MemberService memberService;
    private final CurationService curationService;

    @ApiOperation(value = "큐레이션 등록", notes = "큐레이션 등록")
    @ApiResponses({
            @ApiResponse(code = 2091, message = "큐레이션 등록 성공 (201)"),
            @ApiResponse(code = 4091, message = "용어 ID 요청 형식이 잘못되었습니다. (400)"),
    })
    @PostMapping("/curation/register")
    public ResponseEntity<ResponseMessage<String>> registerCuration(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @Parameter(name = "CurationRegisterRequestDto", description = "위 설명 참고") @RequestBody CurationRegisterRequestDto curationRegisterRequestDto
    ){
        Member member = memberService.getMemberByToken(token);
        if (!memberService.checkAdmin(member))
            throw new BizException(MemberResponseType.NO_AUTHORIZATION);

        curationService.registerCuration(curationRegisterRequestDto);

        return new ResponseEntity<>(ResponseMessage.create(CurationResponseType.CURATION_REGISTER_SUCCESS), CurationResponseType.CURATION_REGISTER_SUCCESS.getHttpStatus());
    }

}
