package com.google.common.base;

import static org.junit.Assert.*;
import java.util.*;
import static com.google.common.base.Predicates.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        assertTrue(alwaysTrue().apply(null));
        assertTrue(alwaysTrue().apply(0));
        assertTrue(alwaysTrue().apply("abc"));
    }

    @Test
    public void testAlwaysFalse() {
        assertFalse(alwaysFalse().apply(null));
        assertFalse(alwaysFalse().apply(0));
        assertFalse(alwaysFalse().apply("abc"));
    }

    @Test
    public void testIsNull() {
        assertNull(isNull().apply(null));
        assertNotNull(isNull().apply(new Object()));
        assertFalse(isNull().apply("abc"));
    }

    @Test
    public void testNotNull() {
        assertNotNull(notNull().apply(null));
        assertNull(notNull().apply(new Object()));
        assertFalse(notNull().apply("abc"));
    }

    @Test
    public void testNot() {
        Predicate<Boolean> p = isTrue();
        Predicate<Boolean> q = not(p);

        assertFalse(q.apply(true));
        assertTrue(q.apply(false));

        p = isFalse();
        q = not(p);

        assertTrue(q.apply(false));
        assertFalse(q.apply(true));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> p1 = isEqual(Integer.valueOf(1));
        Predicate<Integer> p2 = isEqual(Integer.valueOf(2));
        Predicate<Integer> expected = and(p1, p2);

        assertFalse(expected.apply(0));
        assertFalse(expected.apply(1));
        assertFalse(expected.apply(2));
        assertFalse(expected.apply(3));
        assertFalse(expected.apply(null));

        assertTrue(expected.apply(1));
        assertFalse(expected.apply(2));

        List<Predicate<Integer>> list = Arrays.asList(p1, p2);
        Predicate<Integer> actual = Predicates.and(list);

        assertEquals(expected, actual);
    }

    @Test
    public void testOr() {
        Predicate<Integer> p1 = isEqual(Integer.valueOf(1));
        Predicate<Integer> p2 = isEqual(Integer.valueOf(2));
        Predicate<Integer> expected = or(p1, p2);

        assertFalse(expected.apply(0));
        assertFalse(expected.apply(1));
        assertFalse(expected.apply(2));
        assertFalse(expected.apply(null));

        assertTrue(expected.apply(1));
        assertTrue(expected.apply(2));

        List<Predicate<Integer>> list = Arrays.asList(p1, p2);
        Predicate<Integer> actual = Predicates.or(list);

        assertEquals(expected, actual);
    }

    @Test
    public void testEqualTo() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Predicate<Object> p = equalTo(obj1);

        assertFalse(p.apply(obj2));
        assertFalse(p.apply(null));
        assertTrue(p.apply(obj1));
    }

    @Test
    public void testInstanceOf() {
        Predicate<String> p = instanceOf(String.class);

        assertFalse(p.apply(new Integer(1)));
        assertTrue(p.apply("abc"));
        assertFalse(p.apply(null));
    }

    @Test
    public void testAssignableFrom() {
        Class<String> stringClass = String.class;
        Class<Number> numberClass = Number.class;

        Predicate<Class<?>> p = assignableFrom(numberClass);

        assertFalse(p.apply(stringClass));
        assertTrue(p.apply(Number.class));
        assertTrue(p.apply(Object.class));
    }

    @Test
    public void testIn() {
        Collection<String> collection = new ArrayList<>();
        collection.add("abc");
        collection.add("def");

        Predicate<String> p = in(collection);

        assertFalse(p.apply("ghi"));
        assertFalse(p.apply(null));
        assertTrue(p.apply("abc"));
        assertTrue(p.apply("def"));
    }

    @Test
    public void testCompose() {
        Function<String, Integer> func = new Function<String, Integer>() {
            @Override
            public Integer apply(String s) {
                return s.length();
            }
        };
        Predicate<String> p1 = isEmpty();
        Predicate<Integer> p2 = isGreaterThan(0);

        Predicate<String> expected = compose(p1, func);
        Predicate<String> actual = Predicates.compose(p1, func);

        assertEquals(expected, actual);

        assertFalse(expected.apply("abc"));
        assertFalse(expected.apply(null));
        assertTrue(expected.apply(""));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> p = containsPattern("ab.*c");

        assertFalse(p.apply("abcde"));
        assertTrue(p.apply("abxyzc"));
        assertFalse(p.apply("12345"));
        assertFalse(p.apply(null));
    }
}