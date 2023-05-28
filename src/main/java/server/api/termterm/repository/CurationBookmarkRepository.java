package server.api.termterm.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import server.api.termterm.domain.bookmark.CurationBookmark;
import server.api.termterm.domain.curation.Curation;
import server.api.termterm.domain.member.Member;

public interface CurationBookmarkRepository extends JpaRepository<CurationBookmark, Long> {
    CurationBookmark findByCurationAndMember(Curation curation, Member member);
}
