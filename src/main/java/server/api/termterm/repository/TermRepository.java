package server.api.termterm.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.api.termterm.domain.member.Member;
import server.api.termterm.domain.term.Term;
import server.api.termterm.dto.term.TermMinimumDto;

import java.util.List;
import java.util.Optional;

public interface TermRepository extends JpaRepository<Term, Long> {
    @Query("select new server.api.termterm.dto.term.TermMinimumDto(t.id, t.name, tb.status) " +
            "from Term t " +
            "left join TermBookmark tb " +
            "on t.id = tb.term.id and tb.member = :member " +
            "where t.name like concat('%', :name, '%')")
    Optional<List<TermMinimumDto>> getSearchResults(@Param("member") Member member, @Param("name") String name);

}