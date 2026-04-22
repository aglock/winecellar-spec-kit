<!--
Sync Impact Report
- Version change: 1.3.0 -> 1.4.0
- Modified principles:
  - V renamed from "Canonical Reference Data, Geographic Consistency, And Readable Implementation" to
    "Domain Model Fidelity And Canonical Concepts"; heading now reflects the principle's core intent
    (domain model faithfulness to the information model and reuse of canonical types); duplicated
    implementation guidance removed (consolidated into Principle VI)
  - VI expanded to explicitly reference domain-driven design principles
- Added sections: none
- Removed sections: none
- Templates requiring updates:
  - ✅ .specify/templates/plan-template.md — constitution check already covers testing, UX, performance
  - ✅ .specify/templates/spec-template.md — no principle-driven sections changed
  - ✅ .specify/templates/tasks-template.md — no task categories changed
- Follow-up TODOs: none
-->

# winecellar-spec-kit Constitution

## Core Principles

### I. Information Model First
All domain specifications MUST define the information model before proposing a
storage schema, API payload shape, or ORM structure. The information model MUST
optimize for semantic correctness, stable concepts, and explicit relationships
even when the later data model denormalizes for performance or implementation
pragmatism. Specifications MUST describe the meaning of each entity and
relationship in domain terms. They MUST NOT use implementation convenience as a
reason to collapse distinct concepts such as wine master data, bottle
inventory, cellar membership, or event history.

### II. Explicit Collaboration And Access Control
The domain is collaborative by default. A cellar is a shared domain object and
all access control MUST be expressed through cellar membership. Membership MUST
link exactly one user to exactly one cellar and MUST carry an explicit role.
The minimum supported roles are `OWNER`, `CONTRIBUTOR`, and `VIEWER`. An
`OWNER` MUST be able to grant and revoke cellar access. Permissions MUST be
defined per cellar and inherited by compartments and bottles within that
cellar. Specifications MUST NOT define bottle-level or compartment-level ACLs
unless the constitution is amended first.

### III. Separated Domain Boundaries
Specifications MUST model the following concerns as separate domain areas:
identity and access; physical storage structure; wine master data; physical
bottle inventory; and events/audit trail. A bottle inventory record MUST
represent one physical bottle in one cellar and compartment. Wine master data
MUST represent the wine identity independent of any single stored bottle.
Compartments MUST belong to a cellar. Bottles MUST belong to both a cellar and
their current compartment. Specifications MUST NOT merge master data fields into
inventory records when a reusable domain concept already exists.

### IV. Evented Audit Trail
History is a first-class requirement. State-changing actions MUST create event
records in addition to updating current state. Every event MUST record the
acting user, event type, occurrence time, cellar context, and exactly one
primary target object. The allowed primary target categories are `USER_ACCOUNT`,
`BOTTLE`, `COMPARTMENT`, or `CELLAR`. `USER_ACCOUNT` is permitted only for
identity and access events such as registration, activation, sign-in outcome,
session lifecycle, or other account-scoped security actions that may occur
before cellar context exists. Event metadata MAY describe secondary context
such as source and destination compartments for a move, but the primary target
MUST remain singular and explicit. Specifications MUST NOT rely on inferred
history from current state alone.

### V. Domain Model Fidelity And Canonical Concepts
The model MUST reuse canonical domain concepts instead of introducing parallel
text fields for the same meaning. Geographic modeling MUST use the shared
concepts `COUNTRY`, `REGION`, and `APPELLATION` consistently across wines,
grapes, and related reference data. `APPELLATION` is optional for a wine. When
`APPELLATION` is absent, the wine MUST link directly to a `REGION`. Grape data
MUST align with `docs/resources/grapes-masterlist.json`, and a grape's classic
regions MUST reference the same `REGION` concept used everywhere else. Classic
regions MUST NOT be modeled as unconstrained free text in the information
model.

### VI. Code Quality And Maintainability
Code quality is a governing concern, not a cleanup task. Implementations MUST
follow domain-driven design principles and MUST make domain intent obvious to a
human reader through clear naming, cohesive modules, small focused units of
behavior, and explicit boundaries between domain, application, and
infrastructure concerns. Duplication of business rules MUST be reduced by
extracting shared domain logic, but abstractions MUST NOT be introduced unless
they improve clarity or change isolation. Reviewers MUST reject code that is
difficult to understand, mixes unrelated responsibilities, or favors cleverness
over maintainability.

### VII. Testing Standards
Behavior-changing work MUST include automated tests at the level that best
verifies the behavior: unit tests for focused domain logic, integration tests
for cross-component flows, and contract or end-to-end tests where external
interfaces are affected. Tests MUST be readable, deterministic, and traceable
to the specification or user story they validate. A change MUST NOT be treated
as complete if its critical behavior cannot be verified automatically or if the
test suite leaves regressions in cellar permissions, inventory state changes,
event history, or reference-data integrity untested.

### VIII. User Experience Consistency
User experience consistency is required across workflows, not optional polish.
Features MUST use consistent terminology, role semantics, validation behavior,
feedback patterns, and interaction flows across the product. The same cellar,
bottle, compartment, sharing, and geography concepts MUST appear with the same
meaning in UI copy, API contracts, and documentation. Specifications MUST NOT
introduce user-visible inconsistencies in naming, permission outcomes, or state
feedback for similar actions without explicit justification.

### IX. Performance As A Requirement
Performance requirements MUST be stated explicitly for any feature with
user-visible latency, throughput, or data-volume implications. Specifications
MUST define measurable expectations such as response time, rendering latency,
bulk operation limits, or acceptable query cost when the feature scope makes
them relevant. Implementations MUST prefer simple designs that meet the stated
performance target, and MUST NOT introduce speculative complexity without a
measured need. Reviewers MUST require evidence or rationale when a design risks
slow cellar views, bottle moves, search, sharing operations, or event-history
retrieval at expected scale.

## Domain Modeling Rules

1. Entities MUST be named as singular domain nouns in uppercase when shown as
   conceptual model entities, for example `CELLAR`, `BOTTLE`, `WINE`, and
   `CELLAR_MEMBERSHIP`.
2. Relationships MUST be named from business meaning, not storage mechanics.
   Use names such as "belongs to", "contains", "stored in", or "shared with"
   rather than generic labels such as "has link to".
3. Attributes MUST use singular names and MUST describe one fact only. An
   attribute MUST NOT encode multiple concepts that deserve separate entities or
   relationships.
4. Nullable relationships MUST be rare. Every nullable relationship MUST be
   justified in the specification with the business rule that makes absence
   valid. The canonical example is `WINE -> APPELLATION` being nullable when
   `WINE -> REGION` is present instead.
5. A cellar MUST be created with at least one compartment and MUST contain at
   least one compartment at all times. A bottle MUST NOT exist without being
   assigned to both a cellar and a compartment.
6. Membership and permissions MUST be traceable. Specifications MUST identify
   which role can perform each cellar action: view contents, add bottles, move
   bottles, remove bottles, and manage sharing.
7. The information model MUST separate current state from historical facts.
   Current fields such as bottle status MAY exist, but they MUST NOT replace the
   corresponding event history.
8. Specifications MUST prefer references to canonical master data over repeated
   text. If a concept already exists as an entity, a spec MUST justify any new
   text field that overlaps with it.

## Specification & Review Rules

1. Every information-modeling spec MUST include, at minimum, the entities,
   relationships, cardinalities, role model, event model, and all justified
   nullable relationships relevant to the feature.
2. Every spec that touches collaboration MUST state cellar-level permission
   effects for `OWNER`, `CONTRIBUTOR`, and `VIEWER`, including whether the
   feature changes sharing behavior.
3. Every spec that touches bottles, compartments, or cellar lifecycle MUST
   define the events created, the primary target type of each event, and the
   minimum event metadata required for traceability.
4. Every spec that touches identity or access flows MUST define the events
   created, the primary target type of each event, and the minimum event
   metadata required for traceability. `USER_ACCOUNT` is the canonical primary
   target for identity-scoped events unless a stronger domain target exists.
5. Every spec that touches wine, grapes, appellations, or regions MUST verify
   that it reuses canonical `COUNTRY`, `REGION`, and `APPELLATION` concepts and
   stays compatible with `docs/resources/grapes-masterlist.json`.
6. Reviewers MUST reject specifications that introduce parallel text fields for
   canonical concepts, implicit permissions, unbounded nullability, or history
   modeled only as mutable state.
7. Reviewers MUST treat consistency of the information model as higher priority
   than early storage optimization. Pragmatic denormalization MAY be added later,
   but only after the semantic model is explicit.
8. Every implementation-oriented spec MUST define expected code quality,
   required automated test coverage, user experience consistency constraints,
   and any relevant measurable performance targets.
9. Any exception to these rules MUST be documented inline in the specification
   under a clearly labelled "Constitution Exception" heading, stating the rule
   being excepted, the rationale, rejected alternatives, and the impact on
   traceability.

## Governance

This constitution governs all future specs in this repository and supersedes
less specific modeling guidance. Amendments MUST be made by updating this file,
recording the reason for change, and applying semantic versioning to the
constitution itself: MAJOR for incompatible principle changes or removals, MINOR
for new principles or materially expanded obligations, and PATCH for
clarifications that do not change required behavior.

Compliance review is mandatory for every substantial spec change. The reviewer
MUST verify that the proposal:

- models cellar sharing through membership and cellar-level roles
- preserves separation between wine master data and bottle inventory
- uses canonical geography concepts consistently
- defines event history for state-changing actions with a singular explicit
  primary target per-event
- documents every nullable relationship with explicit justification
- follows domain-driven design, clean code, and clean architecture principles
  with clear domain/application/infrastructure boundaries
- includes automated tests for any behavior-changing work, traceable to the
  specification or user story they validate
- preserves UX consistency in terminology, permission semantics, validation
  behavior, and feedback patterns
- states measurable performance expectations for any feature with user-visible
  latency, throughput, or data-volume implications

Specifications and plans MUST NOT proceed with unresolved constitutional
violations unless the violation is explicitly documented, justified, and
approved as a temporary exception.

**Version**: 1.4.0 | **Ratified**: 2026-03-15 | **Last Amended**: 2026-04-21
