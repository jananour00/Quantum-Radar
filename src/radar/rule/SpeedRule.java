// src/radar/rule/SpeedRule.java
package radar.rule;

import radar.model.Observation;
import radar.model.VehicleType;
import radar.model.Violation;
import java.util.Optional;

// shared logic for "vehicle type X can't go over Y km/h" rules - the private
// car, truck and bus speed rules were all basically copy-pasted before this,
// just with different numbers, so pulled the common part out here
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
