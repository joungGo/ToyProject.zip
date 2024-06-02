package pjh.weatherdailylog.service;

import org.hibernate.boot.jaxb.internal.stax.LocalSchemaLocator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pjh.weatherdailylog.WeatherDailyLogApplication;
import pjh.weatherdailylog.domain.DateWeather;
import pjh.weatherdailylog.domain.Diary;
import pjh.weatherdailylog.repository.DateWeatherRepository;
import pjh.weatherdailylog.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@Service
@Transactional(readOnly = true)
public class DiaryService {
    //private static final Logger LOGGER = Logger.getLogger(DiaryService.class.getName());
    private static final Logger logger = LoggerFactory.getLogger(WeatherDailyLogApplication.class);
    //private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=";


    @Value("${openweathermap.key}")
    private String apiKey;

    private final DiaryRepository diaryRepository;
    private final DateWeatherRepository dateWeatherRepository;

    public DiaryService(DiaryRepository diaryRepository, DateWeatherRepository dateWeatherRepository) {
        this.diaryRepository = diaryRepository;
        this.dateWeatherRepository = dateWeatherRepository;
    }

    // 일정 주기마다 저장
    @Transactional
    @Scheduled(cron = " 0 0 1 * * * " ) // 매일 새벽 1시마다 아래 메소드 동작
    public void saveWeatherDate() {
        dateWeatherRepository.save(getWeatherFromApi());
    }

    // 생성
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createDiary(LocalDate date, String text) {
        logger.info("started to create diary");

        /* 날씨 데이터 - API 에서 가져오기
        String weatherData;
        // 1. open weather map에서 날씨 데이터 가져오기
        try {
            weatherData = getWeatherString();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to parse weather data: " + e.getMessage(), e);
            return; // TODO : 추가 예외 처리 예정
        }

        // 2. 받아온 날씨 json으로 파씽하기
        Map<String, Object> parsedWeather;
        try {
            parsedWeather = parseWeather(weatherData);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to parse weather data: " + e.getMessage(), e);
            return; // TODO : 추가 예외 처리 예정
        }*/

        // 날씨 데이터 DB 에서 가져오기
        DateWeather dateWeather = getDateWeather(date);

        // 3. 파싱된 데이터 + 일기 값 => 우리 DB에 넣기
        Diary nowDiary = new Diary();
        /*nowDiary.setWeather(parsedWeather.get("main").toString());
        nowDiary.setIcon(parsedWeather.get("icon").toString());
        nowDiary.setTemperature((Double) parsedWeather.get("temp"));*/
        nowDiary.setDateWeather(dateWeather);
        nowDiary.setText(text);
        nowDiary.setDate(date);

        // DB에 저장
        diaryRepository.save(nowDiary);
        //LOGGER.log(Level.INFO, "Diary created for date: " + date);
        logger.info("end to create diary");
    }

    private DateWeather getWeatherFromApi() {
        String weatherData = getWeatherString();
        Map<String, Object> parsedWeather = parseWeather(weatherData);

        DateWeather dateWeather = new DateWeather();
        dateWeather.setDate(LocalDate.now());
        dateWeather.setWeather(parsedWeather.get("main").toString());
        dateWeather.setIcon(parsedWeather.get("icon").toString());
        dateWeather.setTemperature((Double) parsedWeather.get("temp"));

        return dateWeather;
    }

    private DateWeather getDateWeather(LocalDate date) {
        List<DateWeather> dateWeatherListFromDB = dateWeatherRepository.findAllByDate(date);

        if (dateWeatherListFromDB.isEmpty()) {
            // 새로 api에서 날시 정보를 가져오기
            return getWeatherFromApi();
        } else {
            return dateWeatherListFromDB.get(0); // 조건에 맞는 일기 중 첫번째. => 조건에 맞는 데이터가 복수일 수 있음.(일단 첫번째 것으로 가정)
        }
    }

    // 조회
    @Transactional(readOnly = true)
    public List<Diary> readDiary(LocalDate date) {
        logger.debug("read diaries");
        return diaryRepository.findAllByDate(date);
    }

    // 범위 조회
    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {
        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }

    // 수정 => 현재 같은 일기가 존재할 경우 첫번째 일기를 수정하는 것을 전체로 구현되어 있음.
    // TODO : 같은 일기가 존재할 경우 원하는 일기를 지울 수 있도록 기능 구현 필요
    public void updateDiary(LocalDate date, String text) {
        Diary nowDiary = diaryRepository.getFirstByDate(date);

        if (nowDiary != null) {
            nowDiary.setText(text);
            diaryRepository.save(nowDiary);
            logger.info("Diary updated for date");
            //LOGGER.log(Level.INFO, "Diary updated for date: " + date);
        } else {
            logger.warn("Diary not found for date");
            //LOGGER.log(Level.WARNING, "Diary not found for date: " + date);
        }
    }

    // 삭제
    public void deleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }

    private String getWeatherString() {
        // TODO : apiUrl을 공통으로 빼야 할까? (자주 사용될지 모르겠다..)
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=" + apiKey;

        try {

            BufferedReader br = getBufferedReader(apiUrl);

            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            return response.toString();

        } catch (Exception e) {
            logger.error("Failed to get weather data");
            //LOGGER.log(Level.SEVERE, "Failed to get weather data", e);
            throw new RuntimeException("Failed to get weather data", e);
        }
    }

    private static BufferedReader getBufferedReader(String apiUrl) throws IOException {
        URL url = new URL(apiUrl); // String 형식이었던 apiKey가 Url 형식이 됨.
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // apiUrl을 Http형식으로 연결 시킴
        connection.setRequestMethod("GET"); // api를 호출할 때 GET 형식으로 부를꺼임.
        int responseCode = connection.getResponseCode(); // connection.setRequestMethod("GET"); 이걸 통한 요청에 대한 응답 결과를 받아오는 코드

        BufferedReader br; // BufferedReader 사용이유 : 받아오는 데이터 또는 에러 메시지의 길이가 길수록 처리속도가 느려질 수 있기 때문에 이를 보완할 BufferReader 사용
        if (responseCode == 200) {
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }
        return br;
    }

    private Map<String, Object> parseWeather(String jsonString) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            logger.error("Failed to parse weather data");
            //LOGGER.log(Level.SEVERE, "Failed to parse weather data", e);
            throw new RuntimeException("Failed to parse weather data", e);        }

        // TODO : 지금 가져와야 하는게.. main, icon, Temp
        Map<String, Object> resultMap = new HashMap<>();

        JSONObject mainData = (JSONObject) jsonObject.get("main"); // (JSONObject) 이렇게 해야 (JSONObject)로 인식됨
        resultMap.put("temp", mainData.get("temp"));

        JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
        JSONObject weatherData = (JSONObject) weatherArray.get(0);
        resultMap.put("main", weatherData.get("main"));
        resultMap.put("icon", weatherData.get("icon"));

        return resultMap;
    }
}
