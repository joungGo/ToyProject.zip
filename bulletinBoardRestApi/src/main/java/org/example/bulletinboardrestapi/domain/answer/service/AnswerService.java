package org.example.bulletinboardrestapi.domain.answer.service;

import lombok.RequiredArgsConstructor;
import org.example.bulletinboardrestapi.domain.answer.entity.Answer;
import org.example.bulletinboardrestapi.domain.answer.repository.AnswerRepository;
import org.example.bulletinboardrestapi.domain.question.entity.Question;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    @Transactional
    public Answer create(Question question, String content) {
        return answerRepository.save(
                Answer
                        .builder()
                        .content(content)
                        .question(question)
                        .build()
        );
    }

    public Optional<Answer> getItem(Integer id) {
        return answerRepository.findById(id);
    }

    @Transactional
    public void modifyItem(Answer answer, String content) {
        answer.setContent(content);
    }

    public void deleteItem(Answer answer) {
        answerRepository.delete(answer);
    }
}
