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
import server.api.termterm.response.BizException;
import server.api.termterm.response.ResponseMessage;
import server.api.termterm.response.comment.CommentRegisterRequestDto;
import server.api.termterm.response.comment.CommentResponseType;
import server.api.termterm.response.member.MemberResponseType;
import server.api.termterm.service.comment.CommentService;
import server.api.termterm.service.member.MemberService;
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

    @ApiOperation(value = "나만의 용어 설명 승인", notes = "나만의 용어 설명 승인")
    @ApiResponses({
            @ApiResponse(code = 2064, message = "나만의 용어 설명 승인 완료 (200)"),
    })
    @PutMapping("/comment/accept")
    public ResponseEntity<ResponseMessage<String>> acceptComment(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @Parameter(name = "id", description = "comment id", in = PATH)  @RequestParam(name = "id") Long id
    ){
        Member member = memberService.getMemberByToken(token);
        if (!memberService.checkAdmin(member))
            throw new BizException(MemberResponseType.NO_AUTHORIZATION);

        commentService.acceptComment(id);

        return new ResponseEntity<>(ResponseMessage.create(CommentResponseType.ACCEPT_SUCCESS), CommentResponseType.ACCEPT_SUCCESS.getHttpStatus());
    }
}
