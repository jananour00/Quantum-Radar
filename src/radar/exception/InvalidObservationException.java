// src/radar/exception/InvalidObservationException.java
package radar.exception;

// thrown when a value is there but doesn't make sense - bad plate format,
// negative speed, a date in the future, etc.
public class InvalidObservationException extends RadarException {

    public InvalidObservationException(String message) {
        super(message);
    }
}
