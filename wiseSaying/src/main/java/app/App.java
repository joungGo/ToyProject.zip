package app;

import wiseSayingController.WiseSayingController;

public class App {
    private final WiseSayingController controller;

    // 생성자
    public App() { this.controller = new WiseSayingController(); }

    // 메서드
    public void run() { controller.start(); }
}
