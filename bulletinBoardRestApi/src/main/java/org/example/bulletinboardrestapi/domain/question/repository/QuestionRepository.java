package org.example.bulletinboardrestapi.domain.question.repository;

import org.example.bulletinboardrestapi.domain.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

}
