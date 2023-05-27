package server.api.termterm.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.api.termterm.dto.jwt.TokenDto;
import server.api.termterm.dto.member.BaseMemberInfoDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppleService extends SocialAuthService{
    @Override
    public TokenDto getToken(String code) {
        return null;
    }

    @Override
    public BaseMemberInfoDto getMemberInfo(TokenDto tokenDto) {
        return null;
    }
}
