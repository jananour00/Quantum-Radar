// src/radar/rule/BusSpeedRule.java
package radar.rule;

import radar.model.VehicleType;

/**
 * Speed limit rule specifically for buses.
 * Speed limit: 70 km/h
 * Fee: 12 EGP per km/h over the limit
 */
public class BusSpeedRule extends SpeedRule {

    private static final int MAX_SPEED = 70;
    private static final int FEE_PER_KM_OVER = 12;

    public BusSpeedRule() {
        super(VehicleType.BUS, MAX_SPEED, FEE_PER_KM_OVER);
    }

    @Override
    public String getName() {
        return "Bus Speed";
    }
}
