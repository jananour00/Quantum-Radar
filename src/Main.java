// src/Main.java
import radar.Observation;
import radar.QuRadar;
import radar.VehicleType;
import radar.violation.ViolationType;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Main application to demonstrate the QuRadar system.
 * Shows how observations are processed, fines are generated,
 * and statistics are maintained.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("           QuRadar - Traffic Violation System");
        System.out.println("============================================================\n");

        // Create the radar system
        QuRadar radar = new QuRadar();

        System.out.println("--- Processing Radar Observations ---\n");

        // Observation 1: Private car speeding with no seatbelt
        System.out.println("Observation 1: Private car, 94 km/h, no seatbelt");
        Observation obs1 = new Observation(
                "ABC1234",
                LocalDateTime.now(),
                VehicleType.PRIVATE,
                94,
                false
        );
        radar.observe(obs1);
        System.out.println();

        // Observation 2: Truck speeding
        System.out.println("Observation 2: Truck, 72 km/h, seatbelt on");
        Observation obs2 = new Observation(
                "XYZ7890",
                LocalDateTime.now(),
                VehicleType.TRUCK,
                72,
                true
        );
        radar.observe(obs2);
        System.out.println();

        // Observation 3: Bus with seatbelt violation
        System.out.println("Observation 3: Bus, 65 km/h, no seatbelt");
        Observation obs3 = new Observation(
                "BUS9999",
                LocalDateTime.now(),
                VehicleType.BUS,
                65,
                false
        );
        radar.observe(obs3);
        System.out.println();

        // Observation 4: Perfect driver (no violations)
        System.out.println("Observation 4: Private car, 70 km/h, seatbelt on - NO VIOLATIONS");
        Observation obs4 = new Observation(
                "GOODCAR",
                LocalDateTime.now(),
                VehicleType.PRIVATE,
                70,
                true
        );
        radar.observe(obs4);
        System.out.println();

        // Observation 5: Speeding with seatbelt on
        System.out.println("Observation 5: Private car, 95 km/h, seatbelt on");
        Observation obs5 = new Observation(
                "SPEEDR",
                LocalDateTime.now(),
                VehicleType.PRIVATE,
                95,
                true
        );
        radar.observe(obs5);
        System.out.println();

        // Show all fines (Required Method)
        System.out.println("=== All Fines Issued ===");
        radar.printAllFines();
        System.out.println();

        // Show violation statistics (Required Method)
        System.out.println("=== Violation Statistics ===");
        radar.printViolationStatistics();
        System.out.println();

        // Demonstrate programmatic access
        System.out.println("=== Programmatic Access ===");
        Map<String, Integer> allFines = radar.getAllPossibleFines();
        System.out.println("Fines Map: " + allFines);

        Map<ViolationType, Integer> stats = radar.getViolatedRulesCount();
        System.out.println("Statistics: " + stats);
        System.out.println();

        // Demonstrate validation
        System.out.println("=== Testing Input Validation ===");
        try {
            System.out.println("Attempting invalid plate: 'A' (too short)");
            Observation invalidObs = new Observation(
                    "A",
                    LocalDateTime.now(),
                    VehicleType.PRIVATE,
                    50,
                    true
            );
            radar.observe(invalidObs);
        } catch (IllegalArgumentException e) {
            System.out.println("Correctly rejected: " + e.getMessage());
        }

        try {
            System.out.println("\nAttempting negative speed: -10");
            Observation invalidObs = new Observation(
                    "TEST123",
                    LocalDateTime.now(),
                    VehicleType.PRIVATE,
                    -10,
                    true
            );
            radar.observe(invalidObs);
        } catch (IllegalArgumentException e) {
            System.out.println("Correctly rejected: " + e.getMessage());
        }
        System.out.println();

        // Demonstrate extensibility
        System.out.println("=== Demonstrating Extensibility ===");
        System.out.println("System is extensible - add new rules via radar.addRule()");
        System.out.println("No modification to QuRadar needed for new rules!\n");

        System.out.println("=== Demonstration Complete ===");
    }
}