package pjh.dividendmanageproject.persist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "COMPANY") // 테이블 이름
@Getter
//@Setter 외부에서 인자의 입력으로 값이 변경되지 않는 경우 @Setter 어노테이션을 사용하지 않는 것이 좋다. 변경을 방지하기 위함!
@ToString
@NoArgsConstructor
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id -> AutoIncrement
    private Long id;

    @Column(unique = true) // 중복 금지
    private String ticker;

    private String name;
}
