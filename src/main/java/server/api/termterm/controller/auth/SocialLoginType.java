package server.api.termterm.controller.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialLoginType {
    GOOGLE("google"),
    KAKAO("kakao"),
    APPLE("apple"),
    WITHDRAWED("withdrawed"),
    ;

    private final String value;
}
