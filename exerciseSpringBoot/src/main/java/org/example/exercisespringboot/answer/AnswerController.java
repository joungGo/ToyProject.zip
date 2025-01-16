package org.example.exercisespringboot.answer;

import lombok.RequiredArgsConstructor;
import org.example.exercisespringboot.question.Question;
import org.example.exercisespringboot.question.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    @PostMapping("/create/{id}")                                         // 만약 넘어오는게 없다면 비워두었을 때 오류가 날 수 있으니 defaultValue(안넘어오면 이 값으로 대체됨) 를 설정해준다.
    public String createAnswer(Model model, @PathVariable("id") Integer id, @RequestParam(value="content") String content) { // RequestParam : 요청 파라미터를 메서드의 매개변수(question_detail 의 content,이름이 같아야 함)로 받을 때 사용
        Question question = this.questionService.getQuestion(id);
        this.answerService.create(question, content);
        return String.format("redirect:/question/detail/%s", id);
    }
}