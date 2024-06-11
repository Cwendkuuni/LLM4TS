package org.example.commons.base.test;

import static org.junit.Assert.*;

import com.google.common.base.Predicates;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply("hello"));
        assertTrue(alwaysTrue.apply(1));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Object> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply("hello"));
        assertFalse(alwaysFalse.apply(1));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> isNull = Predicates.isNull();
        assertNull(isNull.apply(null));
        assertFalse(isNull.apply("hello"));
        assertFalse(isNull.apply(1));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNull = Predicates.notNull();
        assertNull(notNull.apply(null));
        assertTrue(notNull.apply("hello"));
        assertTrue(notNull.apply(1));
    }

    @Test
    public void testNot() {
        Predicate<Boolean> predicate = Predicates.alwaysTrue();
        Predicate<Boolean> notPredicate = Predicates.not(predicate);

        assertFalse(notPredicate.apply(true));
        assertTrue(notPredicate.apply(false));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> isPositive = Predicates.not(Predicates.isNull()).and(Predicates.alwaysTrue()).and(new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer > 0;
            }
        });

        Predicate<Integer> andPredicate = Predicates.and(isEven, isPositive);

        assertTrue(andPredicate.apply(2));
        assertTrue(andPredicate.apply(4));
        assertFalse(andPredicate.apply(0));
        assertFalse(andPredicate.apply(-1));

        List<Predicate<Integer>> predicates = Arrays.asList(Predicates.isNull(), Predicates.alwaysTrue(), isEven, isPositive);
        Predicate<Integer> andPredicate2 = Predicates.and(predicates.toArray(new Predicate[0]));

        assertTrue(andPredicate2.apply(2));
        assertTrue(andPredicate2.apply(4));
        assertFalse(andPredicate2.apply(0));
        assertFalse(andPredicate2.apply(-1));
    }

    @Test
    public void testOr() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> isOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 != 0;
            }
        };

        Predicate<Integer> orPredicate = Predicates.or(isEven, isOdd);

        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(1));
        assertFalse(orPredicate.apply(0));

        List<Predicate<Integer>> predicates = Arrays.asList(isEven, isOdd);
        Predicate<Integer> orPredicate2 = Predicates.or(predicates.toArray(new Predicate[0]));

        assertTrue(orPredicate2.apply(2));
        assertTrue(orPredicate2.apply(1));
        assertFalse(orPredicate2.apply(0));
    }

    @Test
    public void testEqualsTo() {
        Predicate<Object> isEqualToNull = Predicates.equalTo(null);
        assertNull(isEqualToNull.apply(null));
        assertFalse(isEqualToNull.apply("hello"));
        assertFalse(isEqualToNull.apply(1));

        Predicate<Object> isEqualToString = Predicates.equalTo("hello");
        assertFalse(isEqualToString.apply(null));
        assertFalse(isEqualToString.apply("world"));
        assertTrue(isEqualToString.apply("hello"));

        Predicate<Object> isEqualToOne = Predicates.equalTo(1);
        assertFalse(isEqualToOne.apply(null));
        assertFalse(isEqualToOne.apply(2));
        assertTrue(isEqualToOne.apply(1));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> isInstanceOfString = Predicates.instanceOf(String.class);
        assertFalse(isInstanceOfString.apply(null));
        assertFalse(isInstanceOfString.apply(1));
        assertTrue(isInstanceOfString.apply("hello"));

        Predicate<Object> isInstanceOfNumber = Predicates.instanceOf(Number.class);
        assertFalse(isInstanceOfNumber.apply(null));
        assertFalse(isInstanceOfNumber.apply("hello"));
        assertTrue(isInstanceOfNumber.apply(1));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> isAssignableFromString = Predicates.assignableFrom(String.class);
        assertFalse(isAssignableFromString.apply(null));
        assertFalse(isAssignableFromString.apply(int.class));
        assertTrue(isAssignableFromString.apply(StringBuilder.class));

        Predicate<Class<?>> isAssignableFromNumber = Predicates.assignableFrom(Number.class);
        assertFalse(isAssignableFromNumber.apply(null));
        assertFalse(isAssignableFromNumber.apply(String.class));
        assertTrue(isAssignableFromNumber.apply(Number.class));
        assertTrue(isAssignableFromNumber.apply(Integer.class));
        assertTrue(isAssignableFromNumber.apply(Double.class));
    }

    @Test
    public void testIn() {
        Collection<Integer> collection = Arrays.asList(1, 2, 3);
        Predicate<Integer> inPredicate = Predicates.in(collection);

        assertFalse(inPredicate.apply(null));
        assertFalse(inPredicate.apply(0));
        assertTrue(inPredicate.apply(1));
        assertTrue(inPredicate.apply(2));
        assertTrue(inPredicate.apply(3));

        List<Predicate<Integer>> predicates = Arrays.asList(Predicates.isNull(), Predicates.alwaysTrue(), isEven, isPositive);
        Predicate<Integer> inPredicate2 = Predicates.in(Collections.singletonList(2));

        assertFalse(inPredicate2.apply(null));
        assertFalse(inPredicate2.apply(0));
        assertFalse(inPredicate2.apply(1));
        assertTrue(inPredicate2.apply(2));
        assertFalse(inPredicate2.apply(3));
    }

    @Test
    public void testCompose() {
        Function<Integer, Integer> function = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer * 2;
            }
        };

        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };

        Predicate<Integer> composePredicate = Predicates.compose(isEven, function);

        assertFalse(composePredicate.apply(1));
        assertTrue(composePredicate.apply(2));
        assertTrue(composePredicate.apply(4));

        List<Predicate<Integer>> predicates = Arrays.asList(isEven, function);
        Predicate<Integer> composePredicate2 = Predicates.compose(predicates.toArray(new Predicate[0]), function);

        assertFalse(composePredicate2.apply(1));
        assertTrue(composePredicate2.apply(2));
        assertTrue(composePredicate2.apply(4));
    }

    @Test
    public void testContainsPattern() {
        Predicate<CharSequence> containsPattern = Predicates.containsPattern(".*hello.*.*");

        assertFalse(containsPattern.apply(null));
        assertFalse(containsPattern.apply("world"));
        assertTrue(containsPattern.apply("hello world"));
        assertTrue(containsPattern.apply("hello"));
        assertTrue(containsPattern.apply("hello world hello"));

        Predicate<CharSequence> containsPatternFromString = Predicates.containsPatternFromString("hello");

        assertFalse(containsPatternFromString.apply(null));
        assertFalse(containsPatternFromString.apply("world"));
        assertTrue(containsPatternFromString.apply("hello world"));
        assertTrue(containsPatternFromString.apply("hello"));
        assertTrue(containsPatternFromString.apply("hello world hello"));
    }
}