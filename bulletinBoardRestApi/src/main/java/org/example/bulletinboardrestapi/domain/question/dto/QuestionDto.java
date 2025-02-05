package org.example.bulletinboardrestapi.domain.question.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.bulletinboardrestapi.domain.question.entity.Question;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
public class QuestionDto {
    private Integer id;
    private String subject;
    private String content;
    @JsonProperty("작성일")
    private LocalDateTime createDate;
    @JsonProperty("수정일")
    private LocalDateTime modifyDate;

    public QuestionDto(Question question) {
        this.id = question.getId();
        this.subject = question.getSubject();
        this.content = question.getContent();
        this.createDate = question.getCreateDate();
        this.modifyDate = question.getModifyDate();
    }
}