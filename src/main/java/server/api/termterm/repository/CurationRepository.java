package server.api.termterm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.api.termterm.domain.curation.Curation;

public interface CurationRepository extends JpaRepository<Curation, Long> {
}
