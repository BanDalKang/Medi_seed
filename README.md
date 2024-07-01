# 💊 폐의약품 수거함 위치 서비스 앱 MediSeed
![그래픽_최종](https://github.com/BanDalKang/Medi_seed/assets/77070839/ff9913e4-56da-4ec4-a232-44d0ab1526ea)

- 전체 개발 기간 : 2024-05-27 ~ 2024-07-05
- 배포 URL :
- 시연영상 URL: https://www.youtube.com/watch?v=AlccWqGRH8I
  <br>

## 프로젝트 소개
### "MediSeed"는 폐의약품 수거함 위치 제공을 통해 올바른 수거 문화를 지향하는 앱입니다.

#### 다음과 같은 기능들이 있어요!

> 🗺️ 사용자의 주변에 있는 폐의약품 수거함 위치를 마커로 표시합니다. (현재 서비스 지역: 대전광역시)
>

> 💊 마커 클릭 시, 해당 수거함의 정보를 표시합니다. (거리/위치/연락처/수거 횟수)
>

> 🌱 수거를 완료한 후, 새싹 페이지에서 나만의 캐릭터에게 먹이를 줄 수 있습니다. 
>

<br>

## 팀원 구성

<div align="center">

| **강현정** | **장혜정** |  **서해윤** | **박민수** |
| :------: |  :------: |:------:| :------: |
| [<img src="https://github.com/BanDalKang/Medi_seed/assets/77070839/107e9450-3e92-4b6f-ada9-351e044de03c" height=150 width=150> <br/> @BanDalKang](https://github.com/BanDalKang) | [<img src="https://github.com/BanDalKang/Medi_seed/assets/159236003/a18d5c68-1d96-456a-aab1-b8536d664a31" height=150 width=150> <br/> @hyezg](https://github.com/hyezg) | [<img src="https://github.com/BanDalKang/Medi_seed/assets/159236003/734615ae-7dcf-4b63-a98e-683f724bf3b9" alt="스크린샷 2024-06-14 160738" height=150 width=150> <br/> @SeoHeaYun](https://github.com/SeoHeaYun) | [<img src="https://github.com/BanDalKang/Medi_seed/assets/159236003/ce7543a0-2767-4fb7-8e2f-a942195b06d8" height=150 width=150> <br/> @eddy-PMS](https://github.com/eddy-PMS) |

</div>

<br>

## 기술 스택 

| 분류 | 이름 |
| --- | --- |
| Architecture | <img src="https://img.shields.io/badge/Repository Pattern-FDECC8"> <img src="https://img.shields.io/badge/MVVM-FDECC8"> |
| Jetpack | <img src="https://img.shields.io/badge/LiveData-FCC624"> <img src="https://img.shields.io/badge/LifeCycle-FCC624"> <img src="https://img.shields.io/badge/ViewModel-FCC624"> <img src="https://img.shields.io/badge/ViewBinding-FCC624">|
| Design Pattern | <img src="https://img.shields.io/badge/DI:Hilt-6DB33F"> <img src="https://img.shields.io/badge/FactoryMethod-6DB33F"> <img src="https://img.shields.io/badge/Singleton-6DB33F"> <img src="https://img.shields.io/badge/Delegate-6DB33F"> <img src="https://img.shields.io/badge/Observer-6DB33F"> | 
| UI | <img src="https://img.shields.io/badge/Fragment-4169E1">  <img src="https://img.shields.io/badge/RecyclerViewAdapter-4169E1">  <img src="https://img.shields.io/badge/XML-4169E1">  <img src="https://img.shields.io/badge/MaterialDesign-4169E1">  <img src="https://img.shields.io/badge/ViewPager2-4169E1">  <img src="https://img.shields.io/badge/BottomSheet-4169E1">  <img src="https://img.shields.io/badge/Splash-4169E1">|
| 네트워크 통신 | <img src="https://img.shields.io/badge/Retrofit2-E0234E"> <img src="https://img.shields.io/badge/Naver Map-E0234E"> <img src="https://img.shields.io/badge/GeoCode-E0234E"> <img src="https://img.shields.io/badge/공공데이터포털-E0234E">|
| 데이터 처리 | <img src="https://img.shields.io/badge/SharedPreference-00DC82"> <img src="https://img.shields.io/badge/Firebase:Realtime DB-00DC82"> <img src="https://img.shields.io/badge/Parcelize-00DC82"> |
| 비동기 처리 | <img src="https://img.shields.io/badge/Coroutine-326CE5"> <img src="https://img.shields.io/badge/Dispatcher-326CE5"> |


<br>

## 주요 기능

### 🏠 [홈 화면]
- 네이버 지도 API를 기반으로, 폐의약품 수거함 위치를 마커로 표시합니다.
- 사용자의 현재 위경도 좌표를 실시간으로 획득하고, 그 위치에 해당하는 지역의 API를 호출합니다.(공공데이터포털 폐의약품 수거함 위치)
- 검색창에 검색값을 입력하면, 검색어에 맞는 수거함의 정보를 알고리즘에 따라 나열합니다.(시간순)

| 홈 화면 |
|----------|
|<img src="https://github.com/BanDalKang/Medi_seed/assets/159236003/a1b0dabd-3b22-435e-8fd9-0d96adff0922" alt="home (online-video-cutter com) (3)" style="width:300px;">|

<br>

### 🗃️ [보관함 화면]
- 새싹이 화면에서 “약 버리기” 또는 “공유하기” 버튼을 누를 경우, 보관함에서 총 횟수 눌렀는지 확인할 수 있습니다.
- 홈 화면에서 마커를 클릭하면 수거함 정보를 표시하는 바텀시트가 나타나고, 이때 좋아요 버튼을 누를 경우 보관함에서 확인할 수 있습니다.

| 보관함 화면 |
|----------|
|<img src="https://github.com/BanDalKang/Medi_seed/assets/77070839/2469095b-8134-4c49-b4e8-abffe23e8aa6" width="300px">|

<br>

### 🌳 [새싹이 화면]
- 약 버리기, 공유하기 버튼으로 새싹이 캐릭터를 키울 수 있습니다. (약 버리기: 20%, 공유하기: 10%)
- 레벨 높아짐에 따라 새싹이가 자랍니다.
- 1 ~ 5레벨까지 있으며 5레벨이 넘어가면 나무가 1그루 추가 됩니다.
- 약 버리기 버튼은 폐의약품을 수거함에 버리고 체크하는 기능으로 수거함 인근에서 일일 1회만 클릭 가능합니다.
- 약 버리기를 하면 바텀시트에서 해당 수거함의 총 수거 횟수가 +1 됩니다.
- 공유하기 버튼은 앱을 공유하는 기능으로 일일 3회만 클릭 가능합니다.

| 레벨업 기능 | 약 버리기 기능 |
|----------|----------|
|<img src="https://github.com/BanDalKang/Medi_seed/assets/77070839/afe3b7ec-da72-4a44-b646-66da99c70aeb" width="300px">|<img src="https://github.com/BanDalKang/Medi_seed/assets/77070839/eaf1c6d4-f751-4110-8251-885195705c56" width="300px">|

<br>

## 와이어프레임

<div align="center">
  <img src="https://github.com/BanDalKang/Medi_seed/assets/77070839/471cd5c5-a0c1-41d7-87e9-3d14b855b10c" width="50%">
</div>
