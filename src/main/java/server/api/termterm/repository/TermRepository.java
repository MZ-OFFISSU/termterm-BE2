package server.api.termterm.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.api.termterm.domain.member.Member;
import server.api.termterm.domain.term.Term;
import server.api.termterm.dto.term.TermMinimumDto;

import java.util.List;

public interface TermRepository extends JpaRepository<Term, Long> {
    @Query("select new server.api.termterm.dto.term.TermMinimumDto(t.id, t.name, tb.status) " +
            "from Term t " +
            "left join TermBookmark tb " +
            "on t.id = tb.term.id and tb.member = :member " +
            "where t.name like concat('%', :name, '%')")
    List<TermMinimumDto> getSearchResults(@Param("member") Member member, @Param("name") String name);

}

//    @Query(value = "select new server.api.termterm.dto.member.MemberInfoDto(" +
//            "m.id, m.account)" +
//            "from Member m " +
//            "where m.account = :account")
//    Optional<MemberInfoDto> getMemberInfoDtoByAccount(@Param("account") String account);