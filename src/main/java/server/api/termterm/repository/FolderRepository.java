package server.api.termterm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.api.termterm.domain.folder.Folder;

public interface FolderRepository extends JpaRepository<Folder, Long> {

}
