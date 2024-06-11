package com.google.common.base;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> predicate = Predicates.alwaysTrue();
        assertTrue(predicate.apply(null));
        assertTrue(predicate.apply(1));
        assertTrue(predicate.apply(3.14));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> predicate = Predicates.alwaysFalse();
        assertFalse(predicate.apply(null));
        assertFalse(predicate.apply(1));
        assertFalse(predicate.apply(3.14));
    }

    @Test
    public void testIsNull() {
        Predicate<Integer> predicate = Predicates.isNull();
        assertFalse(predicate.apply(1));
        assertTrue(predicate.apply(null));
    }

    @Test
    public void testNotNull() {
        Predicate<Integer> predicate = Predicates.notNull();
        assertFalse(predicate.apply(null));
        assertTrue(predicate.apply(1));
    }

    @Test
    public void testNot() {
        Predicate<Integer> predicate1 = Predicates.isNull();
        Predicate<Integer> predicate2 = Predicates.alwaysTrue();
        Predicate<Integer> predicate3 = Predicates.not(predicate1);
        Predicate<Integer> predicate4 = Predicates.not(predicate2);

        assertFalse(predicate3.apply(null));
        assertTrue(predicate3.apply(1));

        assertTrue(predicate4.apply(null));
        assertTrue(predicate4.apply(1));

        assertFalse(predicate3.apply(1));
        assertTrue(predicate4.apply(1));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> predicate1 = Predicates.isNull();
        Predicate<Integer> predicate2 = Predicates.alwaysTrue();

        Predicate<Integer> predicate3 = Predicates.and(predicate1, predicate2);

        assertFalse(predicate3.apply(null));
        assertTrue(predicate3.apply(1));

        assertFalse(predicate3.apply(null));
        assertTrue(predicate3.apply(1));

        List<Predicate<Integer>> list = Arrays.asList(predicate1, predicate2);
        Predicate<Integer> predicate4 = Predicates.and(list.toArray(new Predicate[0]));

        assertFalse(predicate4.apply(null));
        assertTrue(predicate4.apply(1));

        List<Predicate<Integer>> list2 = Arrays.asList(predicate1, predicate2, Predicates.isNull());
        Predicate<Integer> predicate5 = Predicates.and(list2.toArray(new Predicate[0]));

        assertFalse(predicate5.apply(null));
        assertFalse(predicate5.apply(1));
    }

    @Test
    public void testOr() {
        Predicate<Integer> predicate1 = Predicates.isNull();
        Predicate<Integer> predicate2 = Predicates.alwaysTrue();

        Predicate<Integer> predicate3 = Predicates.or(predicate1, predicate2);

        assertFalse(predicate3.apply(null));
        assertTrue(predicate3.apply(1));

        assertTrue(predicate3.apply(null));
        assertTrue(predicate3.apply(1));

        List<Predicate<Integer>> list = Arrays.asList(predicate1, predicate2, Predicates.isNull());
        Predicate<Integer> predicate4 = Predicates.or(list.toArray(new Predicate[0]));

        assertFalse(predicate4.apply(null));
        assertTrue(predicate4.apply(1));

        assertTrue(predicate4.apply(null));
        assertTrue(predicate4.apply(1));
    }

    @Test
    public void testEqualTo() {
        Predicate<Integer> predicate1 = Predicates.equalTo(1);
        Predicate<Integer> predicate2 = Predicates.equalTo(2);

        assertFalse(predicate1.apply(null));
        assertFalse(predicate1.apply(1));
        assertTrue(predicate1.apply(1));

        assertFalse(predicate2.apply(null));
        assertFalse(predicate2.apply(1));
        assertFalse(predicate2.apply(2));
        assertTrue(predicate2.apply(2));

        Predicate<Integer> predicate3 = Predicates.equalTo(null);
        assertFalse(predicate3.apply(null));
        assertTrue(predicate3.apply(null));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> predicate1 = Predicates.instanceOf(String.class);
        Predicate<Object> predicate2 = Predicates.instanceOf(Integer.class);

        assertFalse(predicate1.apply(null));
        assertFalse(predicate1.apply(new String("test")));
        assertFalse(predicate1.apply(1));
        assertFalse(predicate1.apply(new Integer(1)));

        assertTrue(predicate1.apply(new String("test")));
        assertFalse(predicate2.apply(null));
        assertFalse(predicate2.apply(new String("test")));
        assertFalse(predicate2.apply(1));
        assertTrue(predicate2.apply(new Integer(1)));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> predicate1 = Predicates.assignableFrom(String.class);
        Predicate<Class<?>> predicate2 = Predicates.assignableFrom(Integer.class);

        assertFalse(predicate1.apply(null));
        assertTrue(predicate1.apply(String.class));
        assertFalse(predicate1.apply(Integer.class));

        assertTrue(predicate2.apply(null));
        assertFalse(predicate2.apply(String.class));
        assertFalse(predicate2.apply(Integer.class));
        assertTrue(predicate2.apply(Number.class));
    }

    @Test
    public void testIn() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Predicate<Integer> predicate = Predicates.in(list);

        assertFalse(predicate.apply(null));
        assertFalse(predicate.apply(0));
        assertFalse(predicate.apply(4));
        assertTrue(predicate.apply(1));
        assertTrue(predicate.apply(2));
        assertTrue(predicate.apply(3));
    }

    @Test
    public void testCompose() {
        Predicate<Integer> predicate1 = p -> p % 2 == 0;
        Function<Integer, Integer> function = i -> i * 2;

        Predicate<Integer> predicate2 = Predicates.compose(predicate1, function);

        assertFalse(predicate2.apply(1));
        assertFalse(predicate2.apply(3));
        assertTrue(predicate2.apply(4));
        assertTrue(predicate2.apply(6));
    }

    @Test
    public void testContainsPattern() {
        Pattern pattern = Pattern.compile("test");
        Predicate<String> predicate = Predicates.containsPattern(pattern);

        assertFalse(predicate.apply(null));
        assertFalse(predicate.apply(""));
        assertFalse(predicate.apply("hello"));
        assertTrue(predicate.apply("test"));
        assertTrue(predicate.apply("this is a test"));
    }
}