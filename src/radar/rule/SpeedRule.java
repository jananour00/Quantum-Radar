// src/radar/rule/SpeedRule.java
package radar.rule;

import radar.model.Observation;
import radar.model.VehicleType;
import radar.model.Violation;
import java.util.Optional;

/**
 * Common shape for "this vehicle type must not exceed X speed" rules.
 * PrivateSpeedRule, TruckSpeedRule and BusSpeedRule previously repeated
 * the exact same comparison/fee-calculation logic with only the vehicle
 * type, max speed and fee-per-km-over changed. That duplication is
 * factored out here so each concrete rule only declares its own
 * numbers, and a bug fix to the comparison logic only needs to happen
 * in one place.
 */
abstract class SpeedRule implements Rule {

    private final VehicleType applicableType;
    private final int maxSpeed;
    private final int feePerKmOver;

    protected SpeedRule(VehicleType applicableType, int maxSpeed, int feePerKmOver) {
        this.applicableType = applicableType;
        this.maxSpeed = maxSpeed;
        this.feePerKmOver = feePerKmOver;
    }

    @Override
    public Optional<Violation> check(Observation observation) {
        if (observation.getVehicleType() != applicableType) {
            return Optional.empty();
        }
        if (observation.getSpeed() <= maxSpeed) {
            return Optional.empty();
        }

        int exceededBy = observation.getSpeed() - maxSpeed;
        int fee = exceededBy * feePerKmOver;
        String description = String.format(
                "speed of %d exceeded max allowed %d",
                observation.getSpeed(), maxSpeed);

        return Optional.of(new Violation(getName(), description, fee));
    }
}
