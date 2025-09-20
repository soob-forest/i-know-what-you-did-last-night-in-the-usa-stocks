# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

Problem definition → small, safe change → change review → refactor — repeat the loop.

## Mandatory Rules

- Before changing anything, read the relevant files end to end, including all call/reference paths.
- Keep tasks, commits, and PRs small.
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

## Project Overview

A multi-service system for tracking US stock news and delivering summaries via Slack. Users subscribe to stocks through a Slack bot and receive AI-generated summaries of relevant news.

## Architecture

- **API Service** (`api/`): Java 17 Spring Boot 3.2 multi-module project
  - `api/api`: REST API for user and subscription management (`ApiApplication`)
  - `api/slack`: Slack bot integration with Spring Batch jobs (`ServerApplication`)
- **Collector Service** (`collector/`): NestJS TypeScript service for news collection and AI summarization
- **Database**: MySQL with Prisma ORM (collector) and JPA (API)

## Essential Commands

### Java API Services (from `api/`)
```bash
# Build all modules and run tests
./gradlew build

# Run tests only
./gradlew test

# Start API service
./gradlew :api:bootRun

# Start Slack bot service
./gradlew :slack:bootRun
```

### NestJS Collector (from `collector/`)
```bash
# Install dependencies
pnpm install

# Development server with hot reload
pnpm start:dev

# Production build and run
pnpm build
pnpm start:prod

# Linting and formatting
pnpm lint
pnpm format

# Testing
pnpm test          # Unit tests
pnpm test:e2e      # End-to-end tests
pnpm test:cov      # Coverage report

# Database schema changes
pnpm prisma generate
```

## Database Configuration

- MySQL connection: `root:1234@localhost:3306/ikwydlnitus`
- Prisma schema: `collector/prisma/schema.prisma`
- Spring Boot config: `api/**/src/main/resources/application.yml`

## Key Domain Models

- **Stock**: Core entity with ticker symbol and name
- **News**: AI-summarized news articles linked to stocks
- **Subscription**: User subscriptions to specific stocks
- **Member**: User management for API access

## Development Workflow

1. Always run both `./gradlew build` (from `api/`) and `pnpm test` (from `collector/`) before committing
2. Use `.env` file for local secrets (not committed)
3. Slack bot requires `slack.bot-token` and `slack.secret` configuration
4. OpenAI integration requires `OPENAI_API_KEY` environment variable

## Testing Strategy

- Java: JUnit 5 with Spring Boot Test slices
- TypeScript: Jest with unit tests (`*.spec.ts`) and e2e tests (`test/`)
- Ensure all services pass their respective test suites before deployment