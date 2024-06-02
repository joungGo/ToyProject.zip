# Package
Controller > service > repository(domain) > DB

# 사용한 개념 정리

1. LOGGER.____()
: 애플리케이션 실행 중에 발생하는 이벤트나 정보를 기록

   - SEVERE : 심각한 에러나 애플리케이션 충돌을 나타냅니다.
   - WARNING : 잠재적인 문제나 중요하지 않은 에러를 나타냅니다.
   - INFO : 일반적인 정보 메시지를 나타냅니다. 애플리케이션의 정상적인 동작을 나타냅니다.
   - CONFIG : 설정이나 구성 관련 메시지를 나타냅니다.
   - FINE : 디버깅 목적으로 유용한 상세한 정보를 나타냅니다.
   - FINER : FINE보다 더 상세한 디버깅 정보를 나타냅니다.
   - FINEST : 가장 상세한 디버깅 정보를 나타냅니다.

2. 트랜잭션의 연산: 
   
   Spring 트랜잭션 세부설정
   : 
   - Isolation : 격리수준( @Transational(isolation=Isolation._______) )
   - DEFAULT
   - READ_UNCOMMITTED (Dirty Read 발생)
   - READ_COMMITTED (Dirty Read 방지) : 트랜잭션이 실행되는 도중에는 사용X
   - REPEATABLE_READ (Non-Repeatable Read 방지) : 하나의 트랜잭션이 완료될때 까지 접근 제한 LOCK을 건다.
   - SERIALIZABLE (Phantom Read 방지)
   
   Propagation 전파수준 : 트랜잭션 동작 도중 다른 트랜잭션을 호출하는 상황
   : 
   - `REQUIRED` : 현재 트랜잭션이 존재하면 현재 트랜잭션 내에서 실행되고, 존재하지 않으면 새로운 트랜잭션을 시작합니다.
   - 예: 메서드 A가 트랜잭션 내에서 실행되고, 메서드 A가 메서드 B를 호출할 때 B는 A의 트랜잭션을 공유합니다.
   만약 메서드 A가 트랜잭션 외부에서 실행되면, B가 새로운 트랜잭션을 시작합니다.
   - `SUPPORTS` : 트랜잭션이 존재하면 트랜잭션 내에서 실행되고, 존재하지 않으면 트랜잭션 없이 실행됩니다.
   - 예: 메서드 A가 트랜잭션 내에서 실행되면, 메서드 A가 호출하는 메서드 B는 그 트랜잭션을 사용합니다.
   메서드 A가 트랜잭션 없이 실행되면, B도 트랜잭션 없이 실행됩니다.
   - `REQUIRES_NEW` : 항상 새로운 트랜잭션을 시작하고, 기존 트랜잭션이 존재하면 일시 중단합니다.
   - 예: 메서드 A가 트랜잭션 내에서 실행되지만, 메서드 A가 메서드 B를 호출할 때 B는 새로운 트랜잭션을 시작하고 A의 트랜잭션은 일시 중단됩니다.
   B가 완료되면 A의 트랜잭션이 재개됩니다.
   - `NESTED`: 현재 트랜잭션이 존재하면 중첩된 트랜잭션을 시작하고, 존재하지 않으면 새로운 트랜잭션을 시작합니다.
   - 예: 메서드 A가 트랜잭션 내에서 실행될 때, 메서드 A가 호출하는 메서드 B는 중첩된 트랜잭션을 시작합니다.
   중첩된 트랜잭션은 별도로 커밋되거나 롤백될 수 있지만, 외부 트랜잭션이 롤백되면 중첩된 트랜잭션도 롤백됩니다.
   readOnly 속성
   : 
   - 트랜잭션을 읽기 전용 속성을 지정
   - 기본 옵션은 False
   - `@Transactional(readOnly = true)` => INSERT, UPDATE, DELETE 쿼리가 작동하면 오류가 발생한다.
   - 트랜잭션 롤백 예외 : 예외 발생했을 때 트랜잭션을 롤백시킬 경우 설정
   - `@Transactional(rollbackFor=Exception.class)`
   - `@Transactional(noRollbackFor=Exception.class)`
   - `Default` : RuntimeException, Error
   
   Timeout 속성 : 일정 시간 내에 트랜잭션이 끝나지 않으면 롤백
   : 
   - `@Transactional(timeout=10)`

3. 캐싱
: 요청한 것에 대한 응답이 변하지 않을 때만 사용할 수 있음

- 캐싱은 간단하게 과거 경험했던 기억을 찾아 같은 경험을 하게 된다면 더 빠르게 수행이 가능하게 하는 기능이다. 따라서, 과거의 경험과 미래에 겪을 경험이 같아야 한다.
- 장점
  - 요청을 빠르게 처리
  - 서버의 부하가 줄어듬
  - 이번의 경우 API 사용료 절감 가능