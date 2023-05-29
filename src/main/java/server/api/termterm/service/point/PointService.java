package server.api.termterm.service.point;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.termterm.domain.curation.Curation;
import server.api.termterm.domain.curation.CurationPaid;
import server.api.termterm.domain.member.Member;
import server.api.termterm.repository.CurationPaidRepository;
import server.api.termterm.response.base.BizException;
import server.api.termterm.response.point.PointResponseType;

@Service
@Slf4j
@RequiredArgsConstructor
public class PointService {
    private final CurationPaidRepository curationPaidRepository;
    private static final Integer CURATION_POINT = 50;

    private boolean isAffordCuration(Member member){
        return member.getPoint() >= CURATION_POINT;
    }
    @Transactional
    public void payForCuration(Member member, Curation curation) {
        if (!isAffordCuration(member))
            throw new BizException(PointResponseType.NOT_ENOUGH_POINT);

        CurationPaid curationPaid = CurationPaid.builder()
                .member(member)
                .curation(curation)
                .build();

        curationPaidRepository.save(curationPaid);
        member.subPoint(CURATION_POINT);

        log.info("큐레이션 구매 - curationId : {}, memberId : {}", curation.getId(), member.getId());
    }
}
