package pjh.weatherdailylog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pjh.weatherdailylog.domain.Diary;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Integer> {

    // 조회
    List<Diary> findAllByDate(LocalDate date); // 구현부를 작성하지 않아도 자동으로 SQL 쿼리문을 만들어준다. => JPA
    /*
    Spring Data JPA는 인터페이스 기반의 프로그래밍 모델을 제공하며,
    인터페이스를 정의하는 것만으로도 다양한 CRUD 및 쿼리 메소드를 자동으로 생성해줍니다.
    이 기능은 메소드 이름을 분석하여 적절한 SQL 쿼리를 생성하는 방식으로 작동합니다.
    이를 통해 개발자는 복잡한 SQL 쿼리를 작성하지 않고도 데이터베이스 작업을 수행할 수 있습니다.
     */

    // 범위 조회
    List<Diary> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    // 수정
    Diary getFirstByDate(LocalDate date);

    // 삭제
    @Transactional
    void deleteAllByDate(LocalDate date);
}
