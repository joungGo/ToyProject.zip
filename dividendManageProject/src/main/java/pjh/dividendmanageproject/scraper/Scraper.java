package pjh.dividendmanageproject.scraper;

import pjh.dividendmanageproject.model.Company;
import pjh.dividendmanageproject.model.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);

    ScrapedResult scrap(Company company);
}
