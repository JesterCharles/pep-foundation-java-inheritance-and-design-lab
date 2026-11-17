package pricing;

/**
 * The contract every price rule of the shop's checkout fulfils: take a price,
 * return the price after the rule. This interface is complete - the tasks
 * implement it in different ways and compose it.
 *
 * It has exactly one abstract method, so it is a functional interface: a
 * lambda such as {@code price -> price - 1} is a PriceRule too.
 *
 * label() is a default method: an implementing class gets it for free and may
 * replace it with its own version.
 */
public interface PriceRule {

    /** The price after this rule has been applied. */
    double apply(double price);

    /** A short display name; implementations may override it. */
    default String label() {
        return "price rule";
    }
}
