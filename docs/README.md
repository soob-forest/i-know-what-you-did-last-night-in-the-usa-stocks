# MVP 개요 및 로드맵

본 문서는 웹 기능 MVP의 범위와 목표, 단계별 계획을 요약합니다.

## 목표
- Slack OAuth 로그인으로 사용자 식별(`Member.userId` 매핑).
- 웹 대시보드(`/app`): 오늘/지난 밤 요약, 종목별 카드, 원문 링크.
- 설정 페이지(`/app/settings`): 구독 종목, 푸시 시간, 타임존 관리.
- 관리자 콘솔(`/admin/jobs`): 배치 실행/실패 로그 열람.
- API 확장 및 SSE 스트림으로 고영향 속보 제공.

## 범위(기능)
1) 인증: Slack OAuth → JWT 발급, 세션 유지, 역할(사용자/관리자).
2) 뉴스: 날짜·티커별 조회 API, 요약/원문 링크/소스 표시.
3) 구독: 등록/삭제, 푸시 시간·타임존 변경.
4) 알림: SSE `/api/stream/alerts`로 실시간 속보.
5) 관측: 배치 상태/전송 로그 읽기.

## 마일스톤
- M1: API 스펙 구현(REST+SSE), DB 스키마 확정
- M2: 인증(Slack OAuth) + 최소 대시보드
- M3: 설정 화면 + 구독 CRUD
- M4: 관리자 화면 + 배치 연동/로그 표시

## 수용 기준(예시)
- 로그인 후 `/app` 접속 시 사용자 구독 종목의 당일 요약 1초 내 렌더.
- `/app/settings`에서 티커 추가/삭제 시 2초 내 반영(Optimistic UI 가능).
- 관리자 `/admin/jobs`에서 최근 24h 작업 목록/상태/오류 메시지 확인 가능.
- API: `GET /api/news?date=YYYY-MM-DD&tickers=AAPL` 응답 500ms 내(캐시 허용).

더 자세한 엔드포인트와 페이로드는 `api-spec.md`, 아키텍처/데이터 모델은 `web-architecture.md`를 참고하세요.

