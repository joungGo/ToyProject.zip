package org.example.bulletinboardrestapi.domain.question.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class QuestionForm {
    @NotBlank @Length(min = 3, max = 100)
    private String subject;
    @NotBlank @Length(min = 3)
    private String content;
}
