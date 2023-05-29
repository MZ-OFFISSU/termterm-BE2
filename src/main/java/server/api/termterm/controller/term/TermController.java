package server.api.termterm.controller.term;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import server.api.termterm.domain.member.Member;
import server.api.termterm.dto.term.TermDto;
import server.api.termterm.dto.term.TermMinimumDto;
import server.api.termterm.response.base.ApiResponse;
import server.api.termterm.response.term.TermResponseType;
import server.api.termterm.service.member.MemberService;
import server.api.termterm.service.term.TermService;

import java.util.List;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;

@Api(tags = "Term")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class TermController {
    private final MemberService memberService;
    private final TermService termService;

    @ApiOperation(value = "용어 검색", notes = "용어 검색\n 북마크 되어 있으면 YES, 안 되어 있으면 null")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 2051, message = "용어 검색 성공 (200)"),
            @io.swagger.annotations.ApiResponse(code = 4051, message = "검색어에 대한 검색 결과가 없습니다. (404)"),
    })
    @GetMapping("/term/search/{name}")
    public ApiResponse<List<TermMinimumDto>> searchTerm(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @PathVariable(value = "name") String name
    ){
        Member member = memberService.getMemberByToken(token);
        List<TermMinimumDto> termMinimumDtos = termService.searchTerm(member, name);

        return ApiResponse.of(TermResponseType.SEARCH_SUCCESS, termMinimumDtos);
    }

    @ApiOperation(value = "용어 상세", notes = "용어 상세 정보 리턴\n bookmarked: Yes/null")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 2052, message = "용어 상세 정보 조회 성공 (200)"),
            @io.swagger.annotations.ApiResponse(code = 4052, message = "단어가 존재하지 않습니다. (400)"),
    })
    @GetMapping("/term/detail/{id}")
    public ApiResponse<TermDto> getTermDetail(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @PathVariable(value = "id") Long id
    ){
        Member member = memberService.getMemberByToken(token);
        TermDto termDto = termService.getTermDetail(member, id);

        return ApiResponse.of(TermResponseType.DETAIL_SUCCESS, termDto);
    }

    @ApiOperation(value = "용어 북마크", notes = "용어 북마크")
    @GetMapping("/term/bookmark/{id}")
    public ApiResponse<String> bookmarkTerm(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @Parameter(name = "id", description = "term id", in = PATH, required = true) @PathVariable(value = "id") Long id
    ){
        Member member = memberService.getMemberByToken(token);
        termService.bookmarkTerm(member, id);

        return ApiResponse.of(TermResponseType.BOOKMARK_SUCCESS);
    }


}
