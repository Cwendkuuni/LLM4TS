package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply(2));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply(2));
    }

    @Test
    public void testIsNotNull() {
        Predicate<Integer> isNotNull = Predicates.notNull();
        assertFalse(isNotNull.apply(null));
        assertTrue(isNotNull.apply(1));
        assertTrue(isNotNull.apply(2));
    }

    @Test
    public void testIsNull() {
        Predicate<Integer> isNull = Predicates.isNull();
        assertFalse(isNull.apply(1));
        assertFalse(isNull.apply(2));
        assertTrue(isNull.apply(null));
    }

    @Test
    public void testNot() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> notEven = Predicates.not(isEven);

        assertFalse(notEven.apply(2));
        assertTrue(notEven.apply(1));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer > 0;
            }
        };
        Predicate<Integer> and = Predicates.and(isEven, isPositive);

        assertFalse(and.apply(-1));
        assertFalse(and.apply(0));
        assertTrue(and.apply(1));
        assertTrue(and.apply(2));

        List<Predicate<Integer>> predicates = Arrays.asList(isEven, isPositive);
        Predicate<Integer> and2 = Predicates.and(predicates);
        assertTrue(and2.apply(1));
        assertTrue(and2.apply(2));
    }

    @Test
    public void testOr() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer > 0;
            }
        };
        Predicate<Integer> or = Predicates.or(isEven, isPositive);

        assertFalse(or.apply(-1));
        assertFalse(or.apply(0));
        assertTrue(or.apply(1));
        assertTrue(or.apply(2));

        List<Predicate<Integer>> predicates = Arrays.asList(isEven, isPositive);
        Predicate<Integer> or2 = Predicates.or(predicates);
        assertTrue(or2.apply(1));
        assertTrue(or2.apply(2));
    }

    @Test
    public void testEqualTo() {
        Predicate<Integer> equalToThree = Predicates.equalTo(3);

        assertFalse(equalToThree.apply(1));
        assertFalse(equalToThree.apply(2));
        assertTrue(equalToThree.apply(3));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> instanceOfString = Predicates.instanceOf(String.class);

        assertFalse(instanceOfString.apply(1));
        assertFalse(instanceOfString.apply(2));
        assertTrue(instanceOfString.apply("Hello"));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> assignableFromNumber = Predicates.assignableFrom(Number.class);

        assertFalse(assignableFromNumber.apply(String.class));
        assertTrue(assignableFromNumber.apply(Integer.class));
        assertTrue(assignableFromNumber.apply(Number.class));
    }

    @Test
    public void testIn() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Predicate<Integer> in = Predicates.in(numbers);

        assertFalse(in.apply(-1));
        assertFalse(in.apply(6));
        assertTrue(in.apply(1));
        assertTrue(in.apply(2));
        assertTrue(in.apply(3));
        assertTrue(in.apply(4));
        assertTrue(in.apply(5));
    }

    @Test
    public void testCompose() {
        Function<Integer, Boolean> isEven = new Function<Integer, Boolean>() {
            @Override
            public Boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> isEvenPredicate = Predicates.compose(Predicates.alwaysTrue(), isEven);

        assertTrue(isEvenPredicate.apply(2));
        assertTrue(isEvenPredicate.apply(4));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> containsPattern = Predicates.containsPattern("ab*");

        assertFalse(containsPattern.apply("Hello"));
        assertFalse(containsPattern.apply("World"));
        assertTrue(containsPattern.apply("HelloWorld"));
        assertTrue(containsPattern.apply("HelloabWorld"));
    }

    @Test
    public void testContainsPatternString() {
        Predicate<String> containsPattern = Predicates.containsPattern("ab*");

        assertFalse(containsPattern.apply("Hello"));
        assertFalse(containsPattern.apply("World"));
        assertTrue(containsPattern.apply("HelloWorld"));
        assertTrue(containsPattern.apply("HelloabWorld"));
    }
}