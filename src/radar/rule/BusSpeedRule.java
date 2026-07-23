// src/radar/rule/BusSpeedRule.java
package radar.rule;

import radar.Observation;
import radar.VehicleType;
import radar.violation.Violation;
import radar.violation.ViolationType;
import java.util.Optional;

/**
 * Speed limit rule specifically for buses.
 */
public class BusSpeedRule implements Rule {
    private static final int MAX_SPEED = 70;
    private static final int FEE_PER_KM_OVER = 12;
    private static final ViolationType TYPE = ViolationType.SPEED_EXCEEDED;

    @Override
    public Optional<Violation> check(Observation observation) {
        if (observation.getVehicleType() == VehicleType.BUS
                && observation.getSpeed() > MAX_SPEED) {

            int exceededBy = observation.getSpeed() - MAX_SPEED;
            int fee = exceededBy * FEE_PER_KM_OVER;
            String description = String.format("speed of %d exceeded max allowed %d (Bus)",
                    observation.getSpeed(), MAX_SPEED);

            return Optional.of(new Violation(TYPE, description, fee));
        }
        return Optional.empty();
    }
}