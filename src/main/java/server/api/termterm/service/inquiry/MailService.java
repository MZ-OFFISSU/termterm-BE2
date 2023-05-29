package server.api.termterm.service.inquiry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import server.api.termterm.dto.inquiry.InquiryRequestDto;
import server.api.termterm.response.BizException;
import server.api.termterm.response.InternalServerExceptionType;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final ResourceLoader resourceLoader;
    private final JavaMailSender mailSender;

    @Async
    public void sendAcceptMail(InquiryRequestDto inquiryRequestDto){
        try {
            Resource resource = resourceLoader.getResource("classpath:/mail/index.html");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // get inputStream object
            InputStream inputStream = resource.getInputStream();
            // convert inputStream into a byte array
            byte[] dataAsBytes = FileCopyUtils.copyToByteArray(inputStream);

            // convert the byte array into a String
            String html = new String(dataAsBytes, StandardCharsets.UTF_8);

            helper.setText(html, true);
            helper.setFrom("termterm.contact@gmail.com");
            helper.setTo(inquiryRequestDto.getEmail());
            helper.setSubject("[텀텀] 고객님의 문의가 정상적으로 접수되었습니다.");

            mailSender.send(mimeMessage);
        }catch(Exception e){
            throw new BizException(InternalServerExceptionType.INTERNAL_SERVER_ERROR);
        }


//        SimpleMailMessage message = new SimpleMailMessage();
//
//        String mailContent = "안녕하세요. termterm입니다.\n" +
//                "\n" +
//                "고객님의 문의가 정상적으로 접수되었습니다.\n" +
//                "\n" +
//                "휴일을 제외한 평일 하루이내에 답변을 드리는 점에 대해 양해 부탁드립니다.\n" +
//                "\n" +
//                "순차적으로 확인 후 최대한 빠른 시일내에 답변해드리도록 하겠습니다.\n" +
//                "\n" +
//                "휴일을 제외한 하루가 지나지 않아도 답변이 오지 않는다면,\n" +
//                "\n" +
//                "스팸 메일함에 답변이 있을 수 있으니 스팸 메일함을 확인해주세요.\n" +
//                "\n" +
//                "궁금하신 점이 있으시면 언제든지 저희 termterm으로 연락주시기 바랍니다.\n" +
//                "\n" +
//                "감사합니다.";
//
//        message.setFrom("termterm.contact@gmail.com");
//        message.setTo(inquiryRequestDto.getEmail());
//        message.setSubject("[텀텀] 고객님의 문의가 정상적으로 접수되었습니다.");
//        message.setText(mailContent);
//        mailSender.send(message);
    }

}
