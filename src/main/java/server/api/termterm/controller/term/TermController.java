package server.api.termterm.controller.term;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import server.api.termterm.domain.member.Member;
import server.api.termterm.dto.term.TermDto;
import server.api.termterm.dto.term.TermMinimumDto;
import server.api.termterm.dto.term.TermSimpleDto;
import server.api.termterm.response.base.ApiResponse;
import server.api.termterm.response.term.TermResponseType;
import server.api.termterm.service.category.CategoryService;
import server.api.termterm.service.member.MemberService;
import server.api.termterm.service.term.TermService;

import java.util.List;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.*;

@Api(tags = "Term")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class TermController {
    private final MemberService memberService;
    private final TermService termService;
    private final CategoryService categoryService;

    @ApiOperation(value = "용어 검색", notes = "용어 검색\n 북마크 되어 있으면 YES, 안 되어 있으면 null")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 2051, message = "용어 검색 성공 (200)"),
            @io.swagger.annotations.ApiResponse(code = 4051, message = "검색어에 대한 검색 결과가 없습니다. (404)"),
    })
    @GetMapping("/term/search/{name}")
    public ApiResponse<List<TermMinimumDto>> searchTerm(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @PathVariable(value = "name") String name
    ) {
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
    ) {
        Member member = memberService.getMemberByToken(token);
        TermDto termDto = termService.getTermDetail(member, id);

        return ApiResponse.of(TermResponseType.DETAIL_SUCCESS, termDto);
    }

    @ApiOperation(value = "용어 북마크 (임시)", notes = "용어 북마크. 임시용입니다. 폴더 기능이 완성되면 폴더 관련 로직과 함께 수정해야 합니다. ")
    @GetMapping("/term/bookmark/{id}")
    public ApiResponse<String> bookmarkTerm(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @Parameter(name = "id", description = "term id", in = PATH, required = true) @PathVariable(value = "id") Long id
    ) {
        Member member = memberService.getMemberByToken(token);
        termService.bookmarkTerm(member, id);

        return ApiResponse.of(TermResponseType.BOOKMARK_SUCCESS);
    }


    @ApiOperation(value = "전체 용어 리스트", notes = "전체 용어 리스트, 카테고리별.\n 페이지네이션 처리\n category가 없을 경우 추천 단어 리스트\n page: 몇 페이지인지\nsize: 한 페이지 당 불러온 단어 개수")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 2054, message = "용어 리스트 응답 성공"),
    })
    @GetMapping("/term/list")
    public ApiResponse<Page<TermSimpleDto>> getTermList(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @Parameter(name = "category", description = "String : X / pm / marketing / development / design / business / IT", in = QUERY) @RequestParam(value = "category", required = false) String categoryName,
            Pageable pageable
    ) {
        Member member = memberService.getMemberByToken(token);

        if (categoryName == null) {
            return ApiResponse.of(TermResponseType.LIST_SUCCESS, termService.getRecommendedTerms(member, pageable));
        } else {
            return ApiResponse.of(TermResponseType.LIST_SUCCESS, termService.getTermListByCategory(member, categoryService.findByName(categoryName), pageable));
        }
    }

    @ApiOperation(value = "오늘의 용어", notes = "오늘의 용어 4개 리턴 - 사용자의 관심사 기반으로")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 2055, message = "오늘의 용어 응답 성공"),
    })
    @GetMapping("/term/daily")
    public ApiResponse<List<TermSimpleDto>> getDailyTerms(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token
    ){
        Member member = memberService.getMemberByToken(token);
        List<TermSimpleDto> dailyTerms = termService.getDailyTerms(member);

        return ApiResponse.of(TermResponseType.DAILY_SUCCESS, dailyTerms);
    }
}
