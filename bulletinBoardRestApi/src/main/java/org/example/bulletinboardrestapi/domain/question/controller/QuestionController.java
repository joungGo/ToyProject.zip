package org.example.bulletinboardrestapi.domain.question.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bulletinboardrestapi.domain.question.dto.QuestionDto;
import org.example.bulletinboardrestapi.domain.question.dto.QuestionForm;
import org.example.bulletinboardrestapi.domain.question.entity.Question;
import org.example.bulletinboardrestapi.domain.question.service.QuestionService;
import org.example.bulletinboardrestapi.global.dto.RsData;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {
    private final QuestionService questionService;

    // 질문 목록(전체) 조회
    @GetMapping
    public RsData<List<QuestionDto>> getItems() {
        List<Question> questions = questionService.getAllItems();
        List<QuestionDto> questionDtos = questions
                .stream()
                .map(QuestionDto::new)
                .toList();

        return new RsData<>(
                "200-1",
                "질문 목록(전체) 조회가 완료되었습니다.",
                questionDtos
        );
    }

    // 질문(단건) 조회
    @GetMapping("/{id}")
    public RsData<QuestionDto> getItem(@PathVariable Integer id) {
        //Question question = questionService.getItem(id).orElse(null); // NullPointerException 방지 목적
        Question question = questionService.getItem(id).get(); // NoSuchElementException 방지 목적

        return new RsData<>(
                "200-1",
                "질문 조회가 완료되었습니다.",
                new QuestionDto(question)
        );
    }

    // 질문 등록
    @PostMapping("/create")
    public RsData<QuestionDto> create(@RequestBody @Valid QuestionForm questionForm) {

        // DB에 질문 등록
        Question question = questionService.create(questionForm);

        return new RsData<>(
                "200-1",
                "질문 등록이 완료되었습니다.",
                new QuestionDto(question)
        );
    }

    // 질문 수정
    @PutMapping("/{id}")
    public RsData<Void> modify(@PathVariable Integer id, @RequestBody @Valid QuestionForm questionForm) {
        Question question = questionService.getItem(id).get();
        questionService.modifyItem(question, questionForm.getContent(), questionForm.getSubject());
        return new RsData<>(
                "200-1",
                "%d번 글 질문이 완료되었습니다.".formatted(id),
                null
        );
    }

    // 질문 삭제
    @DeleteMapping("/{id}")
    public RsData<Void> delete(@PathVariable Integer id) {
        Question question = questionService.getItem(id).get();
        questionService.deleteItem(question);

        return new RsData<>(
                "200-1",
                "%d번 질문이 삭제되었습니다.".formatted(id),
                null
        );
    }

}
