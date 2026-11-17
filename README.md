# Inheritance and Design lab: interchangeable price rules

A small shop's checkout needs price rules it can swap. An earlier version grew one `Checkout` subclass per promotion.
You build the design that replaced it, in six linked tasks in the package `pricing`:
- one `PriceRule` interface;
- an abstract base for the named rules;
- a record for the order data;
- a checkout that holds a rule instead of extending anything;
- a factory that hides the concrete classes.

Along the way you practise inheritance, interfaces and default methods, records, composition, dependency inversion, lambdas and static factories.

The full learner instructions are in [`.evolv/instructions.md`](.evolv/instructions.md), which the lab platform shows as the lab's instructions.

## Layout

This repository follows the Exa coding-lab scaffold for Java.

| Path | What it is |
|---|---|
| `src/main/java/pricing/Checkout.java` | Lab source |
| `src/main/java/pricing/FlatDiscount.java` | Lab source |
| `src/main/java/pricing/NamedRule.java` | Lab source |
| `src/main/java/pricing/Order.java` | Lab source |
| `src/main/java/pricing/PercentOff.java` | Lab source |
| `src/main/java/pricing/PriceRule.java` | Lab source |
| `src/main/java/pricing/Rules.java` | Lab source |
| `src/test/java/pricing/PricingTest.java` | Visible tests |
| `.evolv/instructions.md` | Learner instructions |
| `.evolv/grade.yml` | Grading manifest, used by Run Tests and by the grader |
| `pom.xml` | Maven build: Java 17 and JUnit 5.11.3, matching the scaffold |
| `Dockerfile` | Image for local Run Tests; warms the Maven cache |
| `expected/test-summary.txt` | The result of a finished lab |

There are no held-out tests in this lab: every test is visible.

## Run the tests

On the platform, select **Run Tests**. From a terminal with JDK 17 or later and Apache Maven 3.9:

```
mvn -q test
```

All 6 tests fail until the `TODO`s are done. That is expected.
