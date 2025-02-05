package org.example.bulletinboardrestapi.domain.answer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class AnswerForm {

    @NotBlank @Length(min = 3)
    private String content;
}
