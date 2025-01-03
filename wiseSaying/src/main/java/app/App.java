package app;

import wiseSayingController.WiseSayingController;

public class App {
    private final WiseSayingController controller;
    public App() {
        this.controller = new WiseSayingController();
    }

    public void run() {
        controller.start();
    }
}
