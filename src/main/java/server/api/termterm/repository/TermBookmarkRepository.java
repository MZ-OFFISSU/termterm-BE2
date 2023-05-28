package server.api.termterm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.api.termterm.domain.bookmark.TermBookmark;
import server.api.termterm.domain.member.Member;
import server.api.termterm.domain.term.Term;

public interface TermBookmarkRepository extends JpaRepository<TermBookmark, Long> {
    TermBookmark findByMemberAndTerm(Member member, Term term);
}
