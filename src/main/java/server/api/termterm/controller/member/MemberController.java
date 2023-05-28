package server.api.termterm.controller.member;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.api.termterm.domain.member.Member;
import server.api.termterm.dto.member.MemberCategoriesUpdateRequestDto;
import server.api.termterm.dto.member.MemberInfoDto;
import server.api.termterm.dto.member.MemberInfoUpdateRequestDto;
import server.api.termterm.response.ResponseMessage;
import server.api.termterm.response.member.MemberResponseType;
import server.api.termterm.service.member.MemberService;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;


@Api(tags = "Member")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class MemberController {

    private final MemberService memberService;

    @ApiOperation(value = "회원정보 조회", notes = "회원정보 조회")
    @ApiResponses({
            @ApiResponse(code = 20102, message = "회원정보 조회 성공 (200)"),
            @ApiResponse(code = 40003, message = "access token 만료 (400)"),
            @ApiResponse(code = 40008, message = "지원되지 않는 JWT 토큰입니다. (400)"),
            @ApiResponse(code = 40101, message = "사용자를 찾을 수 없습니다. (404)"),
    })
    @GetMapping("/member/info")
    public ResponseEntity<ResponseMessage<MemberInfoDto>> getMemberInfo(@RequestHeader(name = "Authorization") String token){
        Member member = memberService.getMemberByToken(token);
        MemberInfoDto memberInfoDto = memberService.getMemberInfo(member);

        return new ResponseEntity<>(ResponseMessage.create(MemberResponseType.MEMBER_INFO_GET_SUCCESS, memberInfoDto), MemberResponseType.MEMBER_INFO_GET_SUCCESS.getHttpStatus());
    }

    @ApiOperation(value = "사용자 정보 수정", notes = "사용자 정보 수정.\n프로필 이미지 변경은 API 따로 개발")
    @ApiResponses({
            @ApiResponse(code = 2023, message = "사용자 정보 수정 성공 (200)"),
            @ApiResponse(code = 4026, message = "이미 사용중인 닉네임입니다. (400)"),
    })
    @PutMapping("/member/info")
    public ResponseEntity<ResponseMessage<String>> updateMemberInfo(
            @RequestHeader(name = "Authorization") String token,
            @Parameter(name = "MemberInfoUpdateRequestDto", description = "String : domain/introduction(소개)/job/nickname,  Integer : yearCareer(연차)") @RequestBody MemberInfoUpdateRequestDto memberInfoUpdateRequestDto
    ){
        Member member = memberService.getMemberByToken(token);
        if (memberService.checkDuplicateNickname(memberInfoUpdateRequestDto.getNickname())){
            return new ResponseEntity<>(ResponseMessage.create(MemberResponseType.DUPLICATE_NICKNAME), MemberResponseType.DUPLICATE_NICKNAME.getHttpStatus());
        }

        memberService.updateMemberInfo(member, memberInfoUpdateRequestDto);
        return new ResponseEntity<>(ResponseMessage.create(MemberResponseType.MEMBER_INFO_UPDATE_SUCCESS), MemberResponseType.MEMBER_INFO_UPDATE_SUCCESS.getHttpStatus());
    }

    @ApiOperation(value = "관심사 업데이트", notes = "사용자의 관심사 업데이트\n{\n\"categories\": [\n\"PM\", \"DESIGN\", \"BUSINESS\"\n]\n}")
    @ApiResponses({
            @ApiResponse(code = 2026, message = "사용자 관심사 업데이트 성공 (200)"),
            @ApiResponse(code = 4101, message = "카테고리가 존재하지 않음 (400)"),
    })
    @PutMapping("/member/info/category")
    public ResponseEntity<ResponseMessage> updateMemberCategories(
            @Parameter(name = "Authorization", description = "Bearer {accessToken}", in = HEADER) @RequestHeader(name = "Authorization") String token,
            @Parameter(name = "MemberCategoriesRequestDto", description = "") @RequestBody MemberCategoriesUpdateRequestDto memberCategoriesUpdateRequestDto
    ){
        Member member = memberService.getMemberByToken(token);
        memberService.updateMemberCategories(member, memberCategoriesUpdateRequestDto);

        return new ResponseEntity<>(ResponseMessage.create(MemberResponseType.MEMBER_INFO_UPDATE_SUCCESS), MemberResponseType.MEMBER_INFO_UPDATE_SUCCESS.getHttpStatus());
    }



    @ApiOperation(value = "회원 탈퇴", notes = "회원 탈퇴")
    @ApiResponses({
            @ApiResponse(code = 20104, message = "회원탈퇴 성공 (204)"),
            @ApiResponse(code = 40101, message = "사용자를 찾을 수 없습니다. (404)"),
            @ApiResponse(code = 40110, message = "회원탈퇴 실패 (500)"),
    })
    @GetMapping("/member/withdraw")
    public ResponseEntity<ResponseMessage<String>> withdraw(@RequestHeader("Authorization") String token){
        memberService.withdraw(memberService.getMemberByToken(token));

        return new ResponseEntity<>(ResponseMessage.create(MemberResponseType.WITHDRAWAL_SUCCESS), MemberResponseType.WITHDRAWAL_SUCCESS.getHttpStatus());
    }

    @ApiOperation(value = "닉네임 중복 체크", notes = "닉네임 중복체크")
    @ApiResponses({
            @ApiResponse(code = 2024, message = "사용 가능한 닉네임입니다. (200)"),
            @ApiResponse(code = 4026, message = "이미 사용중인 닉네임입니다. (400)"),
    })
    @GetMapping("/member/nickname/check")
    public ResponseEntity<ResponseMessage<String>> isNicknameDuplicated(
            @Parameter(name = "nickname", description = "String: 바꾸고자 하는 닉네임", in = QUERY) @RequestParam String nickname
    ){
        if(memberService.checkDuplicateNickname(nickname))
            return new ResponseEntity<>(ResponseMessage.create(MemberResponseType.DUPLICATE_NICKNAME), MemberResponseType.DUPLICATE_NICKNAME.getHttpStatus());
        else
            return new ResponseEntity<>(ResponseMessage.create(MemberResponseType.NOT_DUPLICATED_NICKNAME), MemberResponseType.NOT_DUPLICATED_NICKNAME.getHttpStatus());
    }
}
