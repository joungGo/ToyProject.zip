package pjh.dividendmanageproject.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pjh.dividendmanageproject.model.Company;
import pjh.dividendmanageproject.model.ScrapedResult;
import pjh.dividendmanageproject.persist.entity.CompanyEntity;
import pjh.dividendmanageproject.persist.entity.DividendEntity;
import pjh.dividendmanageproject.persist.repository.CompanyRepository;
import pjh.dividendmanageproject.persist.repository.DividendRepository;
import pjh.dividendmanageproject.scraper.Scraper;

import java.util.List;

@Slf4j // 스케줄러가 실행될 때마다 실행되고 있음을 알려주기 위해 Logging을 사용하기 위한 어노테이션
@Component
@AllArgsConstructor
public class ScraperScheduler {

    private final CompanyRepository companyRepository;
    private final Scraper yahooFinanceScraper;
    private final DividendRepository dividendRepository;

    // 자동화 - 스크랩 한 홈페이지의 정보가 업데이트 되었을 경우, 개발자가 수동으로 추가하는 것 보다 이를 자동으로 추가되게 하기 위함
    // 일정 주기마다 실행 = 자동화
    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling() {
        log.info("scraping scheduler is started");
        // 저장된 회사 목록을 조회
        List<CompanyEntity> companies = this.companyRepository.findAll();

        // 회사마다 배당금 정보를 새로 스크래핑 => 중복된 배당금 정보 저장 방지 => DividendEntity에 Unique 속성 선언
        for (var company : companies) {
            log.info("scraping scheduler is started -> " + company.getName());
            ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(Company.builder()
                                                    .name(company.getName())
                    /* 현재, companies의 회사명들은 CompanyEntity 타입이다.
                    *  하지만, scrap 메소드의 매개변수 타입은 Company를 요구하므로
                    *  builder를 사용해 데이터 타입을 변경해 맞춰줘야 한다.
                    * */
                                                    .ticker(company.getTicker())
                                                    .build());

            // 스크래핑한 배당금 정보 중 DB에 없는 값은 저장
            // this.dividendRepository.saveAll() : DividendEnity에 Unique 속성이 있어 중복된 값이 저장될 경우 saveAll은 모든 데이터를 처리하지 않게 된다.
            scrapedResult.getDividends().stream() // 아래의 e는 getDividends()를 의미
                    .map(e -> new DividendEntity(company.getId(), e))// e가 Dividend 타입인데 현재, DB에 저장해야 하는 상황이니까 DividendEntity 타입으로 변환해주는 과정
                    .forEach(e -> { // e는 위에서 map을 통해 변환된 각각의 DividendEntity를 의미
                        boolean exist = this.dividendRepository.existByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                        if (!exist) {
                            this.dividendRepository.save(e);
                        }
                    });

            // 서버에 연속적으로 요청을 날리게 되면, 요청을 받는 서버에 부하가 가기때문에 요쳥들 사이에 term을 둬야 한다.
            try {
                Thread.sleep(3000); // 실행중인 스레드를 잠시 멈추게 할 때 사용 // 3 seconds
            } catch (InterruptedException e) {
                // throw new RuntimeException(e); // : 예외를 명시적으로 처리하지 않은 코드에서도 해당 예외에 대한 반응을 강제할 수 있다.
                                                  // 프로그램 흐름을 중단시키거나 상위에서 예외를 처리하기 위해 주로 사용
                e.printStackTrace();
                Thread.currentThread().interrupt();
                /*
                예외 정보를 출력하고, 현재 스레드의 인터럽트 상태를 복원합니다.
                예외를 상위로 전파하지 않으며, 주로 예외를 처리하면서도 스레드의 상태를 유지하고 싶을 때 사용됩니다.
                 */
            }

        }




    }
}
