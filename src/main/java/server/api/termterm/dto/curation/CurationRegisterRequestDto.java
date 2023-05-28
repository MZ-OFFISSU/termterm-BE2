package server.api.termterm.dto.curation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurationRegisterRequestDto {
    private String title;
    private String description;
    private String thumbnail;
    private List<String> tags;
    private List<Long> termIds;
    private List<String> categories;
}
