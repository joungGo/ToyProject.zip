package org.example.exercisespringboot.question;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionForm { // 폼 클래스는 입력값 검증할 때뿐만 아니라 입력 항목을 바인딩할 때도 사용한다.
    @NotEmpty(message = "제목은 필수항목입니다.")
    // @NotEmpty : null 이나 empty 문자열이 아닌 경우에만 통과
    @Size(max = 200)
    private String subject;

    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;
}

/*
폼 클래스는 입력값 검증할 때뿐만 아니라 입력 항목을 바인딩할 때도 사용한다.
즉, question_form.html 템플릿의 입력 항목인 subject와 content가 폼 클래스의 subject, content 속성과 바인딩된다.
여기서 바인딩이란 템플릿의 항목과 form 클래스의 속성이 매핑되는 과정을 말한다.
 */