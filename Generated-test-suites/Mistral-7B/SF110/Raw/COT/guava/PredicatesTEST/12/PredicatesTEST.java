package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(new Object()));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Object> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(new Object()));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> isNull = Predicates.isNull();
        assertFalse(isNull.apply(null));
        assertTrue(isNull.apply(new Object()[0]));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNull = Predicates.notNull();
        assertFalse(notNull.apply(null));
        assertTrue(notNull.apply(new Object()));
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
        Predicate<Object> predicate1 = Predicates.isNull();
        Predicate<Object> predicate2 = Predicates.notNull();

        Predicate<Object> andPredicate = Predicates.and(predicate1, predicate2);

        assertFalse(andPredicate.apply(null));
        assertFalse(andPredicate.apply(new Object()));

        Object obj = new Object();
        assertFalse(andPredicate.apply(obj));
        assertFalse(andPredicate.apply(null));

        assertTrue(andPredicate.apply(obj));

        // Edge case: Empty list of predicates
        List<Predicate<Object>> emptyList = new ArrayList<>();
        Predicate<Object> emptyAndPredicate = Predicates.and(emptyList.toArray(new Predicate[0]));
        assertFalse(emptyAndPredicate.apply(null));
        assertFalse(emptyAndPredicate.apply(new Object()));
    }

    @Test
    public void testOr() {
        Predicate<Object> predicate1 = Predicates.isNull();
        Predicate<Object> predicate2 = Predicates.notNull();

        Predicate<Object> orPredicate = Predicates.or(predicate1, predicate2);

        assertTrue(orPredicate.apply(null));
        assertTrue(orPredicate.apply(new Object()));

        Object obj = new Object();
        assertTrue(orPredicate.apply(null));
        assertTrue(orPredicate.apply(obj));

        // Edge case: Empty list of predicates
        List<Predicate<Object>> emptyList = new ArrayList<>();
        Predicate<Object> emptyOrPredicate = Predicates.or(emptyList.toArray(new Predicate[0]));
        assertTrue(emptyOrPredicate.apply(null));
        assertTrue(emptyOrPredicate.apply(new Object()));
    }

    @Test(expected = NullPointerException.class)
    public void testAndWithNullPredicates() {
        Predicate<Object> nullPredicate1 = null;
        Predicate<Object> predicate2 = Predicates.notNull();

        Predicate<Object> andPredicate = Predicates.and(nullPredicate1, predicate2);
        andPredicate.apply(new Object());
    }

    @Test(expected = NullPointerException.class)
    public void testOrWithNullPredicates() {
        Predicate<Object> predicate1 = Predicates.isNull();
        Predicate<Object> nullPredicate2 = null;

        Predicate<Object> orPredicate = Predicates.or(predicate1, nullPredicate2);
        orPredicate.apply(new Object());
    }

    @Test
    public void testEqualTo() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Predicate<Object> eqPredicate = Predicates.equalTo(obj1);
        assertFalse(eqPredicate.apply(obj2));
        assertTrue(eqPredicate.apply(obj1));
    }

    @Test
    public void testInstanceOf() {
        Class<String> stringClass = String.class;
        Predicate<Object> instanceOfPredicate = Predicates.instanceOf(stringClass);
        assertTrue(instanceOfPredicate.apply(""));
        assertFalse(instanceOfPredicate.apply(new Object()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInstanceOfWithInvalidClass() {
        Class<String> stringClass = String.class;
        Predicate<Object> instanceOfPredicate = Predicates.instanceOf(new Object() {}.getClass());
        instanceOfPredicate.apply(new Object());
    }

    @Test
    public void testAssignableFrom() {
        Class<String> stringClass = String.class;
        Class<Object> objectClass = Object.class;
        Predicate<Class<?>> assignableFromPredicate = Predicates.assignableFrom(objectClass);
        assertTrue(assignableFromPredicate.apply(stringClass));
        assertFalse(assignableFromPredicate.apply(new Object() {}.getClass()));
    }

    @Test
    public void testIn() {
        Collection<Object> collection = new ArrayList<>();
        collection.add(new Object());
        Predicate<Object> inPredicate = Predicates.in(collection);
        assertTrue(inPredicate.apply(new Object()));
        assertFalse(inPredicate.apply(null));
        assertFalse(inPredicate.apply(new Object()[0]));
    }

    @Test(expected = NullPointerException.class)
    public void testInWithNullCollection() {
        Predicate<Object> inPredicate = Predicates.in(null);
        inPredicate.apply(new Object());
    }

    @Test(expected = ClassCastException.class)
    public void testInWithInvalidObject() {
        Collection<String> collection = new ArrayList<>();
        collection.add("test");
        Predicate<Object> inPredicate = Predicates.in(collection);
        inPredicate.apply(new Object());
    }

    @Test
    public void testCompose() {
        Predicate<Integer> evenPredicate = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer integer) {
                return integer % 2 == 0;
            }
        };
        Function<Integer, Integer> addOneFunction = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer + 1;
            }
        };
        Predicate<Integer> composedPredicate = Predicates.compose(evenPredicate, addOneFunction);
        assertTrue(composedPredicate.apply(2));
        assertFalse(composedPredicate.apply(1));
    }

    @Test
    public void testContainsPattern() {
        String pattern = "test";
        Predicate<CharSequence> containsPatternPredicate = Predicates.containsPattern(Pattern.compile(pattern));
        String testString1 = "testTest";
        String testString2 = "testPattern";
        assertTrue(containsPatternPredicate.apply(testString1));
        assertTrue(containsPatternPredicate.apply(testString2));
        assertFalse(containsPatternPredicate.apply("testNoMatch"));
    }

    @Test
    public void testContainsPatternFromString() {
        String pattern = "test";
        Predicate<CharSequence> containsPatternPredicate = Predicates.containsPatternFromString(pattern);
        String testString1 = "testTest";
        String testString2 = "testPattern";
        assertTrue(containsPatternPredicate.apply(testString1));
        assertTrue(containsPatternPredicate.apply(testString2));
        assertFalse(containsPatternPredicate.apply("testNoMatch"));
    }
}