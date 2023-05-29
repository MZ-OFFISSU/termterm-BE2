package server.api.termterm.controller.quiz;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.api.termterm.dto.quiz.QuizDailyDto;
import server.api.termterm.response.base.ApiResponse;
import server.api.termterm.response.quiz.QuizResponseType;
import server.api.termterm.service.quiz.QuizService;

import java.util.List;

@Api(tags = "Quiz")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/quiz/daily-quiz")
    public ApiResponse<QuizDailyDto> getDailyQuiz(){
        List<QuizDailyDto> quizDailyDtoList = quizService.getDailyQuiz();

        return ApiResponse.of(QuizResponseType.QUIZ_DAILY_SUCCESS);
    }
}
