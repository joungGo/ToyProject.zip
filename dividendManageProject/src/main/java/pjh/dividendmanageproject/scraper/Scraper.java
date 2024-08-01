package pjh.dividendmanageproject.scraper;

import pjh.dividendmanageproject.model.Company;
import pjh.dividendmanageproject.model.ScrapedResult;

public interface Scraper {
    ScrapedResult scrap(Company company);

    Company scrapCompanyByTicker(String ticker);
}
