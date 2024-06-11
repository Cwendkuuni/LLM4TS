package com.google.common.base;

import static org.junit.Assert.*;

import com.google.common.base.Predicates;
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
        assertNull(isNull.apply(null));
        assertFalse(isNull.apply(new Object()));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNull = Predicates.notNull();
        assertNull(notNull.apply(null));
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
        Predicate<Integer> predicate1 = Predicates.alwaysTrue();
        Predicate<Integer> predicate2 = Predicates.isNull();

        List<Predicate<Integer>> components = Arrays.asList(predicate1, predicate2);
        Predicate<Integer> andPredicate = Predicates.and(components.toArray(new Predicate[0]));

        assertFalse(andPredicate.apply(null));
        assertFalse(andPredicate.apply(0));
        assertTrue(andPredicate.apply(null, new Object()));
        assertTrue(andPredicate.apply(0, new Object()));
        assertTrue(andPredicate.apply(1));
    }

    @Test
    public void testOr() {
        Predicate<Integer> predicate1 = Predicates.isNull();
        Predicate<Integer> predicate2 = Predicates.equalTo(1);

        List<Predicate<Integer>> components = Arrays.asList(predicate1, predicate2);
        Predicate<Integer> orPredicate = Predicates.or(components.toArray(new Predicate[0]));

        assertFalse(orPredicate.apply(null));
        assertFalse(orPredicate.apply(0));
        assertFalse(orPredicate.apply(1));
        assertTrue(orPredicate.apply(null, new Object()));
        assertTrue(orPredicate.apply(1, new Object()));
    }

    @Test
    public void testEqualTo() {
        Object target = new Object();
        Predicate<Object> equalToPredicate = Predicates.equalTo(target);

        assertFalse(equalToPredicate.apply(null));
        assertFalse(equalToPredicate.apply(new Object()));
        assertTrue(equalToPredicate.apply(target));
    }

    @Test
    public void testAssignableFrom() {
        Class<String> clazz1 = String.class;
        Class<Integer> clazz2 = Integer.class;

        Predicate<Class<?>> assignableFromPredicate = Predicates.assignableFrom(clazz1);
        assertFalse(assignableFromPredicate.apply(clazz2));
        assertTrue(assignableFromPredicate.apply(String.class));
    }

    @Test
    public void testInstanceOf() {
        Class<String> clazz = String.class;
        Predicate<Object> instanceOfPredicate = Predicates.instanceOf(clazz);

        assertFalse(instanceOfPredicate.apply(new Object()));
        assertTrue(instanceOfPredicate.apply(""));
    }

    @Test
    public void testIn() {
        Collection<String> collection = Arrays.asList("foo", "bar", "baz");
        Predicate<String> inPredicate = Predicates.in(collection);

        assertFalse(inPredicate.apply(null));
        assertFalse(inPredicate.apply("qux"));
        assertTrue(inPredicate.apply("foo"));
    }

    @Test
    public void testCompose() {
        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };
        Predicate<String> predicate = Predicates.notNull();
        Predicate<Integer> composedPredicate = Predicates.compose(Predicates.alwaysFalse(), function);

        assertFalse(composedPredicate.apply(null));
        assertFalse(composedPredicate.apply(""));
        assertFalse(composedPredicate.apply("foo"));
    }

    @Test
    public void testContainsPattern() {
        String pattern = "[abc]";
        Predicate<String> containsPatternPredicate = Predicates.containsPattern(Pattern.compile(pattern));

        assertFalse(containsPatternPredicate.apply(null));
        assertFalse(containsPatternPredicate.apply(""));
        assertTrue(containsPatternPredicate.apply("abc"));
        assertTrue(containsPatternPredicate.apply("defabc"));
    }
}