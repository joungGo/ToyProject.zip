package wiseSayingController;

import data.Proverb;

import java.util.List;

public interface WiseSayingRepositoryInterface {
    /*
    이 둘의 메서드는 Proverb 데이터가 아닌 File 데이터를 다루는 메서드이므로
    별도의 인터페이스로 분리한다.

    int readLastId();

    void saveLastId(int id);
    */

    void saveProverb(Proverb proverb);

    List<Proverb> loadProverbs();

    void deleteProverbFile(int id);

    void saveDataToFile(List<Proverb> proverbList);

}
