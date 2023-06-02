package server.api.termterm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.api.termterm.domain.member.Member;
import server.api.termterm.domain.term.DailyTerm;
import server.api.termterm.dto.dailyTerm.DailyTermAndStatusDto;

import java.util.List;

public interface DailyTermRepository extends JpaRepository<DailyTerm, Long> {
    @Query("select new server.api.termterm.dto.dailyTerm.DailyTermAndStatusDto(dt, tb.status) " +
            "from DailyTerm dt " +
            "left join TermBookmark tb " +
            "on dt.member = tb.member and dt.term = tb.term " +
            "where dt.member = :member")
    List<DailyTermAndStatusDto> getByMemberWithStatus(@Param("member") Member member);
}
