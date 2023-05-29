package server.api.termterm.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import server.api.termterm.dto.inquiry.InquiryRequestDto;
import server.api.termterm.response.base.ApiResponse;
import server.api.termterm.service.inquiry.MailService;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class MailAspect {
    private final MailService mailService;

    @AfterReturning(
            pointcut = "execution(* server.api.termterm.controller.inquiry.InquiryController.registerInquiry(server.api.termterm.dto.inquiry.InquiryRequestDto))",
            returning = "returned"
    )
    public void afterReturnRegisterInquiry(ResponseEntity<ApiResponse<InquiryRequestDto>> returned){
        InquiryRequestDto inquiryRequestDto = returned.getBody().getData();
        mailService.sendAcceptMail(inquiryRequestDto);
    }
}
