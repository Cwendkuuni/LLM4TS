package com.google.common.base;

import static com.google.common.base.Predicates.alwaysFalse;
import static com.google.common.base.Predicates.alwaysTrue;
import static com.google.common.base.Predicates.assignableFrom;
import static com.google.common.base.Predicates.contains;
import static com.google.common.base.Predicates.containsPattern;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.isNull;
import static com.google.common.base.Predicates.not;
import static com.google.common.base.Predicates.notNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

public class PredicatesTest {

    // Test alwaysTrue and alwaysFalse
    @Test
    public void testAlwaysTrueAndAlwaysFalse() {
        assertTrue(alwaysTrue().apply(null));
        assertTrue(alwaysTrue().apply(new Object()));
        assertFalse(alwaysFalse().apply(null));
        assertFalse(alwaysFalse().apply(new Object()));
    }

    // Test isNull and notNull
    @Test
    public void testIsNullAndNotNull() {
        assertNull(isNull().apply(null));
        assertNotNull(notNull().apply(new Object()));
        assertNull(isNull().apply(new Object()));
        assertNotNull(notNull().apply(null));
    }

    // Test not
    @Test
    public void testNot() {
        Predicate<Boolean> predicate = alwaysFalse();
        Predicate<Boolean> notPredicate = Predicates.not(predicate);
        assertTrue(notPredicate.apply(true));
        assertFalse(notPredicate.apply(false));
    }

    // Test and
    @Test
    public void testAnd() {
        Predicate<Integer> predicate1 = isNull();
        Predicate<Integer> predicate2 = equalTo(1);
        Predicate<Integer> andPredicate = Predicates.and(predicate1, predicate2);

        assertFalse(andPredicate.apply(null));
        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(2));

        assertNull(andPredicate.apply(null));
        assertTrue(andPredicate.apply(1));

        // Test and with more than two predicates
        List<Predicate<Integer>> predicates = new ArrayList<>();
        predicates.add(isNull());
        predicates.add(instanceOf(Integer.class));
        predicates.add(equalTo(1));
        Predicate<Integer> andPredicate3 = Predicates.and(predicates.toArray(new Predicate[predicates.size()][]));

        assertNull(andPredicate3.apply(null));
        assertFalse(andPredicate3.apply(new Object()));
        assertFalse(andPredicate3.apply(0));
        assertFalse(andPredicate3.apply(2));
        assertFalse(andPredicate3.apply(new String("Hello World")));

        assertTrue(andPredicate3.apply(1));
    }

    // Test or
    @Test
    public void testOr() {
        Predicate<Integer> predicate1 = isNull();
        Predicate<Integer> predicate2 = instanceOf(Integer.class);
        Predicate<Integer> predicate3 = equalTo(1);
        Predicate<Integer> orPredicate = Predicates.or(predicate1, predicate2, predicate3);

        assertNull(orPredicate.apply(null));
        assertFalse(orPredicate.apply(new Object()));
        assertFalse(orPredicate.apply(0));
        assertFalse(orPredicate.apply("Hello World"));

        assertNull(orPredicate.apply(null));
        assertFalse(orPredicate.apply(new Object()));
        assertTrue(orPredicate.apply(0));
        assertTrue(orPredicate.apply(1));
        assertTrue(orPredicate.apply(new Integer(1)));
    }

    // Test equalTo
    @Test
    public void testEqualTo() {
        Predicate<String> stringPredicate = Predicates.equalTo("Hello World");
        Predicate<Integer> integerPredicate = Predicates.equalTo(1);

        assertFalse(stringPredicate.apply(null));
        assertFalse(stringPredicate.apply(""));
        assertFalse(stringPredicate.apply("Goodbye World"));
        assertFalse(stringPredicate.apply("Hello World"));
        assertTrue(stringPredicate.apply("Hello World"));

        assertFalse(integerPredicate.apply(null));
        assertFalse(integerPredicate.apply(-1));
        assertFalse(integerPredicate.apply(0));
        assertFalse(integerPredicate.apply(1));
        assertTrue(integerPredicate.apply(1));
    }

    // Test instanceOf
    @Test
    public void testInstanceOf() {
        Predicate<String> stringPredicate = Predicates.instanceOf(String.class);
        Predicate<Integer> integerPredicate = Predicates.instanceOf(Integer.class);

        assertFalse(stringPredicate.apply(null));
        assertFalse(stringPredicate.apply(new Object()));
        assertFalse(stringPredicate.apply(1));
        assertFalse(stringPredicate.apply("Hello World"));
        assertTrue(stringPredicate.apply("Hello World"));

        assertFalse(integerPredicate.apply(null));
        assertFalse(integerPredicate.apply(new Object()));
        assertFalse(integerPredicate.apply("Hello World"));
        assertFalse(integerPredicate.apply("1"));
        assertTrue(integerPredicate.apply(1));
    }

    // Test assignableFrom
    @Test
    public void testAssignableFrom() {
        Predicate<Class<String>> stringPredicate = Predicates.assignableFrom(String.class);
        Predicate<Class<Integer>> integerPredicate = Predicates.assignableFrom(Integer.class);

        assertFalse(stringPredicate.apply(null));
        assertFalse(stringPredicate.apply(Object.class));
        assertFalse(stringPredicate.apply(Integer.class));
        assertTrue(stringPredicate.apply(StringBuffer.class));
        assertTrue(stringPredicate.apply(StringBuilder.class));

        assertFalse(integerPredicate.apply(null));
        assertFalse(integerPredicate.apply(Object.class));
        assertFalse(integerPredicate.apply(String.class));
        assertTrue(integerPredicate.apply(Integer.TYPE.getClass()));
        assertTrue(integerPredicate.apply(Integer.class));
    }

    // Test in
    @Test
    public void testIn() {
        List<Integer> numbers = Arrays.asList(1, 2, 3);
        Predicate<Integer> predicate = Predicates.in(numbers);

        assertFalse(predicate.apply(null));
        assertFalse(predicate.apply(-1));
        assertFalse(predicate.apply(0));
        assertFalse(predicate.apply(1));
        assertTrue(predicate.apply(2));
        assertTrue(predicate.apply(3));
    }

    // Test contains
    @Test
    public void testContains() {
        String str = "Hello World";
        Predicate<CharSequence> predicate = Predicates.contains(Pattern.compile("[Wo]"));

        assertFalse(predicate.apply(null));
        assertFalse(predicate.apply(""));
        assertFalse(predicate.apply("Goodbye World"));
        assertTrue(predicate.apply("Hello World"));
        assertTrue(predicate.apply("Hello World! Hello World"));
    }

    // Test containsPattern
    @Test
    public void testContainsPattern() {
        String str = "Hello World";
        Predicate<CharSequence> predicate = Predicates.containsPattern(".*o.*");

        assertFalse(predicate.apply(null));
        assertFalse(predicate.apply(""));
        assertFalse(predicate.apply("Goodbye World"));
        assertTrue(predicate.apply("Hello World"));
        assertTrue(predicate.apply("Hello World! Hello World"));
    }
}