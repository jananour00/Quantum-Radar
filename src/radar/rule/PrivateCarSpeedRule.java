// src/radar/rule/PrivateCarSpeedRule.java
package radar.rule;

import radar.model.VehicleType;

/**
 * Speed limit rule for private cars.
 * Speed limit: 80 km/h
 * Fee: 10 EGP per km/h over the limit
 */
public class PrivateCarSpeedRule extends SpeedRule {

    private static final int MAX_SPEED = 80;
    private static final int FEE_PER_KM_OVER = 10;

    public PrivateCarSpeedRule() {
        super(VehicleType.PRIVATE, MAX_SPEED, FEE_PER_KM_OVER);
    }

    @Override
    public String getName() {
        return "Private Speed";
    }
}
