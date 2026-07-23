// src/radar/rule/Rule.java
package radar.rule;

import radar.model.Observation;
import radar.model.Violation;
import java.util.Optional;

// one traffic rule - each implementation checks one thing (speed limit,
// seatbelt, etc.) so new rules can just be added without touching QuRadar
public interface Rule {

    // returns a Violation if the observation breaks this rule, empty otherwise
    Optional<Violation> check(Observation observation);

    String getName();
}
