package pjh.dividendmanageproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ScrapedResult {

    // 스크래핑한 회사가 어떤 회사인지
    private Company company;

    // 배당금 리스트 인스턴스를 멤버변수로 가짐
    private List<Dividend> dividendsEntities;

    public ScrapedResult() {this.dividendsEntities = new ArrayList<>(); }
}
