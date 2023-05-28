package server.api.termterm.domain.inquiry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InquiryStatus {
    A("대기 중"),
    C("답변 완료");

    private final String name;
}
