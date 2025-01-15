package org.example.exercisespringboot;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Question {
    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 자동 증가 // strategy 옵션을 생략한다면 @GeneratedValue 애너테이션이 지정된 모든 속성에 번호를 차례로 생성하므로 순서가 일정한 고유 번호를 가질 수 없게 된다.
    private Integer id;

    @Column(length = 200) //  테이블의 열 이름과 일치하는데 열의 세부 설정 // 열의 길이를 200 = VARCHAR(200)
    private String subject;

    @Column(columnDefinition = "TEXT") // 열 데이터의 유형이나 성격을 정의 // TEXT 타입으로 지정
    private String content;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;
    /*
    질문에서 답변을 참조하려면 question.getAnswerList()를 호출
    @OneToMany 애너테이션에 사용된 mappedBy는 참조 엔티티의 속성명을 정의한다.
    즉, Answer 엔티티에서 Question 엔티티를 참조한 속성인 question 을 mappedBy에 전달해야 한다.
     */
}