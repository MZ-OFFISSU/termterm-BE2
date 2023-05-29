package server.api.termterm.controller.member;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import server.api.termterm.domain.member.Member;
import server.api.termterm.dto.member.MemberCategoriesUpdateRequestDto;
import server.api.termterm.dto.member.MemberInfoDto;
import server.api.termterm.dto.member.MemberInfoUpdateRequestDto;
import server.api.termterm.response.base.ApiResponse;
import server.api.termterm.response.amazonS3.AmazonS3ResponseType;
import server.api.termterm.response.base.BizException;
import server.api.termterm.response.member.MemberResponseType;
import server.api.termterm.service.amazonS3.AmazonS3Service;
import server.api.termterm.service.member.MemberService;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;


@Api(tags = "Member")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class MemberController {

    private final MemberService memberService;
    private final AmazonS3Service amazonS3Service;

    @ApiOperation(value = "회원정보 조회", notes = "회원정보 조회")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20102, message = "회원정보 조회 성공 (200)"),
            @io.swagger.annotations.ApiResponse(code = 40003, message = "access token 만료 (400)"),
            @io.swagger.annotations.ApiResponse(code = 40008, message = "지원되지 않는 JWT 토큰입니다. (400)"),
            @io.swagger.annotations.ApiResponse(code = 40101, message = "사용자를 찾을 수 없습니다. (404)"),
    })
    @GetMapping("/member/info")
    public ApiResponse<MemberInfoDto> getMemberInfo(@RequestHeader(name = "Authorization") String token){
        Member member = memberService.getMemberByToken(token);
        MemberInfoDto memberInfoDto = memberService.getMemberInfo(member);

        return ApiResponse.of(MemberResponseType.MEMBER_INFO_GET_SUCCESS, memberInfoDto);
    }

    @ApiOperation(value = "사용자 정보 수정", notes = "사용자 정보 수정.\n프로필 이미지 변경은 API 따로 개발")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20103, message = "사용자 정보 수정 성공 (200)"),
            @io.swagger.annotations.ApiResponse(code = 40106, message = "이미 사용중인 닉네임입니다. (400)"),
    })
    @PutMapping("/member/info")
    public ApiResponse<String> updateMemberInfo(
            @RequestHeader(name = "Authorization") String token,
            @Parameter(name = "MemberInfoUpdateRequestDto", description = "String : domain/introduction(소개)/job/nickname,  Integer : yearCareer(연차)") @RequestBody MemberInfoUpdateRequestDto memberInfoUpdateRequestDto
    ){
        Member member = memberService.getMemberByToken(token);
        if (memberService.checkDuplicateNickname(memberInfoUpdateRequestDto.getNickname())){
            throw new BizException(MemberResponseType.DUPLICATE_NICKNAME);
        }

        memberService.updateMemberInfo(member, memberInfoUpdateRequestDto);
        return ApiResponse.of(MemberResponseType.MEMBER_INFO_UPDATE_SUCCESS);
    }

    @ApiOperation(value = "관심사 업데이트", notes = "사용자의 관심사 업데이트\n{\n\"categories\": [\n\"PM\", \"DESIGN\", \"BUSINESS\"\n]\n}")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20103, message = "사용자 관심사 업데이트 성공 (200)"),
            @io.swagger.annotations.ApiResponse(code = 41001, message = "카테고리가 존재하지 않음 (400)"),
    })
    @PutMapping("/member/info/category")
    public ApiResponse<String> updateMemberCategories(
            @Parameter(name = "Authorization", description = "Bearer {accessToken}", in = HEADER) @RequestHeader(name = "Authorization") String token,
            @Parameter(name = "MemberCategoriesRequestDto") @RequestBody MemberCategoriesUpdateRequestDto memberCategoriesUpdateRequestDto
    ){
        Member member = memberService.getMemberByToken(token);
        memberService.updateMemberCategories(member, memberCategoriesUpdateRequestDto);

        return ApiResponse.of(MemberResponseType.MEMBER_INFO_UPDATE_SUCCESS);
    }

    @ApiOperation(value = "프로필 사진 주소 리턴", notes = "프로필 사진 주소 리턴")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20102, message = "사용자 프로필이미지 url 응답 성공 (200)"),
    })
    @GetMapping("/member/info/profile-image")
    public ApiResponse<String> getProfileImageUrl(
            @Parameter(name = "Authorization", description = "Bearer {accessToken}", in = HEADER) @RequestHeader(name = "Authorization") String token
    ){
        Member member = memberService.getMemberByToken(token);
        String profileImageUrl = memberService.getProfileImageUrl(member);

        return ApiResponse.of(MemberResponseType.MEMBER_INFO_GET_SUCCESS, profileImageUrl);
    }

    @ApiOperation(value = "프로필사진 업로드 API 발급", notes = "AWS presigned-url을 이용, 클라이언트가 S3에 직접적으로 사진을 업로드 할 수 있는 API 발급. \n발급받은 API로 사진과 함께 PUT 요청을 보내고 성공하였으면, 서버의 \"/member/info/profile-image/sync\" 로 꼭!!! API 요청 부탁드립니다.")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20801, message = "presigned-url 발급에 성공하였습니다. (200)"),
            @io.swagger.annotations.ApiResponse(code = 40801, message = "presigned-url 발급에 실패하였습니다. (500)"),
    })
    @GetMapping("/member/info/profile-image/presigned-url")
    public ApiResponse<String> getPresignedUrl(
            @Parameter(name = "Authorization", description = "Bearer {accessToken}", in = HEADER) @RequestHeader(name = "Authorization") String token
    ){
        Member member = memberService.getMemberByToken(token);
        String preSignedUrl = amazonS3Service.getPresignedUrl(member);

        return ApiResponse.of(AmazonS3ResponseType.PRESIGNED_URL_ISSUANCE_SUCCESS, preSignedUrl);
    }

    @ApiOperation(value = "DB에 사용자의 프로필이미지 주소 동기화", notes = "presigned-url을 통해 프로필 이미지가 S3에 정상적으로 업로드 되었으면, 해당 이미지 주소를 DB에도 반영")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20106, message = "데이터베이스에 사용자 프로필이미지 동기화 성공 (200)"),
    })
    @GetMapping("/member/info/profile-image/sync")
    public ApiResponse<String> syncProfileImageUrl(
            @Parameter(name = "Authorization", description = "Bearer {accessToken}", in = HEADER) @RequestHeader(name = "Authorization") String token
    ){
        Member member = memberService.getMemberByToken(token);
        memberService.syncProfileImageUrl(member);

        return ApiResponse.of(MemberResponseType.SYNC_SUCCESS);
    }

    @ApiOperation(value = "프로필이미지 삭제", notes = "이미지를 삭제하고, 기본이미지로 변경")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20107, message = "사용자 프로필이미지 삭제 성공 (200)"),
    })
    @DeleteMapping("/member/info/profile-image")
    public ApiResponse<String> deleteProfileImage(
            @Parameter(name = "Authorization", description = "Bearer {accessToken}", in = HEADER) @RequestHeader(name = "Authorization") String token
    ){
        Member member = memberService.getMemberByToken(token);
        amazonS3Service.removeS3Image(member);
        memberService.initializeProfileImageUrl(member);

        return ApiResponse.of(MemberResponseType.DELETE_PROFILE_IMAGE);
    }

    @ApiOperation(value = "회원 탈퇴", notes = "회원 탈퇴")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20104, message = "회원탈퇴 성공 (204)"),
            @io.swagger.annotations.ApiResponse(code = 40101, message = "사용자를 찾을 수 없습니다. (404)"),
            @io.swagger.annotations.ApiResponse(code = 40110, message = "회원탈퇴 실패 (500)"),
    })
    @GetMapping("/member/withdraw")
    public ApiResponse<String> withdraw(@RequestHeader("Authorization") String token){
        memberService.withdraw(memberService.getMemberByToken(token));

        return ApiResponse.of(MemberResponseType.WITHDRAWAL_SUCCESS);
    }

    @ApiOperation(value = "닉네임 중복 체크", notes = "닉네임 중복체크")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 2024, message = "사용 가능한 닉네임입니다. (200)"),
            @io.swagger.annotations.ApiResponse(code = 4026, message = "이미 사용중인 닉네임입니다. (400)"),
    })
    @GetMapping("/member/nickname/check")
    public ApiResponse<String> isNicknameDuplicated(
            @Parameter(name = "nickname", description = "String: 바꾸고자 하는 닉네임", in = QUERY) @RequestParam String nickname
    ){
        if(memberService.checkDuplicateNickname(nickname))
            return ApiResponse.of(MemberResponseType.DUPLICATE_NICKNAME);
        else
            return ApiResponse.of(MemberResponseType.NOT_DUPLICATED_NICKNAME);
    }
}
