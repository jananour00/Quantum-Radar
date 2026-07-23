# 🚦 Quantum Radar (QuRadar)

A Java traffic-violation detection and fine-management system, refactored to follow SOLID principles with a clear, extensible package structure.

[![Java](https://img.shields.io/badge/Java-11%2B-orange.svg)](https://www.oracle.com/java/)

---

## Table of Contents
- [Overview](#overview)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Class Diagram](#class-diagram)
- [Sequence Diagram](#sequence-diagram)
- [SOLID Principles Applied](#solid-principles-applied)
- [Domain Rules](#domain-rules)
- [Validation vs. Violations](#validation-vs-violations)
- [Build & Run](#build--run)
- [Usage](#usage)
- [Extending the System](#extending-the-system)

---

## Overview

**QuRadar** receives observations from a physical radar (plate number, date, vehicle type, speed, seatbelt status), evaluates each one against a configurable set of traffic rules, generates violations, and issues fines per car. It also reports how many times each individual rule has been broken.

**AI Model used:** None. This is a deterministic, rule-based evaluation system.

| Concept | Class | Responsibility |
|---|---|---|
| Observation | `Observation` | One immutable, self-validating snapshot from the radar |
| Rule | `Rule` (+ implementations) | Checks one observation against one traffic law |
| Violation | `Violation` | The result of one broken rule (rule name, description, fee) |
| Fine | `Fine` | All violations for one car, plus the total amount |
| Orchestrator | `QuRadar` | Runs every rule against an observation, tracks results |
| Presentation | `FineReporter` (+ `ConsoleFineReporter`) | Decides how results are displayed |
| Composition root | `Main` | Wires concrete rules and the reporter together and demos the system |
---

## Architecture

```
┌───────────────────────────────────────────────────────────────┐
│                            Main                                 │
│     (composition root: wires rules + reporter, runs demo)       │
└───────────────────────────────┬─────────────────────────────────┘
                                  │ injects
                                  ▼
┌───────────────────────────────────────────────────────────────┐
│                          QuRadar                                 │
│  observe(Observation) : Optional<Fine>                          │
│  getAllPossibleFines() : Map<String, Integer>                   │
│  getViolatedRulesCount() : Map<String, Integer>                 │
│  ────────────────────────────────────────────────────────────  │
│  Depends ONLY on the Rule interface. Never prints. Never        │
│  instantiates a concrete rule.                                  │
└───────────────┬───────────────────────────────┬─────────────────┘
                 │                               │
                 ▼                               ▼
     ┌───────────────────────┐        ┌───────────────────────┐
     │        Rule             │        │         Fine           │
     │      <<interface>>      │        │  plateNumber           │
     │  check(Observation)     │        │  violations[]           │
     │  getName()               │        │  totalAmount            │
     └───────────┬───────────────┘        └───────────┬─────────────┘
                  │                                     │
   ┌──────────────┼───────────────┬────────────┐        ▼
   ▼              ▼               ▼            ▼    ┌───────────┐
Seatbelt      Private         Truck          Bus /   │ Violation │
 Rule       SpeedRule       SpeedRule    Motorcycle   │ ruleName  │
             (extends         (extends     SpeedRule  │ description│
            SpeedRule)        SpeedRule)  (extends    │ fee       │
                                            SpeedRule) └───────────┘

                                          ┌──────────────────────┐
                     QuRadar's results──▶ │     FineReporter      │
                                          │    <<interface>>      │
                                          │  reportFine()          │
                                          │  reportAllFines()      │
                                          │  reportViolationStats()│
                                          └───────────┬────────────┘
                                                        │
                                                        ▼
                                          ┌──────────────────────┐
                                          │  ConsoleFineReporter  │
                                          └──────────────────────┘
```

**Data flow:** `Main` builds the rule list and a `ConsoleFineReporter`, injects the rules into `QuRadar` → `QuRadar.observe()` loops every rule → each returns `Optional<Violation>` → all violations for that car become one `Fine`, returned to the caller → `Main` hands that `Fine` to the `FineReporter` if it wants it printed.

---

## Project Structure

```
Quantum Radar/
├── README.md
├── src/
│   ├── Main.java                          # Composition root / demo
│   │
│   └── radar/
│       ├── QuRadar.java                   # Orchestrator (observe, reports)
│       │
│       ├── model/
│       │   ├── VehicleType.java           # enum: PRIVATE, TRUCK, BUS, MOTORCYCLE
│       │   ├── Observation.java           # immutable, self-validating
│       │   ├── Violation.java             # one broken rule + fee
│       │   └── Fine.java                  # violations for one car + total
│       │
│       ├── rule/
│       │   ├── Rule.java                  # <<interface>> — the extension point
│       │   ├── SpeedRule.java             # abstract base shared by speed rules
│       │   ├── SeatbeltRule.java
│       │   ├── PrivateCarSpeedRule.java   # extends SpeedRule
│       │   ├── TruckSpeedRule.java        # extends SpeedRule
│       │   ├── BusSpeedRule.java          # extends SpeedRule
│       │   └── MotorcycleSpeedRule.java   # extends SpeedRule
│       │
│       └── report/
│           ├── FineReporter.java          # <<interface>> — presentation abstraction
│           └── ConsoleFineReporter.java   # prints to stdout
```

---

## Class Diagram

```mermaid
classDiagram
    class VehicleType {
        <<enumeration>>
        PRIVATE
        TRUCK
        BUS
        MOTORCYCLE
    }

    class Observation {
        -String plateNumber
        -LocalDateTime date
        -VehicleType vehicleType
        -int speed
        -boolean seatbeltFastened
        +getPlateNumber() String
        +getDate() LocalDateTime
        +getVehicleType() VehicleType
        +getSpeed() int
        +isSeatbeltFastened() boolean
    }

    class Violation {
        -String ruleName
        -String description
        -int fee
        +getRuleName() String
        +getDescription() String
        +getFee() int
    }

    class Fine {
        -String plateNumber
        -List~Violation~ violations
        -int totalAmount
        +getPlateNumber() String
        +getViolations() List~Violation~
        +getTotalAmount() int
    }

    class Rule {
        <<interface>>
        +check(Observation) Optional~Violation~
        +getName() String
    }

    class SpeedRule {
        <<abstract>>
        -VehicleType applicableType
        -int maxSpeed
        -int feePerKmOver
        +check(Observation) Optional~Violation~
    }

    class SeatbeltRule
    class PrivateCarSpeedRule
    class TruckSpeedRule
    class BusSpeedRule
    class MotorcycleSpeedRule

    class QuRadar {
        -List~Rule~ rules
        -List~Fine~ issuedFines
        -Map~String, Integer~ violatedRuleCounts
        +QuRadar(List~Rule~)
        +observe(Observation) Optional~Fine~
        +addRule(Rule) void
        +getAllPossibleFines() Map~String, Integer~
        +getIssuedFines() List~Fine~
        +getViolatedRulesCount() Map~String, Integer~
    }

    class FineReporter {
        <<interface>>
        +reportFine(Fine) void
        +reportAllFines(Map) void
        +reportViolationStatistics(Map) void
    }

    class ConsoleFineReporter

    class Main {
        +main(String[] args) void
    }

    Rule <|.. SeatbeltRule
    Rule <|.. SpeedRule
    SpeedRule <|-- PrivateCarSpeedRule
    SpeedRule <|-- TruckSpeedRule
    SpeedRule <|-- BusSpeedRule
    SpeedRule <|-- MotorcycleSpeedRule
    FineReporter <|.. ConsoleFineReporter
    Observation --> VehicleType
    Fine --> "0..*" Violation
    QuRadar --> "0..*" Rule : injected via constructor
    QuRadar --> "0..*" Fine
    QuRadar ..> Observation : observe()
    Main --> QuRadar : builds & injects rules
    Main --> FineReporter : builds & uses
```

---

## Sequence Diagram

```mermaid
sequenceDiagram
    participant Main
    participant QuRadar
    participant Rule as Rule (each)
    participant Fine
    participant Reporter as FineReporter

    Main->>QuRadar: observe(observation)
    loop for every injected Rule
        QuRadar->>Rule: check(observation)
        Rule-->>QuRadar: Optional<Violation>
        alt violation present
            QuRadar->>QuRadar: collect violation, increment count by rule name
        end
    end
    alt violations not empty
        QuRadar->>Fine: new Fine(plateNumber, violations)
        QuRadar->>QuRadar: store fine
        QuRadar-->>Main: Optional<Fine> (present)
        Main->>Reporter: reportFine(fine)
    else no violations
        QuRadar-->>Main: Optional<Fine> (empty)
    end
    Main->>QuRadar: getAllPossibleFines()
    QuRadar-->>Main: Map<String, Integer>
    Main->>Reporter: reportAllFines(map)
    Main->>QuRadar: getViolatedRulesCount()
    QuRadar-->>Main: Map<String, Integer>
    Main->>Reporter: reportViolationStatistics(map)
```

> `mermaid` blocks render natively on GitHub/GitLab and in most modern IDEs. The ASCII diagram under [Architecture](#architecture) is a plain-text fallback.

---

## SOLID Principles Applied

| Principle | Where |
|---|---|
| **Single Responsibility** | `Observation` only validates/models input. `Rule` implementations only decide violations. `Fine` only aggregates. `QuRadar` only orchestrates. `FineReporter` only displays. Nobody does two of these jobs. |
| **Open/Closed** | `QuRadar` depends solely on `Rule` and `FineReporter`. Adding `MotorcycleSpeedRule` required zero changes to `QuRadar.java` — see the `feat: add MotorcycleSpeedRule` commit. |
| **Liskov Substitution** | Every `SpeedRule` subclass (`PrivateCarSpeedRule`, `TruckSpeedRule`, `BusSpeedRule`, `MotorcycleSpeedRule`) is fully substitutable wherever a `Rule` is expected — `QuRadar` never checks `instanceof`. |
| **Interface Segregation** | `Rule` has exactly two methods, both needed by every implementation. `FineReporter` has exactly the three reporting operations `Main` actually needs — no fat, do-everything interface. |
| **Dependency Inversion** | `QuRadar` takes `List<Rule>` and never instantiates a concrete rule. `Main` is the one place concrete classes (`SeatbeltRule`, `ConsoleFineReporter`, ...) are created and wired together — the composition root. |

---

## Domain Rules

| Rule | Applies to | Condition | Fee |
|---|---|---|---|
| `SeatbeltRule` | Every vehicle | Seatbelt not fastened | 100 EGP flat |
| `PrivateCarSpeedRule` | `PRIVATE` | Speed > 80 km/h | 10 EGP per km/h over |
| `TruckSpeedRule` | `TRUCK` | Speed > 60 km/h | 15 EGP per km/h over |
| `BusSpeedRule` | `BUS` | Speed > 70 km/h | 12 EGP per km/h over |
| `MotorcycleSpeedRule` | `MOTORCYCLE` | Speed > 70 km/h | 8 EGP per km/h over |

Each rule is fully independent — `QuRadar` has no idea any of them exist beyond the shared `Rule` interface.

---

## Validation vs. Violations

Kept deliberately separate, as before:

- **Invalid input** (blank/malformed plate, negative or unrealistic speed, null date, future-dated observation, null vehicle type) is a data-integrity problem. It throws `IllegalArgumentException` at the moment `Observation` is constructed, before it ever reaches a rule.
- **A broken traffic law** (speeding, no seatbelt) is an expected business outcome, not an error. It never throws — it becomes a `Violation` and is billed as part of a `Fine`.

| Field | Validation |
|---|---|
| Plate Number | Not null/blank, alphanumeric, 3–10 characters |
| Date | Not null, not in the future |
| Vehicle Type | Not null |
| Speed | 0–300 km/h |
| Seatbelt | Any boolean — no validation needed |

---

## Build & Run

Requires JDK 11+ (tested on JDK 21).

```bash
# from the project root
javac -d out $(find src -name "*.java")
java -cp out Main
```

---

## Usage

```java
List<Rule> rules = Arrays.asList(
    new SeatbeltRule(),
    new PrivateCarSpeedRule(),
    new TruckSpeedRule(),
    new BusSpeedRule()
);

QuRadar radar = new QuRadar(rules);
FineReporter reporter = new ConsoleFineReporter();

Optional<Fine> fine = radar.observe(new Observation(
    "ABC1234", LocalDateTime.now(), VehicleType.PRIVATE, 94, false));

fine.ifPresent(reporter::reportFine);

reporter.reportAllFines(radar.getAllPossibleFines());
reporter.reportViolationStatistics(radar.getViolatedRulesCount());
```

---

## Extending the System

Adding a new rule never touches `QuRadar`. Example — a rule for emergency vehicles that are exempt from the speed limit but still need seatbelts checked separately isn't even necessary; but for a genuinely new law, e.g. taxis:

```java
package radar.rule;

import radar.model.VehicleType;

public class TaxiSpeedRule extends SpeedRule {
    public TaxiSpeedRule() {
        super(VehicleType.TAXI, 75, 9);
    }

    @Override
    public String getName() {
        return "Taxi Speed";
    }
}
```

Then wire it in wherever rules are assembled (`Main`, or any other composition root):

```java
radar.addRule(new TaxiSpeedRule());
```

`QuRadar.java` stays exactly as it is. This is the Open/Closed Principle in practice, via the **Strategy pattern** (`Rule` as the interchangeable strategy).
