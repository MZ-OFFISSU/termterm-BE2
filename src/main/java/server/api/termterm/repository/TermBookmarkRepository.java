package server.api.termterm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.api.termterm.domain.bookmark.TermBookmark;

public interface TermBookmarkRepository extends JpaRepository<TermBookmark, Long> {
}
