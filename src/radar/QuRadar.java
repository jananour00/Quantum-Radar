// src/radar/QuRadar.java
package radar;

import radar.model.Fine;
import radar.model.Observation;
import radar.model.Violation;
import radar.rule.Rule;

import java.util.*;

/**
 * Quantum Radar System - Traffic Violation Detection and Fine Management
 * =====================================================================
 *
 * QuRadar receives observations from physical radar systems and processes them
 * through a configurable set of traffic rules to detect violations and issue fines.
 *
 * Features:
 * - Handles observations with plate number, date, vehicle type, speed, and seatbelt status
 * - Applies rule-based validation using the Open/Closed Principle for extensibility
 * - Generates violations when rules are broken
 * - Issues fines with detailed violation breakdowns
 * - Provides statistics on all violations and fines issued
 *
 * AI Model Used: None (rule-based evaluation system)
 *
 * Design notes:
 * - QuRadar depends only on the Rule abstraction, never on a concrete rule
 *   class. The rules it enforces are supplied to it (constructor injection)
 *   rather than instantiated internally, so QuRadar has no idea SeatbeltRule
 *   or PrivateCarSpeedRule exist - it can run with any list of Rule
 *   implementations, current or future (Dependency Inversion Principle).
 * - QuRadar is intentionally silent: it records fines and statistics but
 *   never calls System.out itself. Presentation is the responsibility of
 *   the radar.report package (Single Responsibility Principle) - callers
 *   decide what, if anything, to print.
 */
public class QuRadar {

    private final List<Rule> rules;
    private final List<Fine> issuedFines;
    private final Map<String, Integer> violatedRuleCounts;

    /**
     * Constructs a QuRadar enforcing exactly the given rules.
     * Composition (which rules to run) is the caller's responsibility -
     * see Main for how the default rule set is assembled and injected.
     *
     * @param rules the rules to enforce; must not be null
     */
    public QuRadar(List<Rule> rules) {
        if (rules == null) {
            throw new IllegalArgumentException("Rules list cannot be null");
        }
        this.rules = new ArrayList<>(rules);
        this.issuedFines = new ArrayList<>();
        this.violatedRuleCounts = new LinkedHashMap<>();
    }

    /**
     * Adds a new rule to the system dynamically.
     * This enables extension without modifying existing code (Open/Closed Principle).
     *
     * @param rule The rule to add
     * @throws IllegalArgumentException if rule is null
     */
    public void addRule(Rule rule) {
        if (rule == null) {
            throw new IllegalArgumentException("Rule cannot be null");
        }
        rules.add(rule);
    }

    /**
     * Processes a single observation from the physical radar.
     * Checks all applicable rules and, if any are broken, issues and
     * stores a Fine. Does not print anything - see radar.report for that.
     *
     * @param observation The observation from the radar system
     * @return the Fine issued for this observation, or empty if there were no violations
     * @throws IllegalArgumentException if observation is null
     */
    public Optional<Fine> observe(Observation observation) {
        if (observation == null) {
            throw new IllegalArgumentException("Observation cannot be null");
        }

        List<Violation> violations = checkRules(observation);
        if (violations.isEmpty()) {
            return Optional.empty();
        }

        Fine fine = new Fine(observation.getPlateNumber(), violations);
        issuedFines.add(fine);
        updateViolationCounts(violations);
        return Optional.of(fine);
    }

    private List<Violation> checkRules(Observation observation) {
        List<Violation> violations = new ArrayList<>();
        for (Rule rule : rules) {
            rule.check(observation).ifPresent(violations::add);
        }
        return violations;
    }

    private void updateViolationCounts(List<Violation> violations) {
        for (Violation violation : violations) {
            violatedRuleCounts.merge(violation.getRuleName(), 1, Integer::sum);
        }
    }

    /**
     * Gets all fines issued, with total amount per plate number.
     * Required method from specification. Output format: "PLATE -> TOTAL_AMOUNT"
     *
     * @return Map of plate number to total fine amount
     */
    public Map<String, Integer> getAllPossibleFines() {
        Map<String, Integer> totalFines = new LinkedHashMap<>();
        for (Fine fine : issuedFines) {
            totalFines.merge(fine.getPlateNumber(), fine.getTotalAmount(), Integer::sum);
        }
        return totalFines;
    }

    /**
     * Returns every fine issued so far, in the order they occurred, with
     * full violation detail. Useful for reporting layers that need more
     * than the plate/total summary from getAllPossibleFines().
     */
    public List<Fine> getIssuedFines() {
        return Collections.unmodifiableList(issuedFines);
    }

    /**
     * Gets count of how many times each rule (by Rule#getName) has been
     * violated. Required method from specification.
     *
     * @return Map of rule name to violation count
     */
    public Map<String, Integer> getViolatedRulesCount() {
        return new LinkedHashMap<>(violatedRuleCounts);
    }
}
