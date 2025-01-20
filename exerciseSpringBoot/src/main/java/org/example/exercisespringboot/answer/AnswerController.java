package org.example.exercisespringboot.answer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.exercisespringboot.question.Question;
import org.example.exercisespringboot.question.QuestionService;
import org.example.exercisespringboot.user.SiteUser;
import org.example.exercisespringboot.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    // 답변을 등록하는 기능
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")                                         // 만약 넘어오는게 없다면 비워두었을 때 오류가 날 수 있으니 defaultValue(안넘어오면 이 값으로 대체됨) 를 설정해준다.
    /*public String createAnswer(Model model, @PathVariable("id") Integer id, @RequestParam(value="content") String content) { // RequestParam : 요청 파라미터를 메서드의 매개변수(question_detail 의 content,이름이 같아야 함)로 받을 때 사용
        Question question = this.questionService.getQuestion(id);
        this.answerService.create(question, content);
        return String.format("redirect:/question/detail/%s", id);
    }*/

    public String createAnswer(Model model,
                               @PathVariable("id") Integer id,
                               @Valid AnswerForm answerForm,
                               BindingResult bindingResult, // BindingResult : 유효성 검사 결과를 담는 객체, @Valid 로 검사하고 결과를 BindingResult 에 담아줌
                               Principal principal) { // Principal : 현재 사용자 정보를 담고 있는 객체 (spring security 에서 제공)
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName()); // 현재 로그인-사용자 정보를 가져옴
        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "question_detail";
        }
        this.answerService.create(question, answerForm.getContent(), siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }

    // 답변 수정 - 조회
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(@ModelAttribute("answerForm") AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        answerForm.setContent(answer.getContent());
        return "answer_form";
    }

    // 답변 수정 - 저장
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult,
                               @PathVariable("id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "answer_form";
        }
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.answerService.modify(answer, answerForm.getContent());
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }

    // 답변 삭제 - 조회
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.answerService.delete(answer);
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId() );
    }

    // 답변 삭제 - 저장

}