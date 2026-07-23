// src/radar/report/FineReporter.java
package radar.report;

import radar.model.Fine;

import java.util.Map;

/**
 * Responsible for presenting fines and violation statistics.
 *
 * This exists so that QuRadar (business logic) never has to know how or
 * where results are displayed - console today, could be a file, a UI, or
 * a test spy tomorrow, without QuRadar or the Rule/Fine model changing.
 * Single Responsibility Principle: reporting is not QuRadar's job.
 */
public interface FineReporter {

    /**
     * Reports a single fine exactly as issued, in the format:
     * <pre>
     * Traffic fine for car ABC1234
     * Total amount: 400 EGP
     * Violations:
     * - Seatbelt not fastened : 100 EGP
     * - speed of 94 exceeded max allowed 80 : 300 EGP
     * </pre>
     */
    void reportFine(Fine fine);

    /**
     * Reports the plate -> total-amount summary for every fine issued so far.
     */
    void reportAllFines(Map<String, Integer> finesByPlate);

    /**
     * Reports how many times each rule has been violated so far.
     */
    void reportViolationStatistics(Map<String, Integer> violationCounts);
}
