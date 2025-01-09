package wiseSayingService;

import data.Proverb;
import wiseSayingRepository.LastIdRepositoryInterface;
import wiseSayingRepository.WiseSayingRepositoryInterface;
import wiseSayingRepository.WiseSayingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WiseSayingService {
    private final WiseSayingRepositoryInterface repository;
    private final LastIdRepositoryInterface lastIdRepository;
    private final List<Proverb> proverbList; // 데이터 영속성
    private static final int LIST_PER_PAGE = 5;

    public WiseSayingService(WiseSayingRepositoryInterface repository, LastIdRepositoryInterface lastIdRepository) {
        this.repository = repository;
        this.lastIdRepository = lastIdRepository;
        this.proverbList = repository.loadProverbs(); // 데이터 영속성
    }

    // 테스트용 생성자 (Mock 주입 지원)
    public WiseSayingService(WiseSayingRepository repository, LastIdRepositoryInterface lastIdRepository, List<Proverb> proverbList) {
        this.repository = repository;
        this.lastIdRepository = lastIdRepository;
        this.proverbList = proverbList;
    }

    public List<Proverb> getProverbList() {
        return proverbList;
    }

    public void registerProverb(String proverb, String author) {
        int id = lastIdRepository.readLastId();
        Proverb proverbObject = new Proverb(id, proverb, author);
        proverbList.add(proverbObject);
        repository.saveProverb(proverbObject);
        lastIdRepository.saveLastId(id + 1);
        System.out.println(id + "번 명언이 등록되었습니다.");
    }

    public void listProverbs(int page) {
        int itemsPerPage = LIST_PER_PAGE;
        int totalItems = proverbList.size();
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

        if (page < 1 || page > totalPages) {
            System.out.println("존재하지 않는 페이지입니다.");
            return;
        }

        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------");

        int startIndex = (page - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

        for (int i = startIndex; i < endIndex; i++) {
            System.out.println(proverbList.get(i).toString());
        }

        System.out.print("페이지: [");
        for (int i = 1; i <= totalPages; i++) {
            System.out.print(i == page ? " " + i + " " : " " + i + " ");
        }
        System.out.println("]");
    }

    public void searchProverb(String keywordType, String keyword) {
        List<Proverb> results = new ArrayList<>();

        for (Proverb proverb : proverbList) {
            if (keywordType.equals("proverb") && proverb.getProverb().contains(keyword)) {
                results.add(proverb);
            } else if (keywordType.equals("author") && proverb.getAuthor().contains(keyword)) {
                results.add(proverb);
            }
        }

        if (results.isEmpty()) {
            System.out.println("검색 결과가 없습니다.");
            return;
        }

        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------");
        for (Proverb proverb : results) {
            System.out.println(proverb.toString());
        }
    }

    public void updateProverb(int id, Scanner sc) {
        // proverbList 수정
        for (Proverb proverb : proverbList) {
            if (proverb.getId() == id) {
                System.out.println("명언(기존) : " + proverb.getProverb());
                System.out.println("작가(기존) : " + proverb.getAuthor());

                System.out.print("명언(수정) : ");
                String newProverb = sc.nextLine();
                proverb.setProverb(newProverb);

                System.out.print("작가(수정) : ");
                String newAuthor = sc.nextLine();
                proverb.setAuthor(newAuthor);

                // repository 수정
                repository.saveProverb(proverb);
                return;
            } /*
            첫번재 proverb 객체에서 해당 id를 찾지 못하면 바로 실행되므로 틀린 코드 위치
            else {
                System.out.println("찾으시는 id의 명언이 없습니다.");
                return;
            }*/
        }
        System.out.println("찾으시는 id의 명언이 없습니다.");
        return;
    }

    public void deleteProverb(int id) {
        boolean check = false;

        /*for (Proverb proverb : proverbList) {
            if (proverb.getId() == id) {
                proverbList.remove(proverb);
                repository.deleteProverbFile(id);
                System.out.println(id + "번 명언이 삭제되었습니다.");
                check = true;
                break;
            }
        }*/

        for (int i = 0; i < proverbList.size(); i++) {
            if (proverbList.get(i).getId() == id) {
                proverbList.remove(i);
                repository.deleteProverbFile(id);
                System.out.println(id + "번 명언이 삭제되었습니다.");
                check = true;
                break;
            }
        }


        if (!check || id <= 0) {
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        }
    }

    public void buildDataToFile() {
        repository.saveDataToFile(proverbList);
    }
}