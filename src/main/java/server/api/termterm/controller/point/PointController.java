package server.api.termterm.controller.point;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import server.api.termterm.domain.curation.Curation;
import server.api.termterm.domain.member.Member;
import server.api.termterm.response.base.ApiResponse;
import server.api.termterm.response.base.BizException;
import server.api.termterm.response.point.PointResponseType;
import server.api.termterm.service.curation.CurationService;
import server.api.termterm.service.member.MemberService;
import server.api.termterm.service.point.PointService;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;

@Api(tags = "Point")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class PointController {
    private final MemberService memberService;
    private final CurationService curationService;
    private final PointService pointService;

    @ApiOperation(value = "큐레이션 구매", notes = "큐레이션에 포인트 지불")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 2110, message = "큐레이션 구매 성공"),
            @io.swagger.annotations.ApiResponse(code = 4092, message = "ID와 일치하는 큐레이션이 존재하지 않습니다."),
            @io.swagger.annotations.ApiResponse(code = 4110, message = "이미 포인트를 지불하였습니다."),
            @io.swagger.annotations.ApiResponse(code = 4111, message = "포인트가 부족합니다."),
    })
    @PutMapping("/point/pay/curation/{id}")
    public ApiResponse<String> payForCuration(
            @RequestHeader(name = "Authorization") String token,
            @Parameter(name = "id", description = "curation id", in = PATH) @PathVariable Long id
    ){
        Member member = memberService.getMemberByToken(token);
        Curation curation = curationService.findById(id);

        // if user has already paid, throw exception
        if(curationService.getWhetherMemberPaidForCuration(member, curation)){
            throw new BizException(PointResponseType.ALREADY_PAID);
        }

        pointService.payForCuration(member, curation);

        return ApiResponse.of(PointResponseType.CURATION_PAY_SUCCESS);
    }
}
