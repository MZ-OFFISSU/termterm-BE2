package server.api.termterm.controller.member;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.api.termterm.domain.member.Member;
import server.api.termterm.dto.member.MemberInfoDto;
import server.api.termterm.response.ResponseMessage;
import server.api.termterm.response.member.MemberResponseType;
import server.api.termterm.service.member.MemberService;


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


//    @GetMapping("/user/get")
//    public ResponseEntity<SignResponse> getUser(@RequestHeader("Authorization") String token, @RequestParam String account) throws Exception {
//        return new ResponseEntity<>( memberService.getMember(account), HttpStatus.OK);
//    }
//
//    @GetMapping("/admin/get")
//    public ResponseEntity<SignResponse> getUserForAdmin(@RequestParam String account) throws Exception {
//        return new ResponseEntity<>( memberService.getMember(account), HttpStatus.OK);
//    }
}
