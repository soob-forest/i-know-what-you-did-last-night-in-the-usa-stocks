# API 사양 (MVP)

기본 베이스: `https://{host}`
인증: Slack OAuth 로그인 후 발급된 JWT(`Authorization: Bearer <token>`)

## 사용자/설정
- GET `/api/me`
  - 200: `{ id, userId, timezone, pushAt, roles }`
- PUT `/api/me/push-at`
  - body: `{ pushAt: "HH:mm:ss", timezone?: "Area/City" }`
  - 204
- GET `/api/me/subscriptions`
  - 200: `[ { name, ticker } ]`
- POST `/api/me/subscriptions/{ticker}`
  - 201
- DELETE `/api/me/subscriptions/{ticker}`
  - 204

## 뉴스
- GET `/api/news`
  - query: `date=YYYY-MM-DD`, `tickers=AAPL,TSLA`
  - 200: `[{ stock: { name, ticker }, summary, date, links?: [{title,url,source}], score?: number, sentiment?: "pos|neu|neg" }]`

## 알림 스트림(SSE)
- GET `/api/stream/alerts`
  - 헤더: `Accept: text/event-stream`
  - 이벤트: `event: alert` / `data: { ticker, title, url, score }`

## 관리자
- GET `/api/admin/jobs`
  - 200: `[ { id, name, status, startedAt, finishedAt, error? } ]`
- GET `/api/admin/messages`
  - 200: 최근 전송 로그/실패 사유

## 에러 포맷
- 4xx/5xx: `{ error: { code, message, detail? } }`

## 비고(현 코드와의 연결)
- 구독 API는 현 `SubscriptionController`를 확장/정렬(이름 기준 → 인증 사용자 기준) 필요.
- Collector가 저장한 `News`를 `api/api`에서 조회해 반환.
- SSE는 Spring(WebFlux/SSE) 또는 Nest 게이트웨이 중 택1.
