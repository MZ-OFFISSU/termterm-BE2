package server.api.termterm.domain.report;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportType {
    COPYRIGHT("COPYRIGHT", "저작권 침해, 명예훼손"),
    PERSONAL_INFORMATION("PERSONAL_INFORMATION", "개인정보 유출"),
    ADVERTISEMENT("ADVERTISEMENT", "광고 및 홍보성 내용"),
    IRRELEVANT_CONTENT("IRRELEVANT_CONTENT", "용어와 무관한 내용"),
    FRAUD("FRAUD", "사기 또는 거짓 정보"),
    INCORRECT_CONTENT("INCORRECT_CONTENT", "잘못된 정보 포함"),
    DISGUST("DISGUST", "혐오 발언 또는 상징"),
    ABUSE("ABUSE", "욕설, 비방, 선정성 등 미풍양속을 해치는 내용"),
    SPAM("SPAM", "스팸"),
    OTHER("OTEHR", "기타"),
    ;

    private final String name;
    private final String description;

    public static ReportType getReportType(String type){return ReportType.valueOf(type);}
}
