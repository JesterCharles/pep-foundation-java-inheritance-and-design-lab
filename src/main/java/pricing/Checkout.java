package pricing;

/**
 * Task 4: the checkout itself.
 * An earlier version of this shop had FlatDiscountCheckout and
 * PercentOffCheckout extend Checkout, one subclass per promotion - every new
 * promotion meant another subclass. This version composes instead: the
 * checkout HAS a rule, passed in through the constructor, and delegates to
 * it. Any PriceRule works - a named class, an anonymous class or a lambda -
 * and a test can hand in its own rule. That is dependency inversion.
 */
public class Checkout {

    // TODO Task 4: add a private final PriceRule field and assign it in the
    // constructor below.

    public Checkout(PriceRule rule) {
        // TODO Task 4: store the rule in the field
    }

    // Task 4: the final price of the order: the rule applied to its amount.
    public double total(Order order) {
        return -1; // TODO: delegate to the rule
    }

    public static void main(String[] args) {
        Order mugs = new Order("A1", 20.0);
        System.out.println("Flat: " + new Checkout(new FlatDiscount(5)).total(mugs));
        System.out.println("Percent: " + new Checkout(new PercentOff(10)).total(mugs));
        System.out.println("Lambda: " + new Checkout(price -> price).total(mugs));
        System.out.println("Capped: " + new Checkout(Rules.capAt(12)).total(mugs));
        System.out.println("Coded: " + new Checkout(Rules.forCode("flat5")).total(mugs));
        System.out.println("Labels: " + new FlatDiscount(5).label() + " / " + new PercentOff(10).label());
        System.out.println("Describe: " + new FlatDiscount(5).describe());
        System.out.println("Order: " + mugs);
    }
}
