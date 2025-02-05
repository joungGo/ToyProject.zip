package org.example.bulletinboardrestapi.domain.question.service;

import lombok.RequiredArgsConstructor;
import org.example.bulletinboardrestapi.domain.question.dto.QuestionForm;
import org.example.bulletinboardrestapi.domain.question.entity.Question;
import org.example.bulletinboardrestapi.domain.question.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public List<Question> getAllItems() {
        return questionRepository.findAll();
    }

    public Optional<Question> getItem(Integer id) {
        return questionRepository.findById(id);
    }

    @Transactional
    public void modifyItem(Question question, String content, String subject) {
        question.setContent(content);
        question.setSubject(subject);
    }

    public void deleteItem(Question question) {
        questionRepository.delete(question);
    }
}
