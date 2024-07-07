package pjh.dividendmanageproject;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pjh.dividendmanageproject.model.Company;
import pjh.dividendmanageproject.scraper.YahooFinanceScrapper;

import java.io.IOException;

//@SpringBootApplication
public class DividendManageProjectApplication {

    public static void main(String[] args) {
        //SpringApplication.run(DividendManageProjectApplication.class, args);

        YahooFinanceScrapper scraper = new YahooFinanceScrapper();
        //var result = scraper.scrap(Company.builder().ticker("O").build());
        var result = scraper.scrapCompanyByTicker("MMM");
        System.out.println(result);

    }

}
