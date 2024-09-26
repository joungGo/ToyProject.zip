# 사용한 개념
1. 스크래핑
- HTML 문서를 받은 후
- 문서를 파싱 해서
- 필요한 데이터를 추출
  ![img.png](img.png)
> 단, 저작권 문제로 함부로 스크래핑 하면 안됨 + 서버 부하

*그럼 어떤 데이터가 스크래핑 하면 안되는 걸까?*
- 보통 robots.txt 문서에 정의되어 있다.
- 확인하고자 하는 페이지의 `루트 경로 URL` 뒤에 `robots.txt`를 입력한 후 Enter치면 확인 가능
- Disallow로 시작하는 문서가 스크래핑 불가 데이터이다.
> 회사에서는 하나의 서비스가 아닌 여러 서비스를 가지고 프로젝트를 진행하기 때문에 내가 개발 서비스가 어디에서 호출되는지 아는 것이 중요하다.
---
# 기능(API)
1. 배당금 조회
2. 배당금 검색 - 자동완성
3. 회사 리스트 조회
4. 관리자 기능 - 배당금 저장
5. 관리자 기능 - 배당금 삭제
6. 회원 : 회원가입, 로그인, 로그아웃 => 회원 인증
---
# 오류
1. Jsoup의 기본 User-Agent
- 개념
  : Jsoup에는 기본으로 설정된 User-Agent가 있다.

- 상황
  : 일부 서버에서는 이 User-Agent를 봇으로 감지해 접근을 차단하게 된다.
  503 Error가 뜨길래 처음에는 데이터를 너무 많이 가져와서 서버 과부하가 발생했나? 아니면 ip문제인가 싶었지만 모두 아니었다.

- 해결
  : 해결하기 위해 웹 스크래핑 요청을 보낼 때 봇이 아닌 일반 사용자의 브라우저에서 보내는 요청처럼 보이게하기 위해 별도의
  User-Agent를 설정했다.

2. 소켓 타임아웃 오류
- 개념
  : 네트워크 연결이 너무 오래 걸리거나 응답이 없는 경우 타임아웃 오류가 발생한다.

- 상황
  : YahooFinanceScrapper 클래스에서 데이터를 스크래핑할 때 소켓 타임아웃이 발생함

- 해결
  : Jsoup 라이브러리를 사용한 코드엣 `.timeout(50000)`코드를 추가함

> Jsoup의 기본 응답시간은 3초라고 해서 5초로 설정한건데 나중에 1초로 설정했는데도 돌아감.. 왜그러지?

> 가끔, 5초로 설정했을 때도 timeout error가 뜬다. 알아보니까 로컬 네트워크 상황에따라 그럴수 있다고하는데 이게 맞는걸까?

3. Redis 데이터 직렬화 오류
- 개념
  : CacheConfig 클래스에서 Redis의 특정 Serializer를 지정해서 직렬화를 할 수 있다. 
```java
  RedisCacheConfiguration conf = RedisCacheConfiguration.defaultCacheConfig()
  .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())) 
  .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
```
위 코드처럼 현재 사용한 Serializer는 대부분의 데이터 타입을 직렬화 할 수 있지만 `LocalDateTime`에 대한 직렬화 정보가 지정되어 있지 않아 오류 발생.

- 상황
  : (개념과 동일)

- 해결
  : ![img_3.png](img_3.png)

---
# 새로 배운 개념
1. valueOf vs parseInt
- 클래스 : DividendManageProjectApplication
- 코드 : int day = Integer.parseInt(splits[1].replace(",", ""));
- 내용 : valueOf는 Integer를 반환하고 parseInt는 int를 반환한다. 메모리 사용에있어 객체를 생성하지 않는 parseInt가 더 효율적이라고 판단. (아직 자세히는 모르겠음)

2. Thread.currentThread().interrupt(); vs throw new RuntimeException
```markdown
e.printStackTrace(); Thread.currentThread().interrupt();
이 코드는 예외 발생 시 스레드를 적절하게 처리하면서, 예외의 상세한 정보를 출력하고 현재 스레드의 상태를 관리합니다.

e.printStackTrace();:

예외가 발생했을 때, 예외 객체 e의 스택 트레이스를 콘솔에 출력합니다.
스택 트레이스는 예외가 발생한 지점까지의 메소드 호출 경로를 보여주며, 디버깅 시 유용한 정보를 제공합니다.
Thread.currentThread().interrupt();:

현재 실행 중인 스레드에 대해 인터럽트 상태를 설정합니다.
InterruptedException이 발생하면 스레드의 인터럽트 상태가 클리어(clear)되므로, Thread.currentThread().interrupt();를 호출하여 스레드가 여전히 인터럽트 상태임을 표시합니다.
이 코드를 통해, 상위 호출자에게 스레드가 중단(interrupt)되었음을 알리는 것이 좋습니다. 예를 들어, 향후 스레드의 상태를 체크하거나 다른 처리에서 이 인터럽트 상태를 활용할 수 있습니다.
이 패턴은 예외를 처리하되, 스레드를 올바르게 정리하고 인터럽트 상태를 복구하는 데 유용합니다. 다만, 예외를 던지지 않기 때문에 상위 메소드로 예외가 전파되지는 않습니다.

throw new RuntimeException(e);
이 코드는 발생한 예외를 RuntimeException으로 래핑(wrap)하여 다시 던지는 것입니다.

예외 전파:

예외 e를 RuntimeException으로 감싸서 상위 호출 메소드로 전파합니다. 이로 인해, 예외가 잡히지 않는 한 프로그램이 비정상적으로 종료될 수 있습니다.
RuntimeException은 체크 예외(checked exception)가 아닌 언체크 예외(unchecked exception)로, 호출 메소드에서 명시적으로 처리할 필요는 없지만, 예외가 발생하면 프로그램의 흐름에 큰 영향을 미칩니다.
런타임 예외로 변환:

예외를 런타임 예외로 변환하여 던지면, 예외를 명시적으로 처리하지 않은 코드에서도 해당 예외에 대한 반응을 강제할 수 있습니다.
이 패턴은 예외를 무시하지 않고 상위로 전파시켜, 호출 스택 상위에서 적절하게 처리하거나 프로그램을 종료하도록 하는 데 유용합니다.

요약
e.printStackTrace(); Thread.currentThread().interrupt();: 예외 정보를 출력하고, 현재 스레드의 인터럽트 상태를 복원합니다. 예외를 상위로 전파하지 않으며, 주로 예외를 처리하면서도 스레드의 상태를 유지하고 싶을 때 사용됩니다.
throw new RuntimeException(e);: 예외를 런타임 예외로 감싸서 다시 던집니다. 예외를 상위 메소드로 전파시켜 프로그램 흐름을 중단시키거나 상위에서 예외를 처리하도록 강제합니다.
```

3. 캐시(Cache)
- 정의 : 임시로 데이터를 저장하는 공간
- 목적 : 성능 향상 -> 빠른 처리 속도
  - 어떻게? : 한번 읽어온 데이터를 임의의 공간에 저장해, 다음에 같은 데이터에 접근 해야할 때 이를 참조해 빠른 처리를 가능케 한다.
  - 임의의 공간 : 영구적으로 저장할 수도 있고 임의의 시간 후 자동 삭제되게 할 수도 있다.
- Redis : 캐시 서버를 구축할 때 주로 사용
  - Key-value In-memoery Data Store
  - 다양한 형태의 데이터 타입 지원 - Hash, List 와 같은 자료형 사용 가능
  - 디스크가 아닌, `메모리에 접근해 데이터를 처리`하기 때문에 RDBMS(디스크)에 비해 처리 속도가 빠르다.
> Redis는 영속성의 특성도 가지지만, 영구적으로 저장되고 손상되면 안되는 데이터의 원본을 저장하기 보다는, 주로 캐시 서버의 용도로 사용된다.
  - 종류
    - Single : 단일 노드
    - Sentinel : master-slave 구조
    - Cluster : master-slave 구조
      ![img_2.png](img_2.png)
      - M1 : master 노드 1번을 의미, 서버당 1개의 master 노드만 가질 수 있다.
      - S1 : slave 노드 1번을 의미, master 노드의 데이터를 동일하게 복사해서 가지고 있는 노드
        - 단, M1의 slave 노드는 M1이 위치해 있는 서버와 다른 서버에 배치된다. 즉, M1의 slave 노드는 2,3번 서버에 하나씩 배치된다. 연결관계(선)가 이를 의미
        - 만약, M1이 작동을 멈추면 해당 노드의 slave 노드인 s5, s6이 master 노드로 승격되면서 서비스의 정상 운용이 가능하다.
        > 그래서, 운영환경에서 일부 서버가 죽더라도 서버 운영에 지장이 없게 하기 위해 Cluster 구조를 사용한다.
```java
@Cacheable(key = "#companyName", value = "finance")
    public ScrapedResult getDividendByCompanyName(String companyName) {

        // 1. 회사명을 기준으로 회사 정보를 조회
        CompanyEntity company = this.companyRepository.findByName(companyName)
                                                .orElseThrow(() -> new RuntimeException("Can't find the company Name"));
//  ...
```
- #을 붙이는 이유 : **SpEL(Spring Expression Language)**을 활용하여 메서드의 매개변수나 속성에 접근하기 위해서이다.
- #companyName에서 #은 해당 메서드의 매개변수인 companyName에 접근한다는 의미입니다. 즉, key = "#companyName"은 메서드에 전달된 companyName 값을 캐시 키로 사용하겠다는 뜻입니다.
- 이를 통해 getDividendByCompanyName 메서드가 호출될 때, companyName의 값에 따라 캐시를 구분하고, 동일한 companyName으로 호출되면 캐시에 저장된 값을 재사용합니다.
> @Cacheable 애노테이션에서 key = "companyName"과 같이 # 없이 사용하면, SpEL(Spring Expression Language)을 통한 매개변수 참조가 아니라 단순한 문자열로 인식됩니다.

즉, key = "companyName"라고 적으면, companyName이라는 문자열 자체가 캐시 키로 사용됩니다. 결과적으로 메서드를 호출할 때 어떤 값을 전달하든, 항상 동일한 캐시 키 "companyName"을 사용하게 됩니다. 이렇게 되면 다른 회사명을 입력해도 캐시 키가 동일하여, 캐시 충돌이 발생할 수 있습니다.



---
# CodeTelling
## 스크래핑
내가 원하는 사이트를 `http connection`을 맺고 이 커넥션으로부터 html 문서를 받아서 parsing된 형태로 `Document 인스턴스 형태`로
만들어주는 걸 `Jsoup 라이브러리`가 해준다. 이후 필요한 데이터를 찾아 알맞은 코드를 사용해 기능을 구현하면 된다.

---
# 참조 사이트
1. [Jsoup](   https://jsoup.org/apidocs/org/jsoup/Jsoup.html
   ). : HTML 파싱, 조작 그리고 데이터 추출을 위한 라이브러리
```java
// 기본형태 
Connection connection = Jsoup.connect(" ");
Document document = connection.get();
``` 
---
# DB
회사

| column |  type  | unique |  example  |
|:------:|:------:|:------:|:---------:|
|   id   |  Long  |   v    |     1     |
|  name  | String |        | Coca-Cola |
| ticker | String |   v    |   COKE    |

배당금

|   column   |     type      | unique |  example   |
|:----------:|:-------------:|:------:|:----------:|
|     id     |     Long      |   v    |     3      |
| company_id |     Long      |        |     1      |
|    date    | LocalDateTime |        | 2024-07-05 |
|  dividend  |    String     |        |    2.00    |

> 회사 table의 `ticker`를 통해 구분이 가능한데 배당금 table에서 `company_id`로 다시 구분을 해준이유
: 비교연산 속도가 문자열간 비교보다 숫자간 비교가 더 빠르기 때문에 이렇게 설계. (인터넷에서..)
