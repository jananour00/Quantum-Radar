// src/radar/exception/RadarException.java
package radar.exception;

// base exception for the radar system - catch this if you just want "something broke",
// or catch one of the subclasses below if you care which kind of failure it was
public class RadarException extends RuntimeException {

    public RadarException(String message) {
        super(message);
    }

    public RadarException(String message, Throwable cause) {
        super(message, cause);
    }
}
