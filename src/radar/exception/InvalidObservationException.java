// src/radar/exception/InvalidObservationException.java
package radar.exception;

/**
 * Raised when an Observation carries a value that is present but does
 * not satisfy the system's rules for that field - e.g. a plate number
 * with illegal characters, a negative speed, or a date in the future.
 */
public class InvalidObservationException extends RadarException {

    public InvalidObservationException(String message) {
        super(message);
    }
}
