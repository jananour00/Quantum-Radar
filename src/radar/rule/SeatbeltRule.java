// src/radar/rule/SeatbeltRule.java
package radar.rule;

import radar.Observation;
import radar.rule.Rule;
import radar.violation.Violation;
import radar.violation.ViolationType;
import java.util.Optional;

/**
 * Rule that requires all passengers to have seatbelts fastened.
 * Applies to all vehicle types.
 */
public class SeatbeltRule implements Rule {

    private static final int FEE = 100;
    private static final String DESCRIPTION = "Seatbelt not fastened";

    @Override
    public Optional<Violation> check(Observation observation) {
        if (!observation.isSeatbeltFastened()) {
            return Optional.of(new Violation(
                    ViolationType.SEATBELT_NOT_FASTENED,
                    DESCRIPTION,
                    FEE
            ));
        }
        return Optional.empty();
    }
}