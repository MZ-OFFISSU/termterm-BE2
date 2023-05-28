package server.api.termterm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.api.termterm.domain.curation.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByNameIgnoreCase(String name);
}
