package org.example.exercisespringboot.question;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> { // Question 엔티티로 리포지터리를 생성, Integer 는 PK의 타입
    Question findBySubject(String subject); // Question 엔티티의 subject 속성으로 조회하는 메서드 // 단일 결과를 반환하기 때문에 DB에 중복된 대상이 있다면 에러가 발생 -> findByAllSubject 로 변경하면 해결 가능
    Question findBySubjectAndContent(String subject, String content); // subject 와 content 를 조합하여 조회하는 메서드
    List<Question> findBySubjectLike(String subject);  // subject 속성에 대해 like 검색을 수행하는 메서드
}
