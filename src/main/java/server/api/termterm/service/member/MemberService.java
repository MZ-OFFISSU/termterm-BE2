package server.api.termterm.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.termterm.domain.category.Category;
import server.api.termterm.domain.jwt.Token;
import server.api.termterm.domain.member.Authority;
import server.api.termterm.domain.member.Member;
import server.api.termterm.dto.jwt.TokenDto;
import server.api.termterm.dto.member.BaseMemberInfoDto;
import server.api.termterm.dto.member.MemberCategoriesUpdateRequestDto;
import server.api.termterm.dto.member.MemberInfoDto;
import server.api.termterm.dto.member.MemberInfoUpdateRequestDto;
import server.api.termterm.jwt.JwtProvider;
import server.api.termterm.repository.CategoryRepository;
import server.api.termterm.repository.MemberRepository;
import server.api.termterm.repository.TokenRepository;
import server.api.termterm.response.BizException;
import server.api.termterm.response.category.CategoryResponseType;
import server.api.termterm.response.jwt.JwtResponseType;
import server.api.termterm.response.member.MemberResponseType;

import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final CategoryRepository categoryRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public TokenDto issueToken(Member member) {
        member.setRefreshToken(createRefreshToken(member));

        return TokenDto.builder()
                .refresh_token(member.getRefreshToken())
                .access_token(jwtProvider.createToken(member.getIdentifier(), member.getRoles()))
                .build();
    }

    @Transactional
    public Member signup(BaseMemberInfoDto memberInfoDto) {
        Member memberEntity = Member.builder()
                .socialId(memberInfoDto.getSocialId())
                .name(memberInfoDto.getName())
                .email(memberInfoDto.getEmail())
                .nickname(memberInfoDto.getNickname())
                .profileImg(memberInfoDto.getProfileImg())
                .identifier(UUID.randomUUID().toString())
                .build();

        memberEntity.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));

        try {
            return memberRepository.save(memberEntity);
        }catch (Exception e){
            throw new BizException(MemberResponseType.SIGNUP_FAILED);
        }
    }

    @Transactional
    public Member getMember(BaseMemberInfoDto memberInfoDto) {
        return memberRepository.findBySocialIdAndEmail(memberInfoDto.getSocialId(), memberInfoDto.getEmail())
                .orElseThrow(() -> new BizException(MemberResponseType.NOT_FOUND_USER));
    }

    // Refresh Token ================

    /**
     * Refresh 토큰을 생성한다.
     * Redis 내부에는
     * refreshToken:memberId : tokenValue
     * 형태로 저장한다.
     */
    @Transactional
    public String createRefreshToken(Member member) {
        Token token = tokenRepository.save(
                Token.builder()
                        .id(member.getId())
                        .refresh_token(UUID.randomUUID().toString())
                        .expiration(120)
                        .build()
        );
        return token.getRefresh_token();
    }

    @Transactional
    public Token validRefreshToken(Member member, String refreshToken){
        Token token = tokenRepository.findById(member.getId())
                .orElseThrow(() -> new BizException(JwtResponseType.REFRESH_TOKEN_EXPIRED));

        // 해당유저의 Refresh 토큰 만료 : Redis에 해당 유저의 토큰이 존재하지 않음
        if (token.getRefresh_token() == null) {
            return null;
        } else {
            // 리프레시 토큰 만료일자가 얼마 남지 않았을 때 만료시간 연장..?
            if(token.getExpiration() < 10) {
                token.setExpiration(1000);
                tokenRepository.save(token);
            }

            // 토큰이 같은지 비교
            if(!token.getRefresh_token().equals(refreshToken)) {
                return null;
            } else {
                return token;
            }
        }
    }

    @Transactional
    public TokenDto refreshAccessToken(TokenDto token){
        String account = jwtProvider.getAccount(token.getAccess_token());
        Member member = memberRepository.findByIdentifier(account).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정정보입니다."));
        Token refreshToken = validRefreshToken(member, token.getRefresh_token());

        if (refreshToken != null) {
            return TokenDto.builder()
                    .access_token(jwtProvider.createToken(account, member.getRoles()))
                    .refresh_token(refreshToken.getRefresh_token())
                    .build();
        } else {
            throw new BizException(MemberResponseType.LOGOUT_MEMBER);
        }
    }

    @Transactional
    public String getIdentifierByToken(String token){
        // Bearer 검증
        if (!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
            throw new BizException(JwtResponseType.NO_BEARER);
        }else {
            token = token.split(" ")[1].trim();
            return jwtProvider.getAccount(token);
        }
    }

    @Transactional
    public void withdraw(Member member) {
        try {
            member.withdraw();
        }catch (Exception e){
            throw new BizException(MemberResponseType.WITHDRAWAL_FAILED);
        }
    }

    @Transactional
    public Boolean getIsMember(BaseMemberInfoDto memberInfoDto) {
        return memberRepository.existsBySocialIdAndEmail(memberInfoDto.getSocialId(), memberInfoDto.getEmail());
    }

    @Transactional
    public Member getMemberByToken(String token){
        return memberRepository.findByIdentifier(getIdentifierByToken(token))
                .orElseThrow(() -> new BizException(MemberResponseType.NOT_FOUND_USER));
    }

    @Transactional
    public MemberInfoDto getMemberInfo(Member member){
        return MemberInfoDto.of(member);
    }

    @Transactional
    public Boolean checkDuplicateNickname(String nickname){
        return memberRepository.existsByNicknameIgnoreCase(nickname);
    }

    @Transactional
    public void updateMemberInfo(Member member, MemberInfoUpdateRequestDto memberInfoUpdateRequestDto) {
        try {
            member.updateInfo(memberInfoUpdateRequestDto);
        }catch (Exception e){
            throw new BizException(MemberResponseType.FAILED_INFO_UPDATE);
        }
    }

    @Transactional
    public void updateMemberCategories(Member member, MemberCategoriesUpdateRequestDto memberCategoriesUpdateRequestDto) {
        List<String> categoryStrings = memberCategoriesUpdateRequestDto.getCategories();

        if(!checkValidateCategoryString(categoryStrings)){
            throw new BizException(CategoryResponseType.CATEGORY_NOT_EXISTS);
        }

        member.updateCategories(categoryRepository.findByName(categoryStrings));
    }

    private Boolean checkValidateCategoryString(List<String> categories){
        List<String> validateCategories = Arrays.asList("PM", "MARKETING", "DEVELOPMENT", "DESIGN", "BUSINESS", "IT");

        for(String categoryString : categories){
            if (!validateCategories.contains(categoryString)){
                return false;
            }
        }

        return true;

    }

    public String getProfileImageUrl(Member member) {
        return member.getProfileImg();
    }
}