// src/radar/model/Violation.java
package radar.model;

import radar.exception.InvalidObservationException;
import radar.exception.MissingFieldException;

import java.util.Objects;

// one broken rule - name, description, fee.
// ruleName is just a string from whatever Rule caught it, not an enum,
// so adding a new rule never means updating some central list of violation types
public class Violation {
    private final String ruleName;
    private final String description;
    private final int fee;

    public Violation(String ruleName, String description, int fee) {
        if (ruleName == null || ruleName.trim().isEmpty()) {
            throw new MissingFieldException("ruleName");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new MissingFieldException("description");
        }
        if (fee < 0) {
            throw new InvalidObservationException("Fee cannot be negative: " + fee);
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