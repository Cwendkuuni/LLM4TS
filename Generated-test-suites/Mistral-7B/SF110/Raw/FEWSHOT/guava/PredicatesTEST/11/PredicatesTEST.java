package com.google.common.base;

import static org.junit.Assert.*;
import com.google.common.base.Predicates.*;
import java.util.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> alwaysTrue = alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply("test"));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Object> alwaysFalse = alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply("test"));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> isNull = isNull();
        assertNull(isNull.apply(null));
        assertFalse(isNull.apply(1));
        assertFalse(isNull.apply("test"));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNull = notNull();
        assertNull(notNull.apply(null));
        assertTrue(notNull.apply(1));
        assertTrue(notNull.apply("test"));
    }

    @Test
    public void testNot() {
        Predicate<Boolean> predicate = isNull();
        Predicate<Boolean> notPredicate = not(predicate);
        assertFalse(notPredicate.apply(null));
        assertTrue(notPredicate.apply(new Boolean(true)));

        Predicate<String> stringPredicate = alwaysFalse();
        Predicate<String> notStringPredicate = not(stringPredicate);
        assertTrue(notStringPredicate.apply("test"));
        assertFalse(notStringPredicate.apply(null));
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

        Predicate<Integer> andPredicate = and(isEven, isPositive);

        assertTrue(andPredicate.apply(2));
        assertFalse(andPredicate.apply(-2));
        assertFalse(andPredicate.apply(1));

        Predicate<String> stringPredicate1 = alwaysFalse();
        Predicate<String> stringPredicate2 = alwaysTrue();
        Predicate<String> andStringPredicate = and(stringPredicate1, stringPredicate2);
        assertTrue(andStringPredicate.apply("test"));
        assertFalse(andStringPredicate.apply(null));

        Predicate<String> stringPredicate3 = containsPattern("ab");
        Predicate<String> stringPredicate4 = containsPattern("ba");
        Predicate<String> andStringPredicate2 = and(stringPredicate3, stringPredicate4);
        assertTrue(andStringPredicate2.apply("abtest"));
        assertFalse(andStringPredicate2.apply("testba"));
    }

    @Test
    public void testOr() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 != 0;
            }
        };

        Predicate<Integer> orPredicate = or(isEven, isOdd);

        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(1));

        Predicate<String> stringPredicate1 = alwaysFalse();
        Predicate<String> stringPredicate2 = alwaysTrue();
        Predicate<String> orStringPredicate = or(stringPredicate1, stringPredicate2);
        assertTrue(orStringPredicate.apply("test"));
        assertTrue(orStringPredicate.apply(null));

        Predicate<String> stringPredicate3 = containsPattern("ab");
        Predicate<String> stringPredicate4 = containsPattern("ba");
        Predicate<String> orStringPredicate2 = or(stringPredicate3, stringPredicate4);
        assertTrue(orStringPredicate2.apply("abtest"));
        assertTrue(orStringPredicate2.apply("testba"));
    }

    @Test
    public void testEqualTo() {
        Predicate<Integer> isEqualToFive = equalTo(5);
        Predicate<Integer> isEqualToSeven = equalTo(7);

        assertFalse(isEqualToFive.apply(6));
        assertTrue(isEqualToFive.apply(5));
        assertFalse(isEqualToFive.apply(7));

        assertFalse(isEqualToSeven.apply(5));
        assertFalse(isEqualToSeven.apply(7));
        assertTrue(isEqualToSeven.apply(7));
    }

    @Test
    public void testInstanceOf() {
        @SuppressWarnings("unchecked")
        Predicate<Class<StringBuilder>> isInstanceOfStringBuilder = Predicates.instanceOf(StringBuilder.class);
        @SuppressWarnings("unchecked")
        Predicate<Class<StringBuffer>> isInstanceOfStringBuffer = Predicates.instanceOf(StringBuffer.class);

        assertTrue(isInstanceOfStringBuilder.apply(StringBuilder.class));
        assertFalse(isInstanceOfStringBuilder.apply(StringBuffer.class));

        assertTrue(isInstanceOfStringBuffer.apply(StringBuffer.class));
        assertFalse(isInstanceOfStringBuffer.apply(StringBuilder.class));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> isAssignableFromNumber = assignableFrom(Number.class);
        Predicate<Class<?>> isAssignableFromString = assignableFrom(String.class);

        assertTrue(isAssignableFromNumber.apply(Integer.class));
        assertTrue(isAssignableFromNumber.apply(Double.class));
        assertFalse(isAssignableFromNumber.apply(String.class));

        assertTrue(isAssignableFromString.apply(String.class));
        assertFalse(isAssignableFromString.apply(Integer.class));
        assertFalse(isAssignableFromString.apply(Double.class));
    }

    @Test
    public void testIn() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Predicate<Integer> inPredicate = in(numbers);

        assertFalse(inPredicate.apply(-1));
        assertTrue(inPredicate.apply(1));
        assertTrue(inPredicate.apply(2));
        assertFalse(inPredicate.apply(6));

        List<String> strings = Arrays.asList("test1", "test2");
        Predicate<String> inPredicateString = in(strings);
        assertTrue(inPredicateString.apply("test1"));
        assertTrue(inPredicateString.apply("test2"));
        assertFalse(inPredicateString.apply("test3"));
    }

    @Test
    public void testCompose() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Function<Integer, Integer> square = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input * input;
            }
        };

        Predicate<Integer> composedPredicate = compose(isEven, square);

        assertEquals(16, composedPredicate.apply(4).intValue());
        assertFalse(composedPredicate.apply(3).isPresent());
        assertFalse(composedPredicate.apply(5).isPresent());
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> containsPattern = containsPattern("ab");
        Predicate<String> containsPattern2 = containsPattern("ba");

        assertTrue(containsPattern.apply("abtest"));
        assertFalse(containsPattern.apply("testba"));
        assertFalse(containsPattern.apply("test"));

        assertTrue(containsPattern2.apply("testba"));
        assertFalse(containsPattern2.apply("abtest"));
        assertFalse(containsPattern2.apply("test"));
    }
}