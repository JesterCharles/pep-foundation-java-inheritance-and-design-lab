package pricing;

/**
 * Task 2: a rule that takes a percentage off, implementing the interface
 * directly - it needs no shared name state, so the abstract class would be
 * dead weight. It also replaces the interface's default label() with its own.
 */
public class PercentOff implements PriceRule {

    private final double percent;

    public PercentOff(double percent) {
        this.percent = percent;
    }

    // Task 2: the price reduced by the percentage.
    // PercentOff of 10 applied to 200.0 is 180.0.
    @Override
    public double apply(double price) {
        return -1; // TODO
    }

    // TODO Task 2: override label() to return "percent off". FlatDiscount
    // keeps the interface's default; this class replaces it - run the tests
    // and compare the two.
}
