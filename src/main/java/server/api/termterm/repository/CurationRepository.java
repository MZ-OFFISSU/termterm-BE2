package server.api.termterm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.api.termterm.domain.curation.Curation;
import server.api.termterm.dto.curation.CurationSimpleInfoDto;

import java.util.Set;

public interface CurationRepository extends JpaRepository<Curation, Long> {

    @Query(value = "select new server.api.termterm.dto.curation.CurationSimpleInfoDto(c.id, c.title, c.description, c.cnt, cb.status) " +
            "from Curation c " +
            "inner join c.categories cc " +
            "on c.id = cc.id " +
            "inner join CurationBookmark cb " +
            "on c.id = cb.id " +
            "where cc.id = :id")
    Set<CurationSimpleInfoDto> getCurationSimpleInfoDtoByCategory(@Param("id") Long id);
}
