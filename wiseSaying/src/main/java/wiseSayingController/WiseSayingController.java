package wiseSayingController;

import wiseSayingService.WiseSayingService;

import java.util.Scanner;

public class WiseSayingController {
    private final WiseSayingService service;
    private final Scanner sc;

    public WiseSayingController() {
        this.service = new WiseSayingService();
        this.sc = new Scanner(System.in);
    }

    public void start() {

        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            String command = sc.nextLine();

            if (command.trim().equals("등록")) {
                registerProverb();
            } else if (command.trim().equals("목록")) {
                listProverb();
            } else if (command.trim().startsWith("검색")) {
                searchProverb(command);
            } else if (command.trim().startsWith("수정?id=")) {
                updateProverb(command);
            } else if (command.trim().startsWith("삭제?id=")) {
                //deleteProverb(command);
            } else if (command.trim().equals("빌드")) {
                //buildDataProverb();
            } else if (command.trim().equals("종료")) {
                System.out.println("프로그램이 종료되었습니다.");
                break;
            } else {
                System.out.println("잘못된 명령어 입니다.");
            }
        }
    }

    private void registerProverb() {
        // proverbList 에 등록한 명언 추가 -> service 단
        // 실제로 .json 파일 추가 -> repository 단

        System.out.print("명언 : ");
        String proverb = sc.nextLine();

        System.out.print("작가 : ");
        String author = sc.nextLine();

        service.registerProverb(proverb, author);
    }

    private void listProverb() {
        System.out.print("페이지 번호 입력: ");
        int page = Integer.parseInt(sc.nextLine());
        service.listProverbs(page);
    }

    private void searchProverb(String command) {
        String[] parts = command.split("\\?");

        if (parts.length < 2) {
            System.out.println("잘못된 검색입니다. ");
            return;
        }

        String[] keywords = parts[1].split("&");
        if (keywords.length < 2) {
            System.out.println("검색타입 또는 검색어가 누락되었습니다.");
            return;
        }

        String keywordType = keywords[0].split("=")[1];
        String keyword = keywords[1].split("=")[1];
        service.searchProverb(keywordType, keyword);
    }

    private void updateProverb(String command) {
        int id = Integer.parseInt(command.split("=")[1]);
        service.updateProverb(id, sc);

    }
}
