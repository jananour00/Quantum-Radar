// src/radar/model/Violation.java
package radar.model;

import java.util.Objects;

/**
 * Represents a traffic rule violation.
 *
 * Deliberately identifies itself by ruleName (a plain String supplied by
 * the Rule that detected it) rather than a closed enum. A fixed enum of
 * violation "types" plus a switch statement elsewhere in the system is
 * itself an Open/Closed Principle violation: every new rule would force
 * a change to that enum and to whatever switches on it. Since each Rule
 * already knows its own display name (Rule#getName), Violation simply
 * carries that name through — no central registry to edit.
 */
public class Violation {
    private final String ruleName;
    private final String description;
    private final int fee;

    public Violation(String ruleName, String description, int fee) {
        if (ruleName == null || ruleName.trim().isEmpty()) {
            throw new IllegalArgumentException("Rule name cannot be null or empty");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Violation description cannot be empty");
        }
        if (fee < 0) {
            throw new IllegalArgumentException("Fee cannot be negative");
        }

        this.ruleName = ruleName.trim();
        this.description = description.trim();
        this.fee = fee;
    }

    public String getRuleName() { return ruleName; }
    public String getDescription() { return description; }
    public int getFee() { return fee; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Violation violation = (Violation) o;
        return fee == violation.fee &&
                Objects.equals(ruleName, violation.ruleName) &&
                Objects.equals(description, violation.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleName, description, fee);
    }

    @Override
    public String toString() {
        return String.format("%s : %d EGP", description, fee);
    }
}