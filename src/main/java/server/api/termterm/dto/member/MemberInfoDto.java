package server.api.termterm.dto.member;


import lombok.Builder;
import lombok.Getter;
import server.api.termterm.domain.member.Member;

@Getter
@Builder
public class MemberInfoDto {
    private Long memberId;
    private String name;
    private String nickname;
    private String email;
    private String profileImage;
    private String job;
    private String domain;
    private String introduction;
    private Integer point;
    private Integer yearCareer;
//    private List<CategoryDto> categories = new ArrayList<>();

    public static MemberInfoDto of(Member member){
        return MemberInfoDto.builder()
                .memberId(member.getId())
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileImage(member.getProfileImg())
                .job(member.getJob())
                .domain(member.getDomain())
                .introduction(member.getIntroduction())
                .point(member.getPoint())
                .yearCareer(member.getYearCareer())
                .build();
    }
}
