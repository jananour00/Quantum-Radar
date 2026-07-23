// src/radar/exception/InvalidRuleException.java
package radar.exception;

// thrown for bad rule config (right now just "null rule") - separate from
// InvalidObservationException since this is about rule setup, not radar data
public class InvalidRuleException extends RadarException {

    public InvalidRuleException(String message) {
        super(message);
    }
}
