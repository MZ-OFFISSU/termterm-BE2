package server.api.termterm.dto.curation;

public interface CurationSimpleInfoDto {
    Long getCurationId();
    String getTitle();
    String getDescription();
    Integer getCnt();
    String getBookmarked();

}
