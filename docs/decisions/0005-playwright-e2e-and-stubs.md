# ADR-0005: Playwright E2E와 API 스텁

- 상태: 채택/구현 완료
- 결정: `web/`에 Playwright 도입, Next 미들웨어로 `/__test__/news` / `/__test__/ui/app` 스텁 제공.
- 근거: 백엔드 없이도 UI 흐름/범위 토글/빈 상태/오류 폴백을 안정적으로 검증.
