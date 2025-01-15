package org.example.exercisespringboot.Repository;

import org.example.exercisespringboot.Answer;
import org.example.exercisespringboot.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> { // Question 엔티티로 리포지터리를 생성, Integer 는 PK의 타입


}
