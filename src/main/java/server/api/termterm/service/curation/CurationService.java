package server.api.termterm.service.curation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.termterm.domain.bookmark.CurationBookmark;
import server.api.termterm.domain.category.Category;
import server.api.termterm.domain.curation.Curation;
import server.api.termterm.domain.curation.Tag;
import server.api.termterm.domain.member.Member;
import server.api.termterm.domain.term.Term;
import server.api.termterm.dto.curation.CurationRegisterRequestDto;
import server.api.termterm.repository.*;
import server.api.termterm.response.BizException;
import server.api.termterm.response.curation.CurationResponseType;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurationService {
    private final CurationRepository curationRepository;
    private final TermRepository termRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final CurationBookmarkRepository curationBookmarkRepository;


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

    public void bookmarkCuration(Member member, Curation curation) {
        CurationBookmark curationBookmark = curationBookmarkRepository.findByCurationAndMember(curation, member);

        if(curationBookmark == null){
            curationBookmarkRepository.save(new CurationBookmark(member, curation));
            return;
        }

        curationBookmark.bookmark();
    }
}
