package server.api.termterm.service.quiz;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.termterm.domain.term.Term;
import server.api.termterm.dto.quiz.QuizDailyDto;
import server.api.termterm.repository.TermRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuizService {
    private final TermRepository termRepository;

    public List<QuizDailyDto> getDailyQuiz() {
        List<QuizDailyDto> result = new ArrayList<>();
        Random random = new Random();

        for(int i=0; i<5; i++) {
            List<Term> answerSheet = termRepository.findRandomBy();
            List<String> options = new ArrayList<>();
            for(int j=0; j<3; j++) {
                options.add(answerSheet.get(j).getName().split(" :: ")[0]);
            }

            int randN = random.nextInt(3) + 1;
            long answerId = answerSheet.get(randN-1).getId();
            String description = answerSheet.get(randN-1).getDescription();

            QuizDailyDto quiz = QuizDailyDto.builder()
                    .quiz(options)
                    .answerId((long) randN)
                    .termId(answerId).description(description).build();
            result.add(i, quiz);
        }
        return result;
    }
}
