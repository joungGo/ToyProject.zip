package org.example.bulletinboardrestapi.global.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter // 스프링에서 API 응답으로 RsData 객체를 반환할 때, Jackson 라이브러리 등이 해당 객체를 JSON 으로 변환하는 과정에서 getter 메서드를 사용
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RsData<T> {
    private String code;
    private String msg;
    private T data;

    public RsData(String code, String msg) {
        this(code, msg, null);
    }

    @JsonIgnore
    public int getStatusCode() {
        String statusCodeStr = code.split("-")[0];
        return Integer.parseInt(statusCodeStr);
    }

}
