package pricing;

/**
 * Task 1b: a named rule that takes a fixed amount off the price.
 * It extends NamedRule - a flat discount IS a named rule - and inherits
 * describe() and the default label() without repeating them.
 */
public class FlatDiscount extends NamedRule {

    private final double amount;

    public FlatDiscount(double amount) {
        // TODO Task 1: first call the superclass constructor with the name
        // "flat discount" - super(...) must be the first statement - then
        // assign the field.
        this.amount = amount;
    }

    // Task 1: the price minus the amount, but never below 0.
    // Mark it @Override: the compiler then checks it really overrides.
    @Override
    public double apply(double price) {
        return -1; // TODO
    }
}
