package server.api.termterm.dto.member;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class BaseMemberInfoDto {
    public String socialId;
    public String name;
    public String email;
    public String nickname;
    public String profileImg;
}
