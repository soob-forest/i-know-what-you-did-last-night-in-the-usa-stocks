# ADR-0009: SDUI 서비스 분리와 Props Guard

- 상태: 채택/구현 진행
- 결정: `AppUiService`로 스키마 조립을 분리하고, 블록별 Props를 서버/클라 양쪽에서 검증한다.
- 서버 검증: Toolbar(range,q), NewsCard(news{stock{name,ticker},summary,date,links{title,url,source?}}) 유효성 점검. 비어있으면 EmptyState 블록 삽입. 응답에 `version: v1` 추가.
- 클라 검증: 레지스트리에서 타입 가드 후 렌더, 미지정/미지원 블록은 무시 또는 안전 폴백.
- 근거: 신뢰성/보안/확장성(AB 테스트, 버저닝) 확보.
