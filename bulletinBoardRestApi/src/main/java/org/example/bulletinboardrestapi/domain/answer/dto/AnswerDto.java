package org.example.bulletinboardrestapi.domain.answer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.bulletinboardrestapi.domain.answer.entity.Answer;
import org.example.bulletinboardrestapi.domain.question.entity.Question;

import java.time.LocalDateTime;

@Getter
public class AnswerDto {
    private Integer id;
    @JsonProperty("질문")
    private String subject; // 질문의 제목
    @JsonProperty("답변")
    private String content; // 답변의 내용
    @JsonProperty("작성일")
    private LocalDateTime createDate;
    @JsonProperty("수정일")
    private LocalDateTime modifyDate;


    // TODO : 수정 필요 :: 보여질 답변에 관한 정보 필터링
    public AnswerDto(Answer answer) {
        this.id = answer.getId();
        this.subject = answer.getQuestion().getSubject();
        this.content = answer.getContent();
        this.createDate = answer.getCreateDate();
        this.modifyDate = answer.getModifyDate();
    }
}