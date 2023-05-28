package server.api.termterm.controller.term;


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
import server.api.termterm.dto.term.TermMinimumDto;
import server.api.termterm.response.ResponseMessage;
import server.api.termterm.response.term.TermResponseType;
import server.api.termterm.service.member.MemberService;
import server.api.termterm.service.term.TermService;

import java.util.List;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

@Api(tags = "Term")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class TermController {
    private final MemberService memberService;
    private final TermService termService;

    @ApiOperation(value = "용어 검색", notes = "용어 검색")
    @ApiResponses({
            @ApiResponse(code = 2051, message = "용어 검색 성공 (200)"),
            @ApiResponse(code = 4051, message = "검색어에 대한 검색 결과가 없습니다. (400)"),
    })
    @GetMapping("/term/search/{name}")
    public ResponseEntity<ResponseMessage<List<TermMinimumDto>>> searchTerm(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @PathVariable(value = "name") String name
    ){
        Member member = memberService.getMemberByToken(token);
        List<TermMinimumDto> termMinimumDtos = termService.searchTerm(member, name);

        return new ResponseEntity<>(ResponseMessage.create(TermResponseType.SEARCH_SUCCESS, termMinimumDtos), TermResponseType.SEARCH_SUCCESS.getHttpStatus());
    }

    @ApiOperation(value = "용어 북마크", notes = "용어 북마크")
    @GetMapping("/term/bookmark/{id}")
    public ResponseEntity<ResponseMessage<String>> bookmarkTerm(
            @Parameter(name = "Authorization", description = "Bearer {access-token}", in = HEADER, required = true) @RequestHeader(name = "Authorization") String token,
            @PathVariable(value = "id") Long id
    ){
        Member member = memberService.getMemberByToken(token);
        termService.bookmarkTerm(member, id);

        return new ResponseEntity<>(ResponseMessage.create(TermResponseType.BOOKMARK_SUCCESS), TermResponseType.BOOKMARK_SUCCESS.getHttpStatus());
    }

}
