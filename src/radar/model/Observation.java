// src/radar/model/Observation.java
package radar.model;

import radar.exception.InvalidObservationException;
import radar.exception.MissingFieldException;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a single observation from the physical radar.
 * Contains all data captured about a vehicle at a specific moment.
 * Immutable and self-validating.
 *
 * Missing-value policy: a null plateNumber or vehicleType is a hard
 * failure (there is no sane default for "which car" or "what kind of
 * vehicle"), so those raise MissingFieldException. A null date, however,
 * is treated as "the radar didn't stamp a time" and defaults to
 * LocalDateTime.now() rather than rejecting the whole observation -
 * losing a timestamp shouldn't mean losing an otherwise-valid violation.
 */
public class Observation {

    private static final int MIN_SPEED = 0;
    private static final int MAX_REALISTIC_SPEED = 300;
    private static final String PLATE_PATTERN = "^[A-Za-z0-9]{3,10}$";

    private final String plateNumber;
    private final LocalDateTime date;
    private final VehicleType vehicleType;
    private final int speed;
    private final boolean seatbeltFastened;

    /**
     * Constructs a new Observation with full validation.
     *
     * @param plateNumber Vehicle plate number (alphanumeric, 3-10 chars); required
     * @param date Date and time of observation; if null, defaults to now()
     * @param vehicleType Type of vehicle; required
     * @param speed Speed in km/h (0-300)
     * @param seatbeltFastened Whether seatbelt is fastened
     * @throws MissingFieldException if plateNumber or vehicleType is null/blank
     * @throws InvalidObservationException if a present value fails validation
     */
    public Observation(String plateNumber, LocalDateTime date,
                       VehicleType vehicleType, int speed, boolean seatbeltFastened) {
        this.plateNumber = validatePlateNumber(plateNumber);
        this.date = validateDate(date);
        this.vehicleType = validateVehicleType(vehicleType);
        this.speed = validateSpeed(speed);
        this.seatbeltFastened = seatbeltFastened;
    }

    private String validatePlateNumber(String plateNumber) {
        if (plateNumber == null || plateNumber.trim().isEmpty()) {
            throw new MissingFieldException("plateNumber");
        }
        String trimmed = plateNumber.trim();
        if (!trimmed.matches(PLATE_PATTERN)) {
            throw new InvalidObservationException(
                    "Invalid plate number '" + plateNumber + "': must be alphanumeric, 3-10 characters"
            );
        }
        return trimmed.toUpperCase();
    }

    private LocalDateTime validateDate(LocalDateTime date) {
        LocalDateTime effectiveDate = (date == null) ? LocalDateTime.now() : date;
        if (effectiveDate.isAfter(LocalDateTime.now())) {
            throw new InvalidObservationException("Observation date cannot be in the future: " + effectiveDate);
        }
        return effectiveDate;
    }

    private VehicleType validateVehicleType(VehicleType vehicleType) {
        if (vehicleType == null) {
            throw new MissingFieldException("vehicleType");
        }
        return vehicleType;
    }

    private int validateSpeed(int speed) {
        if (speed < MIN_SPEED) {
            throw new InvalidObservationException("Speed cannot be negative: " + speed);
        }
        if (speed > MAX_REALISTIC_SPEED) {
            throw new InvalidObservationException(
                    "Speed " + speed + " exceeds realistic maximum of " + MAX_REALISTIC_SPEED + " km/h");
        }
        return speed;
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