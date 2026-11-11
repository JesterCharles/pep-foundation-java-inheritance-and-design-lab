# Inheritance and Design lab: interchangeable price rules

## Goal

Give a small shop's checkout interchangeable price rules, in six linked tasks across one package, `pricing`.
An earlier version of the shop grew one `Checkout` subclass per promotion; you build the design that replaced it: one `PriceRule` interface, an abstract base for the named rules, a record for the order data, a checkout that composes a rule instead of extending anything, and a factory that hides the concrete classes.
You practise inheritance, interfaces and default methods, records, composition, dependency inversion, lambdas and static factories on one coherent design.
You build and test it with the real JDK and Maven on your own computer.

## Files

**Edit these six files, in task order.** Every `TODO` comment marks its task; the test failure lines carry the same task numbers:

- `starter/src/main/java/pricing/NamedRule.java` and `FlatDiscount.java` - Task 1: the abstract base and the first named rule
- `starter/src/main/java/pricing/PercentOff.java` - Task 2: implement the interface directly
- `starter/src/main/java/pricing/Order.java` - Task 3: turn the class into a record
- `starter/src/main/java/pricing/Checkout.java` - Task 4: compose the injected rule
- `starter/src/main/java/pricing/Rules.java` - Tasks 5 and 6: the lambda rules and the static factory

**Do not change anything else.** In particular:

- `starter/src/main/java/pricing/PriceRule.java` - the complete contract every task builds on
- `tests/pricing/PricingTest.java` - the public tests your design must pass
- `starter/pom.xml` - the build is prewired
- `expected/test-summary.txt` - the finished-state reference

## Prerequisites

You need JDK 25 and Apache Maven 3.9 or later, the same setup as the earlier Java labs.
Check both in a new terminal:

```
java -version
mvn -v
```

- `java -version` must report version 25.
- `mvn -v` must report Apache Maven 3.9 or later, and its `Java version:` line must also say 25.

This lab works the same on macOS, Linux and Windows.
On Windows, use PowerShell or Command Prompt; the commands are the same.
No Docker or other container tool is needed.

## Setup (once, needs the internet)

Open a terminal in the lab's root folder - the one that holds this README, `starter/` and `tests/` - and change into `starter` with `cd starter`; every command in this lab runs there. Then run:

```
mvn -q dependency:go-offline
```

It prints nothing when it succeeds.
If you finished an earlier Java lab on this machine, it finds everything already downloaded.
After this step, everything else in the lab runs offline.

## Tasks

Open the lab's root folder in your IDE or editor; it holds one Maven project, `starter/pom.xml`, and if your editor asks, open or import that file as a Maven project. Whatever your editor shows, the test command below, run in a terminal in `starter`, is the result that counts.
Read `PriceRule.java` first - it is complete and every task builds on it: one abstract method (`apply`), one default method (`label`).
The tests check only visible behavior: what the rules, record and checkout return.

After each task, run the one test command from the `starter` folder:

```
mvn -q -o test
```

Every failure line starts with its task number, so you can work through the tasks in order.

### Task 1: the abstract base and the first named rule

1. Open `NamedRule.java`. The named rules share state (a display name) and code (`describe()`), which is why this base is an abstract class rather than a second interface.
2. Add a `private final String name` field and a constructor `NamedRule(String name)` that assigns it, declare `public abstract double apply(double price);`, and return the stored name from `describe()`.
   The abstract method has no body - that is what makes the class abstract, and why `new NamedRule(...)` cannot compile.
3. Open `FlatDiscount.java`. Make its constructor call `super("flat discount")` as its first statement, then implement `apply`: the price minus the amount, never below 0. Keep `@Override` on it - the compiler then guarantees you really override.
4. Note what `FlatDiscount` gets without writing it: `describe()` from the abstract class and `label()` from the interface's default method.
5. While it is wrong, you see lines such as `Task 1: new FlatDiscount(5).apply(20.0)`.
6. Done when no line starting with `Task 1` appears.

### Task 2: implement the interface directly

1. Open `PercentOff.java`. It needs no shared name state, so it implements `PriceRule` directly - choosing between the abstract class and the interface is itself a design decision.
2. Implement `apply`: the price reduced by the percentage, so `new PercentOff(10).apply(200.0)` is `180.0`.
3. Override `label()` to return `"percent off"`. Run the tests and compare: `FlatDiscount` still answers with the interface's default, your class answers with its own - an implementing class's own method always wins over a default method.
4. Done when no line starting with `Task 2` appears.

### Task 3: the Order record

1. Open `Order.java` and replace the whole class with a record:
   `public record Order(String id, double amount) { ... }`.
   Delete the hand-written constructor and accessors - the record generates the constructor, `id()`, `amount()`, `equals`, `hashCode` and `toString` itself.
2. Add a compact canonical constructor that rejects a negative amount:
   inside the record body, `public Order { if (amount < 0) ... }` - no parameter list, no assignments; the record assigns its final fields after your checks pass.
3. The tests also show what you got for free: two orders with the same data are equal, and `toString()` is `Order[id=A1, amount=20.0]`.
4. Done when no line starting with `Task 3` appears.

### Task 4: compose the checkout

1. Open `Checkout.java` and read the class comment: the subclass-per-promotion design it replaces is the motivation for this task.
2. Add a `private final PriceRule rule` field, assign it in the constructor, and implement `total(Order order)` as pure delegation: the rule applied to `order.amount()`.
3. The checkout now HAS a rule instead of BEING one subclass per rule.
   Because the field's type is the interface, a superclass-typed reference holds any implementation at runtime - the tests hand in `FlatDiscount`, `PercentOff` and plain lambdas, and your one class works with all of them. That a test can inject its own rule is dependency inversion at work.
4. Done when no line starting with `Task 4` appears.

### Task 5: a lambda is a rule

1. Open `Rules.java` and implement `capAt(double max)` - twice, on purpose.
2. First the old way: return a `new PriceRule() { ... }` anonymous class whose `apply` yields the price itself under the cap and `max` otherwise. Run the tests - green.
3. Now the reason lambdas exist: `PriceRule` has exactly one abstract method, so it is a functional interface. Replace the whole anonymous class with a lambda that says the same in one line, and run the tests again - same behavior, five lines shorter.
4. Then use a functional interface the JDK already ships: implement `onlyWhen(Predicate<Double> condition, PriceRule rule)` as a lambda that applies the rule only when `condition.test(price)` is true and otherwise returns the price unchanged. `Predicate` (from `java.util.function`) is the standard one-argument true/false interface - the tests hand one in as a lambda, exactly as they hand in rules.
5. Done when no line starting with `Task 5` appears.

### Task 6: the static factory

1. Implement `Rules.forCode(String code)`: `"flat5"` returns a `FlatDiscount` of 5, `"percent10"` a `PercentOff` of 10, and any other code throws `IllegalArgumentException("unknown code: " + code)`.
2. The return type is the interface, so the caller never learns which class it got - the factory can change implementations tomorrow without touching any caller. That hiding is what a static factory buys; contrast it with a singleton, which would smuggle shared state into every caller.
3. Done when no line starting with `Task 6` appears and the whole run prints nothing.

## Using an AI assistant

If you use an AI assistant in this lab, ask it to explain, not to answer: "why does my compact canonical constructor not compile?" teaches you records; "write the pricing package for me" does not.
Verify any explanation against a test run before you trust it.

## Test

The one test command, run from the `starter` folder, is:

```
mvn -q -o test
```

- Before you start, all six tests fail, and Maven ends with `Tests run: 6, Failures: 6`.
- Each test lists what behavior was wrong. A passing run shows the design behaves correctly; it does not check which Java constructs your code uses, so compare your code with each task's steps yourself.
- If your code does not compile, Maven prints `COMPILATION ERROR` with the file, then `[line,column]`, then the compiler's message. Fix that line and run the test again.

You can also run the checkout itself, which prints one line per rule:

```
mvn -q -o compile
java -cp target/classes pricing.Checkout
```

## Expected output

When every task is complete, `mvn -q -o test` prints nothing and exits with status 0.
`expected/test-summary.txt` shows the full expected result, including the eight lines the program prints.

## Offline note

Only the setup step uses the network.
The test command uses `-o` (offline), so it never downloads anything.
If it reports that an artifact is missing from the local repository, run the setup step once more while online.

## Cleanup

The lab writes only the `starter/target` folder (compiled classes and test reports). Delete it from the `starter` folder:

- macOS and Linux: `rm -rf target`
- Windows PowerShell: `Remove-Item -Recurse -Force target`

The JUnit and plugin downloads stay in your local Maven repository (`~/.m2/repository` by default) so later Java labs can reuse them.

`starter/.gitignore` already keeps Maven's `starter/target` folder out of Git. Your editor and operating system may also create local files in the lab's root folder, such as `.idea/`, `.vscode/` or `.DS_Store`; they are not part of the lab, so if `git status` lists any of them, add them to a `.gitignore` file in the lab's root folder instead of committing them.

## Troubleshooting

- **`mvn: command not found`, or `'mvn' is not recognized`.** Maven's `bin` folder is not on your `PATH`. Add it, then open a new terminal.
- **`mvn -v` shows a Java version other than 25.** Maven uses the JDK named by `JAVA_HOME` when it is set, otherwise the `java` on your `PATH`. Set `JAVA_HOME` to your JDK 25 folder, or unset it, then open a new terminal.
- **`release version 25 not supported`.** Maven is running on an older JDK. Fix it as in the previous item.
- **`Cannot access central in offline mode`, or a missing artifact.** The setup step has not completed. Run `mvn -q dependency:go-offline` once while online.
- **`constructor NamedRule in class NamedRule cannot be applied`.** Once `NamedRule` has your `(String name)` constructor, every subclass constructor must call `super("...")` first.
- **`Order.java` stopped compiling after Task 3.** Delete the old constructor and accessors entirely; a record body keeps only the compact canonical constructor and any extra methods.
