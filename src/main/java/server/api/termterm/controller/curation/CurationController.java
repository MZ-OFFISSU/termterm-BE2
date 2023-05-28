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
import server.api.termterm.dto.curation.CurationDetailDto;
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

    @ApiOperation(value = "큐레이션 북마크 취소", notes = "큐레이션 북마크 취소")
    @ApiResponses({
            @ApiResponse(code = 2094, message = "큐레이션 북마크 취소 성공 (200)"),
            @ApiResponse(code = 4092, message = "ID와 일치하는 큐레이션이 존재하지 않습니다. (404)"),
            @ApiResponse(code = 4093, message = "북마크가 되어있지 않아 취소를 할 수 없습니다. (400)"),
    })
    @PutMapping("/curation/unbookmark/{id}")
    public ResponseEntity<ResponseMessage<String>> unbookmarkCuration(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @PathVariable("id") Long id
    ){
        Member member = memberService.getMemberByToken(token);
        Curation curation = curationService.findById(id);
        curationService.unbookmarkCuration(member, curation);

        return new ResponseEntity<>(ResponseMessage.create(CurationResponseType.CURATION_UNBOOKMARK_SUCCESS), CurationResponseType.CURATION_UNBOOKMARK_SUCCESS.getHttpStatus());
    }

    @ApiOperation(value = "큐래이션 상세", notes = "큐레이션 한개 상세정보 리턴")
    @ApiResponses({
            @ApiResponse(code = 2096, message = "큐레이션 디테일 응답 성공 (200)"),
            @ApiResponse(code = 4092, message = "ID와 일치하는 큐레이션이 존재하지 않습니다. (404)"),
    })
    @GetMapping("/curation/detail/{id}")
    public ResponseEntity<ResponseMessage<CurationDetailDto>> getCurationDetail(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @PathVariable("id") Long id
    ){
        Member member = memberService.getMemberByToken(token);
        Curation curation = curationService.findById(id);

        // 유저 paid 여부 확인
//        Boolean memberPaid = curationService.getWhetherMemberPaidForCuration(member, curation);
        Boolean memberPaid = false;

        CurationDetailDto curationDetailDto = curationService.getCurationDetail(member, curation, memberPaid);

        return new ResponseEntity<>(ResponseMessage.create(CurationResponseType.CURATION_DETAIL_RETURN_SUCCESS, curationDetailDto), CurationResponseType.CURATION_LIST_RETURN_SUCCESS.getHttpStatus());
    }

}
