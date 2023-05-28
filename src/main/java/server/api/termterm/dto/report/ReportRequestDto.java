package server.api.termterm.dto.report;

import lombok.Getter;

@Getter
public class ReportRequestDto {
    private Long commentId;
    private String type;
    private String content;
}
