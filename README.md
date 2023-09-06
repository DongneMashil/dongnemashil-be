# 🏃🏻‍♀️ 동네마실 🏃🏻‍♀️ 

##  💂🏻3조 BE 용사들💂🏻

<a href="https://github.com/junyoung93">
<img src="https://github.com/junyoung93.png" width="100" height="100"/>
</a>
<a href="https://github.com/codegyeon">
<img src="https://github.com/codegyeon.png" width="100" height="100"/>
</a>


<br>

## 💡브랜치 전략 git-flow

main ← develop ← feature/[브랜치명],#[이슈번호]

- main : 제품으로 출시될 수 있는 브랜치
- develop : 다음 출시 버전을 개발하는 브랜치
- feature : 기능을 개발하는 브랜치


<br>

## 💡커밋컨벤션

| 타입     | 설명                                              |
| -------- | ------------------------------------------------- |
| create   | 폴더, 파일 생성                                   |
| edit     | 파일, 기능 수정                                     |
| done     | 기능개발 완료                                     |
| feat     | 새로운 기능 추가                                   |
| fix      | 버그 수정                                         |
| docs     | 문서 수정                                         |
| style    | 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우         |
| refactor | 코드 리팩토링                                     |
| test     | 테스트 코드, 리팩토링 테스트 코드 추가                |
| chore    | 빌드 업무 수정, 패키지 매니저 수정                    |
| merge    | pull 받은 후 바로 푸시를 해야될 경우                |
| fire     | 코드 정리                                      |

- 커밋 메시지 예시
  - merge할때 예시 merge : develop → feature/좋아요 기능 구현,#7
  

<br>

## 💡코드컨벤션

| 항목                | 규칙                                     | 예시                           |
| ------------------- | ---------------------------------------- | ------------------------------ |
| 변수명              | Camel Case로 작성                        | myVariableName                 |
| 파일명              | Pascal Case로 작성                       | MainPage.java                   |
| Commit              | Project Issue 별로 작성 (하기 설명)      | Create : CRUD 스켈레톤 생성    |
| 변수명/함수명 길이  | 4단어 이하 (부득이하게 늘어날 경우 상의) | getUserData                    |
| 약칭 사용           | 사용                            | count (=  cnt)                |
| 주석                | 필요한 경우만 사용, 팀원                    | -                              |
| 커밋 이름           | 커밋타입 : 한글로 설명                   | ‘create : 회원가입페이지 생성’ |
| 줄의 길이          | intellij code style에 default 설정값 = 120       | -                             
| 커밋 전            | 코드 정렬 - Optimizer     | 맥os ctrl + opt + o       |
+ build.gradle에 의존성 주입할 때는 팀원들과 협의 후 추가

<br>

## 💡코드 규칙
1. 사용하지 않는 Enotation, import는 삭제
  - ex) Entity 클래스에서 @Setter 사용하지 않으면 삭제
2. log & System.out.println() 등 확인용 코드 삭제후 커밋
3. Return값 DTO로 통일 

## 💡ERD

![ERD최종](https://github.com/DongneMashil/dongnemashil-be/assets/128367271/6c32971c-a864-4e2a-9036-3fb6bc937622)




## 💡기술 스택

<div align=center> 
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 
  <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white">
  <img src="https://img.shields.io/badge/amazons3-5569A31?style=for-the-badge&logo=amazons3&logoColor=white">


  <br>
  
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">

  <br>
  
  
  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> 

  <img src="https://img.shields.io/badge/nginx-FCC624?style=for-the-badge&logo=nginx&logoColor=black"> 
<img src="https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white">
<img src="https://img.shields.io/badge/amazonrds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white">
  <img src="https://img.shields.io/badge/apache tomcat-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=white">  
  <img src="https://img.shields.io/badge/springsecurity-00AF5C?style=for-the-badge&logo=springsecurity&logoColor=white">
  <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">

  <br>
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white">
<img src="https://img.shields.io/badge/githubactions-181717?style=for-the-badge&logo=githubactions&logoColor=white">
  <img src="https://img.shields.io/badge/gitignoredotio-F05032?style=for-the-badge&logo=gitignoredotio&logoColor=white">

  <br>
</div>

