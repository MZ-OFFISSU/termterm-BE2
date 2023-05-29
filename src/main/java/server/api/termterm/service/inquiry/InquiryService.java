package server.api.termterm.service.inquiry;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.termterm.domain.inquiry.Inquiry;
import server.api.termterm.domain.inquiry.InquiryStatus;
import server.api.termterm.domain.inquiry.InquiryType;
import server.api.termterm.dto.inquiry.InquiryRequestDto;
import server.api.termterm.repository.InquiryRepository;
import server.api.termterm.response.BizException;
import server.api.termterm.response.inquiry.InquiryResponseType;

@Service
@RequiredArgsConstructor
@Slf4j
public class InquiryService {
    private final InquiryRepository inquiryRepository;

    @Transactional
    public Inquiry findById(Long id){
        return inquiryRepository.findById(id)
                .orElseThrow(() -> new BizException(InquiryResponseType.INVALID_INQUIRY_ID));
    }

    private InquiryType getInquiryTypeByName(String name){
        try{
            return InquiryType.getInquiryType(name);
        }catch (IllegalArgumentException e){
            throw new BizException(InquiryResponseType.INVALID_INQUIRY_TYPE);
        }
    }
    @Transactional
    public void registerInquiry(InquiryRequestDto inquiryRequestDto) {
        Inquiry inquiry = Inquiry.builder()
                .email(inquiryRequestDto.getEmail())
                .content(inquiryRequestDto.getContent())
                .status(InquiryStatus.WAITING)
                .type(getInquiryTypeByName(inquiryRequestDto.getType()))
                .build();

        inquiryRepository.save(inquiry);
    }

    @Transactional
    public void completeInquiry(Long id) {
        findById(id).complete();
    }
}
