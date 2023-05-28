package server.api.termterm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.api.termterm.domain.comment.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
