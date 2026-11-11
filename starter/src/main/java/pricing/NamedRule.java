package pricing;

/**
 * Task 1a: the shared base of the named price rules.
 * Subclasses share one piece of state (the display name) and one piece of
 * code (describe()), which is why this is an abstract class and not an
 * interface. It stays abstract: it never says HOW a price changes, only that
 * every named rule has a name.
 */
public abstract class NamedRule implements PriceRule {

    // TODO Task 1: add a private final String field for the name and a
    // constructor NamedRule(String name) that assigns it. Subclasses will
    // call it with super(...).

    // TODO Task 1: declare the abstract method
    //   public abstract double apply(double price);
    // here as well. The class compiles without a body for it - that is what
    // abstract means, and why nobody can write `new NamedRule(...)`.

    // Task 1: return the name the constructor stored.
    public String describe() {
        return "?"; // TODO
    }
}
