package wiseSayingController;

public interface LastIdRepositoryInterface {
    int readLastId();
    void saveLastId(int id);
}
