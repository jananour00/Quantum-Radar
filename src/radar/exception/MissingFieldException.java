// src/radar/exception/MissingFieldException.java
package radar.exception;

// thrown when a required field is just missing (null/blank), not merely invalid -
// kept separate from InvalidObservationException so it's easy to tell "nothing sent"
// apart from "sent something, but it's garbage"
public class MissingFieldException extends InvalidObservationException {

    private final String fieldName;

    public MissingFieldException(String fieldName) {
        super(fieldName + " is required but was missing");
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
