package server.api.termterm.domain.report;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportStatus {
    A("대기 중"),
    C("처리 완료"),
    ;

    private final String name;
}
