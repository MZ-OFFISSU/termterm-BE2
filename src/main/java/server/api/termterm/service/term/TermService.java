package server.api.termterm.service.term;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.termterm.domain.bookmark.TermBookmark;
import server.api.termterm.domain.category.Category;
import server.api.termterm.domain.comment.Comment;
import server.api.termterm.domain.member.Member;
import server.api.termterm.domain.term.Term;
import server.api.termterm.dto.comment.CommentDto;
import server.api.termterm.dto.term.TermDto;
import server.api.termterm.dto.term.TermMinimumDto;
import server.api.termterm.repository.TermBookmarkRepository;
import server.api.termterm.repository.TermRepository;
import server.api.termterm.response.base.BizException;
import server.api.termterm.response.term.TermResponseType;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TermService {
    private final TermRepository termRepository;
    private final TermBookmarkRepository termBookmarkRepository;

    @Transactional(readOnly = true)
    public Term findById(Long id){
        return termRepository.findById(id)
                .orElseThrow(() -> new BizException(TermResponseType.ID_NO_RESULT));
    }

    @Transactional(readOnly = true)
    public List<TermMinimumDto> searchTerm(Member member, String name) {
        List<TermMinimumDto> termMinimumDtos = termRepository.getSearchResults(member, name)
                .orElseThrow(() -> new BizException(TermResponseType.SEARCH_NO_RESULT));

        if (termMinimumDtos.isEmpty())
            throw new BizException(TermResponseType.SEARCH_NO_RESULT);

        return termMinimumDtos;
    }

    @Transactional
    public void bookmarkTerm(Member member, Long id) {
        Term term = findById(id);

        TermBookmark termBookmark = TermBookmark.builder()
                .member(member)
                .term(term)
                .build();

        termBookmarkRepository.save(termBookmark);
    }

    private List<String> getCategoryString(List<Category> categories){
        List<String> result = new ArrayList<>();

        for(Category category : categories){
            result.add(category.getName());
        }

        return result;
    }

    private List<CommentDto> getCommentDtos(List<Comment> comments){
        List<CommentDto> commentDtos = new ArrayList<>();

        for(Comment comment : comments){
            CommentDto commentDto = CommentDto.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .likeCnt(comment.getLikeCnt())
                    .authorName(comment.getMember().getNickname())
                    .authorJob(comment.getMember().getJob())
                    .authorProfileImageUrl(comment.getMember().getProfileImg())
                    .createdDate(comment.getCreatedDate().toString())
                    .build();

            commentDtos.add(commentDto);
        }

        return commentDtos;
    }


    @Transactional(readOnly = true)
    public TermDto getTermDetail(Member member, Long id) {
        TermDto termDto = termRepository.getTermDetail(member, id);
        Term term = termDto.getTerm();

        List<String> categories = getCategoryString(term.getCategories());
        termDto.setCategories(categories);

        List<CommentDto> commentDtos = getCommentDtos(term.getComments());
        termDto.setComments(commentDtos);

        return termDto;
    }
}
