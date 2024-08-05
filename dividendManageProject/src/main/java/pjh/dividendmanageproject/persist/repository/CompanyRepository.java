package pjh.dividendmanageproject.persist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjh.dividendmanageproject.persist.entity.CompanyEntity;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    boolean existsByTicker(String ticker); // springboot에서 정해진 규칙에 의거해 자동으로 이 메소드 내부를 생성해준다.

    Optional<CompanyEntity> findByName(String name);

}
