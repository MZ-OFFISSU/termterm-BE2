package server.api.termterm.dto.inquiry;

import lombok.Getter;

@Getter
public class InquiryRequestDto {
    private String email;
    private String type;
    private String content;

    public InquiryRequestDto trimAll(){
        this.email = email.trim();
        this.content = content.trim();
        this.type = type.trim();

        return this;
    }
}
