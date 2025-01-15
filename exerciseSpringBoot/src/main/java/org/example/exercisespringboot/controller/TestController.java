package org.example.exercisespringboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @GetMapping("/test") // URL 과 매핑된 메서드는 반환값이 무조건 있어야 한다.
    @ResponseBody // 해당 메서드의 반환 값을 문자열로 리턴하라는 의미로 쓰임(이 어노테이션이 없다면 index 라는 이름의 템프릿 파일으 찾게 된다.)
    public String test() {
        return "index";
    }
}
