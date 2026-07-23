// src/radar/model/Observation.java
package radar.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a single observation from the physical radar.
 * Contains all data captured about a vehicle at a specific moment.
 * Immutable and self-validating.
 */
public class Observation {

    private final String plateNumber;
    private final LocalDateTime date;
    private final VehicleType vehicleType;
    private final int speed;
    private final boolean seatbeltFastened;

    /**
     * Constructs a new Observation with full validation.
     *
     * @param plateNumber Vehicle plate number (alphanumeric, 3+ chars)
     * @param date Date and time of observation (must be past or present)
     * @param vehicleType Type of vehicle (cannot be null)
     * @param speed Speed in km/h (0-300)
     * @param seatbeltFastened Whether seatbelt is fastened
     * @throws IllegalArgumentException if any validation fails
     */
    public Observation(String plateNumber, LocalDateTime date,
                       VehicleType vehicleType, int speed, boolean seatbeltFastened) {
        validatePlateNumber(plateNumber);
        validateDate(date);
        validateVehicleType(vehicleType);
        validateSpeed(speed);

        this.plateNumber = plateNumber.trim().toUpperCase();
        this.date = date;
        this.vehicleType = vehicleType;
        this.speed = speed;
        this.seatbeltFastened = seatbeltFastened;
    }

    private void validatePlateNumber(String plateNumber) {
        if (plateNumber == null || plateNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Plate number cannot be null or empty");
        }
        if (!plateNumber.trim().matches("^[A-Za-z0-9]{3,10}$")) {
            throw new IllegalArgumentException(
                    "Invalid plate number format. Must be alphanumeric with 3-10 characters"
            );
        }
    }

    private void validateDate(LocalDateTime date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (date.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Date cannot be in the future");
        }
    }

    private void validateVehicleType(VehicleType vehicleType) {
        if (vehicleType == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }
    }

    private void validateSpeed(int speed) {
        if (speed < 0) {
            throw new IllegalArgumentException("Speed cannot be negative");
        }
        if (speed > 300) {
            throw new IllegalArgumentException("Speed exceeds realistic maximum of 300 km/h");
        }
    }

    // Getters
    public String getPlateNumber() { return plateNumber; }
    public LocalDateTime getDate() { return date; }
    public VehicleType getVehicleType() { return vehicleType; }
    public int getSpeed() { return speed; }
    public boolean isSeatbeltFastened() { return seatbeltFastened; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Observation that = (Observation) o;
        return speed == that.speed &&
                seatbeltFastened == that.seatbeltFastened &&
                Objects.equals(plateNumber, that.plateNumber) &&
                Objects.equals(date, that.date) &&
                vehicleType == that.vehicleType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(plateNumber, date, vehicleType, speed, seatbeltFastened);
    }

    @Override
    public String toString() {
        return String.format("Observation[plate=%s, date=%s, type=%s, speed=%d km/h, seatbelt=%s]",
                plateNumber, date, vehicleType, speed, seatbeltFastened ? "fastened" : "not fastened");
    }
}