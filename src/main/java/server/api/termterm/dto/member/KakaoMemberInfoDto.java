package server.api.termterm.dto.member;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class KakaoMemberInfoDto extends BaseMemberInfoDto{
    private Boolean isDefaultImage;

}
