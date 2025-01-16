package org.example.exercisespringboot.question;

import lombok.RequiredArgsConstructor;
import org.example.exercisespringboot.exception.DataNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public List<Question> getList() {
        return this.questionRepository.findAll();
    }

    // 질문 상세 정보를 가져오는 기능
    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id); // 기능 : id로 해당하는 Question 객체 불러오기
        if (question.isPresent()) {
            return question.get(); // get() : 값(Question 객체)이 존재하는 경우에만 값을 반환
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    // 질문을 저장하는 기능
    public void create(String subject, String content) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q);
    }
}
