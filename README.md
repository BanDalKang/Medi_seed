# 💊 폐의약품 수거함 위치 서비스 앱 MediSeed
이미지 넣기+ 주요 기능 설명 
- 전체 개발 기간 : 2024-05-27 ~ 2024-07-05
- 배포 URL :
- 시연영상 URL: https://www.youtube.com/watch?v=AlccWqGRH8I 
<br>

## 프로젝트 소개

- 사용자 위치에서 가장 가까운 폐의약품 수거함 위치를 알려드립니다. (현재 서비스 지역: 대전광역시)
- 사용자 위치 정보를 통해 근처의 폐의약품 수거함을 마커로 표시합니다.
- 마커 클릭 시, 해당 수거함의 정보를 바텀 시트로 표시합니다.
- 새싹탭에서 새싹 캐릭터를 키울 수 있습니다.

<br>

## 팀원 구성

<div align="center">

| **강현정** | **장혜정** |                                                                  **서해윤**                                                                   | **박민수** |
| :------: |  :------: |:------------------------------------------------------------------------------------------------------------------------------------------:| :------: |
| [<img src="https://github.com/BanDalKang/Medi_seed/assets/77070839/107e9450-3e92-4b6f-ada9-351e044de03c" height=150 width=150> <br/> @BanDalKang](https://github.com/BanDalKang) | [<img src="https://github.com/BanDalKang/Medi_seed/assets/77070839/33912621-d0bd-49d7-9cf1-583f5d9c941a" height=150 width=150> <br/> @hyezg](https://github.com/hyezg) | [<img src="https://github.com/BanDalKang/Medi_seed/assets/77070839/" height=150 width=150> <br/> @SeoHeaYun](https://github.com/SeoHeaYun) | [<img src="https:" height=150 width=150> <br/> @eddy-PMS](https://github.com/eddy-PMS) |

</div>

<br>

## 주요 기능

### 🏠 [홈 화면]
- 네이버 지도 API를 기반으로, 폐의약품 수거함 위치를 마커로 표시합니다.
- 사용자의 현재 위경도 좌표를 실시간으로 획득하고, 그 위치에 해당하는 지역의 API를 호출합니다.(공공데이터포털 폐의약품 수거함 위치)
- 검색창에 검색값을 입력하면, 검색어에 맞는 수거함의 정보를 알고리즘에 따라 나열합니다.(시간순)

| 홈 화면 |
|----------|
|![home (online-video-cutter com) (2)](https://github.com/BanDalKang/Medi_seed/assets/159236003/45e8b3da-448d-4da1-b529-d93779764564)|



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

이미지




































