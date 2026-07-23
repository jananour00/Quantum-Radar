// src/radar/model/Fine.java
package radar.model;

import radar.violation.Violation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a traffic fine issued to a vehicle.
 * Contains all violations for a single observation and calculates total amount.
 */
public class Fine {

    private final String plateNumber;
    private final List<Violation> violations;
    private final int totalAmount;

    /**
     * Constructs a new Fine with the given violations.
     *
     * @param plateNumber The vehicle plate number
     * @param violations List of violations (must not be empty)
     * @throws IllegalArgumentException if plateNumber is empty or violations is empty
     */
    public Fine(String plateNumber, List<Violation> violations) {
        if (plateNumber == null || plateNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Plate number cannot be null or empty");
        }
        if (violations == null || violations.isEmpty()) {
            throw new IllegalArgumentException("Fine must have at least one violation");
        }

        this.plateNumber = plateNumber.trim().toUpperCase();
        this.violations = new ArrayList<>(violations);
        this.totalAmount = violations.stream().mapToInt(Violation::getFee).sum();
    }

    public String getPlateNumber() { return plateNumber; }
    public List<Violation> getViolations() { return Collections.unmodifiableList(violations); }
    public int getTotalAmount() { return totalAmount; }

    /**
     * Prints the fine in the exact format required by the specification.
     */
    public void print() {
        StringBuilder sb = new StringBuilder();
        sb.append("Traffic for car ").append(plateNumber).append("\n");
        sb.append("Total amount: ").append(totalAmount).append(" EGP\n");
        sb.append("Violations:\n");

        for (Violation violation : violations) {
            sb.append("- ").append(violation).append("\n");
        }

        System.out.print(sb.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fine fine = (Fine) o;
        return totalAmount == fine.totalAmount &&
                Objects.equals(plateNumber, fine.plateNumber) &&
                Objects.equals(violations, fine.violations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plateNumber, violations, totalAmount);
    }

    @Override
    public String toString() {
        return String.format("Fine[plate=%s, violations=%d, total=%d EGP]",
                plateNumber, violations.size(), totalAmount);
    }
}