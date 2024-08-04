package pjh.dividendmanageproject.persist.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pjh.dividendmanageproject.model.Company;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "COMPANY")
@Getter
//@Setter 외부에서 인자의 입력으로 값이 변경되지 않는 경우 @Setter 어노테이션을 사용하지 않는 것이 좋다. 변경을 방지하기 위함!
@ToString
@NoArgsConstructor
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticker;

    private String name;

    // DB의 ticker, name에 스크랩한 데이터가 담겨있는 Company의 ticker, name을 저장한다.
    public CompanyEntity(Company company) { // => 생성자
        this.ticker = company.getTicker();
        this.name = company.getName();
    }

}
