package com.google.common.base;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(null));
        assertTrue(alwaysTrue.apply(new Integer(1)));
        assertTrue(alwaysTrue.apply("test"));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Object> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply(null));
        assertFalse(alwaysFalse.apply(new Integer(1)));
        assertFalse(alwaysFalse.apply("test"));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> isNull = Predicates.isNull();
        assertNull(isNull.apply(null));
        assertNull(isNull.apply(new Integer(0)));
        assertNull(isNull.apply(""));
        assertFalse(isNull.apply(new Integer(1)));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNull = Predicates.notNull();
        assertNull(notNull.apply(null));
        assertTrue(notNull.apply(new Integer(1)));
        assertTrue(notNull.apply(""));
        assertTrue(notNull.apply("test"));
    }

    @Test
    public void testNot() {
        Predicate<Boolean> predicate = Predicates.alwaysTrue();
        Predicate<Boolean> notPredicate = Predicates.not(predicate);
        assertFalse(notPredicate.apply(true));
        assertTrue(notPredicate.apply(false));

        Predicate<Integer> predicate2 = Predicates.isNull();
        Predicate<Integer> notPredicate2 = Predicates.not(predicate2);
        assertFalse(notPredicate2.apply(null));
        assertTrue(notPredicate2.apply(new Integer(1)));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isPositive = Predicates.not(Predicates.isNull());

        Predicate<Integer> andPredicate = Predicates.and(isEven, isPositive);
        List<Predicate<Integer>> predicatesList = Arrays.asList(isEven, isPositive);
        Predicate<Integer> andPredicateList = Predicates.and(predicatesList);

        Predicate<Integer> andPredicateFirst = Predicates.and(isEven, isPositive, isPositive);

        assertTrue(andPredicate.apply(2));
        assertTrue(andPredicate.apply(4));
        assertTrue(andPredicate.apply(6));
        assertFalse(andPredicate.apply(-1));
        assertFalse(andPredicate.apply(1));
        assertFalse(andPredicate.apply(null));

        assertTrue(andPredicateList.apply(2));
        assertTrue(andPredicateList.apply(4));
        assertTrue(andPredicateList.apply(6));
        assertFalse(andPredicateList.apply(-1));
        assertFalse(andPredicateList.apply(1));
        assertFalse(andPredicateList.apply(null));

        assertTrue(andPredicateFirst.apply(2));
        assertTrue(andPredicateFirst.apply(4));
        assertTrue(andPredicateFirst.apply(6));
        assertFalse(andPredicateFirst.apply(-1));
        assertFalse(andPredicateFirst.apply(1));
        assertFalse(andPredicateFirst.apply(null));
    }

    @Test
    public void testOr() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> isPositive = Predicates.not(Predicates.isNull());
        Predicate<Integer> isNegative = Predicates.not(isPositive);

        Predicate<Integer> orPredicate = Predicates.or(isEven, isPositive, isNegative);
        List<Predicate<Integer>> predicatesList = Arrays.asList(isEven, isPositive, isNegative);
        Predicate<Integer> orPredicateList = Predicates.or(predicatesList);

        Predicate<Integer> orPredicateFirst = Predicates.or(isEven, isPositive, isNegative, isNegative);

        assertTrue(orPredicate.apply(0));
        assertTrue(orPredicate.apply(1));
        assertTrue(orPredicate.apply(2));
        assertTrue(orPredicate.apply(3));
        assertTrue(orPredicate.apply(4));
        assertTrue(orPredicate.apply(-1));

        assertTrue(orPredicateList.apply(0));
        assertTrue(orPredicateList.apply(1));
        assertTrue(orPredicateList.apply(2));
        assertTrue(orPredicateList.apply(3));
        assertTrue(orPredicateList.apply(4));
        assertTrue(orPredicateList.apply(-1));

        assertTrue(orPredicateFirst.apply(0));
        assertTrue(orPredicateFirst.apply(1));
        assertTrue(orPredicateFirst.apply(2));
        assertTrue(orPredicateFirst.apply(3));
        assertTrue(orPredicateFirst.apply(4));
        assertTrue(orPredicateFirst.apply(-1));
    }

    @Test
    public void testEqualTo() {
        Predicate<Integer> equalToOne = Predicates.equalTo(1);
        Predicate<Integer> equalToTwo = Predicates.equalTo(2);
        Predicate<Integer> equalToNull = Predicates.isNull();

        assertFalse(equalToOne.apply(null));
        assertFalse(equalToOne.apply(0));
        assertTrue(equalToOne.apply(1));
        assertFalse(equalToOne.apply(2));

        assertFalse(equalToTwo.apply(null));
        assertFalse(equalToTwo.apply(1));
        assertFalse(equalToTwo.apply(2));
        assertTrue(equalToTwo.apply(2));

        assertFalse(equalToNull.apply(null));
        assertFalse(equalToNull.apply(1));
        assertFalse(equalToNull.apply(2));
        assertTrue(equalToNull.apply(null));
    }

    @Test
    public void testInstanceOf() {
        Predicate<String> stringInstance = Predicates.instanceOf(String.class);
        Predicate<List<String>> listStringInstance = Predicates.instanceOf(ArrayList.class);
        Predicate<List<String>> listListInstance = Predicates.instanceOf(List.class);

        assertTrue(stringInstance.apply(""));
        assertTrue(stringInstance.apply("test"));
        assertFalse(stringInstance.apply(1));
        assertFalse(stringInstance.apply(new Integer(1)));

        assertTrue(listStringInstance.apply(Arrays.asList("", "test")));
        assertTrue(listStringInstance.apply(new ArrayList<>()));
        assertFalse(listStringInstance.apply(Arrays.asList(1, 2)));
        assertFalse(listStringInstance.apply(new ArrayList<Integer>()));

        assertTrue(listListInstance.apply(Arrays.asList(Arrays.asList("", "test"), Arrays.asList(1, 2))));
        assertTrue(listListInstance.apply(new ArrayList<>()));
        assertTrue(listListInstance.apply(new ArrayList<List<String>>()));
        assertFalse(listListInstance.apply(Arrays.asList(1, 2)));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<List<String>>> listString = List.class;
        Predicate<Class<ArrayList<String>>> arrayList = ArrayList.class;
        Predicate<Class<ArrayList<String>>> listArrayList = ArrayList.class;

        Predicate<Class<?>> assignableFromListString = Predicates.assignableFrom(listString);
        Predicate<Class<?>> assignableFromArrayList = Predicates.assignableFrom(arrayList);
        Predicate<Class<?>> assignableFromListArrayList = Predicates.assignableFrom(listArrayList);

        assertTrue(assignableFromListString.apply(listString));
        assertTrue(assignableFromListString.apply(ArrayList.class));
        assertFalse(assignableFromListString.apply(String.class));

        assertTrue(assignableFromArrayList.apply(ArrayList.class));
        assertTrue(assignableFromArrayList.apply(listArrayList));
        assertFalse(assignableFromArrayList.apply(List.class));

        assertTrue(assignableFromListArrayList.apply(ArrayList.class));
        assertTrue(assignableFromListArrayList.apply(listArrayList));
        assertTrue(assignableFromListArrayList.apply(List.class));
    }

    @Test
    public void testIn() {
        Collection<Integer> collection = new HashSet<Integer>(Arrays.asList(1, 2, 3));
        Predicate<Integer> inCollection = Predicates.in(collection);

        assertFalse(inCollection.apply(null));
        assertFalse(inCollection.apply(-1));
        assertFalse(inCollection.apply(0));
        assertTrue(inCollection.apply(1));
        assertTrue(inCollection.apply(2));
        assertTrue(inCollection.apply(3));
    }

    @Test
    public void testCompose() {
        Function<String, Integer> toInt = new Function<String, Integer>() {
            @Override
            public Integer apply(@Nullable String input) {
                return Integer.parseInt(input);
            }
        };
        Predicate<String> isNegative = new Predicate<String>() {
            @Override
            public boolean apply(@Nullable String input) {
                return input.charAt(0) == '-';
            }
        };
        Predicate<Integer> isNegativeNumber = Predicates.compose(isNegative, toInt);

        assertFalse(isNegativeNumber.apply("1"));
        assertFalse(isNegativeNumber.apply("2"));
        assertFalse(isNegativeNumber.apply("3"));
        assertFalse(isNegativeNumber.apply("4"));
        assertFalse(isNegativeNumber.apply("5"));
        assertTrue(isNegativeNumber.apply("-1"));
        assertTrue(isNegativeNumber.apply("-2"));
        assertTrue(isNegativeNumber.apply("-3"));
        assertTrue(isNegativeNumber.apply("-4"));
        assertTrue(isNegativeNumber.apply("-5"));
    }

    @Test
    public void testContainsPattern() {
        Predicate<String> containsPattern = Predicates.containsPattern("abc");
        Predicate<String> containsPattern2 = Predicates.containsPattern(Pattern.compile("ab.*c"));

        assertFalse(containsPattern.apply(""));
        assertFalse(containsPattern.apply("abc"));
        assertFalse(containsPattern.apply("abcd"));
        assertFalse(containsPattern.apply("abxyzc"));
        assertTrue(containsPattern.apply("abcdabc"));
        assertTrue(containsPattern.apply("abcxyzc"));
        assertTrue(containsPattern.apply("abcdabcxyzc"));

        assertFalse(containsPattern2.apply(""));
        assertFalse(containsPattern2.apply("abc"));
        assertFalse(containsPattern2.apply("abcd"));
        assertFalse(containsPattern2.apply("abcxyzc"));
        assertTrue(containsPattern2.apply("abcdabc"));
        assertTrue(containsPattern2.apply("abcxyzc"));
        assertTrue(containsPattern2.apply("abcdabcxyzc"));
    }
}