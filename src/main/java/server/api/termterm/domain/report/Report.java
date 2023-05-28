package server.api.termterm.domain.report;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.api.termterm.domain.BaseTimeEntity;
import server.api.termterm.domain.comment.Comment;
import server.api.termterm.domain.member.Member;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Report extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPORT_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReportType type;

    private String description;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMMENT_ID")
    private Comment comment;

    public void setStatus(ReportStatus status){
        this.status = status;
    }
}
