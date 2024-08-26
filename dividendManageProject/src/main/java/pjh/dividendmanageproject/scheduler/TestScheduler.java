package pjh.dividendmanageproject.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestScheduler {

    @Scheduled(cron = "0/5 * * * * *")
    public void test() {
        System.out.println("now -> " + System.currentTimeMillis() );
    }

    // 자동화 - 스크랩 한 홈페이지의 정보가 업데이트 되었을 경우, 개발자가 수동으로 추가하는 것 보다 이를 자동으로 추가되게 하기 위함
    // 일정 주기마다 실행 = 자동화
    @Scheduled(cron = "")
    public void yahooFinanceScheduling() {
        // 저장된 회사 목록을 조회


        // 회사마다 배당금 정보를 새로 스크래핑 => 중복된 배당금 정보 저장 방지 =>


        // 스크래핑한 배당금 정보 중 DB에 없는 값은 저장
    }
}
