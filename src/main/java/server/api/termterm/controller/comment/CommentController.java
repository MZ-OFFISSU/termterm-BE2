package server.api.termterm.controller.comment;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.api.termterm.domain.comment.Comment;
import server.api.termterm.domain.member.Member;
import server.api.termterm.domain.term.Term;
import server.api.termterm.dto.report.ReportRequestDto;
import server.api.termterm.response.BizException;
import server.api.termterm.response.ResponseMessage;
import server.api.termterm.dto.comment.CommentRegisterRequestDto;
import server.api.termterm.response.comment.CommentResponseType;
import server.api.termterm.response.member.MemberResponseType;
import server.api.termterm.response.report.ReportResponseType;
import server.api.termterm.service.comment.CommentService;
import server.api.termterm.service.member.MemberService;
import server.api.termterm.service.report.ReportService;
import server.api.termterm.service.term.TermService;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.*;

@Api(tags = "Comment")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class CommentController {
    private final MemberService memberService;
    private final CommentService commentService;
    private final TermService termService;
    private final ReportService reportService;

    @ApiOperation(value = "나만의 용어 설명 등록", notes = "용어 설명 등록")
    @ApiResponses({
            @ApiResponse(code = 2061, message = "나만의 용어 설명 등록 완료 (200)"),
            @ApiResponse(code = 4052, message = "단어가 존재하지 않습니다. (400)"),
    })
    @PostMapping("/comment")
    public ResponseEntity<ResponseMessage<String>> registerComment(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @Parameter(name = "CommentRegisterRequestDto", description = "Long: termId, String: content(내용), String: source(출처)") @RequestBody CommentRegisterRequestDto commentRegisterRequestDto
    ){
        Member member = memberService.getMemberByToken(token);
        Term term = termService.findById(commentRegisterRequestDto.getTermId());
        commentService.registerComment(member, commentRegisterRequestDto, term);

        return new ResponseEntity<>(ResponseMessage.create(CommentResponseType.POST_SUCCESS), CommentResponseType.POST_SUCCESS.getHttpStatus());
    }

    @ApiOperation(value = "좋아요", notes = "나만의 용어 설명 좋아요")
    @ApiResponses({
            @ApiResponse(code = 2062, message = "용어 설명 좋아요 성공 (200)"),
            @ApiResponse(code = 4061, message = "해당하는 나만의 용어 설명이 존재하지 않습니다. (400)"),
            @ApiResponse(code = 4062, message = "사용자가 용어 설명에 이미 좋아요를 눌렀습니다. (400)"),
    })
    @PutMapping("/comment/like/{id}")
    public ResponseEntity<ResponseMessage<String>> likeComment(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @Parameter(name = "id", description = "comment id", in = PATH)  @RequestParam(name = "id") Long id
    ) {
        Member member = memberService.getMemberByToken(token);
        commentService.like(member, id);

        return new ResponseEntity<>(ResponseMessage.create(CommentResponseType.LIKE_SUCCESS), CommentResponseType.LIKE_SUCCESS.getHttpStatus());
    }

    @ApiOperation(value = "좋아요 취소", notes = "나만의 용어 설명 좋아요 취소")
    @ApiResponses({
            @ApiResponse(code = 2063, message = "용어 설명 좋아요 취소 성공 (200)"),
            @ApiResponse(code = 4061, message = "해당하는 나만의 용어 설명이 존재하지 않습니다. (400)"),
            @ApiResponse(code = 4063, message = "사용자가 용어 설명에 좋아요를 누르지 않았습니다. (400)"),
    })
    @PutMapping("/comment/dislike")
    public ResponseEntity<ResponseMessage<String>> dislikeComment(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @Parameter(name = "id", description = "comment id", in = PATH)  @RequestParam(name = "id") Long id
    ) {
        Member member = memberService.getMemberByToken(token);
        commentService.dislike(member, id);

        return new ResponseEntity<>(ResponseMessage.create(CommentResponseType.DISLIKE_SUCCESS), CommentResponseType.DISLIKE_SUCCESS.getHttpStatus());
    }

    @ApiOperation(value = "나만의 용어 설명 승인", notes = "나만의 용어 설명 승인\n관리자 권한일 때만")
    @ApiResponses({
            @ApiResponse(code = 2064, message = "나만의 용어 설명 승인 완료 (200)"),
    })
    @PutMapping("/comment/accept/{id}")
    public ResponseEntity<ResponseMessage<String>> acceptComment(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @Parameter(name = "id", description = "comment id", in = PATH)  @PathVariable(name = "id") Long id
    ){
        Member member = memberService.getMemberByToken(token);
        if (!memberService.checkAdmin(member))
            throw new BizException(MemberResponseType.NO_AUTHORIZATION);

        commentService.acceptComment(id);

        return new ResponseEntity<>(ResponseMessage.create(CommentResponseType.ACCEPT_SUCCESS), CommentResponseType.ACCEPT_SUCCESS.getHttpStatus());
    }

    @ApiOperation(value = "신고하기", notes = "나만의 용어설명 신고하기\n" +
            " 신고 유형 : \n" +
            "    COPYRIGHT(\"COPYRIGHT\", \"저작권 침해, 명예훼손\"),\n" +
            "    PERSONAL_INFORMATION(\"PERSONAL_INFORMATION\", \"개인정보 유출\"),\n" +
            "    ADVERTISEMENT(\"ADVERTISEMENT\", \"광고 및 홍보성 내용\"),\n" +
            "    IRRELEVANT_CONTENT(\"IRRELEVANT_CONTENT\", \"용어와 무관한 내용\"),\n" +
            "    FRAUD(\"FRAUD\", \"사기 또는 거짓 정보\"),\n" +
            "    INCORRECT_CONTENT(\"INCORRECT_CONTENT\", \"잘못된 정보 포함\"),\n" +
            "    DISGUST(\"DISGUST\", \"혐오 발언 또는 상징\"),\n" +
            "    ABUSE(\"ABUSE\", \"욕설, 비방, 선정성 등 미풍양속을 해치는 내용\"),\n" +
            "    SPAM(\"SPAM\", \"스팸\"),\n" +
            "    OTHER(\"OTEHR\", \"기타\"),")
    @ApiResponses({
            @ApiResponse(code = 2071, message = "신고 접수 완료 (200)"),
            @ApiResponse(code = 4071, message = "신고 유형이 존재하지 않습니다. (400)"),
    })
    @PostMapping("/comment/report")
    public ResponseEntity<ResponseMessage<String>> reportComment(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @Parameter(name = "ReportRequestDto", description = "Long: Comment Id / String: type(신고유형) / String: content(신고내용)") @RequestBody ReportRequestDto reportRequestDto
    ){
        Member member = memberService.getMemberByToken(token);
        Comment comment = commentService.findById(reportRequestDto.getCommentId());

        reportService.registerReport(member, comment, reportRequestDto);

        return new ResponseEntity<>(ResponseMessage.create(ReportResponseType.REPORT_WAITING), ReportResponseType.REPORT_WAITING.getHttpStatus());
    }

    @ApiOperation(value = "신고 처리 완료 (관리자)", notes = "관리자가 신고 내용 처리 완료")
    @ApiResponses({
            @ApiResponse(code = 2073, message = "신고 처리 완료 (200")
    })
    @PutMapping("/comment/report/completed/{id}")
    public ResponseEntity<ResponseMessage<String>> completeComment(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @PathVariable("id") Long id
    ){
        Member member = memberService.getMemberByToken(token);
        if (!memberService.checkAdmin(member))
            throw new BizException(MemberResponseType.NO_AUTHORIZATION);

        Comment comment = commentService.findById(id);

        reportService.completeReport(member, comment);

        return new ResponseEntity<>(ResponseMessage.create(ReportResponseType.REPORT_COMPLETED), ReportResponseType.REPORT_COMPLETED.getHttpStatus());
    }
}
