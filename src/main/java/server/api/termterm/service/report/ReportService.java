package server.api.termterm.service.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.termterm.domain.comment.Comment;
import server.api.termterm.domain.member.Member;
import server.api.termterm.domain.report.Report;
import server.api.termterm.domain.report.ReportStatus;
import server.api.termterm.domain.report.ReportType;
import server.api.termterm.dto.report.ReportRequestDto;
import server.api.termterm.repository.ReportRepository;
import server.api.termterm.response.base.BizException;
import server.api.termterm.response.report.ReportResponseType;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private final ReportRepository reportRepository;

    private ReportType getReportType(String type){
        try {
            return ReportType.getReportType(type);
        }catch (IllegalArgumentException e){
            throw new BizException(ReportResponseType.INVALID_REPORT_TYPE);
        }
    }

    @Transactional
    public void registerReport(Member member, Comment comment, ReportRequestDto reportRequestDto) {
        reportRepository.save(Report.builder()
                .type(getReportType(reportRequestDto.getType()))
                .content(reportRequestDto.getContent())
                .status(ReportStatus.WAITING)
                .member(member)
                .comment(comment)
                .build());
    }

    @Transactional
    public void completeReport(Member member, Comment comment) {
        Report report = reportRepository.findByMemberAndComment(member, comment)
                .orElseThrow(() -> new BizException(ReportResponseType.DOES_NOT_EXIST));

        report.completeReport();
    }
}
