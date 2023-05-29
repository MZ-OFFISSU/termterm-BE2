package server.api.termterm.controller.curation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import server.api.termterm.domain.category.Category;
import server.api.termterm.domain.curation.Curation;
import server.api.termterm.domain.member.Member;
import server.api.termterm.dto.curation.CurationDetailDto;
import server.api.termterm.dto.curation.CurationRegisterRequestDto;
import server.api.termterm.dto.curation.CurationSimpleInfoDto;
import server.api.termterm.dto.curation.CurationSimpleInfoDtoInterface;
import server.api.termterm.response.archive.ArchiveResponseType;
import server.api.termterm.response.base.BizException;
import server.api.termterm.response.base.ApiResponse;
import server.api.termterm.response.curation.CurationResponseType;
import server.api.termterm.response.member.MemberResponseType;
import server.api.termterm.service.category.CategoryService;
import server.api.termterm.service.curation.CurationService;
import server.api.termterm.service.member.MemberService;

import java.util.List;
import java.util.Set;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.*;

@Api(tags = "Curation")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class CurationController {
    private final MemberService memberService;
    private final CurationService curationService;
    private final CategoryService categoryService;

    @ApiOperation(value = "큐레이션 등록", notes = "큐레이션 등록")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 2091, message = "큐레이션 등록 성공 (201)"),
    })
    @PostMapping("/curation/register")
    public ApiResponse<String> registerCuration(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @Parameter(name = "CurationRegisterRequestDto", description = "위 설명 참고") @RequestBody CurationRegisterRequestDto curationRegisterRequestDto
    ){
        Member member = memberService.getMemberByToken(token);
        if (!memberService.checkAdmin(member))
            throw new BizException(MemberResponseType.NO_AUTHORIZATION);

        curationService.registerCuration(curationRegisterRequestDto);

        return ApiResponse.of(CurationResponseType.CURATION_REGISTER_SUCCESS);
    }

    @ApiOperation(value = "큐레이션 북마크", notes = "큐레이션 북마크")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 2093, message = "큐레이션 북마크 성공 (200)"),
            @io.swagger.annotations.ApiResponse(code = 4092, message = "ID와 일치하는 큐레이션이 존재하지 않습니다. (404)"),
    })
    @PutMapping("/curation/bookmark/{id}")
    public ApiResponse<String> bookmarkCuration(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @PathVariable("id") Long id
    ){
        Member member = memberService.getMemberByToken(token);
        Curation curation = curationService.findById(id);
        curationService.bookmarkCuration(member, curation);

        return ApiResponse.of(CurationResponseType.CURATION_BOOKMARK_SUCCESS);
    }

    @ApiOperation(value = "큐레이션 북마크 취소", notes = "큐레이션 북마크 취소")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 2094, message = "큐레이션 북마크 취소 성공 (200)"),
            @io.swagger.annotations.ApiResponse(code = 4092, message = "ID와 일치하는 큐레이션이 존재하지 않습니다. (404)"),
            @io.swagger.annotations.ApiResponse(code = 4093, message = "북마크가 되어있지 않아 취소를 할 수 없습니다. (400)"),
    })
    @PutMapping("/curation/unbookmark/{id}")
    public ApiResponse<String> unbookmarkCuration(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @PathVariable("id") Long id
    ){
        Member member = memberService.getMemberByToken(token);
        Curation curation = curationService.findById(id);
        curationService.unbookmarkCuration(member, curation);

        return ApiResponse.of(CurationResponseType.CURATION_UNBOOKMARK_SUCCESS);
    }

    @ApiOperation(value = "큐래이션 상세", notes = "큐레이션 한개 상세정보 리턴")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 2096, message = "큐레이션 디테일 응답 성공 (200)"),
            @io.swagger.annotations.ApiResponse(code = 4092, message = "ID와 일치하는 큐레이션이 존재하지 않습니다. (404)"),
    })
    @GetMapping("/curation/detail/{id}")
    public ApiResponse<CurationDetailDto> getCurationDetail(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @PathVariable("id") Long id
    ){
        Member member = memberService.getMemberByToken(token);
        Curation curation = curationService.findById(id);

        // 유저 paid 여부 확인
        Boolean memberPaid = curationService.getWhetherMemberPaidForCuration(member, curation);

        CurationDetailDto curationDetailDto = curationService.getCurationDetail(member, curation, memberPaid);

        return ApiResponse.of(CurationResponseType.CURATION_DETAIL_RETURN_SUCCESS, curationDetailDto);
    }

    @ApiOperation(value = "카테고리별 큐레이션 리스트", notes = "카테고리별 큐레이션 리스트 리턴\ncategory에 아무것도 넣지 않을 경우 추천 큐레이션")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 2095, message = "큐레이션 리스트 응답 성공 (200)"),
            @io.swagger.annotations.ApiResponse(code = 4101, message = "카테고리가 존재하지 않습니다. (404)"),
    })
    @GetMapping("/curation/list")
    public ApiResponse<List<CurationSimpleInfoDtoInterface>> getCurationsByCategory(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @Parameter(name = "category", description = "String : X / pm / marketing / development / design / business / IT", in = QUERY) @RequestParam(value = "category", required = false) String categoryName
    ){
        Member member = memberService.getMemberByToken(token);
        Category category = categoryService.findByName(categoryName);

        List<CurationSimpleInfoDtoInterface> curationSimpleInfoDtoInterfaces =
                (categoryName == null) ?  curationService.getRecommendedCurations(member) : curationService.getCurationsByCategory(member, category);

        return ApiResponse.of(CurationResponseType.CURATION_LIST_RETURN_SUCCESS, curationSimpleInfoDtoInterfaces);
    }


    @ApiOperation(value = "아카이브한 큐레이션들 목록", notes = "아카이브한 큐레이션들 목록 리턴")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 2302, message = "API 정상 작동 (200)"),
            @io.swagger.annotations.ApiResponse(code = 2303, message = "아카이브한 큐레이션이 아직 없음 (200)"),
            @io.swagger.annotations.ApiResponse(code = 4021, message = "토큰에 해당하는 유저 없음 (404)"),
    })
    @GetMapping("/curation/archived")
    public ApiResponse<Set<CurationSimpleInfoDto>> getCurationArchived(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token
    ){
        Member member = memberService.getMemberByToken(token);

        Set<CurationSimpleInfoDto> curationSimpleInfoDtoSet = curationService.getBookmarkedCurations(member);

        if(curationSimpleInfoDtoSet.size() == 0){
            throw new BizException(ArchiveResponseType.NO_ARCHIVED_CURATION);
        }

        return ApiResponse.of(ArchiveResponseType.GET_ARCHIVED_CURATION_SUCCESS, curationSimpleInfoDtoSet);
    }


}
