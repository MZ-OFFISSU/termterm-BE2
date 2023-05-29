package server.api.termterm.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.api.termterm.domain.member.Member;
import server.api.termterm.domain.term.Term;
import server.api.termterm.dto.term.TermDto;
import server.api.termterm.dto.term.TermMinimumDto;
import server.api.termterm.dto.term.TermSimpleDtoInterface;

import java.util.List;
import java.util.Optional;

public interface TermRepository extends JpaRepository<Term, Long> {
    @Query("select new server.api.termterm.dto.term.TermMinimumDto(t.id, t.name, tb.status) " +
            "from Term t " +
            "left join TermBookmark tb " +
            "on t.id = tb.term.id and tb.member = :member " +
            "where t.name like concat('%', :name, '%')")
    Optional<List<TermMinimumDto>> getSearchResults(@Param("member") Member member, @Param("name") String name);

    @Query("select new server.api.termterm.dto.term.TermDto(t.id, t.name, t.description, t.source, tb.status, t) " +
            "from Term t " +
            "left join TermBookmark tb " +
            "on t.id = tb.term.id and tb.member = :member " +
            "where t.id = :id")
    TermDto getTermDetail(@Param("member") Member member, @Param("id") Long id);

    @Query("select t from Term t where t.id IN :ids")
    List<Term> findByIds(@Param("ids") List<Long> ids);

    @Query(nativeQuery = true, value = "select * from Term t order by RAND() limit 3")
    List<Term> findRandomBy();

    @Query(nativeQuery = true,
            value = "select t.term_id as termId, t.name, t.description, tb.status as bookmarked " +
                    "from term t " +
                    "inner join term_category tc " +
                    "on t.term_id = tc.term_id " +
                    "left join term_bookmark tb " +
                    "on tb.member_id = :memberId and tb.term_id = t.term_id " +
                    "where tc.category_id = :categoryId")
    List<TermSimpleDtoInterface> getTermsByCategory(@Param("memberId") Long memberId, @Param("categoryId") Long categoryId);
}

//    @Query(nativeQuery = true,
//            value = "select c.curation_id as curationId, c.title, c.description, c.cnt, cb.status as bookmarked " +
//                    "from curation c " +
//                    "left join curation_bookmark cb " +
//                    "on cb.curation_id = c.curation_id and cb.member_id = :memberId " +
//                    "left join curation_category cc " +
//                    "on c.curation_id = cc.curation_id " +
//                    "where cc.category_id = :categoryId")
//    List<CurationSimpleInfoDtoInterface> getCurationSimpleInfoDtoByCategory(@Param("memberId") Long memberId, @Param("categoryId") Long categoryId);