package com.google.common.base;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PredicatesTest {

    

    // Tests for alwaysTrue() method
    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply(2.0));
        assertTrue(alwaysTrue.apply("hello"));
    }

    // Tests for alwaysFalse() method
    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply(2.0));
        assertFalse(alwaysFalse.apply("hello"));
    }

    // Tests for isNull() method
    @Test
    public void testIsNull() {
        Predicate<Object> isNull = Predicates.isNull();
        assertFalse(isNull.apply(null));
        assertFalse(isNull.apply(1));
        assertFalse(isNull.apply(2.0));
        assertFalse(isNull.apply("hello"));
        assertTrue(isNull.apply(null));
    }

    // Tests for notNull() method
    @Test
    public void testNotNull() {
        Predicate<Object> notNull = Predicates.notNull();
        assertFalse(notNull.apply(null));
        assertTrue(notNull.apply(1));
        assertTrue(notNull.apply(2.0));
        assertTrue(notNull.apply("hello"));
    }

    // Tests for not() method
    @Test
    public void testNot() {
        Predicate<Boolean> alwaysTrue = Predicates.alwaysTrue();
        Predicate<Boolean> alwaysFalse = Predicates.alwaysFalse();
        Predicate<Boolean> notTrue = Predicates.not(alwaysTrue);
        Predicate<Boolean> notFalse = Predicates.not(alwaysFalse);

        assertFalse(notTrue.apply(true));
        assertTrue(notTrue.apply(false));
        assertTrue(notFalse.apply(true));
        assertFalse(notFalse.apply(false));
    }

    // Tests for and() method with Iterable
    @Test
    public void testAndWithIterable() {
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

        Predicate<Integer> andPredicate = Predicates.and(Arrays.asList(isEven, isPositive));

        assertTrue(andPredicate.apply(2));
        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(-2));
    }

    // Tests for and() method with Predicate...
    @Test
    public void testAndWithPredicates() {
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

        assertTrue(andPredicate.apply(2));
        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(-2));
    }

    // Tests for and() method with two Predicates
    @Test
    public void testAndWithTwoPredicates() {
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

        assertTrue(andPredicate.apply(2));
        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(-2));
    }

    // Tests for or() method with Iterable
    @Test
    public void testOrWithIterable() {
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

        Predicate<Integer> orPredicate = Predicates.or(Arrays.asList(isEven, isOdd));

        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(1));
        assertFalse(orPredicate.apply(-2));
    }

    // Tests for or() method with Predicate...
    @Test
    public void testOrWithPredicates() {
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

        Predicate<Integer> orPredicate = Predicates.or(isEven, isOdd);

        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(1));
        assertFalse(orPredicate.apply(-2));
    }

    // Tests for or() method with two Predicates
    @Test
    public void testOrWithTwoPredicates() {
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

        Predicate<Integer> orPredicate = Predicates.or(isEven, isOdd);

        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(1));
        assertFalse(orPredicate.apply(-2));
    }

    // Tests for equalTo() method
    @Test
    public void testEqualTo() {
        Predicate<Integer> equalToThree = Predicates.equalTo(3);
        assertTrue(equalToThree.apply(3));
        assertFalse(equalToThree.apply(2));
        assertFalse(equalToThree.apply(4));
    }

    // Tests for instanceOf() method
    @Test(expected = IllegalArgumentException.class)
    public void testInstanceOfWithNull() {
        Predicates.instanceOf(null);
    }

    @Test
    public void testInstanceOf() {
        Predicate<String> isString = Predicates.instanceOf(String.class);
        assertTrue(isString.apply("hello"));
        assertFalse(isString.apply(1));
        assertFalse(isString.apply(2.0));
    }

    // Tests for assignableFrom() method
    @Test(expected = IllegalArgumentException.class)
    public void testAssignableFromWithNull() {
        Predicates.assignableFrom(null);
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> isNumber = Predicates.assignableFrom(Number.class);
        assertTrue(isNumber.apply(Integer.class));
        assertTrue(isNumber.apply(Double.class));
        assertFalse(isNumber.apply(String.class));
    }

    // Tests for in() method with Collection
    @Test
    public void testInWithCollection() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
        Predicate<Integer> inPredicate = Predicates.in(numbers);

        assertTrue(inPredicate.apply(1));
        assertTrue(inPredicate.apply(2));
        assertTrue(inPredicate.apply(3));
        assertFalse(inPredicate.apply(4));
        assertFalse(inPredicate.apply(5));
    }

    // Tests for in() method with Iterable
    @Test
    public void testInWithIterable() {
        Iterable<Integer> iterableNumbers = () -> Arrays.asList(1, 2, 3, 4).iterator();
        Predicate<Integer> inPredicate = Predicates.in(iterableNumbers);

        assertTrue(inPredicate.apply(1));
        assertTrue(inPredicate.apply(2));
        assertTrue(inPredicate.apply(3));
        assertFalse(inPredicate.apply(4));
        assertFalse(inPredicate.apply(5));
    }

    // Tests for compose() method
    @Test
    public void testCompose() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };
        Function<Integer, Integer> doubleValue = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input * 2;
            }
        };

        Predicate<Integer> compositePredicate = Predicates.compose(isEven, doubleValue);

        assertFalse(compositePredicate.apply(1));
        assertTrue(compositePredicate.apply(2));
        assertTrue(compositePredicate.apply(4));
    }

    // Tests for containsPattern() method with String pattern
    @Test(expected = IllegalArgumentException.class)
    public void testContainsPatternWithNull() {
        Predicates.containsPattern(null);
    }

    @Test
    public void testContainsPatternWithString() {
        Predicate<String> containsPattern = Predicates.containsPattern("ab");
        assertTrue(containsPattern.apply("abcdab"));
        assertFalse(containsPattern.apply("abc"));
        assertFalse(containsPattern.apply("abcd"));
    }

    // Tests for containsPattern() method with Pattern pattern
    @Test(expected = IllegalArgumentException.class)
    public void testContainsPatternWithNullPattern() {
        Predicates.containsPattern(null);
    }

    @Test
    public void testContainsPatternWithPattern() {
        Predicate<String> containsPattern = Predicates.containsPattern(Pattern.compile("ab"));
        assertTrue(containsPattern.apply("abcdab"));
        assertFalse(containsPattern.apply("abc"));
        assertFalse(containsPattern.apply("abcd"));
    }

    // Test for defensiveCopy() method
    @Test
    public void testDefensiveCopy() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        List<Integer> defensiveCopy = Predicates.defensiveCopy(list);

        assertEquals(list, defensiveCopy);
        try {
            defensiveCopy.add(5);
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    
}