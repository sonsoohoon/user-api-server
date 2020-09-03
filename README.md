# user-api-server
apiserver
api명세
https://velog.io/@shson/api-server-%EB%AA%85%EC%84%B8

사용 기술 
DB - H2(현재 메모리 디비 상태, 영속성 필요시 application.yml에서 jdbc:h2:mem:testdb를 jdbc:h2:~/test로 변경필요
DB Mapper - JPA(하이버네이트)
인증 - 스프링 시큐리티 + JWT토큰
was - 내장톰캣

test소스는 test아래 controller/UserControllerTest.java를 보시기 바랍니다.
테이블 구조는 main 아래 domain/entity디렉토리를 참고 바랍니다.
기능요건, 비기능요건 모두 구현하였습니다.
