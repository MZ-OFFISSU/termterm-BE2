package server.api.termterm.domain.curation;

import lombok.*;
import server.api.termterm.domain.basetime.BaseTimeEntity;
import server.api.termterm.domain.member.Member;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurationPaid extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CURATION_PAID_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "CURATION_ID")
    private Curation curation;

    @Builder
    public CurationPaid(Member member, Curation curation){
        this.member = member;
        this.curation = curation;
    }
}
