package pjh.dividendmanageproject.scraper;

import pjh.dividendmanageproject.model.Company;
import pjh.dividendmanageproject.model.ScrapedResult;

public class NaverFinanceScraper implements Scraper {

    // TODO : YahooFinanceScrapper와 구현 방법 및 구성이 똑같기 때문에 생략! => 나중에 remind 용도로 해보기!!!!

    @Override
    public ScrapedResult scrap(Company company) {
        return null;
    }

    @Override
    public Company scrapCompanyByTicker(String ticker) {
        return null;
    }
}
