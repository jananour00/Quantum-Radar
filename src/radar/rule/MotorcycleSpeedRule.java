// src/radar/rule/MotorcycleSpeedRule.java
package radar.rule;

import radar.model.VehicleType;

/**
 * Speed limit rule for motorcycles.
 * Speed limit: 70 km/h
 * Fee: 8 EGP per km/h over the limit
 */
public class MotorcycleSpeedRule extends SpeedRule {

    private static final int MAX_SPEED = 70;
    private static final int FEE_PER_KM_OVER = 8;

    public MotorcycleSpeedRule() {
        super(VehicleType.MOTORCYCLE, MAX_SPEED, FEE_PER_KM_OVER);
    }

    @Override
    public String getName() {
        return "Motorcycle Speed";
    }
}
