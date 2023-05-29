package server.api.termterm.service.archive;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.termterm.domain.folder.Folder;
import server.api.termterm.domain.member.Member;
import server.api.termterm.dto.archive.FolderRequestDto;
import server.api.termterm.repository.FolderRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ArchiveService {
    private final FolderRepository folderRepository;

    public void postFolder(Member member, FolderRequestDto folderRequestDto) {
        // 폴더 만들 수 있는 상태인지 확인 로직 필요

        Folder folder = Folder.builder()
                .name(folderRequestDto.getName())
                .description(folderRequestDto.getDescription())
                .member(member)
                .build();

        folderRepository.save(folder);
    }
}
