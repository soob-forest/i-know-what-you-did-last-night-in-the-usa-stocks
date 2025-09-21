# ADR-0008: SDUI 도입과 렌더러

- 상태: 채택/구현 진행
- 결정: 서버가 UIBlock 스키마(`/ui/app`)를 내려주고, 클라이언트는 레지스트리 기반 `Renderer`로 렌더.
- 근거: 서버에서 레이아웃/구성 즉시 변경, 실험/AB에 유리. 안전한 화이트리스트 렌더.
- 다음: AppUiService 분리, Props Guard(서버/클라) 강화, EmptyState 추가, 스키마 버저닝.
