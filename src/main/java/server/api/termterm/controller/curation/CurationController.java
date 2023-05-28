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
import server.api.termterm.domain.curation.Curation;
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

    @ApiOperation(value = "큐레이션 북마크", notes = "큐레이션 북마크")
    @ApiResponses({
            @ApiResponse(code = 2093, message = "큐레이션 북마크 성공 (200)"),
            @ApiResponse(code = 4092, message = "ID와 일치하는 큐레이션이 존재하지 않습니다. (404)"),
    })
    @PutMapping("/curation/bookmark/{id}")
    public ResponseEntity<ResponseMessage<String>> bookmarkCuration(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @PathVariable("id") Long id
    ){
        Member member = memberService.getMemberByToken(token);
        Curation curation = curationService.findById(id);
        curationService.bookmarkCuration(member, curation);

        return new ResponseEntity<>(ResponseMessage.create(CurationResponseType.CURATION_BOOKMARK_SUCCESS), CurationResponseType.CURATION_BOOKMARK_SUCCESS.getHttpStatus());
    }

}
