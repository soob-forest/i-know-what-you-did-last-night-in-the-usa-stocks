# ADR-0001: 웹 아키텍처 (Next.js + Spring Boot)

- 상태: 채택
- 결정: 프론트엔드는 Next.js(App Router), 백엔드는 Spring Boot(API) + Collector(NestJS) 유지.
- 근거: SSR/ISR로 빠른 렌더, 기존 JPA/Batch/Slack 통합 활용, 점진적 확장 용이.
- 영향: API는 `/news`, `/ui/app` 등 BFF 역할 일부 수행. 프론트는 `NEXT_PUBLIC_API_BASE_URL`로 API 접근.
