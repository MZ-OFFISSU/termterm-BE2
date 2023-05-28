package server.api.termterm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.api.termterm.domain.curation.Curation;
import server.api.termterm.dto.curation.CurationSimpleInfoDto;

import java.util.List;

public interface CurationRepository extends JpaRepository<Curation, Long> {

    @Query(nativeQuery = true,
            value = "select c.curation_id as curationId, c.title, c.description, c.cnt, cb.status as bookmarked " +
                    "from curation c " +
                    "left join curation_bookmark cb " +
                    "on cb.curation_id = c.curation_id and cb.member_id = :memberId " +
                    "left join curation_category cc " +
                    "on c.curation_id = cc.curation_id " +
                    "where cc.category_id = :categoryId")
    List<CurationSimpleInfoDto> getCurationSimpleInfoDtoByCategory(@Param("memberId") Long memberId, @Param("categoryId") Long categoryId);
}
