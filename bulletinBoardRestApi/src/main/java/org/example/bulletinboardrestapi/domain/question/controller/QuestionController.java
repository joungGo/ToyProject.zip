package org.example.bulletinboardrestapi.domain.question.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bulletinboardrestapi.domain.question.dto.QuestionDto;
import org.example.bulletinboardrestapi.domain.question.dto.QuestionForm;
import org.example.bulletinboardrestapi.domain.question.entity.Question;
import org.example.bulletinboardrestapi.domain.question.service.QuestionService;
import org.example.bulletinboardrestapi.global.dto.RsData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {
    private final QuestionService questionService;

    // TODO : 질문 목록(전체) 조회

    // TODO : 질문(단건) 조회

    // 질문 등록
    @PostMapping
    public RsData<QuestionDto> create(@RequestBody @Valid QuestionForm questionForm) {

        // DB에 질문 등록
        Question question = questionService.create(questionForm);

        return new RsData<>(
                "200-1",
                "질문 등록이 완료되었습니다.",
                new QuestionDto(question)
        );
    }

    // TODO : 질문 수정

    // TODO : 질문 삭제

}
