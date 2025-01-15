package org.example.exercisespringboot.answer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.exercisespringboot.question.Question;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @ManyToOne // @ManyToOne 은 부모(Question) 자식(Answer) 관계를 갖는 구조에서 사용
    private Question question;
    /*
    답변을 통해 질문의 제목을 알고 싶다면 answer.getQuestion().getSubject()를 사용해 접근
     */
}