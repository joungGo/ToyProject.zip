package org.example.bulletinboardrestapi.domain.question.service;

import lombok.RequiredArgsConstructor;
import org.example.bulletinboardrestapi.domain.question.dto.QuestionForm;
import org.example.bulletinboardrestapi.domain.question.entity.Question;
import org.example.bulletinboardrestapi.domain.question.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Transactional
    public Question create(QuestionForm questionForm) {
        return questionRepository.save(
                Question
                        .builder()
                        .content(questionForm.getContent())
                        .subject(questionForm.getSubject())
                        .build()
        );
    }


}
