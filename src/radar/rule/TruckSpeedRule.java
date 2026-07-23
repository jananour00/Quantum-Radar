// src/radar/rule/TruckSpeedRule.java
package radar.rule;

import radar.Observation;
import radar.VehicleType;
import radar.violation.Violation;
import radar.violation.ViolationType;
import java.util.Optional;

/**
 * Speed limit rule for trucks.
 * Speed limit: 60 km/h
 * Fee: 15 EGP per km/h over the limit
 */
public class TruckSpeedRule implements Rule {

    private static final int MAX_SPEED = 60;
    private static final int FEE_PER_KM_OVER = 15;

    @Override
    public Optional<Violation> check(Observation observation) {
        if (observation.getVehicleType() == VehicleType.TRUCK
                && observation.getSpeed() > MAX_SPEED) {

            int exceededBy = observation.getSpeed() - MAX_SPEED;
            int fee = exceededBy * FEE_PER_KM_OVER;
            String description = String.format(
                    "speed of %d exceeded max allowed %d (Truck)",
                    observation.getSpeed(), MAX_SPEED
            );

            return Optional.of(new Violation(
                    ViolationType.SPEED_EXCEEDED,
                    description,
                    fee
            ));
        }
        return Optional.empty();
    }
}