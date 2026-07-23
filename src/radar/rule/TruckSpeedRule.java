// src/radar/rule/TruckSpeedRule.java
package radar.rule;

import radar.model.VehicleType;

/**
 * Speed limit rule for trucks.
 * Speed limit: 60 km/h
 * Fee: 15 EGP per km/h over the limit
 */
public class TruckSpeedRule extends SpeedRule {

    private static final int MAX_SPEED = 60;
    private static final int FEE_PER_KM_OVER = 15;

    public TruckSpeedRule() {
        super(VehicleType.TRUCK, MAX_SPEED, FEE_PER_KM_OVER);
    }

    @Override
    public String getName() {
        return "Truck Speed";
    }
}
