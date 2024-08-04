package pjh.dividendmanageproject.scraper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import pjh.dividendmanageproject.model.Company;
import pjh.dividendmanageproject.model.Dividend;
import pjh.dividendmanageproject.model.ScrapedResult;
import pjh.dividendmanageproject.model.constants.Month;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class YahooFinanceScrapper implements Scraper {

    // finance url
    private static final String STATICS_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";
    // company name url
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";

    private static final long START_TIME = 86400; // 60 * 60 * 24

    /*
     * 왜 static으로 선언?
     * url을 사용하는 객체가 복수개 생성된 경우 Heap 영역에 복수개만큼 메모리가 할당된다.
     * 즉, Heap 영역에 url에 복수개만큼 차지한다는 것이다.
     * 하지만, url을 static으로 선언하면 Static Area 공간에 별도로 url이 저장되고 Heap 영역의 객체(인스턴스)들이 이를 참조하는
     * 방식으로 메모리가 할당(사용)되기 때문에 더 효율적이다.
     * 하지만, static 변수는 모든 변수들이 접근할 수 있기 때문에 주의하자! => 이를 방지하기 위해 final을 사용함.
     * */

    @Override
    public ScrapedResult scrap(Company company) {

        var scrapResult = new ScrapedResult();
        scrapResult.setCompany(company);

        // TODO : "스크래핑" 기능 구현
        try {
            long now = System.currentTimeMillis() / 1000; // 초단위

            String url = String.format(STATICS_URL, company.getTicker(), START_TIME, now);

            Connection connection
                    = Jsoup.connect(url)
                    .timeout(50000)
                    // 해당 서버에서 Jsoup에 설저된 기본 userAgent를 봇으로 착각해 차단하는 오류 발생 => 아래 코드 추가로 해결
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            Document document = connection.get();

            // key, value는 html 문서에서 내가 원하는 데이터가 어떻게 기술되어 있는지 확인 후 기입
            // https://jsoup.org/apidocs/org/jsoup/nodes/Element.html
            Elements parsingDivs // 아래의 조건을 만족하는 데이터가 여러개일 수 있으니 Elements로 받음
                    = document.getElementsByAttributeValue("data-testid", "history-table");

            Element tableElement = parsingDivs.get(0); // 위 조건을 만족하는 데이터 중 첫번째 요소만 가져옴
            //System.out.println("tableElement = " + tableElement);

            //System.out.println("-----------------------------------------------");

            Element thirdDivTable = tableElement.children().get(2); // 3번째 요소

            Element tbody = thirdDivTable.select("table tbody").first();
            // .first() : select가 Elements를 반환하기 때문에 데이터타입을 Element로 하려면 특정 요소를 정해줘야 한다.

            //System.out.println("tbody = " + tbody);

            List<Dividend> dividends = new ArrayList<>();

            for (Element e : tbody.children()) {
                String txt = e.text();
                if (!txt.endsWith("Dividend")) {
                    continue;
                }

                String[] splits = txt.split(" ");
                int month = Month.strToNumber(splits[0]);
                //int day = Integer.valueOf(splits[1].replace(",", ""));
                int day = Integer.parseInt(splits[1].replace(",", ""));
                int year = Integer.parseInt(splits[2]);
                String dividend = splits[3];

                if (month < 0) {
                    throw new RuntimeException("Unexpected Month enum value -> " + splits[0]);
                }

                dividends.add(Dividend.builder()
                        .date(LocalDateTime.of(year, month, day, 1, 0))
                        .dividend(dividend)
                        .build());

            }
            scrapResult.setDividends(dividends);

        } catch (IOException e) {
            // TODO : 스크래핑 과정 중 에러 발생시 출력할 코드 구현 하기
            e.printStackTrace();
        }

        return scrapResult;
    }

    @Override
    public Company scrapCompanyByTicker(String ticker) {
        String url = String.format(SUMMARY_URL, ticker, ticker);


        try {
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .get();
            Element titleElement = document.getElementsByTag("h1").get(1);
            //Element titleElement = document.select("main.layoutContainer.svelte-r172vo h1").first();

            System.out.println("titleElement = " + titleElement);
            String title = titleElement.text().split("\\(")[0];
            System.out.println("title = " + title);

            return Company.builder()
                    .ticker(ticker)
                    .name(title)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
