package pjh.dividendmanageproject.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pjh.dividendmanageproject.model.Company;
import pjh.dividendmanageproject.model.Dividend;
import pjh.dividendmanageproject.model.ScrapedResult;
import pjh.dividendmanageproject.model.constants.CacheKey;
import pjh.dividendmanageproject.persist.entity.CompanyEntity;
import pjh.dividendmanageproject.persist.entity.DividendEntity;
import pjh.dividendmanageproject.persist.repository.CompanyRepository;
import pjh.dividendmanageproject.persist.repository.DividendRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FinanceService { // 배당금 정보 관련 서비스 구현 클래스

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    // 1. (주식정보)요청이 자주 들어로는가?
    // 2. 데이터가 자주 변경되는 데이터 인가?

    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName) {

        log.info("search company -> " + companyName);

        // 1. 회사명을 기준으로 회사 정보를 조회
        CompanyEntity company = this.companyRepository.findByName(companyName)
                                                .orElseThrow(() -> new RuntimeException("Can't find the company Name"));

        // 2. 조회된 회사 ID 로 배당금 정보 조회
        List<DividendEntity> dividendEntities = this.dividendRepository.findAllByCompanyId(company.getId());

        // 3. 결과 조합 후 반환 => Entity를 Model로 변환 후 반환해야 함!
        /*List<Dividend> dividends = new ArrayList<>();
        for (var entity : dividends) {
            dividends.add(Dividend.builder()
                                            .date(entity.getDate())
                                            .dividend(entity.getDividend())
                                            .build());
        }*/

        List<Dividend> dividends = dividendEntities.stream()
                .map(e -> new Dividend(e.getDate(), e.getDividend()))
                        // Dividend : Builder 어노테이션을 사용했을 경우
                        /* Dividend.builder()
                        .date(e.getDate())
                        .dividend(e.getDividend())
                        .build())*/
                        .collect(Collectors.toList());

        return new ScrapedResult(new Company(company.getTicker(), company.getName()),
                // Dividend : Builder 어노테이션을 사용했을 경우
                /*Company.builder()
                .ticker(company.getTicker())
                .name(company.getName())
                .build(),*/
                dividends);
    }
}
