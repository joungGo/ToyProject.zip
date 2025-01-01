package wiseSayingRepository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import data.Proverb;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WiseSayingRepository {
    private static final String DB = "src/main/java/db/";
    private static final String LAST_ID_FILE = DB + "lastId.txt";
    private static final String DATA_PROVERB_FILE = DB + "data.json";

    public int readLastId() {
        File file = new File(LAST_ID_FILE);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println(LAST_ID_FILE + "파일이 생성되었습니다.");
                    return 1;
                } else {
                    System.out.println("파일 생성에 실패했습니다.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try (BufferedReader br = new BufferedReader(new FileReader(LAST_ID_FILE))) {
                return Integer.parseInt(br.readLine());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 1;
    }

    // 데이터 영속성 처리 메서드
    public List<Proverb> loadProverbs() {
        List<Proverb> proverbs = new ArrayList<>();
        File folder = new File(DB);

        File[] files = folder.listFiles(((dir, name) -> name.endsWith(".json")));
        if (files != null) {
            Gson gson = new Gson();

            for (File file : files) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    Proverb proverb = gson.fromJson(br, Proverb.class);
                    proverbs.add(proverb);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (JsonSyntaxException e) {
                    System.out.println("JSON 파싱 오류 발생" + file.getName()); // getName() : 파일명(확장자 포함)
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return proverbs;
    }

    public void saveLastId(int id) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LAST_ID_FILE))) {
            bw.write(String.valueOf(id));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveProverb(Proverb proverbObject) {
        Gson gson = new Gson();
        String json = gson.toJson(proverbObject);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DB + proverbObject.getId() + ".json"))) {
            bw.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteProverbFile(int id) {
        File file = new File(DB + id + ".json");
        if (file.exists()) file.delete();
    }

    public void saveProverbsToData(List<Proverb> proverbList) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_PROVERB_FILE))) {
            gson.toJson(proverbList, bw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
