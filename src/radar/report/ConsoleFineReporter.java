// src/radar/report/ConsoleFineReporter.java
package radar.report;

import radar.model.Fine;
import radar.model.Violation;

import java.util.Map;

/**
 * Prints fines and statistics to standard output. The one concrete
 * FineReporter used by Main today; swapping in a different reporter
 * (e.g. one that writes JSON to a file) never requires touching QuRadar.
 */
public class ConsoleFineReporter implements FineReporter {

    @Override
    public void reportFine(Fine fine) {
        StringBuilder sb = new StringBuilder();
        sb.append("Traffic fine for car ").append(fine.getPlateNumber()).append("\n");
        sb.append("Total amount: ").append(fine.getTotalAmount()).append(" EGP\n");
        sb.append("Violations:\n");
        for (Violation violation : fine.getViolations()) {
            sb.append("- ").append(violation).append("\n");
        }
        System.out.print(sb);
    }

    @Override
    public void reportAllFines(Map<String, Integer> finesByPlate) {
        if (finesByPlate.isEmpty()) {
            System.out.println("No fines issued yet.");
            return;
        }
        for (Map.Entry<String, Integer> entry : finesByPlate.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    @Override
    public void reportViolationStatistics(Map<String, Integer> violationCounts) {
        if (violationCounts.isEmpty()) {
            System.out.println("No violations detected yet.");
            return;
        }
        for (Map.Entry<String, Integer> entry : violationCounts.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
