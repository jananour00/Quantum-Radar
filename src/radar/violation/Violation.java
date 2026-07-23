// src/radar/violation/Violation.java
package radar.violation;

import java.util.Objects;

/**
 * Represents a traffic rule violation.
 * Contains the type of violation and a human-readable description.
 */
public class Violation {
    private final ViolationType type;
    private final String description;
    private final int fee;

    public Violation(ViolationType type, String description, int fee) {
        if (type == null) {
            throw new IllegalArgumentException("Violation type cannot be null");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Violation description cannot be empty");
        }
        if (fee < 0) {
            throw new IllegalArgumentException("Fee cannot be negative");
        }

        this.type = type;
        this.description = description.trim();
        this.fee = fee;
    }

    public ViolationType getType() { return type; }
    public String getDescription() { return description; }
    public int getFee() { return fee; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Violation violation = (Violation) o;
        return fee == violation.fee &&
                type == violation.type &&
                Objects.equals(description, violation.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, description, fee);
    }

    @Override
    public String toString() {
        return String.format("%s : %d EGP", description, fee);
    }
}