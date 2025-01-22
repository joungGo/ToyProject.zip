package org.example.exercisespringboot.question;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.exercisespringboot.answer.Answer;
import org.example.exercisespringboot.user.SiteUser;

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

    @Column(length = 200)
    //  테이블의 열 이름과 일치하는데 열의 세부 설정 // 열의 길이를 200 = VARCHAR(200)
    private String subject;

    @Column(columnDefinition = "TEXT") // 열 데이터의 유형이나 성격을 정의 // TEXT 타입으로 지정
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    // mappedBy 속성은 연관 관계의 주인을 지정하는데 사용 - 부모에만 선언해주면 된다.
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE/*, orphanRemoval = true*/)
    private List<Answer> answerList; // JPA 는 엔티티 간의 관계를 정의하고 관리할 때, 연관된 엔티티의 데이터는 참조 관계로 처리되며, 실제로 해당 필드(answerList)는 테이블에 컬럼으로 직접 생성되지 않습니다.
    /*
    질문에서 답변을 참조하려면 question.getAnswerList()를 호출
    @OneToMany 애너테이션에 사용된 mappedBy는 참조 엔티티의 속성명을 정의한다.
    즉, Answer 엔티티에서 Question 엔티티를 참조한 속성인 question 을 mappedBy에 전달해야 한다.

    [CascadeType.REMOVE 가 없을 때]
    Question 삭제 시, 연관된 Answer 를 먼저 삭제하지 않으면 외래 키 제약 조건 위반 에러가 발생합니다.
    따라서, Answer 를 명시적으로 삭제한 후에 Question 을 삭제해야 합니다.

    [CascadeType.REMOVE 가 있을 때]
    Question 삭제 시, 연관된 Answer 엔티티가 자동으로 삭제됩니다.
    추가적인 코드 작성 없이 간단히 삭제 처리가 가능합니다.
     */

    @ManyToOne
    private SiteUser author; // 질문을 작성한 사용자를 참조하는 속성
}