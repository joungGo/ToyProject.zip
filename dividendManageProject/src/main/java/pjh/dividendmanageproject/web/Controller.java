package pjh.dividendmanageproject.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    // 배당금 조회
    @GetMapping("/finance/dividend/{companyName}")
    public ResponseEntity<?> searchFinance(@PathVariable String companyName) {
        return null;
    }

    // 배당금 검색 - 자동완성
    @GetMapping("/company/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
        return null;
    }

    // 회사 리스트 조회
    @GetMapping("company")
    public ResponseEntity<?> searchCompany() {
        return null;
    }

    // 배당금 데이터 저장
    @PostMapping("/company")
    public ResponseEntity<?> addCompany() {
        return null;
    }

    // 회사 삭제
    @DeleteMapping("/company")
    public ResponseEntity<?> deleteCompany() {
        return null;
    }

}
