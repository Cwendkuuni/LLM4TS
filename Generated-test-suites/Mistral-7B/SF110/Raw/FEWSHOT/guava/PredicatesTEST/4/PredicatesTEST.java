package com.google.common.base;

import static org.junit.Assert.*;
import com.google.common.base.Predicates.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTrue = alwaysTrue();
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply(2));
        assertTrue(alwaysTrue.apply(3));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalse = alwaysFalse();
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply(2));
        assertFalse(alwaysFalse.apply(3));
    }

    @Test
    public void testIsNull() {
        Predicate<Integer> isNull = isNull();
        assertNull(isNull.apply(null));
        assertFalse(isNull.apply(1));
        assertFalse(isNull.apply(2));
        assertFalse(isNull.apply(3));
    }

    @Test
    public void testNotNull() {
        Predicate<Integer> notNull = notNull();
        assertNull(notNull.apply(null));
        assertTrue(notNull.apply(1));
        assertTrue(notNull.apply(2));
        assertTrue(notNull.apply(3));
    }

    @Test
    public void testNot() {
        Predicate<Boolean> isTrue = isNull();
        Predicate<Boolean> isFalse = not(isTrue);

        assertFalse(isFalse.apply(true));
        assertTrue(isFalse.apply(false));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> predicate1 = alwaysTrue();
        Predicate<Integer> predicate2 = isNull();
        Predicate<Integer> andPredicate = and(predicate1, predicate2);

        assertTrue(andPredicate.apply(1));
        assertTrue(andPredicate.apply(null));
        assertFalse(andPredicate.apply(2));
        assertFalse(andPredicate.apply(3));
    }

    @Test
    public void testAndWithMultiplePredicates() {
        Predicate<Integer> predicate1 = isNull();
        Predicate<Integer> predicate2 = isNotNull();
        Predicate<Integer> predicate3 = alwaysFalse();
        Predicate<Integer> andPredicate = and(predicate1, predicate2, predicate3);

        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(2));
        assertFalse(andPredicate.apply(3));
    }

    @Test
    public void testAndWithOnePredicate() {
        Predicate<Integer> predicate = alwaysTrue();
        Predicate<Integer> andPredicate = and(predicate);

        assertTrue(andPredicate.apply(1));
        assertTrue(andPredicate.apply(2));
        assertTrue(andPredicate.apply(3));
    }

    @Test
    public void testOr() {
        Predicate<Integer> predicate1 = alwaysTrue();
        Predicate<Integer> predicate2 = isNull();
        Predicate<Integer> orPredicate = or(predicate1, predicate2);

        assertTrue(orPredicate.apply(1));
        assertTrue(orPredicate.apply(null));
        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(3));
    }

    @Test
    public void testOrWithMultiplePredicates() {
        Predicate<Integer> predicate1 = alwaysTrue();
        Predicate<Integer> predicate2 = isNotNull();
        Predicate<Integer> predicate3 = alwaysFalse();
        Predicate<Integer> orPredicate = or(predicate1, predicate2, predicate3);

        assertTrue(orPredicate.apply(1));
        assertFalse(orPredicate.apply(null));
        assertTrue(orPredicate.apply(2));
        assertFalse(orPredicate.apply(3));
    }

    @Test
    public void testOrWithOnePredicate() {
        Predicate<Integer> predicate = alwaysTrue();
        Predicate<Integer> orPredicate = or(predicate);

        assertTrue(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(3));
    }

    @Test
    public void testEqualsTo() {
        Predicate<Integer> equalsToThree = equalTo(3);
        assertTrue(equalsToThree.apply(3));
        assertFalse(equalsToThree.apply(1));
        assertFalse(equalsToThree.apply(2));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> instanceOfString = instanceOf(String.class);
        assertTrue(instanceOfString.apply(""));
        assertFalse(instanceOfString.apply(1));
        assertFalse(instanceOfString.apply(2.0));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> assignableFromNumber = assignableFrom(Number.class);
        assertTrue(assignableFromNumber.apply(Integer.class));
        assertTrue(assignableFromNumber.apply(Double.class));
        assertFalse(assignableFromNumber.apply(String.class));
    }

    @Test
    public void testIn() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Predicate<Integer> inList = in(list);

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
        Function<Integer, Integer> square = new Function<Integer, Integer>() {
            @Override
            public Integer apply(@Nullable Integer integer) {
                return integer * integer;
            }
        };
        Predicate<Integer> composedPredicate = compose(isEven, square);

        assertFalse(composedPredicate.apply(1));
        assertTrue(composedPredicate.apply(4));
        assertTrue(composedPredicate.apply(9));
        assertTrue(composedPredicate.apply(16));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> containsPattern = containsPattern("ab");
        assertTrue(containsPattern.apply("abcd"));
        assertTrue(containsPattern.apply("efghab"));
        assertFalse(containsPattern.apply("xyz"));
    }

    @Test
    public void testContainsPatternFromString() {
        Predicate<String> containsPattern = containsPatternFromString("aba[bc]d");
        assertTrue(containsPattern.apply("abacd"));
        assertTrue(containsPattern.apply("efghabcd"));
        assertFalse(containsPattern.apply("xyz"));
    }
}