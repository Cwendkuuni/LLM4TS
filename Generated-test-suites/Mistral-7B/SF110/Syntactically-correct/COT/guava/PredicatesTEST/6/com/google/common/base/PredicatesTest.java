package com.example.junit4;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Predicates;

public class PredicatesTest {

    // Test alwaysTrue()
    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(0));
        assertTrue(alwaysTrue.apply(1));
    }

    // Test alwaysFalse()
    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(0));
        assertFalse(alwaysFalse.apply(1));
    }

    // Test isNull()
    @Test
    public void testIsNull() {
        Predicate<Integer> isNull = Predicates.isNull();
        assertFalse(isNull.apply(null));
        assertFalse(isNull.apply(0));
        assertTrue(isNull.apply(null));
    }

    // Test notNull()
    @Test
    public void testNotNull() {
        Predicate<Integer> notNull = Predicates.notNull();
        assertFalse(notNull.apply(null));
        assertTrue(notNull.apply(0));
        assertTrue(notNull.apply(1));
    }

    // Test not()
    @Test
    public void testNot() {
        Predicate<Boolean> truePredicate = Predicates.alwaysTrue();
        Predicate<Boolean> falsePredicate = Predicates.alwaysFalse();
        Predicate<Boolean> notTrue = Predicates.not(truePredicate);
        Predicate<Boolean> notFalse = Predicates.not(falsePredicate);

        assertFalse(notTrue.apply(true));
        assertTrue(notTrue.apply(false));

        assertFalse(notFalse.apply(false));
        assertTrue(notFalse.apply(true));
    }

    // Test and()
    @Test
    public void testAnd() {
        Predicate<Integer> evenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isZeroPredicate = Predicates.isNull();

        Predicate<Integer> andEvenZero = Predicates.and(evenPredicate, isZeroPredicate);

        assertFalse(andEvenZero.apply(1));
        assertFalse(andEvenZero.apply(2));
        assertTrue(andEvenZero.apply(null));
        assertTrue(andEvenZero.apply(0));

        List<Predicate<Integer>> components = Arrays.asList(evenPredicate, isZeroPredicate);
        Predicate<Integer> andComponents = Predicates.and(components);

        assertFalse(andComponents.apply(1));
        assertFalse(andComponents.apply(2));
        assertTrue(andComponents.apply(null));
        assertTrue(andComponents.apply(0));
    }

    // Test or()
    @Test
    public void testOr() {
        Predicate<Integer> evenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isZeroPredicate = Predicates.isNull();

        Predicate<Integer> orEvenZero = Predicates.or(evenPredicate, isZeroPredicate);

        assertFalse(orEvenZero.apply(1));
        assertTrue(orEvenZero.apply(0));
        assertTrue(orEvenZero.apply(null));

        List<Predicate<Integer>> components = Arrays.asList(evenPredicate, isZeroPredicate);
        Predicate<Integer> orComponents = Predicates.or(components);

        assertFalse(orComponents.apply(1));
        assertTrue(orComponents.apply(0));
        assertTrue(orComponents.apply(null));
    }

    // Test equalTo()
    @Test
    public void testEqualTo() {
        Predicate<Integer> equalToThree = Predicates.equalTo(3);
        assertFalse(equalToThree.apply(null));
        assertFalse(equalToThree.apply(1));
        assertTrue(equalToThree.apply(3));

        Predicate<String> equalToHello = Predicates.equalTo("Hello");
        assertFalse(equalToHello.apply(null));
        assertFalse(equalToHello.apply("World"));
        assertTrue(equalToHello.apply("Hello"));
    }

    // Test instanceOf()
    @Test(expected = RuntimeException.class)
    public void testInstanceOfWithNull() {
        Predicates.instanceOf(null);
    }

    @Test
    public void testInstanceOf() {
        Predicate<Class<?>> instanceOfString = Predicates.instanceOf(String.class);
        assertFalse(instanceOfString.apply(null));
        assertFalse(instanceOfString.apply(1));
        assertTrue(instanceOfString.apply(new String("Hello")));
    }

    // Test assignableFrom()
    @Test(expected = RuntimeException.class)
    public void testAssignableFromWithNull() {
        Predicates.assignableFrom(null);
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> assignableFromString = Predicates.assignableFrom(String.class);
        assertFalse(assignableFromString.apply(null));
        assertFalse(assignableFromString.apply(List.class));
        assertTrue(assignableFromString.apply(StringBuilder.class));
    }

    // Test in()
    @Test
    public void testIn() {
        List<Integer> numbers = Arrays.asList(1, 2, 3);
        Predicate<Integer> inNumbers = Predicates.in(numbers);

        assertFalse(inNumbers.apply(-1));
        assertFalse(inNumbers.apply(4));
        assertTrue(inNumbers.apply(1));
        assertTrue(inNumbers.apply(2));
        assertTrue(inNumbers.apply(3));

        Collections.addAll(numbers, 4, 5);
        assertFalse(inNumbers.apply(6));
        assertTrue(inNumbers.apply(4));
        assertTrue(inNumbers.apply(5));
    }

    // Test compose()
    @Test
    public void testCompose() {
        Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Function<Integer, Integer> doubleFunction = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input * 2;
            }
        };

        Predicate<Integer> composeEvenDouble = Predicates.compose(isEvenPredicate, doubleFunction);

        assertFalse(composeEvenDouble.apply(1));
        assertTrue(composeEvenDouble.apply(2));
        assertTrue(composeEvenDouble.apply(4));
    }

    // Test containsPattern()
    @Test
    public void testContainsPattern() {
        Predicate<CharSequence> containsPatternA = Predicates.containsPattern("A.*");
        Predicate<CharSequence> containsPatternB = Predicates.containsPattern("B.*");

        String testStringA = "The quick brown fox jumps over the lazy dog.";
        String testStringB = "The quick brown fox jumps over the quick brown fox.";

        assertTrue(containsPatternA.apply(testStringA));
        assertFalse(containsPatternA.apply(testStringB));

        assertTrue(containsPatternB.apply(testStringB));
        assertFalse(containsPatternB.apply(testStringA));

        Predicate<CharSequence> containsPatternC = Predicates.containsPattern("[A-Z].");
        assertTrue(containsPatternC.apply(testStringA));
        assertTrue(containsPatternC.apply(testStringB));
    }
}