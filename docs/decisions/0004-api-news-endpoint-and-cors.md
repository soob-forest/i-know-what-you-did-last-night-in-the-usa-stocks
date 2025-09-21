# ADR-0004: /news 엔드포인트 및 CORS

- 상태: 채택/구현 완료
- 결정: `GET /news?range=overnight|today&tickers=...` 도입. `TEST_USER_ID` 구독 기반 기본 동작.
- CORS: `web.cors.origins`(기본 http://localhost:3000) 허용.
- 근거: 대시보드 SSR에서 안정적 데이터 접근, 프런트-백 분리 개발.
