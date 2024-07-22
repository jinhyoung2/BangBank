🏦 BANGBANK
-

쉽게 사용할 수 있는 계좌 프로젝트 입니다
  
프로젝트 기능 및 설계
-
- **계좌 검색 기능**
    - 사용자는 계좌 번호로 자신의 계좌를 조회할 수 있습니다.
    - 계좌 잔액, 계좌 개설일, 최근 거래 내역 등을 확인할 수 있습니다.

- **송금 기능 및 송금 이력 조회**
    - 사용자는 자신의 계좌에서 다른 계좌로 송금할 수 있습니다.
    - 송금 이력은 조회 가능하며, 날짜, 금액, 수취인 등의 정보가 포함됩니다.

- **계좌 관리**
    - **계좌 생성**: 신규 계좌(예금,적금 선택 가능)를 개설할 수 있습니다.
    - **계좌 생성**: 계좌 생성 요청을 하면 랜덤으로 14자리 숫자의 계좌번호가 생성됩니다.(중복X)
    - **적금 계좌**: 계좌 생성시 DEPOSIT으로 요청하면 적금 계좌가 개설됩니다. (연이자 5%) 이자는 매월 1일에 자동으로 계산 및 적용됩니다.
    - **계좌 삭제**: 계좌를 삭제할 수 있습니다.
    - **금액 인출**: 계좌에서 돈을 인출할 수 있습니다.
    - **금액 입금**: 계좌에 돈을 입금할 수 있습니다.


- **로그인/로그아웃에 따른 계좌 접근 허가 기능**
    - 사용자는 로그인 후에만 자신의 계좌에 접근할 수 있습니다.



    
ERD
-
![bangBankreal](https://github.com/user-attachments/assets/9504c878-0746-4a0f-90ef-89a515103cb3)


