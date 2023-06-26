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
import server.api.termterm.domain.comment.CommentLike;
import server.api.termterm.domain.comment.CommentLikeStatus;
import server.api.termterm.domain.member.Member;
import server.api.termterm.domain.term.DailyTerm;
import server.api.termterm.domain.term.Term;
import server.api.termterm.dto.comment.CommentDto;
import server.api.termterm.dto.dailyTerm.DailyTermAndStatusDto;
import server.api.termterm.dto.term.TermDto;
import server.api.termterm.dto.term.TermMinimumDto;
import server.api.termterm.dto.term.TermSimpleDto;
import server.api.termterm.dto.term.TermSimpleDtoInterface;
import server.api.termterm.repository.CommentLikeRepository;
import server.api.termterm.repository.DailyTermRepository;
import server.api.termterm.repository.TermBookmarkRepository;
import server.api.termterm.repository.TermRepository;
import server.api.termterm.response.base.BizException;
import server.api.termterm.response.term.TermResponseType;

import java.io.*;
import java.sql.Clob;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TermService {
    private final TermRepository termRepository;
    private final TermBookmarkRepository termBookmarkRepository;
    private final DailyTermRepository dailyTermRepository;
    private final CommentLikeRepository commentLikeRepository;

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
        TermBookmark termBookmark = termBookmarkRepository.findByMemberAndTerm(member, findById(id));

        if(termBookmark != null){
            termBookmark.bookmark();
            return;
        }

        // 존재하지 않으면 새로 만들고 저장
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

    /**
     * term 디테일 API에 담길 Comment 정보 구성하기
     */
    private List<CommentDto> getCommentDtos(List<Comment> comments, Member member){
        List<CommentDto> commentDtos = new ArrayList<>();

        for(Comment comment : comments){

            CommentLike commentLike = commentLikeRepository.findByMemberAndComment(member, comment);
            CommentLikeStatus liked = commentLike == null ? CommentLikeStatus.NO : commentLike.getStatus();

            commentDtos.add(CommentDto.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .likeCnt(comment.getLikeCnt())
                    .authorName(comment.getMember().getNickname())
                    .authorJob(comment.getMember().getJob())
                    .authorProfileImageUrl(comment.getMember().getProfileImg())
                    .source(comment.getSource())
                    .liked(liked)
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
        termDto.setComments(getCommentDtos(term.getComments(), member));

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


    private List<Long> getCategoryIds(List<Category> categories){
        List<Long> categoryIds = new ArrayList<>();
        for(Category category : categories){
            categoryIds.add(category.getId());
        }

        return categoryIds;
    }

    private List<TermSimpleDto> saveDailyTerms(List<Term> terms, Member member){
        List<TermSimpleDto> ret = new ArrayList<>();

        for(Term term : terms){
            dailyTermRepository.save(DailyTerm.builder().member(member).term(term).build());
            BookmarkStatus status = getBookmarkStatusByMemberAndTerm(member, term);

            ret.add(TermSimpleDto.builder()
                            .id(term.getId())
                            .name(term.getName())
                            .bookmarked(status)
                            .build());
        }

        return ret;
    }

    @Transactional
    public BookmarkStatus getBookmarkStatusByMemberAndTerm(Member member, Term term){
        TermBookmark termBookmark = termBookmarkRepository.findByMemberAndTerm(member, term);
        return (termBookmark == null) ? BookmarkStatus.NO : termBookmark.getStatus();
    }


    @Transactional
    public List<TermSimpleDto> updateDailyTerms(List<DailyTermAndStatusDto> dailyTerms, List<Term> newTerms, Member member){
        List<TermSimpleDto> ret = new ArrayList<>();
        Iterator<Term> termsIterator = newTerms.iterator();

        for(DailyTermAndStatusDto dailyTermAndStatus : dailyTerms){
            DailyTerm dailyTerm = dailyTermAndStatus.getDailyTerm();

            Term newTerm = termsIterator.next();
            dailyTerm.updateTerm(newTerm);

            BookmarkStatus status = getBookmarkStatusByMemberAndTerm(member, newTerm);

            ret.add(TermSimpleDto.builder()
                            .id(newTerm.getId())
                            .name(newTerm.getName())
                            .description(newTerm.getDescription())
                            .bookmarked(status)
                            .build());
        }

        return ret;
    }

    private List<Term> getNewDailyTerms4(Member member){
        List<Long> categoryIds = getCategoryIds(member.getCategories());

        List<Term> dailyTerms4 = new ArrayList<>();
        for(TermSimpleDtoInterface t : termRepository.getTermsByCategoriesOnly4(member.getId(), categoryIds)){
            dailyTerms4.add(termRepository.findById(t.getTermId())
                    .orElseThrow(()-> new BizException(TermResponseType.ID_NO_RESULT)));
        }

        return dailyTerms4;

    }

    @Transactional
    public List<TermSimpleDto> refreshDailyTerms(Member member) {
        List<Term> newDailyTerms4 = getNewDailyTerms4(member);

        List<DailyTermAndStatusDto> dailyTerms = dailyTermRepository.getByMemberWithStatus(member);

        if(dailyTerms.isEmpty()){
            return saveDailyTerms(newDailyTerms4, member);
        }else{
            return updateDailyTerms(dailyTerms, newDailyTerms4, member);
        }

    }


    private Boolean isLatest(DailyTerm dailyTerm){
        LocalDate lastRefreshedDate = dailyTerm.getLastRefreshedDate();
        LocalDate today = LocalDate.now();

        return ChronoUnit.DAYS.between(lastRefreshedDate, today) == 0;
    }

    @Transactional
    public List<TermSimpleDto> getDailyTerms(Member member) {
        List<TermSimpleDto> res = new ArrayList<>();
        for(DailyTermAndStatusDto dailyTermAndStatus :  dailyTermRepository.getByMemberWithStatus(member)){
            DailyTerm dailyTerm = dailyTermAndStatus.getDailyTerm();
            Term term = dailyTerm.getTerm();

            // 오늘 날짜가 아닐 경우 갱신 후 응답
            if(!isLatest(dailyTerm)){
                return refreshDailyTerms(member);
            }

            res.add(TermSimpleDto.builder()
                    .id(term.getId())
                    .name(term.getName())
                    .description(term.getDescription())
                    .bookmarked(dailyTermAndStatus.getStatus())
                    .build());
        }

        return res;
    }
}
