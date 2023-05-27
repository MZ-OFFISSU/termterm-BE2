package server.api.termterm.service.auth;

import server.api.termterm.dto.jwt.TokenDto;
import server.api.termterm.dto.member.BaseMemberInfoDto;

public abstract class SocialAuthService {
    abstract public TokenDto getToken(String code);
    abstract public BaseMemberInfoDto getMemberInfo(TokenDto tokenDto);
}
