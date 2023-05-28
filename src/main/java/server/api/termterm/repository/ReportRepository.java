package server.api.termterm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.api.termterm.domain.comment.Comment;
import server.api.termterm.domain.member.Member;
import server.api.termterm.domain.report.Report;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findByMemberAndComment(Member member, Comment comment);
}
