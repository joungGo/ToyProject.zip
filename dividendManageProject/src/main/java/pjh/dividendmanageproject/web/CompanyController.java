package pjh.dividendmanageproject.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company") // 하위의 경로에서 공통되는 경로를 빼는 작업
public class CompanyController {

    // 배당금 검색 - 자동완성
    //@GetMapping("/company/autocomplete")
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
        return null;
    }

    // 회사 리스트 조회
    //@GetMapping("company")
    @GetMapping
    public ResponseEntity<?> searchCompany() {
        return null;
    }

    // 배당금 데이터 저장
    @PostMapping
    public ResponseEntity<?> addCompany() {
        return null;
    }

    // 회사 삭제
    @DeleteMapping
    public ResponseEntity<?> deleteCompany() {
        return null;
    }
}
