package pricing;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Public tests for the Inheritance and Design lab. Each test checks visible
 * behavior: what the rules, record and checkout return.
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
class PricingTest {

    @Test
    void task1AbstractBase() {
        FlatDiscount flat = new FlatDiscount(5);
        assertAll(
                () -> assertEquals(15.0, flat.apply(20.0), "Task 1: new FlatDiscount(5).apply(20.0)"),
                () -> assertEquals(0.0, flat.apply(3.0),
                        "Task 1: a flat discount never pushes the price below 0"),
                () -> assertEquals("flat discount", flat.describe(),
                        "Task 1: describe() returns the name given to super(...)"),
                () -> assertEquals("price rule", flat.label(),
                        "Task 1: FlatDiscount keeps the interface's default label()"));
    }

    @Test
    void task2InterfaceImplementation() {
        PercentOff tenOff = new PercentOff(10);
        assertAll(
                () -> assertEquals(180.0, tenOff.apply(200.0), "Task 2: new PercentOff(10).apply(200.0)"),
                () -> assertEquals(50.0, new PercentOff(50).apply(100.0),
                        "Task 2: new PercentOff(50).apply(100.0)"),
                () -> assertEquals(0.0, new PercentOff(100).apply(80.0),
                        "Task 2: new PercentOff(100).apply(80.0)"),
                () -> assertEquals("percent off", tenOff.label(),
                        "Task 2: PercentOff replaces the default label() with its own"));
    }

    @Test
    void task3OrderRecord() {
        Order mugs = new Order("A1", 20.0);
        Order sameData = new Order("A1", 20.0);
        Order other = new Order("A2", 20.0);
        assertAll(
                () -> assertEquals("A1", mugs.id(), "Task 3: id() accessor"),
                () -> assertEquals(20.0, mugs.amount(), "Task 3: amount() accessor"),
                () -> assertEquals(mugs, sameData,
                        "Task 3: records compare by their data, so equal data means equal orders"),
                () -> assertNotEquals(mugs, other, "Task 3: different ids are different orders"),
                () -> assertEquals(mugs.hashCode(), sameData.hashCode(),
                        "Task 3: equal records have equal hash codes"),
                () -> assertEquals("Order[id=A1, amount=20.0]", mugs.toString(),
                        "Task 3: the record's generated toString"),
                () -> assertThrows(IllegalArgumentException.class, () -> new Order("A3", -1.0),
                        "Task 3: the compact canonical constructor rejects a negative amount"));
    }

    @Test
    void task4CheckoutComposition() {
        Order mugs = new Order("A1", 20.0);
        assertAll(
                () -> assertEquals(15.0, new Checkout(new FlatDiscount(5)).total(mugs),
                        "Task 4: a checkout composed with a flat discount"),
                () -> assertEquals(18.0, new Checkout(new PercentOff(10)).total(mugs),
                        "Task 4: the same checkout class works with a percent rule"),
                () -> assertEquals(20.0, new Checkout(price -> price).total(mugs),
                        "Task 4: the test supplies its own rule as a lambda - dependency inversion"),
                () -> assertEquals(1.0, new Checkout(price -> 1.0).total(mugs),
                        "Task 4: the checkout must delegate to the injected rule, not compute itself"));
    }

    @Test
    void task5CapLambda() {
        PriceRule cap = Rules.capAt(12.0);
        assertNotNull(cap, "Task 5: capAt must return a rule, not null");
        assertAll(
                () -> assertEquals(12.0, cap.apply(20.0), "Task 5: capAt(12).apply(20.0)"),
                () -> assertEquals(8.0, cap.apply(8.0), "Task 5: capAt(12).apply(8.0)"),
                () -> assertEquals(12.0, cap.apply(12.0), "Task 5: capAt(12).apply(12.0)"));
        PriceRule bulkOnly = Rules.onlyWhen(price -> price >= 100.0, new FlatDiscount(10));
        assertNotNull(bulkOnly, "Task 5: onlyWhen must return a rule, not null");
        assertAll(
                () -> assertEquals(190.0, bulkOnly.apply(200.0),
                        "Task 5: onlyWhen applies the rule when the Predicate holds"),
                () -> assertEquals(50.0, bulkOnly.apply(50.0),
                        "Task 5: onlyWhen leaves the price unchanged when the Predicate is false"),
                () -> assertEquals(90.0, bulkOnly.apply(100.0),
                        "Task 5: onlyWhen at the boundary, 100 counts as large"));
    }

    @Test
    void task6Factory() {
        PriceRule flat = Rules.forCode("flat5");
        PriceRule percent = Rules.forCode("percent10");
        assertNotNull(flat, "Task 6: forCode(\"flat5\") must return a rule, not null");
        assertNotNull(percent, "Task 6: forCode(\"percent10\") must return a rule, not null");
        assertAll(
                () -> assertEquals(15.0, flat.apply(20.0), "Task 6: forCode(\"flat5\").apply(20.0)"),
                () -> assertEquals(18.0, percent.apply(20.0), "Task 6: forCode(\"percent10\").apply(20.0)"),
                () -> assertThrows(IllegalArgumentException.class, () -> Rules.forCode("mystery"),
                        "Task 6: an unknown code must be rejected with IllegalArgumentException"));
    }
}
