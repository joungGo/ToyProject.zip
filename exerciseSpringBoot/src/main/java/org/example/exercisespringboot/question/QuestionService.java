package org.example.exercisespringboot.question;

import lombok.RequiredArgsConstructor;
import org.example.exercisespringboot.exception.DataNotFoundException;
import org.example.exercisespringboot.user.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    // 질문 목록을 가져오는 기능 - 페이징
    public Page<Question> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        /*
        위 두코드에 의해 sorts 리스트에는 '값'이 할당되는 것이 아닌, '정렬 조건'이 추가되는 것이다.
        즉, sorts 리스트에는 '정렬 조건'이 추가되어 있을 뿐이며, 실제로 정렬이 이루어지는 것은 아니다.
        'createDate 속성을 기준으로 내림차순 정렬' 이라는 조건 1개가 추가되었고 이 정렬 조건을 가지고 페이징을 하기 위해 매개변수로 아래의 코드에 사용된다.
         */
        //Pageable pageable = PageRequest.of(page, 10);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.questionRepository.findAll(pageable);
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
    public void create(String subject, String content, SiteUser user) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);
        this.questionRepository.save(q);
    }

    // 질문 수정 기능
    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

    // 질문 삭제 기능
    public void delete(Question question) {
        this.questionRepository.delete(question);
    }
}
