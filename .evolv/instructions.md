# Inheritance and Design lab: interchangeable price rules

A small shop's checkout needs price rules it can swap. An earlier version grew one `Checkout` subclass per promotion.
You build the design that replaced it, in six linked tasks in the package `exercise`:
- one `PriceRule` interface;
- an abstract base for the named rules;
- a record for the order data;
- a checkout that holds a rule instead of extending anything;
- a factory that hides the concrete classes.

Along the way you practise inheritance, interfaces and default methods, records, composition, dependency inversion, lambdas and static factories.

## Quick start

1. Open the lab project. The files you edit are in `src/main/java/exercise/`; the checklist below names them.
2. Select **Run Tests** before you change anything.

   **All 6 tests fail at first. That is expected: the TODOs are not done yet.**
   Every failure message starts with its task number, such as `Task 1:`.
3. Read `PriceRule.java` first, then work through the checklist below in order. After each task, select **Run Tests** again and read the remaining `Task N:` messages.
4. **You are finished when all 6 tests pass.** Then select **Submit**.

```
OPEN project -> RUN TESTS: 6 fail (expected)
  -> read PriceRule.java -> READ the "Task N:" messages -> EDIT the next TODO -> RUN TESTS again
  -> repeat for Tasks 1-6 -> SUBMIT when all 6 tests pass
```

In words: open the project, run the tests, read which tasks fail, complete one task's TODO, run the tests again, and submit when they all pass.

## Checklist

Every file is in `src/main/java/exercise/`, and each task is marked with a `TODO` comment:

- [ ] Task 1: `NamedRule.java` and `FlatDiscount.java` (an abstract class and a subclass)
- [ ] Task 2: `PercentOff.java` (implementing an interface directly)
- [ ] Task 3: `Order.java` (turning a class into a record)
- [ ] Task 4: `Checkout.java` (composition)
- [ ] Task 5: `Rules.java`, `capAt` and `onlyWhen` (anonymous classes, then lambdas)
- [ ] Task 6: `Rules.java`, `forCode` (a static factory)

A task is done when no failure message starts with its number.
Edit only those six files.
- `PriceRule.java` is the complete contract that every task builds on.
- The tests in `src/test/java/exercise/` are restored to their original state before your work is graded, so changes to them do not count. Leave the build file `pom.xml` and `expected/test-summary.txt` as they are.

## Task steps

`PriceRule.java` is complete: it has one abstract method, `apply`, and one default method, `label`.
The tests check only visible behavior: what the rules, the record and the checkout return.

### Task 1: the abstract base and the first named rule

1. Open `NamedRule.java`. The named rules share state (a display name) and code (`describe()`). That is why this base is an abstract class rather than a second interface.
2. Add a `private final String name` field and a constructor `NamedRule(String name)` that assigns it.
   Declare `public abstract double apply(double price);`, and return the stored name from `describe()`.
   The abstract method has no body. That is what makes the class abstract, and why `new NamedRule(...)` cannot compile.
3. Open `FlatDiscount.java`. Make its constructor call `super("flat discount")` as its first statement.
   Then implement `apply`: the price minus the amount, never below 0. Keep `@Override` on it, so the compiler guarantees you really override.
4. Notice what `FlatDiscount` gets without writing it: `describe()` from the abstract class, and `label()` from the interface's default method.
5. While it is wrong, you see messages such as `Task 1: new FlatDiscount(5).apply(20.0)`.

<details><summary>Hint</summary>

If you see `constructor NamedRule in class NamedRule cannot be applied`, a subclass constructor does not call `super("...")` first.

</details>

### Task 2: implement the interface directly

1. Open `PercentOff.java`. It needs no shared name, so it implements `PriceRule` directly. Choosing between the abstract class and the interface is itself a design decision.
2. Implement `apply`: the price reduced by the percentage, so `new PercentOff(10).apply(200.0)` is `180.0`.
3. Override `label()` to return `"percent off"`. Run the tests and compare:
   - `FlatDiscount` still answers with the interface's default;
   - your class answers with its own `label()`, because a class's own method always wins over a default method.

### Task 3: the Order record

1. Open `Order.java` and replace the whole class with a record: `public record Order(String id, double amount) { ... }`.
   Delete the hand-written constructor and accessors. The record generates the constructor, `id()`, `amount()`, `equals`, `hashCode` and `toString` itself.
2. Add a compact canonical constructor that rejects a negative amount. Inside the record body, write `public Order { if (amount < 0) ... }`.
   It has no parameter list and no assignments: the record assigns its final fields after your checks pass.
3. The tests also show what you got for free: two orders with the same data are equal, and `toString()` is `Order[id=A1, amount=20.0]`.

<details><summary>Hint</summary>

If `Order.java` stops compiling, check that the old constructor and accessors are gone.

</details>

### Task 4: compose the checkout

1. Open `Checkout.java` and read the class comment. It describes the subclass-per-promotion design this task replaces.
2. Add a `private final PriceRule rule` field and assign it in the constructor.
   Implement `total(Order order)` as pure delegation: the rule applied to `order.amount()`.
3. The checkout now HAS a rule instead of BEING one subclass per rule.
   The field's type is the interface, so it can hold any implementation at runtime. The tests hand in `FlatDiscount`, `PercentOff` and plain lambdas, and your one class works with all of them.
   A test can inject its own rule: that is dependency inversion at work.

### Task 5: a lambda is a rule

1. Open `Rules.java` and implement `capAt(double max)` twice, on purpose.
2. First the old way. Return a `new PriceRule() { ... }` anonymous class whose `apply` gives back the price itself when it is under the cap, and `max` otherwise. Run the tests: green.
3. Now see why lambdas exist. `PriceRule` has exactly one abstract method, so it is a functional interface.
   Replace the whole anonymous class with a lambda that says the same in one line, and run the tests again. The behavior is the same, and the code is five lines shorter.
4. Then use a functional interface the JDK already ships. Implement `onlyWhen(Predicate<Double> condition, PriceRule rule)` as a lambda: it applies the rule only when `condition.test(price)` is true, and otherwise returns the price unchanged.
   `Predicate`, from `java.util.function`, is the standard one-argument true/false interface. The tests hand one in as a lambda, exactly as they hand in rules.

### Task 6: the static factory

1. Implement `Rules.forCode(String code)`:
   - `"flat5"` returns a `FlatDiscount` of 5;
   - `"percent10"` returns a `PercentOff` of 10;
   - any other code throws `IllegalArgumentException("unknown code: " + code)`.
2. The return type is the interface, so the caller never learns which class it got. The factory can change implementations tomorrow without touching any caller.
   That hiding is what a static factory buys. A singleton is different: it would put shared state into every caller.

### About the test results

A passing run shows the design behaves correctly.
It does not check which Java constructs your code uses, so compare your code with each task's steps yourself.

## Using an AI assistant

If you use an AI assistant, ask it to explain, not to answer.
"Why does my compact canonical constructor not compile?" teaches you records; "write the pricing package for me" does not.
Check any explanation with a test run before you trust it.

## Run the program (optional)

The checkout prints one line per rule. In a terminal, from the project folder:

```
mvn -q compile
java -cp target/classes exercise.Checkout
```

`expected/test-summary.txt` shows the finished result, including the eight lines the program prints.

## Troubleshooting

- **`COMPILATION ERROR`.** Your code does not compile yet. Maven names the file, then `[line,column]`, then the problem. Fix that line and run the tests again.
