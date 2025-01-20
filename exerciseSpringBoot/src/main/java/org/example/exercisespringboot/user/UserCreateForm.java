package org.example.exercisespringboot.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 검증을 위한 클래스
public class UserCreateForm {
    @Size(min = 3, max = 25) // @Size 는 문자열의 길이가 최소 길이(min)와 최대 길이(max) 사이에 해당하는지를 검증
    @NotEmpty(message = "사용자ID는 필수항목입니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String password2;

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email // @Email 은 해당 속성의 값이 이메일 형식과 일치하는지를 검증
    private String email;
}
