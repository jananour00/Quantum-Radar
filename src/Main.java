// src/Main.java
import radar.QuRadar;
import radar.model.Fine;
import radar.model.Observation;
import radar.model.VehicleType;
import radar.report.ConsoleFineReporter;
import radar.report.FineReporter;
import radar.rule.BusSpeedRule;
import radar.rule.PrivateCarSpeedRule;
import radar.rule.Rule;
import radar.rule.SeatbeltRule;
import radar.rule.TruckSpeedRule;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Composition root and demo for the QuRadar system.
 *
 * This is the one place in the codebase that knows about concrete Rule
 * classes and the concrete ConsoleFineReporter. It assembles them and
 * injects them into QuRadar, which otherwise depends only on the Rule
 * and FineReporter abstractions (Dependency Inversion Principle).
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("           QuRadar - Traffic Violation System");
        System.out.println("============================================================\n");

        List<Rule> rules = Arrays.asList(
                new SeatbeltRule(),
                new PrivateCarSpeedRule(),
                new TruckSpeedRule(),
                new BusSpeedRule()
        );
        QuRadar radar = new QuRadar(rules);
        FineReporter reporter = new ConsoleFineReporter();

        System.out.println("--- Processing Radar Observations ---\n");

        observeAndReport(radar, reporter,
                "Observation 1: Private car, 94 km/h, no seatbelt",
                new Observation("ABC1234", LocalDateTime.now(), VehicleType.PRIVATE, 94, false));

        observeAndReport(radar, reporter,
                "Observation 2: Truck, 72 km/h, seatbelt on",
                new Observation("XYZ7890", LocalDateTime.now(), VehicleType.TRUCK, 72, true));

        observeAndReport(radar, reporter,
                "Observation 3: Bus, 65 km/h, no seatbelt",
                new Observation("BUS9999", LocalDateTime.now(), VehicleType.BUS, 65, false));

        observeAndReport(radar, reporter,
                "Observation 4: Private car, 70 km/h, seatbelt on - NO VIOLATIONS",
                new Observation("GOODCAR", LocalDateTime.now(), VehicleType.PRIVATE, 70, true));

        observeAndReport(radar, reporter,
                "Observation 5: Private car, 95 km/h, seatbelt on",
                new Observation("SPEEDR", LocalDateTime.now(), VehicleType.PRIVATE, 95, true));

        System.out.println("=== All Fines Issued ===");
        reporter.reportAllFines(radar.getAllPossibleFines());
        System.out.println();

        System.out.println("=== Violation Statistics ===");
        reporter.reportViolationStatistics(radar.getViolatedRulesCount());
        System.out.println();

        System.out.println("=== Programmatic Access ===");
        Map<String, Integer> allFines = radar.getAllPossibleFines();
        System.out.println("Fines Map: " + allFines);
        Map<String, Integer> stats = radar.getViolatedRulesCount();
        System.out.println("Statistics: " + stats);
        System.out.println();

        System.out.println("=== Testing Input Validation ===");
        rejectInvalid("Attempting invalid plate: 'A' (too short)",
                () -> new Observation("A", LocalDateTime.now(), VehicleType.PRIVATE, 50, true));
        rejectInvalid("Attempting negative speed: -10",
                () -> new Observation("TEST123", LocalDateTime.now(), VehicleType.PRIVATE, -10, true));
        System.out.println();

        System.out.println("=== Demonstrating Extensibility ===");
        System.out.println("System is extensible - add new rules via radar.addRule(new YourRule())");
        System.out.println("No modification to QuRadar needed for new rules!\n");

        System.out.println("=== Demonstration Complete ===");
    }

    private static void observeAndReport(QuRadar radar, FineReporter reporter, String label, Observation observation) {
        System.out.println(label);
        Optional<Fine> fine = radar.observe(observation);
        fine.ifPresent(reporter::reportFine);
        System.out.println();
    }

    private static void rejectInvalid(String label, ObservationSupplier supplier) {
        System.out.println(label);
        try {
            supplier.get();
            System.out.println("Unexpectedly accepted invalid input!");
        } catch (IllegalArgumentException e) {
            System.out.println("Correctly rejected: " + e.getMessage());
        }
    }

    @FunctionalInterface
    private interface ObservationSupplier {
        Observation get();
    }
}
