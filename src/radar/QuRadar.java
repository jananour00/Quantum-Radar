// src/radar/QuRadar.java
package radar;

import radar.exception.InvalidRuleException;
import radar.exception.MissingFieldException;
import radar.model.Fine;
import radar.model.Observation;
import radar.model.Violation;
import radar.rule.Rule;

import java.util.*;

/**
 * Takes observations from the radar, runs them against whatever rules
 * it was given, and keeps track of fines + how often each rule gets broken.
 *
 * Rules are passed in through the constructor instead of being hardcoded here,
 * so adding a new rule (like MotorcycleSpeedRule) doesn't mean touching this class.
 * Doesn't print anything itself - that's handled in radar.report.
 */
public class QuRadar {

    private final List<Rule> rules;
    private final List<Fine> issuedFines;
    private final Map<String, Integer> violatedRuleCounts;

    // rules come from the caller (see Main) - QuRadar just enforces them
    public QuRadar(List<Rule> rules) {
        if (rules == null) {
            throw new InvalidRuleException("Rules list cannot be null");
        }
        this.rules = new ArrayList<>(rules);
        this.issuedFines = new ArrayList<>();
        this.violatedRuleCounts = new LinkedHashMap<>();
    }

    // lets you plug in another rule after construction, no changes needed elsewhere
    public void addRule(Rule rule) {
        if (rule == null) {
            throw new InvalidRuleException("Rule cannot be null");
        }
        rules.add(rule);
    }

    // checks one observation against all rules, issues a fine if anything failed
    public Optional<Fine> observe(Observation observation) {
        if (observation == null) {
            throw new MissingFieldException("observation");
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

    // total fine amount per plate, e.g. "ABC123 -> 500"
    public Map<String, Integer> getAllPossibleFines() {
        Map<String, Integer> totalFines = new LinkedHashMap<>();
        for (Fine fine : issuedFines) {
            totalFines.merge(fine.getPlateNumber(), fine.getTotalAmount(), Integer::sum);
        }
        return totalFines;
    }

    // full fine history in order, with violation details (not just the totals above)
    public List<Fine> getIssuedFines() {
        return Collections.unmodifiableList(issuedFines);
    }

    // how many times each rule got broken
    public Map<String, Integer> getViolatedRulesCount() {
        return new LinkedHashMap<>(violatedRuleCounts);
    }
}
