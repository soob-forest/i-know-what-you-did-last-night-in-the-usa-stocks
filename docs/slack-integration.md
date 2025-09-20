# Slack 통합 가이드 (MVP)

## 목적
- Slack 사용자 인증과 앱 명령을 통해 `Member.userId`와 매핑하고, DM 발송/설정을 제어합니다.

## OAuth 로그인 플로우
- Slack 앱 생성 → Redirect URL: `https://{host}/api/auth/slack/callback`
- 권한(Scopes): `users:read`, `chat:write`, `commands`, `channels:read`(필요 시)
- 순서:
  1) Next.js에서 Slack OAuth 시작 → 사용자 승인
  2) 콜백에서 Slack userId 추출 → `api/api`가 `Member` 조회/생성
  3) JWT 발급(HttpOnly 쿠키) 후 `/app`으로 리디렉션

## 슬래시 커맨드(예시)
- `/등록 AAPL` → 사용자 없으면 가입+기본 pushAt 09:00, 종목 구독 추가
- `/구독목록` → 현재 구독 종목 반환
- 오류 시: 메시지로 원인 회신(존재하지 않는 티커, 중복 구독 등)

## 설정/보안
- 비밀키: `SLACK_BOT_TOKEN`, `SLACK_BOT_SECRET`은 서버 환경변수로만 주입
- 재시도/레이트리밋: Slack 재시도 헤더 처리, 429 대응 백오프
- 검증: 서명 검증으로 콜백/커맨드 위변조 방지

## 배치 발송 연계
- `sendNewsJob`이 `pushAt` 시각 매칭 사용자에게 당일 `News` 요약 DM 전송
- 실패건 DLQ 또는 재시도 큐(간단히: 재시도 n회 + 로그 기록)

## 현 코드 연결 포인트
- `api/slack`의 `SlackService`, `SendNewsConfig`, `SendNewsScheduler`
- `MemberService`, `SubscriptionService` 호출로 사용자·구독 상태 변경
