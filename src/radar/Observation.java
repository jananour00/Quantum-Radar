package radar;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a single observation from the physical radar.
 * Contains all data captured about a vehicle at a specific moment.
 */
public class Observation {
    private final String plateNumber;
    private final LocalDateTime date;
    private final VehicleType vehicleType;
    private final int speed;
    private final boolean seatbeltFastened;

    public Observation(String plateNumber, LocalDateTime date,
                       VehicleType vehicleType, int speed, boolean seatbeltFastened) {
        validatePlateNumber(plateNumber);
        validateDate(date);
        validateVehicleType(vehicleType);
        validateSpeed(speed);

        this.plateNumber = plateNumber.trim();
        this.date = date;
        this.vehicleType = vehicleType;
        this.speed = speed;
        this.seatbeltFastened = seatbeltFastened;
    }

    private void validatePlateNumber(String plateNumber) {
        if (plateNumber == null || plateNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Plate number cannot be null or empty");
        }
        // Simple format validation - alphanumeric, at least 3 characters
        if (!plateNumber.trim().matches("^[A-Z0-9]{3,}$")) {
            throw new IllegalArgumentException("Invalid plate number format. Must be alphanumeric with at least 3 characters");
        }
    }

    private void validateDate(LocalDateTime date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        // Date should be in the past or present
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
        if (speed > 300) { // Realistic maximum
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
        return String.format("Observation[plate=%s, date=%s, type=%s, speed=%d, seatbelt=%s]",
                plateNumber, date, vehicleType, speed, seatbeltFastened);
    }
}