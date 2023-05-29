package server.api.termterm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.api.termterm.domain.bookmark.BookmarkStatus;
import server.api.termterm.domain.curation.Curation;
import server.api.termterm.domain.member.Member;
import server.api.termterm.dto.curation.CurationSimpleInfoDto;
import server.api.termterm.dto.curation.CurationSimpleInfoDtoInterface;

import java.util.List;
import java.util.Set;

public interface CurationRepository extends JpaRepository<Curation, Long> {

    @Query(nativeQuery = true,
            value = "select c.curation_id as curationId, c.title, c.description, c.cnt, cb.status as bookmarked " +
                    "from curation c " +
                    "left join curation_bookmark cb " +
                    "on cb.curation_id = c.curation_id and cb.member_id = :memberId " +
                    "left join curation_category cc " +
                    "on c.curation_id = cc.curation_id " +
                    "where cc.category_id = :categoryId")
    List<CurationSimpleInfoDtoInterface> getCurationSimpleInfoDtoByCategory(@Param("memberId") Long memberId, @Param("categoryId") Long categoryId);

    @Query("select new server.api.termterm.dto.curation.CurationSimpleInfoDto(c.id, c.title, c.description, c.cnt, cb.status) " +
            "from Curation c " +
            "inner join CurationBookmark cb " +
            "on c.id = cb.curation.id " +
            "where cb.member = :member and cb.status = :status")
    Set<CurationSimpleInfoDto> getCurationSimpleInfoDtoByMemberBookmarked(@Param("member") Member member, @Param("status") BookmarkStatus status);
}
