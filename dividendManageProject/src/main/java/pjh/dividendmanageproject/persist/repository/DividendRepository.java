package pjh.dividendmanageproject.persist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjh.dividendmanageproject.persist.entity.DividendEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DividendRepository extends JpaRepository<DividendEntity, Long> {

    List<DividendEntity> findAllByCompanyId(Long companyId);

    boolean existByCompanyIdAndDate(Long companyId, LocalDateTime date);
}
