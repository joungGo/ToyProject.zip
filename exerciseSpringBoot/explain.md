# application properties
`spring.jpa.hibernate.ddl-auto`의 규칙

우리는 `spring.jpa.hibernate.ddl-auto`를 `update`로 설정했다. 또 어떤 설정값이 있는지 간단히 알아보자.

- **none** : 엔티티가 변경되더라도 데이터베이스를 변경하지 않는다.
- **update** : 엔티티의 변경된 부분만 데이터베이스에 적용한다.
- **validate** : 엔티티와 테이블 간에 차이점이 있는지 검사만 한다.
- **create** : 스프링 부트 서버를 시작할 때 테이블을 모두 삭제한 후 다시 생성한다.
- **create-drop** : `create`와 동일하지만 스프링 부트 서버를 종료할 때에도 테이블을 모두 삭제한다.

개발 환경에서는 보통 `update` 모드를 사용하고, 운영 환경에서는 `none` 또는 `validate`를 주로 사용한다.

---

# 어노테이션 및 속성 설명

1. cascade = CascadeType.REMOVE
: 게시판 서비스에서는 질문 하나에 답변이 여러 개 작성될 수 있다. 그런데 보통 게시판 서비스에서는 질문을 삭제하면 그에 달린 답변들도 함께 삭제된다. 질문을 삭제하면 그에 달린 답변들도 모두 삭제되도록 cascade = CascadeType.REMOVE를 사용했다. https://www.baeldung.com/jpa-cascade-types을 참고!

2. @Autowired
: 객체를 주입하는 방식에는 @Autowired 애너테이션을 사용하는 것 외에 Setter 메서드 또는 생성자를 사용하는 방식이 있다. 순환 참조 문제와 같은 이유로 개발 시 @Autowired보다는 생성자를 통한 객체 주입 방식을 권장한다. 하지만 테스트 코드의 경우 JUnit이 생성자를 통한 객체 주입을 지원하지 않으므로 테스트 코드 작성 시에만 @Autowired를 사용하고 실제 코드 작성 시에는 생성자를 통한 객체 주입 방식을 사용한다.

3. @Configuration
: 위 어노테이션이 붙어 있는 클래스 안의 메소드를 `빈`으로 사용하고 싶을 때 클래스에 붙이는 어노테이션. 빈으로 사용하고 싶은 메서드에는 @Bean 어노테이션을 붙여준다.

```java 
@Configuration
public class HelloConfig {
    @Bean
    public Hello hello1() {
        return new Hello();
    }

    @Bean
    public Hello hello2(Hello hello1) {
        return new Hello();
    }
}
```

빈 이름
- @Component : 클래스 이름의 첫 글자를 소문자로 바꾼 이름으로 빈을 등록한다.
- @Bean : 메서드 이름을 빈 이름으로 사용한다. -> 위 예제에서는 hello() 메서드가 빈 이름이 된다.

🤔 빈 이름이 중복되면 어떻게 될까?

```java
@Configuration
public class HelloConfig {
    @Bean
    public Hello hello() {
        return new Hello();
    }

    @Bean
    @Primary
    public Hello hello() {
        return new Hello();
    }
}
```
- 해결 방법
  1. 빈 이름을 다르게 지정한다.
  2. @Bean(name = "hello1")과 같이 name 속성을 사용하여 빈 이름을 지정한다.
  3. @Primary 어노테이션을 사용하여 우선순위를 지정한다.
  4. @Qualifier 어노테이션을 사용하여 빈 이름을 지정한다.

```java
@Configuration
public class HelloConfig {

    @Bean("hello")
    public Hello hello1() {
        return new Hello();
    }

    @Bean("hello")
    public AnotherHello hello2() {
        return new AnotherHello();
    }
}

@Component
public class MyService {

    private final AnotherHello hello;

    @Autowired
    public MyService(@Qualifier("hello") AnotherHello hello) {
        this.hello = hello;
    }
}
```

4. ![img.png](img.png)


---

# 파일 설명
- layout.html : html 을 표준형식으로 구현 및 변환하는 과정 중 중복되는 부분을 layout.html로 분리하여 사용