
Problem definition → small, safe change → change review → refactor — repeat the loop.

## Mandatory Rules

- Before changing anything, read the relevant files end to end, including all call/reference paths.
- Keep tasks, commits, and PRs small.
- commit message는 한글로 작성
- If you make assumptions, record them in the Issue/PR/ADR.
- Never commit or log secrets; validate all inputs and encode/normalize outputs.
- Avoid premature abstraction and use intention-revealing names.
- Compare at least two options before deciding.

## Mindset

- Think like a senior engineer.
- Don’t jump in on guesses or rush to conclusions.
- Always evaluate multiple approaches; write one line each for pros/cons/risks, then choose the simplest solution.

## Code & File Reference Rules

- Read files thoroughly from start to finish (no partial reads).
- Before changing code, locate and read definitions, references, call sites, related tests, docs/config/flags.
- Do not change code without having read the entire file.
- Before modifying a symbol, run a global search to understand pre/postconditions and leave a 1–3 line impact note.

## Required Coding Rules

- Before coding, write a Problem 1-Pager: Context / Problem / Goal / Non-Goals / Constraints.
- Enforce limits: file ≤ 300 LOC, function ≤ 50 LOC, parameters ≤ 5, cyclomatic complexity ≤ 10. If exceeded, split/refactor.
- Prefer explicit code; no hidden “magic.”
- Follow DRY, but avoid premature abstraction.
- Isolate side effects (I/O, network, global state) at the boundary layer.
- Catch only specific exceptions and present clear user-facing messages.
- Use structured logging and do not log sensitive data (propagate request/correlation IDs when possible).
- Account for time zones and DST.

## Testing Rules

- New code requires new tests; bug fixes must include a regression test (write it to fail first).
- Tests must be deterministic and independent; replace external systems with fakes/contract tests.
- Include ≥1 happy path and ≥1 failure path in e2e tests.
- Proactively assess risks from concurrency/locks/retries (duplication, deadlocks, etc.).
- 테스트를 직접 실행하여 검증한다.
- Java: JUnit 5 via Spring Boot Starter Test. Prefer slice tests for web/data; name tests after unit under test.
- TypeScript: Jest. Unit tests in `src/**/**.spec.ts`; e2e in `test/**`. Aim for meaningful coverage (`pnpm test:cov`).

## Security Rules

- Never leave secrets in code/logs/tickets.
- Validate, normalize, and encode inputs; use parameterized operations.
- Apply the Principle of Least Privilege.

## Clean Code Rules

- Use intention-revealing names.
- Each function should do one thing.
- Keep side effects at the boundary.
- Prefer guard clauses first.
- Symbolize constants (no hardcoding).
- Structure code as Input → Process → Return.
- Report failures with specific errors/messages.
- Make tests serve as usage examples; include boundary and failure cases.

## Anti-Pattern Rules

- Don’t modify code without reading the whole context.
- Don’t expose secrets.
- Don’t ignore failures or warnings.
- Don’t introduce unjustified optimization or abstraction.
- Don’t overuse broad exceptions.

# Repository Guidelines

## Project Structure & Modules
- `api/` — Java 17, Spring Boot 3.2 multi-module Gradle project.
  - `api/api` — REST API service (`ApiApplication`).
  - `api/slack` — Slack + Spring Batch jobs (`ServerApplication`).
  - Config: `api/**/src/main/resources/application.yml`.
- `collector/` — NestJS (TypeScript) service with Prisma and schedulers.
  - Source: `collector/src/**`; tests: `collector/test/**` and `*.spec.ts`.
  - Schema: `collector/prisma/schema.prisma` (MySQL).
- Root: `.env` (local secrets), `README.md`, `LICENSE`.

## Build, Test, and Run
- Java (from `api/`):
  - Build: `./gradlew build` — compiles all modules, runs JUnit.
  - Test: `./gradlew test` — runs unit tests.
  - Run API: `./gradlew :api:bootRun` (uses local `application.yml`).
  - Run Slack: `./gradlew :slack:bootRun`.
- Node/Nest (from `collector/`):
  - Install: `pnpm install`
  - Dev server: `pnpm start:dev`
  - Build: `pnpm build`; Prod: `pnpm start:prod`
  - Lint/Format: `pnpm lint` / `pnpm format`
  - Tests: `pnpm test`, `pnpm test:e2e`, `pnpm test:cov`
  - Prisma: `pnpm prisma generate` (after schema changes)