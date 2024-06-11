package com.example.junit;

import com.google.common.base.Predicates;
import org.junit.Test;
import static org.junit.Assert.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply(2));
        assertTrue(alwaysTrue.apply(3));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply(2));
        assertFalse(alwaysFalse.apply(3));
    }

    @Test
    public void testIsNull() {
        Predicate<String> isNull = Predicates.isNull();
        assertNull(isNull.apply(null));
        assertFalse(isNull.apply(""));
        assertFalse(isNull.apply("abc"));
    }

    @Test
    public void testNotNull() {
        Predicate<String> notNull = Predicates.notNull();
        assertNull(notNull.apply(null));
        assertTrue(notNull.apply(""));
        assertTrue(notNull.apply("abc"));
    }

    @Test
    public void testNot() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> notEven = Predicates.not(isEven);
        assertFalse(notEven.apply(0));
        assertTrue(notEven.apply(1));
        assertTrue(notEven.apply(2));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input > 0;
            }
        };

        Predicate<Integer> andPredicate = Predicates.and(isEven, isPositive);
        assertFalse(andPredicate.apply(-1));
        assertFalse(andPredicate.apply(0));
        assertTrue(andPredicate.apply(1));
        assertTrue(andPredicate.apply(2));

        Predicate<Integer> isNegativeOrEven = Predicates.or(Predicates.not(Predicates.isPositive()), isEven);
        assertTrue(isNegativeOrEven.apply(-1));
        assertTrue(isNegativeOrEven.apply(0));
        assertTrue(isNegativeOrEven.apply(2));
    }

    @Test
    public void testIn() {
        Collection<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Predicate<Integer> isIn = Predicates.in(numbers);
        assertFalse(isIn.apply(6));
        assertTrue(isIn.apply(1));
        assertTrue(isIn.apply(2));
        assertFalse(isIn.apply(6));
        assertTrue(isIn.apply(5));
    }

    @Test
    public void testEqualTo() {
        Predicate<String> isEqualTo = Predicates.equalTo("abc");
        assertFalse(isEqualTo.apply("def"));
        assertTrue(isEqualTo.apply("abc"));
    }

    @Test(expected = NullPointerException.class)
    public void testInstanceOfNull() {
        Predicates.instanceOf(null);
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> isString = Predicates.instanceOf(String.class);
        assertFalse(isString.apply(1));
        assertTrue(isString.apply("abc"));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> isAssignableFromString = Predicates.assignableFrom(String.class);
        assertFalse(isAssignableFromString.apply(Integer.class));
        assertTrue(isAssignableFromString.apply(StringBuilder.class));
    }
}