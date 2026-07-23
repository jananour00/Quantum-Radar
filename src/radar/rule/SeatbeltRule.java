// src/radar/rule/SeatbeltRule.java
package radar.rule;

import radar.model.Observation;
import radar.model.Violation;
import java.util.Optional;

/**
 * Rule that requires all passengers to have seatbelts fastened.
 * Applies to all vehicle types.
 */
public class SeatbeltRule implements Rule {

    private static final int FEE = 100;
    private static final String DESCRIPTION = "Seatbelt not fastened";
    private static final String NAME = "Seatbelt";

    @Override
    public Optional<Violation> check(Observation observation) {
        if (!observation.isSeatbeltFastened()) {
            return Optional.of(new Violation(NAME, DESCRIPTION, FEE));
        }
        return Optional.empty();
    }

    @Override
    public String getName() {
        return NAME;
    }
}
