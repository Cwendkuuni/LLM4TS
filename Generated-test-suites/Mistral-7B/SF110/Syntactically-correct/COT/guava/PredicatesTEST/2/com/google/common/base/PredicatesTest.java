package com.google.common.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class PredicatesTest {

    

    // Test alwaysTrue()
    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply(2));
        assertTrue(alwaysTrue.apply(3));
    }

    // Test alwaysFalse()
    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply(2));
        assertFalse(alwaysFalse.apply(3));
    }

    // Test isNull()
    @Test
    public void testIsNull() {
        Predicate<String> isNull = Predicates.isNull();
        assertNull(isNull.apply(null));
        String str = new String();
        assertFalse(isNull.apply(str));
    }

    // Test notNull()
    @Test
    public void testNotNull() {
        Predicate<String> notNull = Predicates.notNull();
        String str = new String();
        assertTrue(notNull.apply(str));
        assertNull(notNull.apply(null));
    }

    // Test not()
    @Test
    public void testNot() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isOdd = Predicates.not(isEven);
        assertFalse(isEven.apply(1));
        assertTrue(isOdd.apply(1));
        assertTrue(isEven.apply(2));
        assertFalse(isOdd.apply(2));
    }

    // Test and()
    @Test
    public void testAnd() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input > 0;
            }
        };
        Predicate<Integer> andPredicate = Predicates.and(isEven, isPositive);
        assertTrue(andPredicate.apply(2));
        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(-1));
    }

    // Test or()
    @Test
    public void testOr() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isPositive = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input > 0;
            }
        };
        Predicate<Integer> orPredicate = Predicates.or(isEven, isPositive);
        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(1));
        assertFalse(orPredicate.apply(-1));
    }

    // Test equalTo()
    @Test
    public void testEqualTo() {
        Predicate<String> equalsString = Predicates.equalTo("sample");
        assertEquals("sample", equalsString.apply("sample"));
        assertNotEquals("different", equalsString.apply("sample"));
    }

    // Test instanceOf()
    @Test(expected = UnsupportedOperationException.class)
    public void testInstanceOfUnsupportedOperationException() {
        Predicate<Class<?>> instanceOfPredicate = Predicates.instanceOf(String.class);
        assertFalse(instanceOfPredicate.apply(new Object()));
    }

    // Test assignableFrom()
    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> assignableFromPredicate = Predicates.assignableFrom(String.class);
        assertTrue(assignableFromPredicate.apply(StringBuilder.class));
        assertTrue(assignableFromPredicate.apply(StringBuffer.class));
        assertFalse(assignableFromPredicate.apply(Integer.class));
    }

    // Test in()
    @Test
    public void testIn() {
        Collection<Integer> collection = Arrays.asList(1, 2, 3);
        Predicate<Integer> inPredicate = Predicates.in(collection);
        assertTrue(inPredicate.apply(1));
        assertTrue(inPredicate.apply(2));
        assertFalse(inPredicate.apply(0));
        assertFalse(inPredicate.apply(4));
    }

    // Test compose()
    @Test
    public void testCompose() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Function<Integer, Integer> square = new Function<Integer, Integer>() {
            @Override
            public Integer apply(@Nullable Integer input) {
                return input * input;
            }
        };
        Predicate<Integer> composePredicate = Predicates.compose(isEven, square);
        assertTrue(composePredicate.apply(4));
        assertFalse(composePredicate.apply(3));
    }

    // Test containsPattern()
    @Test
    public void testContainsPattern() {
        Pattern pattern = Pattern.compile("sample");
        Predicate<String> containsPatternPredicate = Predicates.containsPattern(pattern);
        assertTrue(containsPatternPredicate.apply("samplesample"));
        assertFalse(containsPatternPredicate.apply("different"));
        assertFalse(containsPatternPredicate.apply("samplesampleabc"));
    }

    // Test containsPatternFromString()
    @Test
    public void testContainsPatternFromString() {
        Pattern pattern = Pattern.compile("sample");
        Predicate<String> containsPatternFromStringPredicate = Predicates.containsPatternFromString(pattern.pattern());
        assertTrue(containsPatternFromStringPredicate.apply("samplesample"));
        assertFalse(containsPatternFromStringPredicate.apply("different"));
        assertFalse(containsPatternFromStringPredicate.apply("samplesampleabc"));
    }

    // Test edge cases and exception handling
    @Test(expected = NullPointerException.class)
    public void testNullPointerException() {
        Predicate<Object> isNullPredicate = Predicates.isNull();
        isNullPredicate.apply(null);
    }

    @Test(expected = ClassCastException.class)
    public void testClassCastException() {
        Collection<Object> collection = Arrays.asList(1, "two", 3.0);
        Predicate<Object> inPredicate = Predicates.in(collection);
        inPredicate.apply(new String("two"));
    }

    
}