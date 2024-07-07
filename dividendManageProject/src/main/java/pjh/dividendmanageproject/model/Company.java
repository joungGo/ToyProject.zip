package pjh.dividendmanageproject.model;

import lombok.Builder;
import lombok.Data;

@Data // @Getter, @Setter, @ToString, @RequiredArgsConstructor, @EqualsAndHashCode 포함하는 어노테이션
@Builder
/*
인자를 쌓는 형태로 전달할 수 있게 하는 어노테이션.
코드 구현시 구현부와 호출부를 분리하여 구현하는 경우가 많아서 어떤 값이 어떤 인자로 전달되어 있는지 인지하기 어렵다.
이부분을 해결하게 하는 어노테이션이 @Builder 이다.
https://medium.com/@thecodebean/builder-design-pattern-implementation-in-java-6adc6fd99ee0
*/
public class Company {

    private String ticker;
    private String name;
}
