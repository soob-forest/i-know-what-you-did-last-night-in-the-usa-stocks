# 테스트 가이드

## 범용 원칙
- 새 기능엔 테스트를 작성하고 실행 결과를 확인합니다.
- 단위 → 슬라이스 → 통합 순으로 좁게 시작해 점차 넓힙니다.

## Java(Spring)
- 실행: `cd api && ./gradlew test`
- 주요 테스트
  - `api/news` 패키지: 서비스/컨트롤러 테스트(JUnit5, Mockito, WebMvcTest)
  - `api/slack`: 슬랙 API는 Mockito로 모킹하여 네트워크/DB 의존 제거
  - `api/ui`: `/ui/app` 스키마 구조 검증(WebMvcTest)

## Node(Collector, Next.js)
- Collector(Jest): `cd collector && npm test` (또는 pnpm)
- Next.js(Playwright): `cd web && npx playwright install && npm run e2e`
  - 미들웨어 스텁으로 백엔드 없이도 시나리오 검증 가능

## 커버리지/범위 제안
- 서비스 로직: 정상/엣지 케이스(빈 데이터, 잘못된 입력)
- 컨트롤러: 2xx/4xx 경계, CORS 헤더, 스키마 구조
- E2E: 기본 렌더, 토글/검색, 빈 상태/오류 폴백, SDUI 모드
