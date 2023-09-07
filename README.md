![cover](https://github.com/DongneMashil/.github/assets/127714273/c70b3a58-420a-4a3d-9125-81286952cdbf)

# 🏃🏻‍♂️ 우리 동네의 새로운 발견, 동네마실

https://dongnemashil.me

나만의 숨은 산책로를 공유할 수 있는 위치 기반 커뮤니티 서비스입니다.

산책로는 기존 위치 기반 서비스나 지도 서비스에서 쉽게 찾아내기 어렵습니다. 동네마실은 산책을 통해 일상에 활력을 되찾는 사람들이 쉽고 편하게 새로운 산책로를 공유하고, 미처 몰랐던 주변의 아름다운 풍경을 재발견할 수 있기를 바라는 마음을 담아 만들어진 커뮤니티형 위치 기반 서비스입니다.

## 📆 프로젝트 기간

2023/0728 ~ 2023/09/08

## 😀 팀원 소개

<table>
  <tbody>
    <tr>
       <td align="center"><a href="https://github.com/codegyeon"><img src="https://github.com/codegyeon.png" width="100px;" alt="BE 정기현"/><br /><sub><b>BE VL 정기현</b></sub></a></td>
       <td align="center"><a href="https://github.com/junyoung93"><img src="https://github.com/junyoung93.png" width="100px;" alt="BE 박준영"/><br /><sub><b>BE 박준영</b></sub></a></td>
      </tr>
  </tbody>
</table>

## 💡 주요 기능 및 서비스

  <details>
<summary>검색으로 걷고 싶은 장소 구경하기 (태그 필터링)</summary>

![태그필터링1](https://github.com/DongneMashil/.github/assets/127714273/f83c2303-3b2e-4247-a057-aecc2e958f5e)

태그 선택을 통해 유저의 관심사에 맞는 피드를 필터링할 수 있습니다.

메인 페이지와 주소 키워드를 통한 검색 결과에 적용 가능합니다.

- 상단의 태그를 선택하면 게시글 필터링이 되고, 해제하면 다시 전체 게시물이 보여집니다.

![태그필터링2](https://github.com/DongneMashil/.github/assets/127714273/8f9dbe3a-5d78-4f97-acf7-1cab45ef8043)

- 기본으로 ‘인기순’ 정렬되어 있고 ‘최신순’ 정렬도 가능합니다.
  - 태그가 선택된 상태에서도 정렬이 가능합니다.

![태그필터링3](https://github.com/DongneMashil/.github/assets/127714273/c688982b-2045-4063-9861-a46bc4249be0)

</details>

<details>
<summary>검색으로 걷고 싶은 장소 구경하기 (키워드 검색)</summary>
주소의 키워드를 입력해 지역별 산책로 리뷰를 볼 수 있습니다.

![검색1](https://github.com/DongneMashil/.github/assets/127714273/76757475-8323-4a8f-8538-2f4e014607d4)

- 검색 결과 데이터의 페이지네이션은 메인 페이지와 마찬가지로 무한 스크롤로 구현하였습니다.
- 검색된 피드는 지도보기 기능을 통해 위치를 확인할 수 있고, 각각의 상세페이지로 이동할 수 있습니다.

![검색2](https://github.com/DongneMashil/.github/assets/127714273/a46f091e-c196-48c8-8cb4-2d71a1776d5f)

</details>

<details>
<summary>검색으로 걷고 싶은 장소 구경하기 (반경 검색)</summary>

위치 정보를 활용하여, 유저가 있는 위치의 일정 반경 내 게시글들을 검색하여 지도로 보여주는 기능입니다.

![반경검색](https://github.com/DongneMashil/.github/assets/127714273/2eea74e1-8921-4a9e-884b-74894d3397c8)
f)

- 최초 유저의 현 위치를 기준으로 실행됩니다.
- 귀여운 동동이 마커를 드래그하여 원하는 곳의 반경 내 산책로를 추가 검색할 수 있습니다.
</details>

<details>
<summary>메인페이지 산책로를 둘러보기</summary>

- 유저들의 게시물을 필터링, 정렬하여 볼 수 있습니다.
- 무한스크롤을 구현하였습니다.

[모바일]

![메인페이지1](https://github.com/DongneMashil/.github/assets/127714273/c452ed91-add5-49c7-9d4f-29b06ab7bff4)
8734fc)

[PC] Masonry Layout 구현 (Naver egjs-grid)

![메인페이지2](https://github.com/DongneMashil/.github/assets/127714273/e08a7c61-8ca0-4799-9c16-8790fbb46e36)

</details>

<details>
<details>
<summary>나만의 산책로 소개하기</summary>

- 내가 산책한 위치와 멋진 사진, 동영상을 공유할 수 있어요.
  자유롭게 태그를 선택해 산책의 분위기까지 표현해보세요!

![글쓰기1](https://github.com/DongneMashil/.github/assets/127714273/d5b7df81-57cd-4b41-8ec2-c67c0a5710f6)

- 글쓰기(지도)

<img width="774" alt="글쓰기2" src="https://github.com/DongneMashil/.github/assets/127714273/b95ec164-4479-4693-bedb-3e96d6229106">

     - 페이지에 처음 들어갈때 현위치를 가져오고 유저가 원하는데로 핀을 움직여서 원하는 주소값을 가져올 수 있습니다.

- 글쓰기(주소검색)

<img width="825" alt="글쓰기3" src="https://github.com/DongneMashil/.github/assets/127714273/bd861b67-e006-4263-841d-0cc56d4ac486">

    - 카카오 로컬 API를 이용하여 유저가 키워드나 주소를 이용해서 검색해서 해당 주소를 가져오는 기능을 구현하였습니다.

- 글쓰기(기본)

<img width="586" alt="글쓰기4" src="https://github.com/DongneMashil/.github/assets/127714273/cf1dabcc-d81d-4fa7-808c-3c6439edd402">
    
    - 태그를 선택하고, 제목, 내용을 입력하였습니다.
    - 사진및 동영상을 선택하고 사진일 경우 대표이미지 설정이 가능합니다.
    
- 글쓰기(수정)
    
<img width="586" alt="글쓰기5" src="https://github.com/DongneMashil/.github/assets/127714273/7f04d41b-bd40-4ef9-b4b3-7f09ca2951bb">

    - 상세페이지에서 수정하기 버튼을 누르면 수정하는 페이지로 이동하며, 글쓰기의 모든 부분을 수정할 수 있습니다.

</details>

<details>
<summary>내 정보 관리하기</summary>

![마이1](https://github.com/DongneMashil/.github/assets/127714273/b8d0a4ff-64e9-4de8-b72b-08ec79722482)

- 내가 쓴 게시글, 댓글, 좋아요한 게시글을 조회할 수 있습니다.
- 내 정보 수정 / 프로필 사진 수정이 가능합니다.

![마이2](https://github.com/DongneMashil/.github/assets/127714273/4434d89c-f59b-481d-ac9c-7c5ff68734fc) 2)

    - 프로필 사진 크롭 / 리사이징
        - 프로필 사진을 1/1 고정비율로 잘라서 업로드하는 기능
        - 자름과 동시에 100*100px로 축소하여 저장합니다.

</details>

<summary>산책로 자세히 보기</summary>

![상세1](https://github.com/DongneMashil/.github/assets/127714273/18245a37-8aca-4888-9d61-b132cc653176)

게시글의 내용을 확인하는 페이지입니다.

- 상세페이지 위치정보 보여주기
  현 위치와 비교해서 게시글의 위치를 보여주는 기능입니다. (km단위로 표기)

![상세2](https://github.com/DongneMashil/.github/assets/127714273/9f24d7b1-95af-4177-8713-5dfef3c67a8a)

- 사진 원본 사이즈로 보여주기
  사진을 누르면 원본 크기로 보여주고, 다운로드가 가능합니다.

![상세3](https://github.com/DongneMashil/.github/assets/127714273/1dbd7575-4aed-4120-8f08-896e5a022abe)

</details>

<details>
  
<summary>산책로 좋아요 / 댓글달기</summary>
    
- 좋아요 기능
    
    낙관적 업데이트(Optimistic Update) 를 사용하여 즉시 상태 업데이트 후 서버와 통신합니다.
    
    - 서버 응답과 예상값이 다를경우 즉시 정정 기능
    - 0.5초에 1번만 요청을 보낼수 있도록 쓰로틀링 적용
    - 상태 관리를 통한 서버 요청 중복 방지
- 댓글 쓰기 기능
    
    댓글 입력시 해당 지점까지 자동 스크롤 기능, 무한스크롤, 수정/삭제 기능이 있습니다.
    
![상세4](https://github.com/DongneMashil/.github/assets/127714273/1a61f486-af18-464f-8eb9-0b0ba3cbb21d)

</details>

<details>
  
<summary>다른 유저의 산책로 모아보기</summary>
    
- 다른 유저의 게시글 조회 하기
    
    유저 닉네임을 클릭하면 해당 유저의 게시글을 볼수 있습니다.
    
![상세6](https://github.com/DongneMashil/.github/assets/127714273/b9f8e492-1684-4086-ac62-7363f12af1e3)

    - 무한스크롤로 구현되어 있습니다.

</details>

## 🔧 사용 기술 스택

### Back-End

<img src="https://img.shields.io/badge/JAVA-orange?style=for-the-badge"> <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"> <img src="https://img.shields.io/badge/Sping Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white"> <img src="https://img.shields.io/badge/Spring Security-569A31?style=for-the-badge&logo=springsecurity&logoColor=white"> <img src="https://img.shields.io/badge/AWS S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white"> <img src="https://img.shields.io/badge/AWS EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white"> <img src="https://img.shields.io/badge/AWS RDS-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white"> <img src="https://img.shields.io/badge/Github actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white"> <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"> <img src="https://img.shields.io/badge/nginx-FCC624?style=for-the-badge&logo=nginx&logoColor=black"> 
<img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white"> <img src="https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white">

## ⚙️ 서비스 아키텍처

![architecture](https://github.com/DongneMashil/dongnemashil-fe/assets/86649284/14746f92-409d-4dd4-b75d-426cc18a6288)

## 💭 주요 기술적 의사결정
### Back-End

| 기술           | 도입 이유                                                                                                                                                                                                                                                                                                                                                                                                                |
| -------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Github Action  | - Jenkins와 고민하던 중 우리 프로젝트에는 Docker 사용이 리소스 낭비라 판단하여 비교적 간단하고 CICD 작업에 수월한 Github Action 적용, Code Deploy와 같이 사용하여 효율적인 배포 및 관리</br> - 코드를 push하거나 pull request를 생성할때 자동으로 워크플로우를 실행하여 생산성을 높여주고 비밀 키, 토큰 등 민감한 정보를 안전하게 저장하고 워크플로우에서 사용할 수 잇게 해주는 secrets 관리 기능을 제공해서 보안에 좋음 |
| S3             | - EC2 의 경우 t2 를 사용하고 있고, 스토리지가 제한적이기 때문에 파일을 저장하는 행위는 별도의 저장공간이 필요함. </br> - Amazon S3는 대용량 파일 및 객체 저장에 최적화되어 있으며, 확장성과 내구성을 제공하여 데이터를 안전하게 저장하고 관리할 수 있는 환경을 제공함.</br> - 데이터 손실이 적고, 백업 및 복원이 쉽다.</br> - 애플리케이션 배포를 쉽게 도와줌.                                                           |
| Swagger        | - API를 설계, 빌드, 문서화 및 사용하기 위한 오픈소스 프레임워크.</br> - API의 스펙을 프로젝트 코드와 연동하여 문서거 자동으로 업데이트 되므로 빠른 개발이 가능함.</br> - API의 명세가 명확하게 문서화 되어 있어 개발 팀 내부에서 협업을 용이하게 함.                                                                                                                                                                     |
| SpringSecurity | - 사용자 인증, 권한 부여, 접근 제어 등의 보안 기능을 쉽게 구현할 수 있게 도와줌.</br> - 엑세스 제어를 통해 특정 URL 의 대한 접근만 허용할 수 있음.                                                                                                                                                                                                                                                                       |
| MySQL          | - RDBMS(관계형데이터베이스), 데이터의 일관성과 정확성을 보장하고 복잡한 쿼리 처리에 용이함. </br> - 안정적인 트랜잭션을 제공하고 표준화 된 쿼리 언어인 SQL 사용이 가능하단 장점.                                                                                                                                                                                                                                         |
| NGINX          | - 많은 트래픽환경에서도 안정적으로 작동이여서 높은 동시 연결 처리능력을 가지고 있음.</br> - 적은 메모리를 사용해서 많은 연결처리를 할 수 있는 로드밸런서 역할도 수행 가능.</br> - 설정 파일을 통해 세부적인 조정이 가능하고 다양한 사용 사례에 적용 가능.                                                                                                                                                                |
| Route53        | 전세계 분산 위치에 있는 DNS 서버로 구성 되어 있어 서비스 가용성 신뢰성이 뛰어나며, 트래픽이 증가하더라도 자동으로 스케일링 되므로 운영 오버헤드 성능을 유지 가능. 또한 보안적으로도 다양한 보안기능을 제공하여 안전한 서비스 운용 가능.                                                                                                                                                                                  |
| Thumbnailator  | - 기술적 대안으로는 Marvin,imgScalr,Thumbnailator</br> - Marvin의 경우 압축 비율이 우수하다는 reference를 확인했으나 처리 속도가 빠르고 화질이 깨지지 않는 imgScalr,Thumbnailator을 사용하는 것이 낫다고 최종 판단했음.</br> - 두개의 라이브러리를 테스트 했을 때 속도,화질은 비슷했으나 용량을 조금 더 줄여주는 Thumbnailator를 채택을 함.                                                                              |
| Redis          | - 레디스의 특성 중 하나인 인메모리 저장 방식으로 기존 mysql 보다 빠른 조회가 가능.</br> - 사용자가 늘어나고 , 데이터의 양이 많아지면 조회 속도가 떨어지기 때문에 레디스에 미리 캐싱하여 보다 빠른 데이터를 제공할수 있음.                                                                                                                                                                                                |
| JPQL           | - 편의성과 데이터베이스의 중립성을 고려해 현재 프로젝트에서 적합하다는 판단을 했음.</br> - 특히 반경검색을 구현할 때 Native Query을 사용했었는데 JPQL과 비교했을때 코드의 복잡성이 비교적 높아 JPQL 채택.                                                                                                                                                                                                                |

## 📊API 명세

[동네마실 Swagger에서 확인해보기](https://dongnemasila.shop/swagger-ui/index.html#/)

## 📐 ERD

![image](https://github.com/DongneMashil/.github/assets/128367271/4fad8663-81bc-4f7c-a2b7-00936d3c529d)

## ⚠️트러블슈팅

[동네마실 브로셔 확인하기](https://cosmic-soybean-5d3.notion.site/fb2409ce476d49ca88a67b68be485b4f?pvs=4)

<details>

---

<summary>Redis Sentinel 세팅</summary>
  
**이슈 내용**

sentinel세팅 시 senitnel.conf에서 senitnel monitor mymaster 경로가 계속 replica로 잡히는 현상 → master로 설정을 해도 replica으로 변경이 되는 문제가 발생

**해결을 위한 시도**

1. chmod 권한 문제인지 권한을 777로 변경했지만 해결이 안됨
2. replica의 sentinel.conf설정이 replica 자신을 바라보는 확인을 해봤지만 replica senitnel.conf는 master를 바라봄
3. 구글링

**해결 방안**

ubuntu 환경이 아닌 root 환경에서 설정을 하니 해결

<img width="548" alt="rediserror" src="https://github.com/DongneMashil/.github/assets/127714273/e4bced4f-ed6f-4e55-85e1-5f18ec370e97">

</details>

<details>
<summary>시큐리티 에러 핸들링</summary>
  
  **문제 상황**

시큐리티 에러 발생시 핸들링이 안되던 문제

**문제 원인 파악**

시큐리티 에러는 필터체인에서 생기는 문제이기 때문에 controller 에서 해당 Exception 처리가 불가.

**해결 방안**

`SecurityFilterChain` 의 `http.exceptionHandling()` 을 통해 인증과정에서 생기는 에러를 설정할 수 있다.

![security1](https://github.com/DongneMashil/.github/assets/127714273/86c839c4-ffb5-49e8-b3e9-94955fcc3f5d)

임의로 401 에러를 주었으나 여전히 프론트에서 어떤 내용의 에러인지 식별이 불가능.

![security2](https://github.com/DongneMashil/.github/assets/127714273/503db7c1-0f26-4e65-b934-1d74c01199d1)
![security3](https://github.com/DongneMashil/.github/assets/127714273/c55ab1c0-a21a-4d2d-933e-5cce0bbcab5d)

authenticationEntryPoint 를 재설정 하여
인증과정에서 에러 발생시 request 에 setAttribute 를 통해 해당 에러를 전달 받아 처리를 하였다.

---

</details>

<details>

---

<summary>AWS S3 버킷에서 파일 삭제</summary>

**문제 상황**

이미지 동영상을 삭제시 DB 테이블에서는 삭제가 되지만 S3 버켓에서는 삭제가 되지않고 유지

```java
//수정 전 코드
public void delete(String fileUrl) {

        String keyName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        amazonS3.deleteObject(bucket, keyName);
    }

```

**해결 방안**

![aws1](https://github.com/DongneMashil/.github/assets/127714273/a5a1f045-a49f-4e3a-a7a4-51877931c4fb)

원본이미지명

![aws2](https://github.com/DongneMashil/.github/assets/127714273/cb973c88-0d86-4638-923b-7dd6527cb946)

업로드 후 버킷에서 확인할 수 있었음.
객체개요를 확인 해보니 인코딩 되서 fileUrl → 파일명 이 인코딩 되서 들어가는 걸 확인할 수 있었음
S3객체를 디코딩해서 디비와 맞으면 삭제 → 디코딩 하는 코드를 추가 해서 해결할 수 있었음 🎉

---

</details>


## 📚스터디

https://cosmic-soybean-5d3.notion.site/74d52937736e4e8db95d96a8a7ed638a?pvs=4


