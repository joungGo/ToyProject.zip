package org.example.bulletinboardrestapi.domain.answer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bulletinboardrestapi.domain.answer.dto.AnswerDto;
import org.example.bulletinboardrestapi.domain.answer.dto.AnswerForm;
import org.example.bulletinboardrestapi.domain.answer.entity.Answer;
import org.example.bulletinboardrestapi.domain.answer.service.AnswerService;
import org.example.bulletinboardrestapi.domain.question.dto.QuestionDto;
import org.example.bulletinboardrestapi.domain.question.dto.QuestionForm;
import org.example.bulletinboardrestapi.domain.question.entity.Question;
import org.example.bulletinboardrestapi.domain.question.service.QuestionService;
import org.example.bulletinboardrestapi.global.dto.RsData;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {
    private final AnswerService answerService;
    private final QuestionService questionService;

    // 답변(단건) 조회
    @GetMapping("/{id}")
    public RsData<AnswerDto> getItem(@PathVariable Integer id) {
        Answer answer = answerService.getItem(id).get(); // NoSuchElementException 방지 목적

        return new RsData<>(
                "200-1",
                "답변 조회가 완료되었습니다.",
                new AnswerDto(answer)
        );
    }

    // 답변 등록
    @PostMapping("/create/{id}")
    public RsData<AnswerDto> create(@PathVariable Integer id, @RequestBody @Valid AnswerForm answerFrom) {

        Question question = questionService.getItem(id).get();
        Answer answer = answerService.create(question, answerFrom.getContent());

        return new RsData<>(
                "200-1",
                "답변 등록이 완료되었습니다.",
                new AnswerDto(answer)
        );
    }

    // 답변 수정
    @PutMapping("/{id}")
    public RsData<Void> modify(@PathVariable Integer id, @RequestBody @Valid AnswerForm answerForm) {
        Answer answer = answerService.getItem(id).get();
        answerService.modifyItem(answer, answerForm.getContent());
        return new RsData<>(
                "200-1",
                "%d번 글 답변이 완료되었습니다.".formatted(id),
                null
        );
    }

    // 답변 삭제
    @DeleteMapping("/{id}")
    public RsData<Void> delete(@PathVariable Integer id) {
        Answer answer = answerService.getItem(id).get();
        answerService.deleteItem(answer);

        return new RsData<>(
                "200-1",
                "%d번 답변이 삭제되었습니다.".formatted(id),
                null
        );
    }

}
