Today
==============
 하루를 취침 시간부터 기상 시간을 제외하고, 일정한 간격의 시간 주기(30분, 1시간 ..)마다 텍스트 입력 팝업창을 띄워주고, 팝업이 뜰 때마다 현재 내가 하고 있던 일을 입력하여, 하루가 끝나면 오늘 하루 종일 어떤 일을 했는지 객관적으로 볼 수 있는 안드로이드 어플리케이션

Requirement
------------------------
> **Note:**

> - 하루를 기록 할 수 있도록 구현
> - 과거의 하루를 찾아서 볼 수 있도록 구현
> - 정해진 시간(30분, 1시간 등등..)마다 알람이 울리도록 설정
> - 시간 간격을 조절 할 수 있도록 설정
> - 텍스트를 입력 할 수 있는 팝업창이 뜨고 텍스트를 입력함으로서 기록
> - 방해 금지 모드
> - 기상 시간, 취침 시간 설정

Development Progress
------------------------
> **Note:**

> - ButterKnife 적용
> - SQLite ProtoType 구현 완료
> 시간과 업무를 데이터베이스에 저장
> - AlarmManger 기본적인 동작 구현
> - SettingFragment 구현 및 작동 확인
> - Text 팝업 ProtoType 구현

TODO:
------------------------
> **Note:**

> - AlarmManger SystemClock에서 RealTime Clock으로 교체
> - 하고 있던 일을 등록한 시간과 해당하는 일의 정보를 관리하는 Model Class 생성
> > **!** 몇 월 며칠을 parameter로 받아서 처리를 할 수 있도록
> > **!** 하루를 DB에 저장 및 관리하는 역할
> 
> - View에 뿌려지는 데이터가 하루 단위로 뿌려지도록 구현
> - TestCase 작성

### Libray

> - Butterknife : 안드로이드의 View와 Activity를 보다 쉽게 연결해주고, 코드의 반복을 줄여주는 라이브러리
> http://jakewharton.github.io/butterknife/

### Development Environment

> - Language : JAVA
> - IDE : Android Studio
> - DB : SQLite

