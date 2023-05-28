package server.api.termterm.service.curation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.termterm.domain.bookmark.BookmarkStatus;
import server.api.termterm.domain.bookmark.CurationBookmark;
import server.api.termterm.domain.bookmark.TermBookmark;
import server.api.termterm.domain.category.Category;
import server.api.termterm.domain.curation.Curation;
import server.api.termterm.domain.curation.Tag;
import server.api.termterm.domain.member.Member;
import server.api.termterm.domain.term.Term;
import server.api.termterm.dto.curation.CurationDetailDto;
import server.api.termterm.dto.curation.CurationRegisterRequestDto;
import server.api.termterm.dto.curation.CurationSimpleInfoDto;
import server.api.termterm.dto.term.TermSimpleDto;
import server.api.termterm.repository.*;
import server.api.termterm.response.BizException;
import server.api.termterm.response.curation.CurationResponseType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurationService {
    private final CurationRepository curationRepository;
    private final TermRepository termRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final CurationBookmarkRepository curationBookmarkRepository;
    private final TermBookmarkRepository termBookmarkRepository;


    @Transactional(readOnly = true)
    public Curation findById(Long curationId){
        return curationRepository.findById(curationId)
                .orElseThrow(() -> new BizException(CurationResponseType.CURATION_DOESNT_EXIST));
    }

    private List<Tag> getTags(List<String> tagNames){
        List<Tag> tags = new ArrayList<>();

        for(String tagName : tagNames){
            Tag tag = tagRepository.findByNameIgnoreCase(tagName);
            tags.add((tag == null) ? saveTag(tagName) : tag);
        }

        return tags;
    }

    private Tag saveTag(String tagName){
        return tagRepository.save(new Tag(tagName));
    }

    @Transactional
    public void registerCuration(CurationRegisterRequestDto curationRegisterRequestDto) {
        List<Tag> tags = getTags(curationRegisterRequestDto.getTags());
        List<Term> terms = termRepository.findByIds(curationRegisterRequestDto.getTermIds());
        List<Category> categories = categoryRepository.findByNames(curationRegisterRequestDto.getCategories());

        Curation curation = Curation.builder()
                .title(curationRegisterRequestDto.getTitle())
                .description(curationRegisterRequestDto.getDescription())
                .thumbnail(curationRegisterRequestDto.getThumbnail())
                .tags(tags)
                .terms(terms)
                .categories(categories)
                .build();

        curation.synchronizeCnt();
        curationRepository.save(curation);
    }

    @Transactional
    public void bookmarkCuration(Member member, Curation curation) {
        CurationBookmark curationBookmark = curationBookmarkRepository.findByCurationAndMember(curation, member);

        if(curationBookmark == null){
            curationBookmarkRepository.save(new CurationBookmark(member, curation));
            return;
        }

        curationBookmark.bookmark();
    }

    @Transactional
    public void unbookmarkCuration(Member member, Curation curation) {
        CurationBookmark curationBookmark = curationBookmarkRepository.findByCurationAndMember(curation, member);

        if(curationBookmark == null){
            throw new BizException(CurationResponseType.CURATION_NOT_BOOKMARKED);
        }

        curationBookmark.unbookmark();
    }

    private List<TermSimpleDto> getTermSimpleDtos(Member member, List<Term> terms){
        List<TermSimpleDto> termSimpleDtos = new ArrayList<>();
        for(Term term : terms){
            TermBookmark termBookmark = termBookmarkRepository.findByMemberAndTerm(member, term);

            TermSimpleDto termSimpleDto = TermSimpleDto.builder()
                    .id(term.getId())
                    .name(term.getName())
                    .description(term.getDescription())
                    .bookmarked((termBookmark == null) ? BookmarkStatus.NO : termBookmark.getStatus())
                    .build();

            termSimpleDtos.add(termSimpleDto);
        }

        return termSimpleDtos;
    }

    private Category getRandomCategory(List<Category> categories){
        Random randomGenerator = new Random();
        return categories.get(randomGenerator.nextInt(categories.size()));
    }

    // 함께 보면 더 좋은 용어 모음집
    private Set<CurationSimpleInfoDto> getCurationSimpleInfoDtos(Curation curation){
        Category category = getRandomCategory(curation.getCategories());

        return curationRepository.getCurationSimpleInfoDtoByCategory(category.getId());
    }

    private List<String> getTagStrings(List<Tag> tags){
        List<String> tagStrings = new ArrayList<>();

        for(Tag tag : tags){
            tagStrings.add(tag.getName());
        }

        return tagStrings;
    }

    @Transactional(readOnly = true)
    public CurationDetailDto getCurationDetail(Member member, Curation curation, Boolean memberPaid) {
        List<TermSimpleDto> termSimpleDtos = getTermSimpleDtos(member, curation.getTerms());
        Set<CurationSimpleInfoDto> curationSimpleInfoDtos = getCurationSimpleInfoDtos(curation);
        List<String> tagStrings = getTagStrings(curation.getTags());

        return CurationDetailDto.builder()
                .paid(memberPaid)
                .curationSimpleInfos(curationSimpleInfoDtos)
                .termSimples(termSimpleDtos)
                .tags(tagStrings)
                .build();
    }
}
