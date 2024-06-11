package com.example.test.predicates;

import static org.junit.Assert.*;

import com.google.common.base.Predicates;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PredicatesTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testAlwaysTrue() {
        Predicate<Object> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(new Object()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAlwaysFalse() {
        Predicate<Object> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(new Object()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testIsNull() {
        Predicate<Object> isNull = Predicates.isNull();
        assertNull(isNull.apply(null));
        assertNull(isNull.apply(new Object()));
        assertFalse(isNull.apply(new String("test")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNotNull() {
        Predicate<Object> notNull = Predicates.notNull();
        assertNull(notNull.apply(null));
        assertNotNull(notNull.apply(new Object()));
        assertNotNull(notNull.apply(new String("test")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNot() {
        Predicate<Boolean> truePredicate = Predicates.alwaysTrue();
        Predicate<Boolean> falsePredicate = Predicates.alwaysFalse();

        Predicate<Boolean> notTrue = Predicates.not(truePredicate);
        assertFalse(notTrue.apply(true));
        assertTrue(notTrue.apply(false));

        Predicate<Boolean> notFalse = Predicates.not(falsePredicate);
        assertTrue(notFalse.apply(true));
        assertFalse(notFalse.apply(false));
    }

    @SuppressWarnings("unchecked")
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

        Predicate<Integer> and = Predicates.and(isEven, isPositive);

        List<Predicate<Integer>> predicates = Arrays.asList(isEven, isPositive);
        Predicate<Integer> andWithList = Predicates.and(predicates.toArray(new Predicate[0]));

        List<Integer> testValues = Arrays.asList(1, 2, 3, 4, 5, 6);
        for (int value : testValues) {
            if (value % 2 == 0 && value > 0) {
                assertTrue(and.apply(value));
                assertTrue(andWithList.apply(value));
            } else {
                assertFalse(and.apply(value));
                assertFalse(andWithList.apply(value));
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testOr() {
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

        Predicate<Integer> or = Predicates.or(isEven, isPositive);

        List<Predicate<Integer>> predicates = Arrays.asList(isEven, isPositive);
        Predicate<Integer> orWithList = Predicates.or(predicates.toArray(new Predicate[0]));

        List<Integer> testValues = Arrays.asList(1, 2, 3, 4, 5, 6);
        for (int value : testValues) {
            if (value % 2 == 0 || value > 0) {
                assertTrue(or.apply(value));
                assertTrue(orWithList.apply(value));
            } else {
                assertFalse(or.apply(value));
                assertFalse(orWithList.apply(value));
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testInstanceOf() {
        Class<String> stringClass = String.class;
        Predicate<Object> instanceOfString = Predicates.instanceOf(stringClass);

        assertTrue(instanceOfString.apply("test"));
        assertFalse(instanceOfString.apply(new Object()));
        assertFalse(instanceOfString.apply(1));
    }

    @Test
    public void testAssignableFrom() {
        Class<Number> numberClass = Number.class;
        Class<Integer> integerClass = Integer.class;

        Predicate<Class<?>> assignableFromNumber = Predicates.assignableFrom(numberClass);
        Predicate<Class<?>> assignableFromInteger = Predicates.assignableFrom(integerClass);

        assertTrue(assignableFromNumber.apply(Number.class));
        assertTrue(assignableFromNumber.apply(Integer.class));
        assertTrue(assignableFromNumber.apply(Number[].class));
        assertTrue(assignableFromNumber.apply(Integer[].class));

        assertTrue(assignableFromInteger.apply(Integer.class));
        assertTrue(assignableFromInteger.apply(Number.class));
        assertFalse(assignableFromInteger.apply(String.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testIn() {
        List<String> testList = Arrays.asList("apple", "banana", "cherry");
        Predicate<String> inTestList = Predicates.in(testList);

        List<String> otherTestList = Arrays.asList("orange", "grape", "pear");
        Predicate<String> inOtherTestList = Predicates.in(otherTestList);

        assertTrue(inTestList.apply("apple"));
        assertTrue(inTestList.apply("banana"));
        assertFalse(inTestList.apply("cherry"));
        assertFalse(inTestList.apply("orange"));
        assertFalse(inTestList.apply("grape"));
        assertFalse(inTestList.apply("pear"));

        assertTrue(inOtherTestList.apply("orange"));
        assertFalse(inOtherTestList.apply("apple"));
        assertFalse(inOtherTestList.apply("banana"));
        assertFalse(inOtherTestList.apply("cherry"));
        assertFalse(inOtherTestList.apply("pear"));
    }

    @Test
    public void testEmptyCollection() {
        Predicate<Collection<?>> isEmpty = Predicates.isNull();
        Predicate<Collection<?>> notEmpty = Predicates.not(Predicates.isNull());

        List<Predicate<Collection<?>>> emptyPredicates = Arrays.asList(isNull(), isEmpty);
        Predicate<Collection<?>> andEmpty = Predicates.and(emptyPredicates.toArray(new Predicate[0]));

        List<Predicate<Collection<?>>> notEmptyPredicates = Arrays.asList(notNull(), notEmpty);
        Predicate<Collection<?>> orEmptyAndNotEmpty = Predicates.or(notEmptyPredicates.toArray(new Predicate[0]));

        List<Collection<?>> testCollections = Arrays.asList(Collections.emptyList(), Collections.emptyList(), null);
        for (Collection<?> collection : testCollections) {
            if (collection == null) {
                assertTrue(andEmpty.apply(collection));
                assertTrue(orEmptyAndNotEmpty.apply(collection));
            } else {
                assertTrue(collection.isEmpty());
                assertFalse(orEmptyAndNotEmpty.apply(collection));
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputForCompositionPredicate() {
        Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input + 1;
            }
        };

        Predicate<Boolean> isPositive = new Predicate<Boolean>() {
            @Override
            public boolean apply(Boolean input) {
                return input > 0;
            }
        };

        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> compositionPredicate = Predicates.compose(isPositive, addOne);

        Integer nullInput = null;
        compositionPredicate.apply(nullInput);
    }

    @Test(expected = ClassCastException.class)
    public void testCompositionPredicateWithNonNumericInput() {
        Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input + 1;
            }
        };

        Predicate<Boolean> isPositive = new Predicate<Boolean>() {
            @Override
            public boolean apply(Boolean input) {
                return input > 0;
            }
        };

        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        Predicate<Integer> compositionPredicate = Predicates.compose(isPositive, addOne);

        String nonNumericInput = "test";
        compositionPredicate.apply(nonNumericInput);
    }
}