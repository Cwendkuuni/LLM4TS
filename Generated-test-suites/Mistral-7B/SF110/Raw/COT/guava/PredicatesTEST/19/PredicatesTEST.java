package com.google.common.base;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public class PredicatesTest {

    

    // Test alwaysTrue method
    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(0));
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply(new Object()));
    }

    // Test alwaysFalse method
    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(0));
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply(new Object()));
    }

    // Test isNull method
    @Test
    public void testIsNull() {
        Predicate<Object> isNull = Predicates.isNull();
        assertFalse(isNull.apply(null));
        assertFalse(isNull.apply(new Object()));
        assertTrue(isNull.apply(null));
        assertTrue(isNull.apply(new String[]{}));
    }

    // Test notNull method
    @Test
    public void testNotNull() {
        Predicate<Object> notNull = Predicates.notNull();
        assertFalse(notNull.apply(null));
        assertTrue(notNull.apply(new Object()));
        assertTrue(notNull.apply(new String[]{}));
    }

    // Test not method
    @Test
    public void testNot() {
        Predicate<Boolean> isTrue = Predicates.alwaysTrue();
        Predicate<Boolean> isFalse = Predicates.alwaysFalse();
        Predicate<Boolean> not = Predicates.not(isTrue);
        Predicate<Boolean> result = Predicates.and(isTrue, isFalse);
        Predicate<Boolean> expectedNot = Predicates.not(result);

        assertTrue(not.apply(true));
        assertFalse(not.apply(false));
        assertTrue(expectedNot.apply(false));
        assertFalse(expectedNot.apply(true));
    }

    // Test and method with Iterable
    @Test
    public void testAndIterable() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable final Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable final Integer input) {
                return input % 2 != 0;
            }
        };
        Predicate<Integer> and = Predicates.and(Arrays.asList(isEven, isOdd));

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        for (int i : list) {
            if (and.apply(i)) {
                fail("Unexpected result: " + i);
            }
        }
    }

    // Test and method with var-args
    @Test
    public void testAndVarargs() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable final Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable final Integer input) {
                return input % 2 != 0;
            }
        };
        Predicate<Integer> and = Predicates.and(isEven, isOdd);

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        for (int i : list) {
            if (and.apply(i)) {
                fail("Unexpected result: " + i);
            }
        }
    }

    // Test and method with two Predicates
    @Test
    public void testAndTwoPredicates() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable final Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable final Integer input) {
                return input % 2 != 0;
            }
        };
        Predicate<Integer> and = Predicates.and(isEven, isOdd);

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        for (int i : list) {
            if (and.apply(i)) {
                fail("Unexpected result: " + i);
            }
        }
    }

    // Test or method with Iterable
    @Test
    public void testOrIterable() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable final Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable final Integer input) {
                return input % 2 != 0;
            }
        };
        Predicate<Integer> or = Predicates.or(Arrays.asList(isEven, isOdd));

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        for (int i : list) {
            if (!or.apply(i)) {
                fail("Unexpected result: " + i);
            }
        }
    }

    // Test or method with var-args
    @Test
    public void testOrVarargs() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable final Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable final Integer input) {
                return input % 2 != 0;
            }
        };
        Predicate<Integer> or = Predicates.or(isEven, isOdd);

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        for (int i : list) {
            if (!or.apply(i)) {
                fail("Unexpected result: " + i);
            }
        }
    }

    // Test or method with two Predicates
    @Test
    public void testOrTwoPredicates() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable final Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isOdd = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable final Integer input) {
                return input % 2 != 0;
            }
        };
        Predicate<Integer> or = Predicates.or(isEven, isOdd);

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        for (int i : list) {
            if (!or.apply(i)) {
                fail("Unexpected result: " + i);
            }
        }
    }

    // Test equalsTo method with null
    @Test
    public void testEqualsToNull() {
        Predicate<Object> equalsToNull = Predicates.equalTo(null);

        assertFalse(equalsToNull.apply(new Object()));
        assertTrue(equalsToNull.apply(null));
    }

    // Test equalsTo method with non-null
    @Test
    public void testEqualsToNonNull() {
        Predicate<Object> equalsToObject = Predicates.equalTo("test");

        assertFalse(equalsToObject.apply(new Object()));
        assertTrue(equalsToObject.apply("test"));
    }

    // Test instanceOf method
    @Test(expected = AssertionError.class)
    public void testInstanceOf() {
        Predicate<Object> instanceOfString = Predicates.instanceOf(String.class);
        instanceOfString.apply(new Integer(1));
    }

    // Test assignableFrom method
    @Test(expected = AssertionError.class)
    public void testAssignableFrom() {
        Predicate<Class<?>> assignableFromString = Predicates.assignableFrom(String.class);
        assignableFromString.apply(Integer.class);
    }

    // Test in method with null
    @Test
    public void testInNull() {
        Collection<Integer> collection = Collections.emptyList();
        Predicate<Object> in = Predicates.in(collection);

        assertTrue(in.apply(null));
    }

    // Test in method with non-matching element
    @Test
    public void testInNonMatchingElement() {
        Collection<Integer> collection = Arrays.asList(1, 2, 3, 4, 5);
        Predicate<Object> in = Predicates.in(collection);

        assertFalse(in.apply(6));
    }

    // Test in method with matching element
    @Test
    public void testInMatchingElement() {
        Collection<Integer> collection = Arrays.asList(1, 2, 3, 4, 5);
        Predicate<Object> in = Predicates.in(collection);

        assertTrue(in.apply(2));
    }

    // Test compose method
    @Test
    public void testCompose() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable final Integer input) {
                return input % 2 == 0;
            }
        };
        Function<Integer, Boolean> isPositive = new Function<Integer, Boolean>() {
            @Override
            public Boolean apply(@Nullable final Integer input) {
                return input > 0;
            }
        };
        Predicate<Integer> compose = Predicates.compose(isEven, isPositive);

        List<Integer> list = Arrays.asList(-1, 0, 1, 2, 3, 4, 5);

        for (int i : list) {
            if (!compose.apply(i)) {
                fail("Unexpected result: " + i);
            }
        }
    }

    // Test containsPattern method with null
    @Test(expected = NullPointerException.class)
    public void testContainsPatternNull() {
        Predicate<CharSequence> containsPattern = Predicates.containsPattern("(.*).txt");
        containsPattern.apply(null);
    }

    // Test containsPattern method with non-matching string
    @Test
    public void testContainsPatternNonMatchingString() {
        Predicate<CharSequence> containsPattern = Predicates.containsPattern("(.*).txt");

        assertFalse(containsPattern.apply("test.java"));
    }

    // Test containsPattern method with matching string
    @Test
    public void testContainsPatternMatchingString() {
        Predicate<CharSequence> containsPattern = Predicates.containsPattern("(.*).txt");

        assertTrue(containsPattern.apply("test.txt"));
    }

    
}