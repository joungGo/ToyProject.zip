package org.example.bulletinboardrestapi.domain.answer.repository;

import org.example.bulletinboardrestapi.domain.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {
}
