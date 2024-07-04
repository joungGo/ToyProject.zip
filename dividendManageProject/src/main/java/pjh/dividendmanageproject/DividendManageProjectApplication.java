package pjh.dividendmanageproject;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

//@SpringBootApplication
public class DividendManageProjectApplication {

    public static void main(String[] args) {
        //SpringApplication.run(DividendManageProjectApplication.class, args);

        // TODO : "스크래핑" 기능 구현
        try {
            Connection connection
                    = Jsoup.connect("https://finance.yahoo.com/quote/O/history/?frequency=1mo&period1=782487000&period2=1719460987")
                    // 해당 서버에서 Jsoup에 설저된 기본 userAgent를 봇으로 착각해 차단하는 오류 발생 => 아래 코드 추가로 해결
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            Document document = connection.get();

            // key, value는 html 문서에서 내가 원하는 데이터가 어떻게 기술되어 있는지 확인 후 기입
            // https://jsoup.org/apidocs/org/jsoup/nodes/Element.html
            Elements elements // 아래의 조건을 만족하는 데이터가 여러개일 수 있으니 Elements로 받음
                    = document.getElementsByAttributeValue("class", "table svelte-ewueuo");
            Element element = elements.get(0);

            Element tbody = element.children().get(1);
            for (Element e : tbody.children()) {
                String txt = e.text();
                if (!txt.endsWith("Dividend")) {
                    continue;
                }

                String[] splits = txt.split(" ");
                String month = splits[0];
                //int day = Integer.valueOf(splits[1].replace(",", ""));
                int day = Integer.parseInt(splits[1].replace(",", ""));
                int year = Integer.parseInt(splits[2]);
                String dividend = splits[3];

                System.out.println(year + " / " + month + " / " + day + " /" + " -> " + dividend);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
