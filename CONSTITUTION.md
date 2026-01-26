# Constitution Template (with examples)

This document captures how the project is governed and how decisions are made. Replace the example text with your team's actual policies.

## 1) Purpose
Describe the mission and goals of the project.

Example:
> Provide a reliable, secure Service Catalog API and UI for internal PLR workflows, with a focus on data quality and operational simplicity.

## 2) Scope
Define what is in and out of scope for the project.

Example:
> In scope: Backend APIs, database schema migrations, and frontend UI for catalog CRUD.  
> Out of scope: Authentication/authorization, reporting dashboards, and long-term data warehousing.

## 3) Principles
List the guiding principles for decisions.

Example:
1. Security and compliance first.
2. Favor clarity over cleverness.
3. Prefer incremental change over risky refactors.
4. Operational visibility (logs, metrics) is required for new features.

## 4) Roles & Responsibilities
Define roles and who can approve what.

Example:
- Product owner: Defines requirements and acceptance criteria.
- Tech lead: Approves architectural changes and dependencies.
- Maintainers: Review and merge routine changes.
- Contributors: Submit PRs with tests and documentation updates.

## 5) Decision-Making Process
Explain how decisions are made and recorded.

Example:
- Routine changes: PR review + 1 maintainer approval.
- Architecture or dependency changes: design note + tech lead approval.
- Breaking changes: RFC + sign-off from product owner and tech lead.

## 6) Quality & Testing
Define expectations for testing and quality.

Example:
- New endpoints require unit and integration tests.
- CI must pass before merge.
- High-risk changes require rollback plan.

## 7) Security & Compliance
Define required security practices.

Example:
- Secrets are stored in approved secret managers.
- No PHI/PII in logs.
- Security review required for auth or data exposure changes.

## 8) Release Process
Describe how releases are performed and versioned.

Example:
- Semantic versioning for tagged releases.
- Releases are cut from `main` after green CI.
- Release notes are required for each deployment.

## 9) Support & Incident Handling
Define how incidents are triaged and who is responsible.

Example:
- On-call rotation monitors alerts.
- Severity definitions documented in `RUNBOOK.md`.
- Post-incident review required for Sev-1.

## 10) Change History
Track updates to this constitution.

Example:
- 2026-01-26: Initial template created.
