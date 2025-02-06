package org.example.bulletinboardrestapi.domain.answer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.example.bulletinboardrestapi.domain.question.entity.Question;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT") // 이걸 설정하지 않으면 기본값인 VARCHAR(255)로 설정된다.
    private String content;

    @CreatedDate
    private LocalDateTime createDate; // 이 컬럼의 기본 데이터 타입은 TIMESTAMP

    @LastModifiedDate
    private LocalDateTime modifyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    //@JsonIgnoreProperties("answerList")
    private Question question;
}
