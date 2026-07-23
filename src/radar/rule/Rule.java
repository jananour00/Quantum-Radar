// src/radar/rule/Rule.java
package radar.rule;

import radar.model.Observation;
import radar.model.Violation;
import java.util.Optional;

/**
 * Interface for all traffic rules.
 * Following Open/Closed Principle - new rules can be added without modifying QuRadar.
 *
 * This is the Strategy pattern in action.
 */
public interface Rule {

    /**
     * Checks if the given observation violates this rule.
     *
     * @param observation The observation to check
     * @return Optional containing a Violation if the rule is broken, empty otherwise
     */
    Optional<Violation> check(Observation observation);


    String getName();
}
