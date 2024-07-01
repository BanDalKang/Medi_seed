# 💊 폐의약품 수거함 위치 서비스 앱 MediSeed
![그래픽_최종](https://github.com/BanDalKang/Medi_seed/assets/77070839/ff9913e4-56da-4ec4-a232-44d0ab1526ea)

### 프로젝트 일정
2024-05-27 ~ 2024-07-05

### URL
- 배포 링크 :
- 시연영상 : https://www.youtube.com/watch?v=AlccWqGRH8I

<br>

## 프로젝트 소개
### "MediSeed"는 폐의약품 수거함 위치 제공을 통해 올바른 수거 문화를 지향하는 앱입니다.

#### 다음과 같은 기능들이 있어요!
> 🗺️ 사용자의 주변에 있는 폐의약품 수거함 위치를 마커로 표시합니다. (현재 서비스 지역: 대전광역시 서구)

> 💊 마커 클릭 시, 해당 수거함의 정보를 표시합니다.

> 🌱 새싹이 페이지에서 약 수거를 완료해서 새싹 캐릭터를 키울 수 있습니다.

<br>

## 팀원 구성

<div>

| **강현정** | **장혜정** |  **서해윤** | **박민수** |
| :------: |  :------: |:------:| :------: |
| [<img src="https://github.com/BanDalKang/Medi_seed/assets/77070839/107e9450-3e92-4b6f-ada9-351e044de03c" height=150 width=150> <br/> @BanDalKang](https://github.com/BanDalKang) | [<img src="https://github.com/BanDalKang/Medi_seed/assets/159236003/a18d5c68-1d96-456a-aab1-b8536d664a31" height=150 width=150> <br/> @hyezg](https://github.com/hyezg) | [<img src="https://github.com/BanDalKang/Medi_seed/assets/159236003/734615ae-7dcf-4b63-a98e-683f724bf3b9" alt="스크린샷 2024-06-14 160738" height=150 width=150> <br/> @SeoHeaYun](https://github.com/SeoHeaYun) | [<img src="https://github.com/BanDalKang/Medi_seed/assets/159236003/ce7543a0-2767-4fb7-8e2f-a942195b06d8" height=150 width=150> <br/> @eddy-PMS](https://github.com/eddy-PMS) |

</div>

<br>

## 기술 스택 

| 분류 | 이름 |
| --- | --- |
| Architecture | <img src="https://img.shields.io/badge/Repository Pattern-FDECC8"> <img src="https://img.shields.io/badge/MVVM-FDECC8"> |
| Jetpack | <img src="https://img.shields.io/badge/LiveData-F7DF1E"> <img src="https://img.shields.io/badge/LifeCycle-F7DF1E"> <img src="https://img.shields.io/badge/ViewModel-F7DF1E"> <img src="https://img.shields.io/badge/ViewBinding-F7DF1E">|
| Design Pattern | <img src="https://img.shields.io/badge/DI:Hilt-6DB33F"> <img src="https://img.shields.io/badge/FactoryMethod-6DB33F"> <img src="https://img.shields.io/badge/Singleton-6DB33F"> <img src="https://img.shields.io/badge/Delegate-6DB33F"> <img src="https://img.shields.io/badge/Observer-6DB33F"> | 
| UI | <img src="https://img.shields.io/badge/Fragment-4169E1">  <img src="https://img.shields.io/badge/RecyclerViewAdapter-4169E1">  <img src="https://img.shields.io/badge/XML-4169E1">  <img src="https://img.shields.io/badge/MaterialDesign-4169E1">  <img src="https://img.shields.io/badge/ViewPager2-4169E1">  <img src="https://img.shields.io/badge/BottomSheet-4169E1">  <img src="https://img.shields.io/badge/Splash-4169E1">|
| 네트워크 통신 | <img src="https://img.shields.io/badge/Retrofit2-E0234E"> <img src="https://img.shields.io/badge/Naver Map-E0234E"> <img src="https://img.shields.io/badge/GeoCode-E0234E"> <img src="https://img.shields.io/badge/공공데이터포털-E0234E">|
| 데이터 처리 | <img src="https://img.shields.io/badge/SharedPreference-00DC82"> <img src="https://img.shields.io/badge/Firebase:Realtime DB-00DC82"> <img src="https://img.shields.io/badge/Parcelize-00DC82"> |
| 비동기 처리 | <img src="https://img.shields.io/badge/Coroutine-326CE5"> <img src="https://img.shields.io/badge/Dispatcher-326CE5"> |


<br>

## 와이어프레임

<div>
  <img src="https://github.com/BanDalKang/Medi_seed/assets/77070839/471cd5c5-a0c1-41d7-87e9-3d14b855b10c" width="50%">
</div>

<br>

## 주요 기능

### 🏠 [홈 화면]
- ${\textsf{\color{green}네이버 지도 API}}$를 기반으로, 폐의약품 수거함 위치를 마커로 표시.
- 사용자의 현재 위경도 좌표를 실시간으로 획득하고, 그 위치에 해당하는 지역의 API를 호출. (공공데이터포털 폐의약품 수거함 위치)
- 검색창에 검색값을 입력하면, 검색어에 맞는 수거함의 정보를 알고리즘에 따라 나열. (거리순)

| 홈 화면 |
|----------|
|<img src="https://github.com/BanDalKang/Medi_seed/assets/159236003/a1b0dabd-3b22-435e-8fd9-0d96adff0922" alt="home (online-video-cutter com) (3)" style="width:300px;">|

<br>

### 🗃️ [보관함 화면]
- 새싹이 화면에서 ${\textsf{\color{green}약 버리기}}$ 또는 ${\textsf{\color{green}공유하기}}$ 버튼을 누를 경우, 보관함에서 총 횟수 눌렀는지 확인 가능.
- 홈 화면에서 마커를 클릭하면 수거함 정보를 표시하는 바텀시트가 나타나고, 이때 좋아요 버튼을 누를 경우 보관함에서 확인 가능.

| 보관함 화면 |
|----------|
|<img src="https://github.com/BanDalKang/Medi_seed/assets/77070839/2469095b-8134-4c49-b4e8-abffe23e8aa6" width="300px">|

<br>

### 🌳 [새싹이 화면]
- ${\textsf{\color{green}약 버리기}}$, ${\textsf{\color{green}공유하기}}$ 버튼으로 새싹이 캐릭터 키우기. (약 버리기: +20%, 공유하기: +10%)
- 레벨 높아짐에 따라 새싹이가 성장.
- 1 ~ 5레벨까지 있으며 5레벨이 넘어가면 나무가 1그루 추가됨.
- 약 버리기 버튼은 폐의약품을 수거함에 버리고 체크하는 기능으로 수거함 인근에서 일일 1회만 클릭 가능.
- 약 버리기를 하면 바텀시트에서 해당 수거함의 총 수거 횟수 증가.
- 공유하기 버튼은 앱을 공유하는 기능으로 일일 3회만 클릭 가능.

| 레벨업 기능 | 약 버리기 기능 |
|----------|----------|
|<img src="https://github.com/BanDalKang/Medi_seed/assets/77070839/afe3b7ec-da72-4a44-b646-66da99c70aeb" width="300px">|<img src="https://github.com/BanDalKang/Medi_seed/assets/77070839/eaf1c6d4-f751-4110-8251-885195705c56" width="300px">|

<br>

## 트러블 슈팅 및 기술적 의사결정

### 🚀 의사결정: API 호출 vs CSV 파일
**ISSUE**
- 폐의약품 수거함 관련 데이터들을 CSV파일로 로컬에서 저장하여 관리할 것인가, API로 호출하여 받아올 것인가?

**SOLVE**
- CSV 파일을 활용할 경우, API를 지역별로 일일이 호출할 필요 없이 전국의 수거함 정보들을 파일에 한 번에 관리하기 때문에, 지역관리와 코드관리가 쉽다는 장점이 있음.
- 그러나, 사용자 입장에서는 최신 데이터를 제공받을 수 없고, 지역을 전국적으로 확장하게 된다면 그만큼 앱이 무거워지게 된다는 단점이 존재.
- 이에비해, API를 호출할 경우, 잦은 호출과 그에 따른 코드 관리가 번거롭더라도, 사용자에게 양질의 최신 정보를 반영해 줄 수 있음. <br>
&nbsp;&nbsp;&nbsp;&nbsp;-> API를 호출하여 데이터를 받아오기로 결정

### 🚀 API 연속 호출에 의한 비효율성 해결 ###
**ISSUE**
- 공공 데이터 포털에서는 총 84개로 지역이 세분화 되어 있고, 마커를 표시해주기 위해서는 각 지역들의 위경도 좌표가 필요함.
- 그러나, 위경도 지원이 되지 않는 지역이 절반 이상이었기 때문에, 도로명 주소를 위경도 좌표로 반환하도록 구현하기 위해 네이버에서 지원하는 GeoCode API를 활용하기로 결정.
- 하지만, 한 지역당 수거함이 100개라고 가정했을 때, 84개의 지역에 대한 호출과 더불어 각각의 수거함에 대해 위경도 좌표를 호출하도록 코드를 짠다면, 수없이 많은 API호출로 인해 앱 성능에 큰 영향을 미칠 것으로 예상.
- 사용자가 A지역에 거주하고 있는데도, 그 이외의 B,C,D 지역의 데이터들을 호출하게 된다면 매우 비효율적.

**SOLVE** <br>
&nbsp;&nbsp;&nbsp;&nbsp;-> 각 지역마다 보이지 않는 가상의 바운더리를 만들고, 하버사인 공식(Harversine Formula)을 통해 사용자가 해당 바운더리에 진입하였을 때, 그 바운더리에 해당하는 지역의 데이터들만 호출되도록 구현.

### 🚀 검색 정렬 알고리즘 ###
**ISSUE**
- 초기에는 검색창의 약국이름 정렬 기준을 '첫글자'만 포함되도록 구현하였고, 그러다보니 아래와 같은 제한사항이 발생.<br>
ex) '도마태평양약국'을 검색하고 싶으나, 사용자가 '도마'라는 부분이 기억이 나지않아 '태평양약국'이라고만 검색할 경우, 검색창에 '도마태평양약국'이 제시되지 않음.

**SOLVE**
<br>
&nbsp;&nbsp;&nbsp;&nbsp;-> 정렬 기준에서 '음절'도 포함하도록 구현함으로써, '태평양약국'이라고만 검색해도 '도마태평양약국'이 제시되도록 하였음.

### 🚀 HILT- @Qualifier Annotation ###
**ISSUE**
- Retrofit Network를 Hilt를 적용할 때, 서로 다른 baseUrl이 2개 이상일 경우, 상황에 따라 다른 의존성을 주입할 수 있도록 컴파일러에게 알려야 할 필요가 있었음.

**SOLVE**
<br>
&nbsp;&nbsp;&nbsp;&nbsp;-> 각 의존성을 구별해줄 수 있도록, @Qualifier annotation과 함께 annoation class를 만들어 준 후, @Provides 함수의 파라미터에 해당 annotation 적용하여 문제해결.
