package org.example.exercisespringboot.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor // final 또는 @NotNull 이 붙은 필드를 생성자로 만들어줌
/*
public QuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }
 */
@Controller
public class QuestionController {

    private final QuestionRepository questionRepository;

    @GetMapping("/question/list")
    //@ResponseBody // 템플릿 엔진을 사용하기 때문에 더이상 이 어노테이션은 필요X
    public String list(Model model) { // Model 객체는 자바 클래스(Java class)와 템플릿(template) 간의 연결 고리 역할, Model 객체에 값을 담아 두면 템플릿에서 그 값을 사용할 수 있다.
                                      // 키-값 쌍으로 데이터를 저장 :: key 는 템플릿에서 사용되는 변수 이름이고, "value"는 템플릿에서 렌더링될 데이터
        List<Question> questionList = this.questionRepository.findAll();
        model.addAttribute("questionList", questionList);
        return "question_list";
    }
}
