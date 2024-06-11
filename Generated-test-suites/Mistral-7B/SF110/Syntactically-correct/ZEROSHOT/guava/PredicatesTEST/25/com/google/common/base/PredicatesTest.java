package com.google.common.base;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

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
    public void testIsNull() {
        Predicate<Integer> isNull = Predicates.isNull();
        assertFalse(isNull.apply(null));
        assertFalse(isNull.apply(1));
        assertTrue(isNull.apply(null));
    }

    @Test
    public void testNotNull() {
        Predicate<Integer> notNull = Predicates.notNull();
        assertFalse(notNull.apply(null));
        assertTrue(notNull.apply(1));
        assertTrue(notNull.apply(2));
    }

    @Test
    public void testNot() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> notEven = Predicates.not(isEven);
        assertTrue(notEven.apply(1));
        assertFalse(notEven.apply(2));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer > 0;
            }
        };
        Predicate<Integer> and = Predicates.and(isEven, isPositive);
        assertFalse(and.apply(-1));
        assertFalse(and.apply(0));
        assertTrue(and.apply(1));
        assertTrue(and.apply(2));

        List<Predicate<Integer>> list = Arrays.asList(isEven, isPositive);
        Predicate<Integer> and2 = Predicates.and(list);
        assertFalse(and2.apply(-1));
        assertFalse(and2.apply(0));
        assertTrue(and2.apply(1));
        assertTrue(and2.apply(2));
    }

    @Test
    public void testOr() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer > 0;
            }
        };
        Predicate<Integer> or = Predicates.or(isEven, isPositive);
        assertTrue(or.apply(-1));
        assertTrue(or.apply(0));
        assertTrue(or.apply(1));
        assertTrue(or.apply(2));

        List<Predicate<Integer>> list = Arrays.asList(isEven, isPositive);
        Predicate<Integer> or2 = Predicates.or(list);
        assertTrue(or2.apply(-1));
        assertTrue(or2.apply(0));
        assertTrue(or2.apply(1));
        assertTrue(or2.apply(2));
    }

    @Test
    public void testEqualTo() {
        Predicate<Integer> isEqualToFive = Predicates.equalTo(5);
        assertFalse(isEqualToFive.apply(null));
        assertFalse(isEqualToFive.apply(1));
        assertFalse(isEqualToFive.apply(2));
        assertTrue(isEqualToFive.apply(5));
        assertFalse(isEqualToFive.apply(6));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> isInstanceOfString = Predicates.instanceOf(String.class);
        assertFalse(isInstanceOfString.apply(null));
        assertFalse(isInstanceOfString.apply(1));
        assertTrue(isInstanceOfString.apply(""));
        assertFalse(isInstanceOfString.apply(new Object()));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> assignableFromInteger = Predicates.assignableFrom(Integer.class);
        assertFalse(assignableFromInteger.apply(Boolean.class));
        assertFalse(assignableFromInteger.apply(String.class));
        assertTrue(assignableFromInteger.apply(Number.class));
        assertTrue(assignableFromInteger.apply(Integer.class));
    }

    @Test
    public void testIn() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Predicate<Integer> inList = Predicates.in(list);
        assertFalse(inList.apply(null));
        assertFalse(inList.apply(-1));
        assertTrue(inList.apply(1));
        assertTrue(inList.apply(2));
        assertTrue(inList.apply(3));
        assertFalse(inList.apply(4));
    }

    @Test
    public void testCompose() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Function<Integer, Integer> doubleValue = new Function<Integer, Integer>() {
            @Override
            public Integer apply(@Nullable Integer integer) {
                return integer * 2;
            }
        };
        Predicate<Integer> composed = Predicates.compose(isEven, doubleValue);
        assertFalse(composed.apply(-1));
        assertFalse(composed.apply(1));
        assertTrue(composed.apply(2));
        assertTrue(composed.apply(4));
    }

    @Test
    public void testContainsPattern() throws IOException {
        Predicate<String> containsPattern = Predicates.containsPattern("abc.*def");
        String text = "The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog.";
        String text2 = "The quick brown fox jumps over the quick brown fox.";
        assertTrue(containsPattern.apply(text));
        assertFalse(containsPattern.apply(text2));
    }

    @Test
    public void testContainsPatternFromString() {
        Predicate<String> containsPattern = Predicates.containsPattern("abc.*def");
        assertTrue(containsPattern.apply("The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog."));
        assertFalse(containsPattern.apply("The quick brown fox jumps over the quick brown fox."));
    }
}