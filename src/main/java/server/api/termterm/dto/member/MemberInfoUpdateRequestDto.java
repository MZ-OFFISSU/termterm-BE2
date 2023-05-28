package server.api.termterm.dto.member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoUpdateRequestDto {
    private String nickname;
    private String domain;
    private String job;
    private Integer yearCareer;
    private String introduction;

    public MemberInfoUpdateRequestDto trimAll(){
        this.nickname = nickname.replace(" ", "");
        this.domain = domain.trim();
        this.job = job.trim();
        this.introduction = introduction.trim();

        return this;
    }
}
