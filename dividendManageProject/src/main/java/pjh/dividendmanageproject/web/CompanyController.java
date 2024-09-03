package pjh.dividendmanageproject.web;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import pjh.dividendmanageproject.model.Company;
import pjh.dividendmanageproject.persist.entity.CompanyEntity;
import pjh.dividendmanageproject.service.CompanyService;

@RestController
@RequestMapping("/company") // 하위의 경로에서 공통되는 경로를 빼는 작업
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    // 배당금 검색 - 자동완성
    //@GetMapping("/company/autocomplete")
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
        var result = this.companyService.getCompanyNamesByKeyword(keyword);

        return ResponseEntity.ok(result);
    }

    // 회사 리스트 조회
    //@GetMapping("company")
    @GetMapping
    public ResponseEntity<?> searchCompany(final Pageable pageable) { // pageable 값이 변하는 것을 방지하기 위해 final 사용
        Page<CompanyEntity> companies = this.companyService.getAllCompany(pageable);
        return ResponseEntity.ok(companies);
    }

    // 배당금 데이터 저장
    @PostMapping
    public ResponseEntity<?> addCompany(@RequestBody Company request) {
        String ticker = request.getTicker().trim();
        if (ObjectUtils.isEmpty(ticker)) {
            throw new RuntimeException("ticker is empty");
        }

        Company company = this.companyService.save(ticker);
        // 입력된 ticker에 의해 company 정보가 저장될 때 trie에 company name도 같이 저장되게 해당 메소드 실행부분 추가
        this.companyService.addAutocompleteKeyword(company.getName());

        return ResponseEntity.ok(company);
    }

    // 회사 삭제
    @DeleteMapping
    public ResponseEntity<?> deleteCompany() {
        return null;
    }
}
