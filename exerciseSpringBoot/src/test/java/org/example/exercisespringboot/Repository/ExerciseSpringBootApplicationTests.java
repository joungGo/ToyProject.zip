package org.example.exercisespringboot.Repository;

import org.example.exercisespringboot.Answer;
import org.example.exercisespringboot.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest // ExerciseSpringBootApplicationTests 클래스가 스프링부트 테스트 클래스임을 명시
class ExerciseSpringBootApplicationTests {

    @Autowired
    // 질문 엔티티의 데이터를 생성할 때 리포지터리를 사용하기 위해 `의존성 주입(DI)`을 사용 // DI : 스프링이 객체를 대신 생성하여 주입해주 는 것
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void testJpa1() {
        /*Question q1 = new Question();
        q1.setSubject("sbb가 무엇인가요?");
        q1.setContent("sbb에 대해서 알고 싶습니다.");
        q1.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q1);  // 첫번째 질문 저장*/

        List<Question> all = this.questionRepository.findAll();
        assertEquals(2, all.size());

        Question q1 = all.get(0); // get(index) : 행단위 탐색
        assertEquals("sbb가 무엇인가요?", q1.getSubject());

        Optional<Question> oq = this.questionRepository.findById(1);
        if (oq.isPresent()) { // isPresent() : 값이 존재하는지 확인
            Question q2 = oq.get();
            assertEquals("sbb가 무엇인가요?", q2.getSubject());
        }

        Question q2 = this.questionRepository.findBySubject("sbb가 무엇인가요?");
        assertEquals(1, q2.getId());
    }

    @Test
    void testJpa2() {
        Question q = this.questionRepository.findBySubjectAndContent(
                "sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
        assertEquals(1, q.getId());
    }

    @Test
    void testJpa3() {
        List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
        Question q = qList.get(0);
        assertEquals("sbb가 무엇인가요?", q.getSubject());
    }

    @Test
    void testJpa4() {
        List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
        Question q = qList.get(0);
        assertEquals("sbb가 무엇인가요?", q.getSubject());
    }

    @Test
    void testJap5() {
        Optional<Question> oq = this.questionRepository.findById(2);
        assertTrue(oq.isPresent());
        Question q = oq.get();

        Answer a = new Answer();
        a.setContent("네 자동으로 생성됩니다.");
        a.setQuestion(q);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
        a.setCreateDate(LocalDateTime.now());
        this.answerRepository.save(a);
    }

    @Transactional
    @Test
    void testJpa6() {
        Optional<Question> oq = this.questionRepository.findById(2);
        assertTrue(oq.isPresent());
        Question q = oq.get();

        List<Answer> answerList = q.getAnswerList();

        assertEquals(1, answerList.size());
        assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());

        /*
        에러 :
        org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role: org.example.exercisespringboot.Question.answerList: could not
        initialize proxy - no Session

        이 에러는 테스트 코드에서만 발생한다. QuestionRepository 가 findById 메서드를 통해 Question 객체를 조회하고 나면 DB 세션이 끊어지기 때문이다.
        왜냐하면 QuestionRepository 가 findById 메서드를 통해 Question 객체를 조회하고 나면 DB 세션이 끊어지기 때문이다.
        DB 세션이란 스프링 부트 애플리케이션과 데이터베이스 간의 연결을 뜻한다.
         */
    }
}