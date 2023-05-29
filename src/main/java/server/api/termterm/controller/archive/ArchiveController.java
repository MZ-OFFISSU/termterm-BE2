package server.api.termterm.controller.archive;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import server.api.termterm.domain.member.Member;
import server.api.termterm.dto.archive.FolderRequestDto;
import server.api.termterm.response.archive.ArchiveResponseType;
import server.api.termterm.response.base.ApiResponse;
import server.api.termterm.service.archive.ArchiveService;
import server.api.termterm.service.curation.CurationService;
import server.api.termterm.service.member.MemberService;


@Api(tags = "Archive")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class ArchiveController {

    private final ArchiveService archiveService;
    private final MemberService memberService;

    @ApiOperation(value = "새로운 폴더 생성", notes = "새로운 폴더 생성")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 2301, message = "새로운 폴더 생성 성공 (200)"),
    })
    @PostMapping("/archive/folder/new")
    public ApiResponse<String> postNewFolder(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody FolderRequestDto folderRequestDto
    ){
        Member member = memberService.getMemberByToken(token);

        // 폴더 이름 중복 검사 로직 필요
        archiveService.postFolder(member, folderRequestDto);

        return ApiResponse.of(ArchiveResponseType.FOLDER_CREATE_SUCCESS);
    }
}
