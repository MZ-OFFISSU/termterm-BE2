package server.api.termterm.dto.curation;

import lombok.Builder;
import lombok.Getter;
import server.api.termterm.dto.term.TermSimpleDto;

import java.util.List;

@Getter
@Builder
public class CurationDetailDto {
     // 본 큐레이션에 포인트 지불 여부
    private Boolean paid;

     // list of words - only id, name and description
    private List<TermSimpleDto> termSimples;

     //함께 보면 더 좋은 용어 모음집
    private List<CurationSimpleInfoDtoInterface> curationSimpleInfos;

     // 연관태그
    private List<String> tags;

}
