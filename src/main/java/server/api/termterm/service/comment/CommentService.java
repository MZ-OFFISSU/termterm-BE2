package server.api.termterm.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.termterm.domain.comment.Comment;
import server.api.termterm.domain.comment.CommentStatus;
import server.api.termterm.domain.member.Member;
import server.api.termterm.domain.term.Term;
import server.api.termterm.repository.CommentRepository;
import server.api.termterm.response.comment.CommentRegisterRequestDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;

    @Transactional
    public void registerComment(Member member, CommentRegisterRequestDto commentRegisterRequestDto, Term term) {
        Comment comment = Comment.builder()
                .term(term)
                .member(member)
                .content(commentRegisterRequestDto.getContent())
                .source(commentRegisterRequestDto.getSource())
                .status(CommentStatus.WAITING)
                .likeCnt(0)
                .build();

        commentRepository.save(comment);
    }
}
