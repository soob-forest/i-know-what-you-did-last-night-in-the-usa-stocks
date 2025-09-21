# ADR-0010: SDUI URL 스킴 Allowlist, 링크 제한, 캐시, 버전 분기

- 상태: 채택/구현 진행
- 결정:
  - URL 스킴 allowlist: http/https만 허용(기타 스킴 차단)
  - 링크 수 제한: 카드당 최대 N개(기본 5)
  - 캐시: AppUiService 결과를 (userId, range, q, tickers) 키로 짧은 TTL(기본 30s) 캐싱
  - 캐시 무효화: 사용자 구독 변경 시 해당 userId 관련 키 전부 제거
  - 버전: 응답에 `version: v1` 포함, 클라이언트는 버전 분기 처리
- 근거: 보안/성능/호환성 확보
