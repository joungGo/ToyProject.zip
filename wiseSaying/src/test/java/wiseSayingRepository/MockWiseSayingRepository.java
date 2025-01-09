package wiseSayingRepository;

import data.Proverb;

import java.util.ArrayList;
import java.util.List;

public class MockWiseSayingRepository implements WiseSayingRepositoryInterface, LastIdRepositoryInterface {
    private final List<Proverb> mockData = new ArrayList<>();
    private int lastId = 1;

    @Override
    public int readLastId() {
        return lastId;
    }

    @Override
    public void saveLastId(int id) {
        this.lastId = id;
    }

    @Override
    public void saveProverb(Proverb proverb) {
        mockData.add(proverb);
    }

    @Override
    public List<Proverb> loadProverbs() {
        return new ArrayList<>(mockData); // 복사본 반환
    }

    @Override
    public void deleteProverbFile(int id) {
        mockData.removeIf(proverb -> proverb.getId() == id);
    }

    @Override
    public void saveDataToFile(List<Proverb> proverbList) {
        mockData.clear();
        mockData.addAll(proverbList);
    }
}
