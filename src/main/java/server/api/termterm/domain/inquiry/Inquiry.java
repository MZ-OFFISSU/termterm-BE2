package server.api.termterm.domain.inquiry;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.api.termterm.domain.BaseTimeEntity;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Inquiry extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INQUIRY_ID")
    private Long id;

    private String email;

    @Column(length = 1000)
    private String content;

    @Enumerated(EnumType.STRING)
    private InquiryStatus status;

    @Enumerated(EnumType.STRING)
    private InquiryType type;

    @Builder
    public Inquiry(String email, String content, InquiryStatus status, InquiryType type) {
        this.email = email;
        this.content = content;
        this.status = status;
        this.type = type;
    }

    public void setStatus(InquiryStatus status){
        this.status = status;
    }

}
