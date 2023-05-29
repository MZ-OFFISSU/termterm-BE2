package server.api.termterm.dto.quiz;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizDto {
    private Long id;
    private String name;
}
