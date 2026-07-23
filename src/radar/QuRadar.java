// src/radar/QuRadar.java
package radar;

import radar.fine.Fine;
import radar.rule.Rule;
import radar.rule.SeatbeltRule;
import radar.rule.PrivateCarSpeedRule;
import radar.rule.TruckSpeedRule;
import radar.rule.BusSpeedRule;
import radar.violation.Violation;
import radar.violation.ViolationType;

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
 * The system is designed to be easily extended with new rules without modifying
 * existing code - simply add new Rule implementations to the rules list.
 *
 * @author [Your Name]
 * @version 1.0
 */
public class QuRadar {

    private final List<Rule> rules;
    private final Map<String, List<Fine>> finesByPlate;
    private final Map<ViolationType, Integer> violationCounts;

    /**
     * Constructs a new QuRadar with default rules registered.
     */
    public QuRadar() {
        this.rules = new ArrayList<>();
        this.finesByPlate = new HashMap<>();
        this.violationCounts = new HashMap<>();

        // Register default rules
        registerDefaultRules();
    }

    /**
     * Registers the default set of traffic rules.
     * New rules can be added here or via addRule() method.
     */
    private void registerDefaultRules() {
        rules.add(new SeatbeltRule());
        rules.add(new PrivateCarSpeedRule());
        rules.add(new TruckSpeedRule());
        rules.add(new BusSpeedRule());
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
     * Checks all applicable rules and generates violations and fines if needed.
     *
     * @param observation The observation from the radar system
     * @throws IllegalArgumentException if observation is null
     */
    public void observe(Observation observation) {
        if (observation == null) {
            throw new IllegalArgumentException("Observation cannot be null");
        }

        List<Violation> violations = checkRules(observation);

        if (!violations.isEmpty()) {
            // Create and store the fine
            Fine fine = new Fine(observation.getPlateNumber(), violations);
            storeFine(fine);

            // Update violation statistics
            updateViolationCounts(violations);

            // Print the fine (as specified in requirements)
            fine.print();
        }
    }

    /**
     * Checks all registered rules against an observation.
     *
     * @param observation The observation to check
     * @return List of violations found
     */
    private List<Violation> checkRules(Observation observation) {
        List<Violation> violations = new ArrayList<>();

        for (Rule rule : rules) {
            Optional<Violation> violation = rule.check(observation);
            violation.ifPresent(violations::add);
        }

        return violations;
    }

    /**
     * Stores a fine in the system, indexed by plate number.
     */
    private void storeFine(Fine fine) {
        finesByPlate.computeIfAbsent(fine.getPlateNumber(),
                k -> new ArrayList<>()).add(fine);
    }

    /**
     * Updates the violation count statistics.
     */
    private void updateViolationCounts(List<Violation> violations) {
        for (Violation violation : violations) {
            violationCounts.merge(violation.getType(), 1, Integer::sum);
        }
    }

    /**
     * Gets all fines issued, with total amount per plate number.
     * Required method from specification.
     * Output format: "PLATE -> TOTAL_AMOUNT"
     *
     * @return Map of plate number to total fine amount
     */
    public Map<String, Integer> getAllPossibleFines() {
        Map<String, Integer> totalFines = new HashMap<>();

        for (Map.Entry<String, List<Fine>> entry : finesByPlate.entrySet()) {
            String plateNumber = entry.getKey();
            int totalAmount = entry.getValue().stream()
                    .mapToInt(Fine::getTotalAmount)
                    .sum();
            totalFines.put(plateNumber, totalAmount);
        }

        return totalFines;
    }

    /**
     * Gets count of each violation type that has been detected.
     * Required method from specification.
     *
     * @return Map of violation type to count
     */
    public Map<ViolationType, Integer> getViolatedRulesCount() {
        return new HashMap<>(violationCounts);
    }

    /**
     * Prints all fines in the required format.
     */
    public void printAllFines() {
        Map<String, Integer> allFines = getAllPossibleFines();
        if (allFines.isEmpty()) {
            System.out.println("No fines issued yet.");
            return;
        }

        for (Map.Entry<String, Integer> entry : allFines.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    /**
     * Prints all violation statistics.
     */
    public void printViolationStatistics() {
        Map<ViolationType, Integer> counts = getViolatedRulesCount();
        if (counts.isEmpty()) {
            System.out.println("No violations detected yet.");
            return;
        }

        for (Map.Entry<ViolationType, Integer> entry : counts.entrySet()) {
            String violationName = formatViolationType(entry.getKey());
            System.out.println(violationName + " : " + entry.getValue());
        }
    }

    /**
     * Formats the violation type for human-readable output.
     */
    private String formatViolationType(ViolationType type) {
        switch (type) {
            case SEATBELT_NOT_FASTENED:
                return "Seatbelt";
            case SPEED_EXCEEDED:
                return "Speed Exceeded";
            default:
                return type.name().replace('_', ' ');
        }
    }
}