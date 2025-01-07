package wiseSayingController;

import data.Proverb;

import java.util.List;

public interface WiseSayingRepositoryInterface {
    int readLastId();

    void saveLastId(int id);

    void saveProverb(Proverb proverb);

    List<Proverb> loadProverbs();

    void deleteProverbFile(int id);

    void saveDataToFile(List<Proverb> proverbList);

}
