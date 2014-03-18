package info.javaperformance.money.performance;

/**
 * Wrapper for test results
 */
public class TestResult {
    public final String name;
    public long longRate;
    public long bdRate;

    public TestResult(String name, long longRate) {
        this.name = name;
        this.longRate = longRate;
    }
}
