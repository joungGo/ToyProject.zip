package pjh.dividendmanageproject.service;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

@Service // 싱글톤
@AllArgsConstructor
public class CompanyService {

    private final Trie trie; // 이 trie는 프로젝트 내에서 하나로만 사용(유지)되어야 하므로 별도의 빈으로 생성하여 사용한다. = 싱글톤

    // Scrap
    private final Scraper yahooFinanceScraper;

    // DB
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    // DB에 저장하지 않은 company만 저장하도록 하는 기능 = filtering func.
    public Company save(String ticker) {
        boolean exist = this.companyRepository.existsByTicker(ticker);
        if (exist) {
            throw new RuntimeException("already exist ticker -> " + ticker);
        }

        return storeCompanyAndDividend(ticker);
    }

    // 회사 조회 기능 - DB 조회
    public Page<CompanyEntity> getAllCompany(Pageable pageable) {
        return this.companyRepository.findAll(pageable);
    }

    // 회사 및 배당금 정보 저장 기능
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
        return company;
    }

    // DB에서 조건에 맞는 데이터 가져오기 기능
    public List<String> getCompanyNamesByKeyword(String keyword) {
        Pageable limit = PageRequest.of(0, 10);
        Page<CompanyEntity> companyEntities = this.companyRepository.findByNameStartingWithIgnoreCase(keyword, limit);
        return companyEntities.stream()
                .map(e -> e.getName()) // e = companyEntities
                .collect(Collectors.toList());
    }

    // 자동완성 - trie에 회사명 저장하기
    public void addAutocompleteKeyword(String keyword) {
        this.trie.put(keyword, null);
        /*
        * trie는 keyword에 집중되는 구조이다. 그래서 value를 null로 처리해도 상관없다.
        * value의 경우 각 keyword의 검색 빈도수를 체크하는 기능을 만든다고 할 때 추가적으로 구현하면 된다.*/
    }

    // 자동완성 - trie에서 회사명 조회하기
    public List<String> autocomplete(String keyword) {
        return (List<String>) this.trie.prefixMap(keyword).keySet()
                .stream()
                //.limit(10) 가져올 개수에 제한을 두는 것.
                .collect(Collectors.toList());
    }

    // 자동완성 - trie에 저장된 데이터 삭제하기
    public void deleteAutocompleteKeyword(String keyword) {
        this.trie.remove(keyword);
    }
}
