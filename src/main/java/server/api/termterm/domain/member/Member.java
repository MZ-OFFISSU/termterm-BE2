package server.api.termterm.domain.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.api.termterm.domain.bookmark.CurationBookmark;
import server.api.termterm.domain.bookmark.TermBookmark;
import server.api.termterm.domain.category.Category;
import server.api.termterm.domain.comment.Comment;
import server.api.termterm.domain.comment.CommentLike;
import server.api.termterm.domain.curation.CurationPaid;
import server.api.termterm.domain.report.Report;
import server.api.termterm.domain.term.DailyTerm;
import server.api.termterm.dto.member.MemberInfoUpdateRequestDto;

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

    @Enumerated(EnumType.STRING)
    private SocialLoginType socialType;
    private String identifier;
    private String refreshToken;

    @ManyToMany
    @JoinTable(name = "MEMBER_CATEGORY",
            joinColumns = @JoinColumn(name = "MEMBER_ID"),
            inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID"))
    private List<Category> categories;

    @OneToMany(mappedBy = "member")
    private List<Comment> comments;

    @OneToMany(mappedBy = "member")
    private List<CommentLike> commentLikes;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyTerm> dailyTerms;

    @OneToMany(mappedBy = "member")
    private List<Report> reports;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CurationBookmark> curationBookmarks;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TermBookmark> termBookmarks;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CurationPaid> curationPaids;

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

    public void updateInfo(MemberInfoUpdateRequestDto memberInfoUpdateRequestDto){
        this.nickname = memberInfoUpdateRequestDto.getNickname();
        this.domain = memberInfoUpdateRequestDto.getDomain();
        this.job = memberInfoUpdateRequestDto.getJob();
        this.yearCareer = memberInfoUpdateRequestDto.getYearCareer();
        this.introduction = memberInfoUpdateRequestDto.getIntroduction();
    }

    public void updateProfileImg(String profileImg){
        this.profileImg = profileImg;
    }

    public void updateCategories(List<Category> categories){
        this.categories = categories;
    }


    @PrePersist
    public void initLikeCnt(){
        this.point = 0;
    }

    public void addPoint(Integer point){
        this.point += point;
    }

    public void subPoint(Integer point){
        this.point -= point;
    }

}
