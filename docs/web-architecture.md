# 웹 아키텍처 (MVP)

## 선택안
- 프런트엔드: Next.js(SSG/SSR 혼합) 별도 앱.
- 백엔드: `api/api`가 BFF/REST 역할(인증, 구독, 뉴스, SSE). `collector`는 수집/요약·저장 담당.
- 슬랙/배치: `api/slack` 유지(배치 발송, 슬래시 명령).

## 통합 흐름
- 인증: Next.js → Slack OAuth → 콜백에서 JWT 발급(`api/api`) → 쿠키 저장.
- 데이터: `collector`가 MySQL에 `News`/`Stock` 저장 → `api/api`가 조회/응답.
- 알림: `api/api`의 SSE `/api/stream/alerts` 구독(속보/고득점 이벤트).

## 데이터 모델 보강
- Member: `timezone`(IANA), `roles`("USER"|"ADMIN").
- Subscription: `pushAt`(로컬 기준), 중복 구독 방지 유지.
- News(옵션): `links[{title,url,source}]`, `score`, `sentiment` 필드 추가 고려.

## 페이지/라우팅
- 사용자: `/app`(대시보드), `/app/settings`(구독/시간/타임존), `/app/history?symbol=...&date=...`
- 관리자: `/admin/jobs`, `/admin/messages`

## 비기능 요구
- 성능: `/api/news` 500ms 내 응답(인덱스/캐시 고려).
- 보안: JWT HttpOnly 쿠키, 슬랙 시크릿/토큰은 서버 보관.
- 관측성: 배치 잡/전송 로그 구조화 로그 + 메트릭.

## 구현 체크리스트
- [ ] OAuth: Slack 앱 설정(redirect, scopes) + 콜백 엔드포인트
- [ ] JWT 발급/검증 + 역할
- [ ] 구독 API 정리(인증 사용자 기준)
- [ ] 뉴스 조회 API + 인덱스/캐시
- [ ] SSE 알림 채널
- [ ] Next.js 페이지/상태(SWR) + 보호 라우팅

## SDUI(Server-Driven UI) 설계
- 블록 타입(UIBlock): `{ type: string, props?: object, children?: UIBlock[] }`
- 컴포넌트 레지스트리(클라이언트): 허용된 컴포넌트만 렌더 → 보안/일관성 확보
- 렌더러: 깊이 우선으로 children을 재귀 렌더
- 예시 스키마(JSON)
```
{
  "blocks": [
    { "type": "Toolbar", "props": { "range": "overnight", "q": "" } },
    { "type": "Container", "children": [
      { "type": "NewsGrid", "children": [
        { "type": "NewsCard", "props": { "news": { "stock": {"name":"Apple Inc.","ticker":"AAPL"}, "summary":"...", "date":"2025-06-01", "links": [] } } }
      ] }
    ] }
  ]
}
```
- 엔드포인트: `GET /ui/app?range=overnight|today&q=...` (Spring), 응답은 위 스키마
- 페이지 동작: `/app?sdui=1` 사용 시 SDUI 스키마 fetch → Renderer 렌더, 기본은 로컬 스키마 조립
