package server.api.termterm.controller.auth;

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
import server.api.termterm.dto.jwt.TokenDto;
import server.api.termterm.dto.member.BaseMemberInfoDto;
import server.api.termterm.response.BizException;
import server.api.termterm.response.ResponseMessage;
import server.api.termterm.response.auth.AuthResponseType;
import server.api.termterm.response.jwt.JwtResponseType;
import server.api.termterm.service.auth.AppleService;
import server.api.termterm.service.auth.GoogleService;
import server.api.termterm.service.auth.KakaoService;
import server.api.termterm.service.auth.SocialAuthService;
import server.api.termterm.service.member.MemberService;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;

@Api(tags = "Auth (login, logout, token reissue)")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class AuthController {
    private final GoogleService googleService;
    private final KakaoService kakaoService;
    private final AppleService appleService;
    private final MemberService memberService;


    @ApiOperation(value = "소셜 로그인", notes = "카카오/구글/애플 소셜 로그인 요청. 회원가입이 안되어 있을경우 동시에 진행함. ")
    @ApiResponses({
            @ApiResponse(code = 2011, message = "로그인 성공 (200)"),
            @ApiResponse(code = 4021, message = "존재하지 않는 사용자 (400)"),
            @ApiResponse(code = 4010, message = "요청에 인가코드가 존재하지 않음 (400)"),
            @ApiResponse(code = 4012, message = "소셜 타입 경로가 유효하지 않음 (400)"),
            @ApiResponse(code = 4013, message = "구글 서버와의 연결 실패 (504)"),
            @ApiResponse(code = 4014, message = "카카오 서버와의 연결 실패 (505)"),
    })
    @PostMapping("/auth/{socialType}")
    public ResponseEntity<ResponseMessage<TokenDto>> auth(
            @Parameter(name = "auth-code", description = "카카오/구글/애플 로부터 받은 인가코드", in = HEADER) @RequestHeader(name = "auth-code") String authorizationCode,
            @Parameter(name = "socialType", description = "String: kakao/google/apple", in = PATH) @PathVariable("socialType") String socialType
    ){
        if (authorizationCode == null) {
            throw new BizException(AuthResponseType.NO_AUTHORIZATION_CODE);
        }

        SocialAuthService socialAuthService = getSocialAuthServiceBySocialType(socialType);
        TokenDto socialToken = socialAuthService.getToken(authorizationCode);
        BaseMemberInfoDto memberInfoDto = socialAuthService.getMemberInfo(socialToken);

        Member member = memberService.getIsMember(memberInfoDto) ? memberService.getMember(memberInfoDto) : memberService.signup(memberInfoDto);
        TokenDto serviceToken = memberService.issueToken(member);

        return new ResponseEntity<>(ResponseMessage.create(AuthResponseType.LOGIN_SUCCESS, serviceToken), AuthResponseType.LOGIN_SUCCESS.getHttpStatus());
    }

    @ApiOperation(value = "토큰 재발급", notes = "토큰 재발급")
    @ApiResponses({
            @ApiResponse(code = 20001, message = "토큰 재발급 성공 (200)"),
            @ApiResponse(code = 40004, message = "리프레시 토큰 만료 (400) - 재 로그인 필요"),
            @ApiResponse(code = 40101, message = "사용자를 찾을 수 없습니다. (404)"),
    })
    @PostMapping("/auth/token/refresh")
    public ResponseEntity<ResponseMessage<TokenDto>> refresh(@RequestBody TokenDto token) {
        TokenDto tokenDto = memberService.refreshAccessToken(token);

        return new ResponseEntity<>(ResponseMessage.create(JwtResponseType.TOKEN_REISSUED, tokenDto), JwtResponseType.TOKEN_REISSUED.getHttpStatus());
    }

    private SocialAuthService getSocialAuthServiceBySocialType(String type){
        if(type.equals(SocialLoginType.KAKAO.getValue()))           return kakaoService;
        else if(type.equals(SocialLoginType.GOOGLE.getValue()))     return googleService;
        else if(type.equals(SocialLoginType.APPLE.getValue()))      return appleService;
        else
            throw new BizException(AuthResponseType.INVALID_SOCIAL_TYPE);
    }

}
