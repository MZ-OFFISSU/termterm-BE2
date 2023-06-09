package server.api.termterm.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.termterm.domain.comment.Comment;
import server.api.termterm.domain.comment.CommentLike;
import server.api.termterm.domain.comment.CommentLikeStatus;
import server.api.termterm.domain.comment.CommentStatus;
import server.api.termterm.domain.member.Member;
import server.api.termterm.domain.term.Term;
import server.api.termterm.repository.CommentLikeRepository;
import server.api.termterm.repository.CommentRepository;
import server.api.termterm.response.base.BizException;
import server.api.termterm.dto.comment.CommentRegisterRequestDto;
import server.api.termterm.response.comment.CommentResponseType;

@Service
@RequiredArgsConstructor
@Slf4j
@DynamicInsert
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public Comment findById(Long id){
        return commentRepository.findById(id)
                .orElseThrow(() -> new BizException(CommentResponseType.NO_RESULT));
    }

    @Transactional
    public CommentLike findByMemberAndComment(Member member, Comment comment){
        return commentLikeRepository.findByMemberAndComment(member, comment);
    }

    @Transactional
    public void registerComment(Member member, CommentRegisterRequestDto commentRegisterRequestDto, Term term) {
        commentRepository.save(Comment.builder()
                .term(term)
                .member(member)
                .content(commentRegisterRequestDto.getContent())
                .source(commentRegisterRequestDto.getSource())
                .status(CommentStatus.WAITING)
                .build());
    }

    @Transactional
    public void saveNewCommentLike(Member member, Comment comment){
        commentLikeRepository.save(new CommentLike(member, comment, CommentLikeStatus.YES));
        comment.addLike();
    }

    @Transactional
    public void like(Member member, Long id) {
        Comment comment = findById(id);
        CommentLike commentLike = findByMemberAndComment(member, comment);

        // 사용자가 해당 커멘트에 좋아요를 누른 이력이 없을 때
        if(commentLike == null){
            saveNewCommentLike(member, comment);
            return;
        }

        // 사용자가 해당 커멘트에 좋아요를 누른 상태일 때
        if(commentLike.getStatus() == CommentLikeStatus.YES){
            throw new BizException(CommentResponseType.DID_LIKED);
        }

        // 사용자가 해당 커멘트에 좋아요를 누르고 좋아요 취소를 한 이력이 있을 때
        commentLike.like();
        comment.addLike();
    }

    @Transactional
    public void dislike(Member member, Long id) {
        Comment comment = findById(id);
        CommentLike commentLike = findByMemberAndComment(member, comment);

        // 사용자가 해당 커멘트에 좋아요를 누른 이력이 없거나 좋아요 취소 상태일 때
        if(commentLike == null || commentLike.getStatus() == CommentLikeStatus.NO){
            throw new BizException(CommentResponseType.DID_NOT_LIKED);
        }

        commentLike.dislike();
        comment.subLike();
    }

    @Transactional
    public void acceptComment(Long id) {
        this.findById(id).accept();
    }
}
