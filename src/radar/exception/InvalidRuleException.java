// src/radar/exception/InvalidRuleException.java
package radar.exception;

/**
 * Raised when QuRadar is asked to enforce a rule that cannot actually
 * be used - currently just "null rule", but kept as its own type so a
 * future check (e.g. a rule with a blank getName()) has somewhere to
 * report to without overloading InvalidObservationException, which is
 * about radar data, not rule configuration.
 */
public class InvalidRuleException extends RadarException {

    public InvalidRuleException(String message) {
        super(message);
    }
}
