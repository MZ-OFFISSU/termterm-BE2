package server.api.termterm.domain.inquiry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InquiryType {
    USE("USER", "이용 문의"),
    AUTH("AUTH", "로그인/회원가입 문의"),
    REPORT("REPORT", "서비스 불편/오류 제보"),
    SUGGESTION("SUGGESTION", "서비스 제안"),
    OTHER("OTHER", "기타 문의");

    private final String name;
    private final String description;

    public static InquiryType getInquiryType(String type){
        return InquiryType.valueOf(type);
    }
}
