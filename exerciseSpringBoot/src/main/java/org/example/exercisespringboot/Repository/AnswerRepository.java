package org.example.exercisespringboot.Repository;

import org.example.exercisespringboot.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

}
