// src/radar/exception/MissingFieldException.java
package radar.exception;

/**
 * Raised when a required field on incoming radar data is missing
 * (null, or blank for Strings) rather than merely out of range.
 *
 * Kept distinct from InvalidObservationException so callers can tell
 * "the sensor sent nothing for this field" apart from "the sensor sent
 * a value, but it's not a legal one" - useful if, e.g., missing data
 * should be logged/retried differently than a bad reading.
 */
public class MissingFieldException extends InvalidObservationException {

    private final String fieldName;

    public MissingFieldException(String fieldName) {
        super(fieldName + " is required but was missing");
        this.fieldName = fieldName;
    }

    /** Name of the field that was missing, e.g. "plateNumber". */
    public String getFieldName() {
        return fieldName;
    }
}
