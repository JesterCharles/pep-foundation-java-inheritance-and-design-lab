package exercise.hidden;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import exercise.Checkout;
import exercise.FlatDiscount;
import exercise.Order;
import exercise.PercentOff;
import exercise.PriceRule;
import exercise.Rules;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/** Held-out tests: graded on submit, never shown to the learner. Edge cases for every task. */
@TestMethodOrder(MethodOrderer.MethodName.class)
class HeldOutTests {

    @Test
    void task1FlatDiscountEdgeCases() {
        FlatDiscount flat = new FlatDiscount(5);
        assertAll(
                () -> assertEquals(0.0, flat.apply(3.0),
                        "Task 1: a flat discount never pushes the price below 0"),
                () -> assertEquals("price rule", flat.label(),
                        "Task 1: FlatDiscount keeps the interface's default label()"));
    }

    @Test
    void task2PercentOffEdgeCases() {
        assertAll(
                () -> assertEquals(50.0, new PercentOff(50).apply(100.0),
                        "Task 2: new PercentOff(50).apply(100.0)"),
                () -> assertEquals(0.0, new PercentOff(100).apply(80.0),
                        "Task 2: new PercentOff(100).apply(80.0)"));
    }

    @Test
    void task3OrderRecordEquality() {
        Order mugs = new Order("A1", 20.0);
        Order sameData = new Order("A1", 20.0);
        Order other = new Order("A2", 20.0);
        assertAll(
                () -> assertNotEquals(mugs, other, "Task 3: different ids are different orders"),
                () -> assertEquals(mugs.hashCode(), sameData.hashCode(),
                        "Task 3: equal records have equal hash codes"));
    }

    @Test
    void task4CheckoutDelegatesToTheInjectedRule() {
        Order mugs = new Order("A1", 20.0);
        assertAll(
                () -> assertEquals(20.0, new Checkout(price -> price).total(mugs),
                        "Task 4: the test supplies its own rule as a lambda - dependency inversion"),
                () -> assertEquals(1.0, new Checkout(price -> 1.0).total(mugs),
                        "Task 4: the checkout must delegate to the injected rule, not compute itself"));
    }

    @Test
    void task5Boundaries() {
        PriceRule cap = Rules.capAt(12.0);
        PriceRule bulkOnly = Rules.onlyWhen(price -> price >= 100.0, new FlatDiscount(10));
        assertAll(
                () -> assertEquals(12.0, cap.apply(12.0), "Task 5: capAt(12).apply(12.0)"),
                () -> assertEquals(90.0, bulkOnly.apply(100.0),
                        "Task 5: onlyWhen at the boundary, 100 counts as large"));
    }

    @Test
    void task6UnknownCode() {
        assertThrows(IllegalArgumentException.class, () -> Rules.forCode("mystery"),
                "Task 6: an unknown code must be rejected with IllegalArgumentException");
    }
}
