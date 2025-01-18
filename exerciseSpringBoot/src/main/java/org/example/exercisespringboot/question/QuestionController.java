package org.example.exercisespringboot.question;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.exercisespringboot.answer.AnswerForm;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor // final 또는 @NotNull 이 붙은 필드를 생성자로 만들어줌
/*
public QuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }
 */
@Controller
@RequestMapping("/question")
// @Restcontroller : @Controller + @ResponseBody
public class QuestionController {

    //private final QuestionRepository questionRepository;

    // TODO : QuestionController 가 QuestionService 를 통해 QuestionRepository 를 사용하도록 수정
    private final QuestionService questionService;

    // 질문 목록을 보여주는 페이지로 이동
    @GetMapping("/list")
    //@ResponseBody // 템플릿 엔진을 사용하기 때문에 더이상 이 어노테이션은 필요X
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) { // Model 객체는 자바 클래스(Java class)와 템플릿(template) 간의 연결 고리 역할, Model 객체에 값을 담아 두면 템플릿에서 그 값을 사용할 수 있다.
                                      // 키-값 쌍으로 데이터를 저장 :: key 는 템플릿에서 사용되는 변수 이름이고, "value"는 템플릿에서 렌더링될 데이터
        //List<Question> questionList = this.questionRepository.findAll();
        Page<Question> paging = this.questionService.getList(page);

        // model.addAttribute("questionList", questionList);
        model.addAttribute("paging", paging);
        return "question_list";
    }

    // 질문 상세 페이지로 이동
    @GetMapping(value = "/detail/{id}") // URL 경로에 있는 변수를 가져와서 사용 가능
    public String detail(Model model, @PathVariable("id") Integer id,
                         @ModelAttribute("answerForm") AnswerForm answerForm) { // @PathVariable : URL 경로의 일부를 메서드 매개변수로 바인딩할 때 사용
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question); // id에 맞게 가져온 Question 객체를 모델에 담아서 템플릿으로 전달
        return "question_detail"; // view(템플릿) 를 반환
    }

    // 질문 등록 페이지로 이동
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) { // 매개변수로 바인딩 된 것은 Model 에 전달하지 않아도 사용이 가능하다.
        return "question_form";
    }

    // 질문을 등록하는 기능
    @PostMapping("/create")
    /*public String questionCreate(@RequestParam(value="subject") String subject, @RequestParam(value="content") String content) {
        this.questionService.create(subject, content);
        return "redirect:/question/list"; // 질문 저장후 질문목록으로 이동
    }*/

    // @Valid : 폼 클래스에 설정한 검증 규칙을 적용
    public String questionCreate(@Valid @ModelAttribute("questionForm") QuestionForm questionForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        this.questionService.create(questionForm.getSubject(), questionForm.getContent());
        return "redirect:/question/list";
    }
    /*
    subject, content 항목을 지닌 폼이 전송되면 QuestionForm 의 subject, content 속성이 자동으로 바인딩된다.
    @Valid 애너테이션을 적용하면 QuestionForm 의 @NotEmpty, @Size 등으로 설정한 검증 기능이 동작한다.
    그리고 이어지는 BindingResult 매개변수는 @Valid 애너테이션으로 검증이 수행된 결과를 의미하는 객체
     */

}
