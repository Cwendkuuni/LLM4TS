package com.google.common.base;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Predicates;

public class PredicatesTest {

    

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTruePredicate = Predicates.alwaysTrue();
        assertTrue(alwaysTruePredicate.apply(1));
        assertTrue(alwaysTruePredicate.apply(2));
        assertTrue(alwaysTruePredicate.apply(3));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalsePredicate = Predicates.alwaysFalse();
        assertFalse(alwaysFalsePredicate.apply(1));
        assertFalse(alwaysFalsePredicate.apply(2));
        assertFalse(alwaysFalsePredicate.apply(3));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> isNullPredicate = Predicates.isNull();

        assertFalse(isNullPredicate.apply(1));
        assertFalse(isNullPredicate.apply("Hello"));
        assertTrue(isNullPredicate.apply(null));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNullPredicate = Predicates.notNull();

        assertFalse(notNullPredicate.apply(null));
        assertTrue(notNullPredicate.apply(1));
        assertTrue(notNullPredicate.apply("Hello"));
    }

    @Test
    public void testNotPredicate() {
        Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> notEvenPredicate = Predicates.not(isEvenPredicate);

        assertFalse(notEvenPredicate.apply(2));
        assertTrue(notEvenPredicate.apply(1));
        assertTrue(notEvenPredicate.apply(3));
    }

    @Test
    public void testAndPredicateWithIterable() {
        Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> isOddPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 != 0;
            }
        };
        Predicate<Integer> andPredicate = Predicates.and(isEvenPredicate, isOddPredicate);

        assertFalse(andPredicate.apply(0));
        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(2));
        assertFalse(andPredicate.apply(3));
        assertFalse(andPredicate.apply(4));
    }

    @Test
    public void testAndPredicateWithArray() {
        Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> isOddPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 != 0;
            }
        };
        Predicate<Integer> andPredicate = Predicates.and(isEvenPredicate, isOddPredicate);

        assertFalse(andPredicate.apply(0));
        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(2));
        assertFalse(andPredicate.apply(3));
        assertFalse(andPredicate.apply(4));
    }

    @Test
    public void testAndPredicateWithTwoPredicates() {
        Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> isOddPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 != 0;
            }
        };
        Predicate<Integer> andPredicate = Predicates.and(isEvenPredicate, isOddPredicate);

        assertFalse(andPredicate.apply(0));
        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(2));
        assertFalse(andPredicate.apply(3));
        assertFalse(andPredicate.apply(4));
    }

    @Test
    public void testOrPredicateWithIterable() {
        Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> isOddPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 != 0;
            }
        };
        Predicate<Integer> orPredicate = Predicates.or(isEvenPredicate, isOddPredicate);

        assertFalse(orPredicate.apply(0));
        assertFalse(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(3));
        assertTrue(orPredicate.apply(4));
    }

    @Test
    public void testOrPredicateWithArray() {
        Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> isOddPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 != 0;
            }
        };
        Predicate<Integer> orPredicate = Predicates.or(isEvenPredicate, isOddPredicate);

        assertFalse(orPredicate.apply(0));
        assertFalse(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(3));
        assertTrue(orPredicate.apply(4));
    }

    @Test
    public void testOrPredicateWithTwoPredicates() {
        Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Predicate<Integer> isOddPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 != 0;
            }
        };
        Predicate<Integer> orPredicate = Predicates.or(isEvenPredicate, isOddPredicate);

        assertFalse(orPredicate.apply(0));
        assertFalse(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(3));
        assertTrue(orPredicate.apply(4));
    }

    @Test
    public void testEqualToPredicateWithNull() {
        Predicate<Object> isNullPredicate = Predicates.isNull();
        Predicate<Object> equalToNullPredicate = Predicates.equalTo(null);

        assertTrue(isNullPredicate.apply(null));
        assertTrue(equalToNullPredicate.apply(null));
    }

    @Test
    public void testEqualToPredicateWithNotNull() {
        Predicate<Object> isNullPredicate = Predicates.isNull();
        Predicate<Object> equalToPredicate = Predicates.equalTo(new Object());

        assertFalse(isNullPredicate.apply(new Object()));
        assertFalse(equalToPredicate.apply(null));
        assertTrue(equalToPredicate.apply(new Object()));
    }

    @Test(expected = NullPointerException.class)
    public void testInstanceOfPredicateWithNull() {
        Predicate<Object> instanceOfPredicate = Predicates.instanceOf(String.class);
        instanceOfPredicate.apply(null);
    }

    @Test(expected = ClassCastException.class)
    public void testInstanceOfPredicateWithNotCompatibleType() {
        Predicate<Object> instanceOfPredicate = Predicates.instanceOf(Integer.class);
        instanceOfPredicate.apply("Hello");
    }

    @Test
    public void testInstanceOfPredicateWithCompatibleType() {
        Predicate<Object> instanceOfPredicate = Predicates.instanceOf(String.class);
        assertTrue(instanceOfPredicate.apply("Hello"));
        assertFalse(instanceOfPredicate.apply(1));
    }

    @Test
    public void testAssignableFromPredicateWithNotCompatibleType() {
        Predicate<Class<?>> assignableFromPredicate = Predicates.assignableFrom(String.class);
        Predicate<Class<?>> assignableToIntegerPredicate = Predicates.assignableFrom(Integer.class);

        assertFalse(assignableFromPredicate.apply(Integer.class));
        assertFalse(assignableFromPredicate.apply(assignableToIntegerPredicate));
        assertTrue(assignableToIntegerPredicate.apply(Integer.class));
        assertTrue(assignableToIntegerPredicate.apply(assignableFromPredicate));
    }

    @Test
    public void testInPredicateWithEmptyCollection() {
        Predicate<Integer> inPredicate = Predicates.in(Collections.emptyList());

        assertFalse(inPredicate.apply(1));
        assertFalse(inPredicate.apply(2));
        assertFalse(inPredicate.apply(3));
        assertFalse(inPredicate.apply(4));
    }

    @Test
    public void testInPredicateWithCollectionContainingValue() {
        List<Integer> collection = Arrays.asList(1, 2, 3);
        Predicate<Integer> inPredicate = Predicates.in(collection);

        assertTrue(inPredicate.apply(1));
        assertTrue(inPredicate.apply(2));
        assertTrue(inPredicate.apply(3));
        assertFalse(inPredicate.apply(4));
    }

    @Test
    public void testComposePredicate() {
        Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer integer) {
                return integer % 2 == 0;
            }
        };
        Function<Integer, Integer> multipleByTwoFunction = new Function<Integer, Integer>() {
            @Override
            public Integer apply(@Nullable Integer input) {
                return input * 2;
            }
        };
        Predicate<Integer> composedPredicate = Predicates.compose(isEvenPredicate, multipleByTwoFunction);

        assertFalse(composedPredicate.apply(1));
        assertFalse(composedPredicate.apply(3));
        assertTrue(composedPredicate.apply(2));
        assertTrue(composedPredicate.apply(4));
    }

    @Test
    public void testContainsPatternPredicate() {
        Predicate<CharSequence> containsPatternPredicate = Predicates.containsPattern("Hello.*World");

        assertFalse(containsPatternPredicate.apply("Hello"));
        assertTrue(containsPatternPredicate.apply("HelloWorld"));
        assertFalse(containsPatternPredicate.apply("Hi"));
    }

    @Test
    public void testContainsPatternFromStringPredicate() {
        Predicate<CharSequence> containsPatternPredicate = Predicates.containsPatternFromString("Hello.*World");

        assertFalse(containsPatternPredicate.apply("Hello"));
        assertTrue(containsPatternPredicate.apply("HelloWorld"));
        assertFalse(containsPatternPredicate.apply("Hi"));
    }

    
}