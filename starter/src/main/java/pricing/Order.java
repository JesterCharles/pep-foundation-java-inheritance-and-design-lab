package pricing;

/**
 * Task 3: one order at the checkout, as a record.
 * A record is the right fit here: an order is plain immutable data - an id
 * and an amount - and the record gives you the constructor, accessors,
 * equals, hashCode and toString in one line.
 *
 * TODO Task 3: turn this class into
 *   public record Order(String id, double amount) { ... }
 * and add a compact canonical constructor that rejects a negative amount with
 *   throw new IllegalArgumentException("amount must not be negative");
 * Delete everything the record now provides by itself.
 */
public class Order {

    public Order(String id, double amount) {
        // TODO Task 3: replaced by the record's compact canonical constructor
    }

    public String id() {
        return "?"; // TODO Task 3: the record provides this accessor
    }

    public double amount() {
        return -1; // TODO Task 3: the record provides this accessor
    }
}
