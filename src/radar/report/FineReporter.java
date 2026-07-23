// src/radar/report/FineReporter.java
package radar.report;

import radar.model.Fine;

import java.util.Map;

// handles displaying fines/stats, so QuRadar doesn't need to care whether
// that's printing to console, writing to a file, or something else later
public interface FineReporter {

    // prints one fine, e.g.:
    // Traffic fine for car ABC1234
    // Total amount: 400 EGP
    // Violations:
    // - Seatbelt not fastened : 100 EGP
    void reportFine(Fine fine);

    // plate -> total amount, for every fine issued so far
    void reportAllFines(Map<String, Integer> finesByPlate);

    // how many times each rule has been broken so far
    void reportViolationStatistics(Map<String, Integer> violationCounts);
}
