package pricing;

/**
 * Tasks 5 and 6: the shop's rule toolbox - static factory methods.
 * A static factory hides WHICH implementation the caller gets; the caller
 * only ever sees a PriceRule. Nobody creates a Rules object.
 */
public final class Rules {

    private Rules() {
    }

    // Task 5a (anonymous class, then lambda): a rule that caps the price at
    // max - the price itself under the cap, otherwise max. Write it TWICE, as
    // the README says: first as an anonymous new PriceRule() { ... } class,
    // run the tests, then shrink it to a lambda - PriceRule has one abstract
    // method, so a lambda can be the whole implementation.
    public static PriceRule capAt(double max) {
        return null; // TODO: anonymous class first, then the lambda
    }

    // Task 5b (a standard functional interface): a rule that applies the
    // given rule only when the condition holds for the price, and otherwise
    // leaves the price unchanged. Predicate is java.util.function's ready-made
    // one-argument test; its test(value) method answers the boolean.
    public static PriceRule onlyWhen(java.util.function.Predicate<Double> condition, PriceRule rule) {
        return null; // TODO: a lambda that asks condition.test(price) first
    }

    // Task 6 (static factory): the rule for a promotion code.
    //   "flat5"     -> a FlatDiscount of 5
    //   "percent10" -> a PercentOff of 10
    //   anything else -> throw new IllegalArgumentException("unknown code: " + code)
    // The caller never learns which class it got - that is the point.
    public static PriceRule forCode(String code) {
        return null; // TODO: choose the implementation behind the interface
    }
}
