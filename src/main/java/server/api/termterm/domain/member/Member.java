package server.api.termterm.domain.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.api.termterm.controller.auth.SocialLoginType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    private String socialId;
    private String name;
    private String email;
    private String profileImg;

    @Column(unique = true)
    private String nickname;

    private String introduction;
    private String job;
    private Integer yearCareer;
    private String domain;
    private Integer point;
    private SocialLoginType socialType;
    private String identifier;
    private String refreshToken;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Authority> roles = new ArrayList<>();

    public void setRoles(List<Authority> role) {
        this.roles = role;
        role.forEach(o -> o.setMember(this));
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void withdraw(){
        this.email = "withdrawed@with.draw";
        this.domain = null;
        this.introduction = null;
        this.name = null;
        this.nickname = null;
        this.socialId = null;
        this.socialType = SocialLoginType.WITHDRAWED;
        this.yearCareer = 0;
        this.identifier = null;
    }
}
