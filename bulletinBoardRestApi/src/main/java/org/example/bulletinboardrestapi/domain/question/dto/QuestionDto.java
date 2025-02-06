package org.example.bulletinboardrestapi.domain.question.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.bulletinboardrestapi.domain.answer.dto.AnswerDto;
import org.example.bulletinboardrestapi.domain.answer.entity.Answer;
import org.example.bulletinboardrestapi.domain.question.entity.Question;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class QuestionDto {
    private Integer id;
    private String subject;
    private String content;
    @JsonProperty("작성일")
    private LocalDateTime createDate;
    @JsonProperty("수정일")
    private LocalDateTime modifyDate;
    @JsonProperty("답변 목록")
    private List<Answer> answerList;

    public QuestionDto(Question question) {
        this.id = question.getId();
        this.subject = question.getSubject();
        this.content = question.getContent();
        this.createDate = question.getCreateDate();
        this.modifyDate = question.getModifyDate();
        this.answerList = question.getAnswerList();
        // 자세한 내용은 HELP.md 에 여러 해결 방법을 기재!
        /*this.answerList = question.getAnswerList()
                .stream()
                .map(AnswerDto::new)
                .toList();*/
    }
}