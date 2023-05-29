package server.api.termterm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.api.termterm.domain.curation.Curation;
import server.api.termterm.domain.curation.CurationPaid;
import server.api.termterm.domain.member.Member;

import java.util.Optional;

public interface CurationPaidRepository extends JpaRepository<CurationPaid, Long> {
    Optional<CurationPaid> findByMemberAndCuration(Member member, Curation curation);
}
