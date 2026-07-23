// src/radar/rule/MotorcycleSpeedRule.java
package radar.rule;

import radar.model.VehicleType;

/**
 * Speed limit rule for motorcycles.
 * Speed limit: 70 km/h
 * Fee: 8 EGP per km/h over the limit
 *
 * Added purely to demonstrate the Open/Closed Principle in practice:
 * this is a brand-new vehicle rule and neither QuRadar.java, VehicleType
 * usage elsewhere, nor any other rule class had to change - only this
 * new file, plus one new enum constant, plus one line wiring it into
 * Main's rule list.
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
