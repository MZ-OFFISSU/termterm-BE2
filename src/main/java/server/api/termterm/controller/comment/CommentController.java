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
import server.api.termterm.domain.member.Member;
import server.api.termterm.domain.term.Term;
import server.api.termterm.response.ResponseMessage;
import server.api.termterm.response.comment.CommentRegisterRequestDto;
import server.api.termterm.response.comment.CommentResponseType;
import server.api.termterm.service.comment.CommentService;
import server.api.termterm.service.member.MemberService;
import server.api.termterm.service.term.TermService;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

@Api(tags = "Comment")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class CommentController {
    private final MemberService memberService;
    private final CommentService commentService;
    private final TermService termService;

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

}
