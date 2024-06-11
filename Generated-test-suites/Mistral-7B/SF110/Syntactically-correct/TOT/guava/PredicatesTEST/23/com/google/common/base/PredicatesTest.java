package com.google.common.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PredicatesTest
{
    @Test
    public void testAlwaysTrue()
    {
        Predicate<Integer> alwaysTrue = Predicates.alwaysTrue();

        // typical use case
        assertTrue(alwaysTrue.apply(1));
        assertTrue(alwaysTrue.apply(2));
        assertTrue(alwaysTrue.apply(3));

        // edge case: null input
        assertNull(alwaysTrue.apply(null));

        // error scenario: input of wrong type
        try {
            alwaysTrue.apply("test");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAlwaysFalse()
    {
        Predicate<Integer> alwaysFalse = Predicates.alwaysFalse();

        // typical use case
        assertFalse(alwaysFalse.apply(1));
        assertFalse(alwaysFalse.apply(2));
        assertFalse(alwaysFalse.apply(3));

        // edge case: null input
        assertNull(alwaysFalse.apply(null));

        // error scenario: input of wrong type
        try {
            alwaysFalse.apply("test");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsNull()
    {
        Predicate<Integer> isNull = Predicates.isNull();

        // typical use case
        assertNull(isNull.apply(null));

        // edge case: non-null input
        assertFalse(isNull.apply(1));
        assertFalse(isNull.apply(2));
        assertFalse(isNull.apply(3));
    }

    @Test
    public void testNot()
    {
        Predicate<Integer> isEven = new Predicate<Integer>()
        {
            @Override
            public boolean apply(Integer input)
            {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> notEven = Predicates.not(isEven);

        // typical use case
        assertFalse(notEven.apply(0));
        assertTrue(notEven.apply(1));
        assertTrue(notEven.apply(2));

        // edge case: null input
        assertNull(notEven.apply(null));

        // error scenario: input of wrong type
        try {
            notEven.apply("test");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAnd()
    {
        Predicate<Integer> isEven = new Predicate<Integer>()
        {
            @Override
            public boolean apply(Integer input)
            {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isPositive = new Predicate<Integer>()
        {
            @Override
            public boolean apply(Integer input)
            {
                return input > 0;
            }
        };

        Predicate<Integer> and = Predicates.and(isEven, isPositive);

        // typical use case
        assertFalse(and.apply(-2));

        // edge cases: both conditions are not met
        assertFalse(and.apply(0));
        assertFalse(and.apply(2));
        assertFalse(and.apply(3));
        assertFalse(and.apply(4));

        // typical use case: both conditions are met
        assertTrue(and.apply(4));
        assertTrue(and.apply(6));

        // edge case: null input for one condition
        assertNull(and.apply(null, isPositive));

        // error scenarios: inputs of wrong type or null
        try {
            and.apply(isEven, "test");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            and.apply(null, isPositive);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testOr()
    {
        Predicate<Integer> isEven = new Predicate<Integer>()
        {
            @Override
            public boolean apply(Integer input)
            {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> isPositive = new Predicate<Integer>()
        {
            @Override
            public boolean apply(Integer input)
            {
                return input > 0;
            }
        };

        Predicate<Integer> or = Predicates.or(isEven, isPositive);

        // typical use case
        assertTrue(or.apply(0));
        assertTrue(or.apply(1));
        assertTrue(or.apply(2));
        assertTrue(or.apply(3));
        assertTrue(or.apply(4));
        assertTrue(or.apply(6));

        // edge cases: only one condition is met
        assertTrue(or.apply(1));
        assertTrue(or.apply(2));

        // edge case: null input for one condition
        assertNull(or.apply(null, isPositive));

        // error scenarios: inputs of wrong type or null
        try {
            or.apply(isEven, "test");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            or.apply(null, isPositive);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testEqualTo()
    {
        Predicate<Integer> equalToThree = Predicates.equalTo(3);

        // typical use case
        assertTrue(equalToThree.apply(3));

        // edge cases: not equal
        assertFalse(equalToThree.apply(1));
        assertFalse(equalToThree.apply(2));
        assertFalse(equalToThree.apply(4));

        // error scenario: null input
        assertNull(equalToThree.apply(null));

        // error scenario: input of wrong type
        try {
            equalToThree.apply("test");
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // expected
        }
    }

    @Test
    public void testInstanceOf()
    {
        Predicate<Object> instanceOfString = Predicates.instanceOf(String.class);

        // typical use case: input is an instance of the expected class
        assertTrue(instanceOfString.apply("test"));

        // edge case: input is not an instance of the expected class
        assertFalse(instanceOfString.apply(1));
        assertFalse(instanceOfString.apply(2));
        assertFalse(instanceOfString.apply(3));
        assertFalse(instanceOfString.apply(true));
        assertFalse(instanceOfString.apply(null));

        // error scenario: null input
        assertNull(instanceOfString.apply(null));

        // error scenario: input of wrong type
        try {
            instanceOfString.apply(List.class);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // expected
        }
    }

    @Test
    public void testAssignableFrom()
    {
        Predicate<Class<?>> assignableFromNumber = Predicates.assignableFrom(Number.class);

        // typical use case: input is assignable from the expected class
        assertTrue(assignableFromNumber.apply(Integer.class));
        assertTrue(assignableFromNumber.apply(Long.class));
        assertTrue(assignableFromNumber.apply(Double.class));
        assertTrue(assignableFromNumber.apply(Float.class));

        // edge case: input is not assignable from the expected class
        assertFalse(assignableFromNumber.apply(String.class));
        assertFalse(assignableFromNumber.apply(Boolean.class));
        assertFalse(assignableFromNumber.apply(Character.class));

        // error scenario: null input
        assertNull(assignableFromNumber.apply(null));

        // error scenario: input of wrong type
        try {
            assignableFromNumber.apply("test");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIn()
    {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        Predicate<Integer> inList = Predicates.in(list);

        // typical use case: input is in the list
        assertTrue(inList.apply(1));
        assertTrue(inList.apply(2));
        assertTrue(inList.apply(3));
        assertTrue(inList.apply(4));
        assertTrue(inList.apply(5));

        // edge case: input is not in the list
        assertFalse(inList.apply(0));

        // error scenario: null input
        assertNull(inList.apply(null));

        // error scenario: input of wrong type
        try {
            inList.apply("test");
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // expected
        }
    }

    @Test
    public void testCompose()
    {
        Predicate<Integer> isEven = new Predicate<Integer>()
        {
            @Override
            public boolean apply(Integer input)
            {
                return input % 2 == 0;
            }
        };

        Function<Integer, Integer> square = new Function<Integer, Integer>()
        {
            @Override
            public Integer apply(Integer input)
            {
                return input * input;
            }
        };

        Predicate<Integer> composed = Predicates.compose(isEven, square);

        // typical use case: both input and predicate are valid
        assertTrue(composed.apply(4));
        assertTrue(composed.apply(16));

        // edge case: input is invalid
        assertFalse(composed.apply(2));
        assertFalse(composed.apply(3));

        // error scenario: predicate is invalid
        try {
            composed.apply(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testContainsPattern()
    {
        Predicate<CharSequence> containsPattern = Predicates.containsPattern(Pattern.compile("a.*b"));

        // typical use case: input satisfies the pattern
        assertTrue(containsPattern.apply("test string"));
        assertTrue(containsPattern.apply("test a string b"));
        assertTrue(containsPattern.apply("test aabbbbb"));

        // edge case: input does not satisfy the pattern
        assertFalse(containsPattern.apply("test string without a or b"));

        // error scenario: null input
        assertNull(containsPattern.apply(null));

        // error scenario: input of wrong type
        try {
            containsPattern.apply(1);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // expected
        }
    }
}