package pjh.dividendmanageproject.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/finance") // 하위의 경로에서 공통되는 경로를 빼는 작업
public class FinanceController {

    // 배당금 조회
    //@GetMapping("/finance/dividend/{companyName}")
    @GetMapping("/dividend/{companyName}")
    public ResponseEntity<?> searchFinance(@PathVariable String companyName) {
        return null;
    }
}
