package server.api.termterm.domain.term;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.api.termterm.domain.member.Member;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class DailyTerm {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DAILY_TERM_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TERM_ID")
    private Term term;

    @Builder.Default
    private LocalDate lastRefreshedDate = LocalDate.now();

    @PrePersist
    private void setTime(){
        this.lastRefreshedDate = LocalDate.now();
    }

    public void updateTerm(Term term){
        this.term = term;
        this.lastRefreshedDate = LocalDate.now();
    }
}
