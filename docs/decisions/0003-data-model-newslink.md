# ADR-0003: NewsLink 데이터 모델 도입

- 상태: 채택/구현 완료
- 결정: `News`에 1:N `NewsLink` 추가(JPA/Prisma), Collector가 요약과 링크 저장.
- 근거: 카드에 원문 링크/소스 표시를 위한 구조화.
- 영향: JPA 엔티티(`News.links`), Prisma 스키마(`NewsLink`), Collector 저장 로직 변경.
