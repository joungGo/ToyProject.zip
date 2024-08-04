package pjh.dividendmanageproject.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import pjh.dividendmanageproject.model.Company;
import pjh.dividendmanageproject.model.ScrapedResult;
import pjh.dividendmanageproject.persist.entity.CompanyEntity;
import pjh.dividendmanageproject.persist.entity.DividendEntity;
import pjh.dividendmanageproject.persist.repository.CompanyRepository;
import pjh.dividendmanageproject.persist.repository.DividendRepository;
import pjh.dividendmanageproject.scraper.Scraper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService {

    // Scrap
    private final Scraper yahooFinanceScraper;

    // DB
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public Company save(String ticker) {
        return null;
    }

    //
    private Company storeCompanyAndDividend(String ticker) {

        // ticker 를 기준으로 회사를 스크래핑
        Company company = this.yahooFinanceScraper.scrapCompanyByTicker(ticker);

        // 해당 회사가 없을 경우 => error 발생
        if (ObjectUtils.isEmpty(company)) {
            throw new RuntimeException("failed to scrap ticker -> " + ticker);
        }

        // 해당 회사가 존재할 경우, 회사의 배당금 정보를 스크래핑
        ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(company);

        // 스크래핑 결과
        CompanyEntity companyEntity
                = this.companyRepository.save(new CompanyEntity(company)); // new CompanyEntity(company) : 생성자 호출

        List<DividendEntity> dividendEntityList = scrapedResult.getDividends().stream()
                .map(e -> new DividendEntity(companyEntity.getId(), e)) // getDividends의 item 하나하나가 e에 해당함.
                .collect(Collectors.toList());

        this.dividendRepository.saveAll(dividendEntityList);
        return null;
    }


}
