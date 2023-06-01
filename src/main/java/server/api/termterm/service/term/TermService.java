package server.api.termterm.service.term;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.termterm.domain.bookmark.BookmarkStatus;
import server.api.termterm.domain.bookmark.TermBookmark;
import server.api.termterm.domain.category.Category;
import server.api.termterm.domain.comment.Comment;
import server.api.termterm.domain.member.Member;
import server.api.termterm.domain.term.Term;
import server.api.termterm.dto.comment.CommentDto;
import server.api.termterm.dto.term.TermDto;
import server.api.termterm.dto.term.TermMinimumDto;
import server.api.termterm.dto.term.TermSimpleDto;
import server.api.termterm.dto.term.TermSimpleDtoInterface;
import server.api.termterm.repository.TermBookmarkRepository;
import server.api.termterm.repository.TermRepository;
import server.api.termterm.response.base.BizException;
import server.api.termterm.response.term.TermResponseType;

import java.io.*;
import java.sql.Clob;
import java.util.*;
import java.util.stream.Collectors;

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
        termBookmarkRepository.save(TermBookmark.builder()
                .member(member)
                .term(findById(id))
                .build());
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
            commentDtos.add(CommentDto.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .likeCnt(comment.getLikeCnt())
                    .authorName(comment.getMember().getNickname())
                    .authorJob(comment.getMember().getJob())
                    .authorProfileImageUrl(comment.getMember().getProfileImg())
                    .createdDate(comment.getCreatedDate().toString())
                    .build());
        }

        return commentDtos;
    }


    @Transactional(readOnly = true)
    public TermDto getTermDetail(Member member, Long id) {
        TermDto termDto = termRepository.getTermDetail(member, id);
        Term term = termDto.getTerm();

        termDto.setCategories(getCategoryString(term.getCategories()));
        termDto.setComments(getCommentDtos(term.getComments()));

        return termDto;
    }

    @Transactional(readOnly = true)
    public Page<TermSimpleDto> getRecommendedTerms(Member member, Pageable pageable) {
        List<TermSimpleDto> lists = new ArrayList<>();

        for (Category category : member.getCategories()){
            lists.addAll(convertCollectionTermSimpleDto(termRepository.getTermsByCategory(member.getId(), category.getId())));
        }

        return getPageImplByList(new ArrayList<>(lists.stream().distinct().collect(Collectors.toList())), pageable);
    }

    @Transactional(readOnly = true)
    public Page<TermSimpleDto> getTermListByCategory(Member member, Category category, Pageable pageable) {
        return getPageImplByList(convertCollectionTermSimpleDto(termRepository.getTermsByCategory(member.getId(), category.getId())), pageable);
    }

    private List<TermSimpleDto> convertCollectionTermSimpleDto(List<TermSimpleDtoInterface> list){
        List<TermSimpleDto> l = new ArrayList<>();

        for(TermSimpleDtoInterface t : list){
            l.add(TermSimpleDto.builder()
                    .id(t.getTermId())
                    .name(t.getName())
                    .description(clobToString(t.getDescription()))
                    .bookmarked((t.getBookmarked() == null) ? BookmarkStatus.NO : BookmarkStatus.valueOf(t.getBookmarked()))
                    .build());
        }

        return l;
    }

    private PageImpl<TermSimpleDto> getPageImplByList(List<TermSimpleDto> lists, Pageable pageable){
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), lists.size());

        return new PageImpl<>(lists.subList(start, end), pageable, lists.size());
    }

    private String clobToString(Clob clob) {
        try {
            StringBuilder s = new StringBuilder();
            BufferedReader br = new BufferedReader(clob.getCharacterStream());
            String ts;
            while ((ts = br.readLine()) != null) {
                s.append(ts).append("\n");
            }
            br.close();
            return s.toString().replace("\n", "");
        }catch (Exception e){
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<TermSimpleDto> getDailyTerms(Member member) {
        List<Long> categoryIds = new ArrayList<>();
        for (Category category : member.getCategories()) {
            categoryIds.add(category.getId());
        }

        return convertCollectionTermSimpleDto(termRepository.getTermsByCategoriesOnly4(member.getId(), categoryIds));
    }
}
