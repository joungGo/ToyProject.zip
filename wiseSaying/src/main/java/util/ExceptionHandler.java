package util;

import java.io.IOException;

public class ExceptionHandler {
    public static void handleIOException(IOException e, String message) {
        System.out.println(message + ": " + e.getMessage());
        e.printStackTrace();
    }

    public static void handleJsonSyntaxException(Exception e, String fileName) {
        System.out.println("JSON 파싱 오류 발생: " + fileName);
        e.printStackTrace();
    }

    public static void handleGeneralException(Exception e, String message) {
        System.out.println(message + ": " + e.getMessage());
        e.printStackTrace();
    }
}
