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
- [Exception Handling](#exception-handling)
- [Sample Output](#sample-output)
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
| Errors | `RadarException` (+ `InvalidObservationException`, `MissingFieldException`, `InvalidRuleException`) | Distinguishes missing data from invalid data from bad rule wiring |
| Composition root | `Main` | Wires concrete rules and the reporter together and demos the system |

---
## Results and Observations:
<img width="622" height="190" alt="image" src="https://github.com/user-attachments/assets/9234fd0d-477a-42ff-88d9-5b4abe634c27" />
<img width="537" height="157" alt="image" src="https://github.com/user-attachments/assets/a75a5f64-241f-47de-afb3-9318189578d9" />
<img width="505" height="152" alt="image" src="https://github.com/user-attachments/assets/24caef05-3351-4bd1-9794-6b9c4c73379d" />
<img width="290" height="176" alt="image" src="https://github.com/user-attachments/assets/423f522e-9415-4e91-90b9-f66d3d4b1321" /><img width="420" height="155" alt="image" src="https://github.com/user-attachments/assets/faf5dd9b-fc64-48ad-a8f3-e66f70b975a1" />








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
│       ├── exception/
│       │   ├── RadarException.java            # base type for all radar errors
│       │   ├── InvalidObservationException.java  # present but invalid value
│       │   ├── MissingFieldException.java     # required field was null/blank
│       │   └── InvalidRuleException.java      # bad Rule wiring into QuRadar
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

    class RadarException {
        <<runtime exception>>
    }
    class InvalidObservationException
    class MissingFieldException {
        -String fieldName
        +getFieldName() String
    }
    class InvalidRuleException

    RadarException <|-- InvalidObservationException
    RadarException <|-- InvalidRuleException
    InvalidObservationException <|-- MissingFieldException
    Observation ..> InvalidObservationException : throws
    Observation ..> MissingFieldException : throws
    QuRadar ..> InvalidRuleException : throws
    QuRadar ..> MissingFieldException : throws

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

Kept deliberately separate:

- **Invalid input** (blank/malformed plate, negative or unrealistic speed, future-dated observation, null vehicle type) is a data-integrity problem. It throws a `radar.exception` type at the moment `Observation` (or `Fine`/`Violation`) is constructed, before it ever reaches a rule.
- **A broken traffic law** (speeding, no seatbelt) is an expected business outcome, not an error. It never throws — it becomes a `Violation` and is billed as part of a `Fine`.

| Field | Validation | On failure |
|---|---|---|
| Plate Number | Not null/blank, alphanumeric, 3–10 characters | `MissingFieldException` (null/blank) or `InvalidObservationException` (bad format) |
| Date | Not in the future | `null` is **not** an error — defaults to `LocalDateTime.now()`. Only a future date throws `InvalidObservationException`. |
| Vehicle Type | Not null | `MissingFieldException` |
| Speed | 0–300 km/h | `InvalidObservationException` |
| Seatbelt | Any boolean | No validation needed |

---

## Exception Handling

Every error the system can raise extends `radar.exception.RadarException` (itself an unchecked `RuntimeException`), so a caller can catch broadly with one type or narrowly with a specific one:

```
RadarException                        (base — "something in the radar system failed")
├── InvalidObservationException       (a value was present but not legal)
│   └── MissingFieldException         (a required field was null/blank; carries getFieldName())
└── InvalidRuleException              (QuRadar was given a null/unusable Rule)
```

Design intent:

- **Missing vs. invalid are different problems.** `MissingFieldException` (e.g. no plate number sent at all) is distinct from `InvalidObservationException` (e.g. a plate number was sent, but it's `"A"`). A caller integrating with a real radar feed may want to log/retry these differently — a sensor that skipped a field vs. one that sent garbage.
- **Missing is not always fatal.** A `null` date isn't rejected — `Observation` treats it as "the sensor didn't stamp a time" and defaults to `LocalDateTime.now()` rather than discarding an otherwise-valid observation. Plate number and vehicle type have no sane default, so those stay required.
- **No bare `IllegalArgumentException` anywhere in the system anymore.** Every throw site names the exact `radar.exception` type, so `catch (MissingFieldException e)` and `catch (InvalidObservationException e)` are both meaningful to a caller instead of everything collapsing into one generic type.
- **Unchecked by design.** A malformed radar observation is a data problem to be handled at the boundary (validate, log, skip), not a checked exception every internal method signature would have to declare.

`Main` demonstrates catching each level of the hierarchy — narrowest first, falling back to `RadarException` — see the `=== Testing Exception Handling ===` section of its output.

---

## Sample Output

Running `Main` produces, for the first observation (`Private car, 94 km/h, no seatbelt`), exactly the format the spec requires:

```
Traffic fine for car ABC1234
Total amount: 400 EGP
Violations:
- Seatbelt not fastened : 100 EGP
- speed of 94 exceeded max allowed 80 : 300 EGP
```

And the exception-handling demo output looks like:

```
Missing plate number (null) -> MissingFieldException
  Caught MissingFieldException (field: plateNumber): plateNumber is required but was missing

Invalid plate: 'A' (too short) -> InvalidObservationException
  Caught InvalidObservationException: Invalid plate number 'A': must be alphanumeric, 3-10 characters

Missing date (null) is NOT rejected - it defaults to now():
  Accepted with date defaulted to: 2026-07-23T11:42:07.123
```

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
