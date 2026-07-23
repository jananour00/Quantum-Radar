// src/radar/exception/RadarException.java
package radar.exception;

/**
 * Base type for every exception raised by the QuRadar system.
 *
 * Callers that just want to catch "something went wrong in the radar
 * system" can catch this one type; callers that care about the exact
 * failure (missing field vs. invalid range vs. bad rule) can catch the
 * more specific subclasses below. Kept unchecked (extends RuntimeException)
 * because a bad Observation is a data problem, not a recoverable control-flow
 * condition the caller is forced to handle line-by-line.
 */
public class RadarException extends RuntimeException {

    public RadarException(String message) {
        super(message);
    }

    public RadarException(String message, Throwable cause) {
        super(message, cause);
    }
}
