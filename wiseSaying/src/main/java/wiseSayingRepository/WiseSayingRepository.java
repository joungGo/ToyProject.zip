package wiseSayingRepository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import data.Proverb;

import java.io.*;
import java.nio.Buffer;
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
                    System.out.println("파일이 생성되었습니다: " + file.getAbsolutePath());
                } else {
                    System.out.println("파일 생성에 실패했습니다.");
                }
                return 1;

            } catch (IOException e) {
                System.out.println("파일 생성 중 오류 발생: " + e.getMessage());
            }
        } else {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                return Integer.parseInt(br.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 1;
    }

    public void saveLastId(int id) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(LAST_ID_FILE));
            bw.write(String.valueOf(id));
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveProverb(Proverb proverbObject) {
        Gson gson = new Gson();
        String json = gson.toJson(proverbObject);

        try {
            FileWriter writer = new FileWriter(DB + proverbObject.getId() + ".json");
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public List<Proverb> loadProverbs() throws FileNotFoundException {
        File folder = new File(DB);
        List<Proverb> proverbs = new ArrayList<>();

        File[] files = folder.listFiles(((dir, name) -> name.endsWith(".json")));
        if (files != null) {
            Gson gson = new Gson();

            for (File file : files) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    Proverb proverb = gson.fromJson(br, Proverb.class);
                    proverbs.add(proverb);
                } catch (JsonSyntaxException e) {
                    System.out.println("JSON 파싱 오류가 발생했습니다: " + file.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return proverbs;
    }

    public void deleteProverbFile(int id) {
        File file = new File(DB + id + ".json");
        if (file.exists()) {
            if (file.delete()) {
                System.out.println(id + ".json 파일이 성공적으로 삭제되었습니다.");
            } else {
                System.out.println(id + ".json 파일 삭제에 실패하였습니다.");
            }
        } else {
            System.out.println(id + ".json 파일이 존재하지 않습니다.");
        }
    }

    public void saveDataToFile(List<Proverb> proverbList) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        // String json = gson.toJson(proverbList);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_PROVERB_FILE))) {
            gson.toJson(proverbList, bw);
            System.out.println("빌드 성공");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
