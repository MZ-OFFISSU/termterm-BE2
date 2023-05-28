package server.api.termterm.response.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.termterm.response.BaseResponseType;


@Getter
@AllArgsConstructor
public enum MemberResponseType implements BaseResponseType {
    // Exception
    NO_AUTHORIZATION(40100, "권한이 없습니다.", HttpStatus.UNAUTHORIZED),
    NOT_FOUND_USER(40101,"사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_AUTHENTICATION(40102,"인증 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED),
    DUPLICATE_USER(40103,"이미 존재하는 사용자입니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_PASSWORD(40104,"비밀번호를 입력해주세요",HttpStatus.BAD_REQUEST),
    LOGOUT_MEMBER(40105,"로그아웃된 사용자입니다.",HttpStatus.OK),
    DUPLICATE_NICKNAME(40106, "이미 사용중인 닉네임입니다.", HttpStatus.BAD_REQUEST),
    FAILED_INFO_UPDATE(40107,"유저 정보 업데이트 실패", HttpStatus.BAD_REQUEST),
    SESSION_EXPIRED(40108,"세션이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    SIGNUP_FAILED(40109, "회원가입에 실패하였습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    WITHDRAWAL_FAILED(40110, "회원탈퇴 실패", HttpStatus.INTERNAL_SERVER_ERROR),

    // SUCCESS
    SIGNUP_SUCCESS(20100, "회원가입 성공", HttpStatus.OK),
    LOGIN_SUCCESS(20101, "로그인 성공", HttpStatus.OK),
    MEMBER_INFO_GET_SUCCESS(20102, "사용자 정보 조회 성공", HttpStatus.OK),
    MEMBER_INFO_UPDATE_SUCCESS(20103, "사용자 정보 수정 성공", HttpStatus.OK),
    WITHDRAWAL_SUCCESS(20104, "회원 탈퇴 성공", HttpStatus.OK),
    NOT_DUPLICATED_NICKNAME(20105, "사용할 수 있는 닉네임입니다.", HttpStatus.OK),
    SYNC_SUCCESS(20106, "데이터베이스에 사용자 프로필이미지 동기화 성공", HttpStatus.OK),
    DELETE_PROFILE_IMAGE(20107, "사용자 프로필이미지 삭제 성공", HttpStatus.OK),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
