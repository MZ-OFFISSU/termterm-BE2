package server.api.termterm.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import server.api.termterm.domain.comment.Comment;
import server.api.termterm.domain.comment.CommentLike;
import server.api.termterm.domain.member.Member;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    CommentLike findByMemberAndComment(Member member, Comment comment);
}
