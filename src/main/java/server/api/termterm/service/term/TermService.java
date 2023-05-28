package server.api.termterm.service.term;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.api.termterm.domain.bookmark.TermBookmark;
import server.api.termterm.domain.member.Member;
import server.api.termterm.domain.term.Term;
import server.api.termterm.dto.term.TermMinimumDto;
import server.api.termterm.repository.TermBookmarkRepository;
import server.api.termterm.repository.TermRepository;
import server.api.termterm.response.BizException;
import server.api.termterm.response.term.TermResponseType;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TermService {
    private final TermRepository termRepository;
    private final TermBookmarkRepository termBookmarkRepository;

    private Term findById(Long id){
        return termRepository.findById(id)
                .orElseThrow(() -> new BizException(TermResponseType.ID_NO_RESULT));
    }

    public List<TermMinimumDto> searchTerm(Member member, String name) {
        return termRepository.getSearchResults(member, name);
    }

    public void bookmarkTerm(Member member, Long id) {
        Term term = findById(id);

        TermBookmark termBookmark = TermBookmark.builder()
                .member(member)
                .term(term)
                .build();

        termBookmarkRepository.save(termBookmark);
    }
}
