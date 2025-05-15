# SPRING PLUS
레벨 13. 대용량 데이터 처리 실험

### DB에 100만개의 대용량 데이터 삽입
![1](https://github.com/user-attachments/assets/743bb86b-3a7c-4a02-a75b-ce9e75b2e1e1)

실제로 JVM heap memory가 못버텨서 (기존 700mb를 3500mb로 늘림) 100만개까진 들어가지 않았고 위와 같이 34만개만 집어넣고 진행

![2](https://github.com/user-attachments/assets/b0647ec9-f6dc-44ed-86b3-eff8bfc4cff4)

위와 같이 실제 있는 데이터로 진행


![5](https://github.com/user-attachments/assets/3e62979f-6594-40d4-b37d-b49149f7b7c2)

유저를 닉네임 기준으로 목록검색을 하는 API에서 Page vs Slice 중 뭐를 사용하는게 더 빠를까에 포커스를 맞춤

애초에 Slice가 더 빠르다는 사실은 알고 있었으나 직접, 얼마나 차이나는지 확인 해보고 싶었음


### Page 시간측정 
![4](https://github.com/user-attachments/assets/7e372970-94d8-442c-9b7f-37c0960c36df)

테스트 코드 시작부터 성공까지 = 293ms


### Slice 시간측정
![3](https://github.com/user-attachments/assets/90b8ad2a-14ac-45a5-8a0f-c75f2cd99180)

테스트 코드 시작부터 성공까지 = 232ms


| API 반환타입 | Page  | Slice |
|----------|-------|-------|
| 호출시간     | 293ms | 232ms |

계산해 보면 Slice는 Page 보다 약 20.8% 더 빠르다
물론 Page나 Slice면서 단일 객체만 조회하기 때문에 더 정확하게 실험하려면 다수의 결과가 나오는 테스트를 짜야하지만
닉네임이 정확하게 일치해야 검색되게끔 조건이 걸려 있어서 이렇게 할 수 밖에 없었다.

성능을 끌어올리기 위해 쿼리 DSL의 Dto로 바로 필요한 필드만 반환하는 Projection을 사용했으나 이는 Page와 Slice 둘다 적용 되어 있기 때문에
따로 이미지를 올리진 않았습니다.
