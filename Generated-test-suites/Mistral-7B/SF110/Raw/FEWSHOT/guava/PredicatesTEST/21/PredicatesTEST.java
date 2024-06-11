package com.google.common.base;

import static com.google.common.base.Predicates.alwaysFalse;
import static com.google.common.base.Predicates.alwaysTrue;
import static com.google.common.base.Predicates.assignableFrom;
import static com.google.common.base.Predicates.contains;
import static com.google.common.base.Predicates.containsPattern;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.isNull;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.isNullOrEmpty;
import static com.google.common.base.Predicates.not;
import static com.google.common.base.Predicates.notNull;
import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        assertTrue(alwaysTrue().apply(null));
        assertTrue(alwaysTrue().apply(5));
        assertTrue(alwaysTrue().apply("foo"));
    }

    @Test
    public void testAlwaysFalse() {
        assertFalse(alwaysFalse().apply(null));
        assertFalse(alwaysFalse().apply(5));
        assertFalse(alwaysFalse().apply("foo"));
    }

    @Test
    public void testIsNull() {
        assertTrue(isNull().apply(null));
        assertFalse(isNull().apply(new Object()));
        assertFalse(isNull().apply("foo"));
    }

    @Test
    public void testNotNull() {
        assertFalse(notNull().apply(null));
        assertTrue(notNull().apply(new Object()));
        assertFalse(notNull().apply("foo"));
    }

    @Test
    public void testNot() {
        final Predicate<Boolean> p1 = alwaysTrue();
        final Predicate<Boolean> p2 = alwaysFalse();
        final Predicate<Boolean> p3 = not(p1);
        final Predicate<Boolean> p4 = not(p2);

        assertFalse(p3.apply(true));
        assertTrue(p3.apply(false));
        assertTrue(p4.apply(false));
        assertFalse(p4.apply(true));
    }

    @Test
    public void testAnd() {
        final Predicate<Integer> p1 = isNull();
        final Predicate<Integer> p2 = isNull();
        final Predicate<Integer> p3 = isNull().and(isNull());
        final Predicate<Integer> p4 = isNull().and(notNull());
        final Predicate<Integer> p5 = isNull().and(instanceOf(File.class));

        assertFalse(p1.apply(new Integer(42)));
        assertFalse(p2.apply(new Integer(42)));
        assertFalse(p1.apply(null));
        assertFalse(p2.apply(null));
        assertTrue(p3.apply(null));
        assertFalse(p3.apply(new Integer(42)));
        assertFalse(p4.apply(null));
        assertTrue(p4.apply(new Integer(42)));
        assertFalse(p5.apply(new Integer(42)));
        assertFalse(p5.apply(null));
        assertFalse(p5.apply(new File("foo.txt")));

        List<Predicate<Integer>> list = Arrays.asList(isNull(), isNull());
        Predicate<Integer> and = Predicates.and(list);
        assertTrue(and.apply(null));
        assertFalse(and.apply(new Integer(42)));
    }

    @Test
    public void testOr() {
        final Predicate<Integer> p1 = isNull();
        final Predicate<Integer> p2 = isNull();
        final Predicate<Integer> p3 = isNull().or(isNull());
        final Predicate<Integer> p4 = isNull().or(notNull());
        final Predicate<Integer> p5 = isNull().or(instanceOf(File.class));

        assertFalse(p1.apply(new Integer(42)));
        assertFalse(p2.apply(new Integer(42)));
        assertTrue(p3.apply(null));
        assertTrue(p3.apply(new Integer(42)));
        assertFalse(p4.apply(null));
        assertTrue(p4.apply(new Integer(42)));
        assertFalse(p5.apply(new Integer(42)));
        assertFalse(p5.apply(null));
        assertFalse(p5.apply(new File("foo.txt")));

        List<Predicate<Integer>> list = Arrays.asList(isNull(), notNull());
        Predicate<Integer> or = Predicates.or(list);
        assertFalse(or.apply(null));
        assertTrue(or.apply(new Integer(42)));
    }

    @Test
    public void testEqualTo() {
        final Object obj1 = new Object();
        final Object obj2 = new Object();
        final Object obj3 = obj1;
        final int num1 = 42;
        final int num2 = 42;
        final String str1 = "foo";
        final String str2 = "foo";

        assertTrue(equalTo(obj1).apply(obj1));
        assertFalse(equalTo(obj1).apply(obj2));
        assertTrue(equalTo(obj1).apply(obj3));
        assertTrue(equalTo(num1).apply(num2));
        assertTrue(equalTo(str1).apply(str2));
    }

    @Test
    public void testInstanceOf() {
        assertFalse(instanceOf(Integer.class).apply(new File("foo.txt")));
        assertTrue(instanceOf(File.class).apply(new File("foo.txt")));
        assertFalse(instanceOf(File.class).apply(new Integer(42)));

        assertEquals(File.class, instanceOf(File.class).and(instanceOf(File.class)).apply(new File("foo.txt")).getType());
    }

    @Test
    public void testAssignableFrom() {
        assertFalse(assignableFrom(Integer.class).apply(String.class));
        assertTrue(assignableFrom(String[].class).apply(List.class));
        assertFalse(assignableFrom(String[].class).apply(Collection.class));

        assertEquals(List.class, assignableFrom(List.class).and(assignableFrom(Collection.class)).apply(new ArrayList<>()).getType());
    }

    @Test
    public void testIn() {
        final List<Integer> intList = Arrays.asList(1, 2, 3, 4, 5);
        final Collection<String> strColl = new HashSet<>();
        strColl.add("foo");
        strColl.add("bar");

        assertTrue(in(intList).apply(1));
        assertTrue(in(intList).apply(2));
        assertTrue(in(intList).apply(3));
        assertTrue(in(intList).apply(4));
        assertTrue(in(intList).apply(5));
        assertFalse(in(intList).apply(6));

        assertTrue(in(strColl).apply("foo"));
        assertTrue(in(strColl).apply("bar"));
        assertFalse(in(strColl).apply("baz"));
    }

    @Test
    public void testCompose() {
        final Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };

        final Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input + 1;
            }
        };

        final Predicate<Integer> isOdd = not(isEven);
        final Predicate<Integer> isEvenAndAddOne = Predicates.compose(isEven, addOne);

        assertFalse(isEvenAndAddOne.apply(1));
        assertFalse(isEvenAndAddOne.apply(3));
        assertTrue(isEvenAndAddOne.apply(4));
        assertTrue(isEvenAndAddOne.apply(6));
    }

    @Test
    public void testContainsPattern() {
        final String pattern = "foo";
        final Predicate<String> containsPatternPredicate = Predicates.containsPattern(pattern);
        final String str1 = "foo";
        final String str2 = "barfoo";
        final String str3 = "foobar";

        assertTrue(containsPatternPredicate.apply(str1));
        assertTrue(containsPatternPredicate.apply(str2));
        assertTrue(containsPatternPredicate.apply(str3));

        final Pattern pattern2 = Pattern.compile("foo");
        final Predicate<String> containsPatternPredicate2 = Predicates.contains(pattern2);
        assertTrue(containsPatternPredicate2.apply(str1));
        assertTrue(containsPatternPredicate2.apply(str2));
        assertTrue(containsPatternPredicate2.apply(str3));
    }

    @Test
    public void testContainsPatternFromString() {
        final String pattern = "foo";
        final Predicate<String> containsPatternPredicate = Predicates.containsPattern(pattern);
        final String str1 = "foo";
        final String str2 = "barfoo";
        final String str3 = "foobar";

        assertTrue(containsPatternPredicate.apply(str1));
        assertTrue(containsPatternPredicate.apply(str2));
        assertTrue(containsPatternPredicate.apply(str3));

        final Pattern pattern2 = Pattern.compile(pattern);
        final Predicate<String> containsPatternPredicate2 = Predicates.containsPattern(pattern2);
        assertTrue(containsPatternPredicate2.apply(str1));
        assertTrue(containsPatternPredicate2.apply(str2));
        assertTrue(containsPatternPredicate2.apply(str3));

        final Predicate<String> containsPatternFromStringPredicate = Predicates.containsPatternFromString(pattern);
        assertTrue(containsPatternFromStringPredicate.apply(str1));
        assertTrue(containsPatternFromStringPredicate.apply(str2));
        assertTrue(containsPatternFromStringPredicate.apply(str3));
    }

    @Test
    public void testIsEmpty() {
        final Collection<String> emptyColl = Collections.emptyList();
        final String emptyStr = "";

        assertTrue(isNullOrEmpty(emptyColl));
        assertTrue(isNullOrEmpty(emptyStr));

        final List<String> strList = Arrays.asList("foo", "bar", "baz");
        assertFalse(isNullOrEmpty(strList));

        final String nonEmptyStr = "foo";
        assertFalse(isNullOrEmpty(nonEmptyStr));
    }
}