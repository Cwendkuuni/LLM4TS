package com.example.test.predicates;

import com.google.common.base.Predicate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Object> alwaysTrue = Predicates.alwaysTrue();
        assert(alwaysTrue.apply(null) == true);
        assert(alwaysTrue.apply("hello") == true);
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Object> alwaysFalse = Predicates.alwaysFalse();
        assert(alwaysFalse.apply(null) == false);
        assert(alwaysFalse.apply("hello") == false);
    }

    @Test
    public void testIsNull() {
        Predicate<Object> isNull = Predicates.isNull();
        assert(isNull.apply(null) == true);
        assert(isNull.apply("hello") == false);

        Object obj = null;
        assert(isNull.apply(obj) == true);

        String str = null;
        assert(isNull.apply(str) == true);
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNull = Predicates.notNull();
        assert(notNull.apply(null) == false);
        assert(notNull.apply("hello") == true);

        Object obj = null;
        assert(notNull.apply(obj) == false);

        String str = null;
        assert(notNull.apply(str) == false);

        Object nonNullObj = new Object();
        assert(notNull.apply(nonNullObj) == true);

        String nonNullStr = "hello";
        assert(notNull.apply(nonNullStr) == true);
    }

    @Test
    public void testNot() {
        Predicate<Boolean> predicate = Predicates.alwaysTrue();
        Predicate<Boolean> notPredicate = Predicates.not(predicate);

        assert(notPredicate.apply(true) == false);
        assert(notPredicate.apply(false) == true);

        Predicate<Integer> integerPredicate = Predicates.alwaysFalse();
        Predicate<Integer> notIntegerPredicate = Predicates.not(integerPredicate);

        assert(notIntegerPredicate.apply(1) == true);
        assert(notIntegerPredicate.apply(0) == false);
    }

    @Test
    public void testAnd() {
        Predicate<Integer> predicate1 = Predicates.alwaysTrue();
        Predicate<Integer> predicate2 = Predicates.isNull();
        Predicate<Integer> andPredicate = Predicates.and(predicate1, predicate2);

        assert(andPredicate.apply(null) == false);
        assert(andPredicate.apply(1) == true);
        assert(andPredicate.apply(null) == false);

        List<Predicate<Integer>> predicates = Arrays.asList(Predicates.alwaysTrue(), Predicates.isNull());
        Predicate<Integer> listAndPredicate = Predicates.and(predicates);

        assert(listAndPredicate.apply(null) == false);
        assert(listAndPredicate.apply(1) == true);
        assert(listAndPredicate.apply(null) == false);

        Predicate<Integer> predicate3 = Predicates.isEqualTo(1);
        Predicate<Integer> andPredicate2 = Predicates.and(predicate1, predicate3);

        assert(andPredicate2.apply(null) == false);
        assert(andPredicate2.apply(1) == true);
        assert(andPredicate2.apply(2) == false);
    }

    @Test
    public void testOr() {
        Predicate<Integer> predicate1 = Predicates.alwaysTrue();
        Predicate<Integer> predicate2 = Predicates.isNull();
        Predicate<Integer> orPredicate = Predicates.or(predicate1, predicate2);

        assert(orPredicate.apply(null) == true);
        assert(orPredicate.apply(1) == true);
        assert(orPredicate.apply(null) == true);

        List<Predicate<Integer>> predicates = Arrays.asList(Predicates.alwaysTrue(), Predicates.isNull());
        Predicate<Integer> listOrPredicate = Predicates.or(predicates);

        assert(listOrPredicate.apply(null) == true);
        assert(listOrPredicate.apply(1) == true);
        assert(listOrPredicate.apply(null) == true);

        Predicate<Integer> predicate3 = Predicates.isEqualTo(1);
        Predicate<Integer> orPredicate2 = Predicates.or(predicate1, predicate3);

        assert(orPredicate2.apply(null) == true);
        assert(orPredicate2.apply(1) == true);
        assert(orPredicate2.apply(2) == false);
    }

    @Test(expected = NullPointerException.class)
    public void testAndWithNull() {
        Predicate<Integer> predicate1 = Predicates.isNull();
        Predicate<Integer> predicate2 = null;
        Predicate<Integer> andPredicate = Predicates.and(predicate1, predicate2);
        andPredicate.apply(1);
    }

    @Test(expected = NullPointerException.class)
    public void testOrWithNull() {
        Predicate<Integer> predicate1 = null;
        Predicate<Integer> predicate2 = Predicates.isNull();
        Predicate<Integer> orPredicate = Predicates.or(predicate1, predicate2);
        orPredicate.apply(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAndWithDifferentTypes() {
        Predicate<String> predicate1 = Predicates.alwaysTrue();
        Predicate<Integer> predicate2 = Predicates.isNull();
        Predicate<Object> andPredicate = Predicates.and(predicate1, predicate2);
        andPredicate.apply("hello");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOrWithDifferentTypes() {
        Predicate<String> predicate1 = Predicates.alwaysTrue();
        Predicate<Integer> predicate2 = Predicates.isNull();
        Predicate<Object> orPredicate = Predicates.or(predicate1, predicate2);
        orPredicate.apply("hello");
    }

    @Test
    public void testEqualsTo() {
        Predicate<Integer> predicate = Predicates.equalTo(1);
        assert(predicate.apply(1) == true);
        assert(predicate.apply(2) == false);
        assert(predicate.apply(null) == false);

        Predicate<String> stringPredicate = Predicates.equalTo("hello");
        assert(stringPredicate.apply("hello") == true);
        assert(stringPredicate.apply("world") == false);
        assert(stringPredicate.apply(null) == false);
    }

    @Test
    public void testInstanceOf() {
        Predicate<String> stringPredicate = Predicates.instanceOf(String.class);
        assert(stringPredicate.apply("hello") == true);
        assert(stringPredicate.apply(1) == false);
        assert(stringPredicate.apply(new Object()) == false);

        Predicate<Number> numberPredicate = Predicates.instanceOf(Number.class);
        assert(numberPredicate.apply(1) == true);
        assert(numberPredicate.apply("hello") == false);
        assert(numberPredicate.apply(new Object()) == false);
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> assignableFromPredicate = Predicates.assignableFrom(String.class);
        assert(assignableFromPredicate.apply(String.class) == true);
        assert(assignableFromPredicate.apply(Number.class) == false);
        assert(assignableFromPredicate.apply(Object.class) == true);

        Predicate<Class<?>> assignableToPredicate = Predicates.assignableFrom(Number.class);
        assert(assignableToPredicate.apply(String.class) == false);
        assert(assignableToPredicate.apply(Number.class) == true);
        assert(assignableToPredicate.apply(Object.class) == true);
    }

    @Test
    public void testIn() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Predicate<Integer> inPredicate = Predicates.in(list);
        assert(inPredicate.apply(1) == true);
        assert(inPredicate.apply(2) == true);
        assert(inPredicate.apply(3) == true);
        assert(inPredicate.apply(4) == false);

        List<String> stringList = Arrays.asList("hello", "world");
        Predicate<String> inStringPredicate = Predicates.in(stringList);
        assert(inStringPredicate.apply("hello") == true);
        assert(inStringPredicate.apply("world") == true);
        assert(inStringPredicate.apply("goodbye") == false);

        List<Object> objectList = Arrays.asList(1, "hello", new Object());
        Predicate<Object> inObjectPredicate = Predicates.in(objectList);
        assert(inObjectPredicate.apply(1) == true);
        assert(inObjectPredicate.apply("hello") == true);
        assert(inObjectPredicate.apply(new Object()) == true);
        assert(inObjectPredicate.apply(new Integer(4)) == false);
    }

    @Test(expected = NullPointerException.class)
    public void testInWithNull() {
        List<Integer> list = null;
        Predicate<Integer> inPredicate = Predicates.in(list);
        inPredicate.apply(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInWithDifferentTypes() {
        List<Integer> intList = Arrays.asList(1, 2, 3);
        List<String> stringList = Arrays.asList("hello", "world");
        Predicate<Object> inPredicate = Predicates.in(intList, "hello");
        inPredicate.apply(1);
    }

    @Test
    public void testCompose() {
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(@Nullable Integer input) {
                return input % 2 == 0;
            }
        };

        Function<Integer, Integer> square = new Function<Integer, Integer>() {
            @Override
            public Integer apply(@Nullable Integer input) {
                return input * input;
            }
        };

        Predicate<Integer> composePredicate = Predicates.compose(isEven, square);
        assert(composePredicate.apply(1) == false);
        assert(composePredicate.apply(2) == true);
        assert(composePredicate.apply(3) == false);

        Predicate<String> stringIsEmpty = new Predicate<String>() {
            @Override
            public boolean apply(@Nullable String input) {
                return input == null || input.isEmpty();
            }
        };

        Function<String, Integer> stringLength = new Function<String, Integer>() {
            @Override
            public Integer apply(@Nullable String input) {
                return input.length();
            }
        };

        Predicate<String> composeStringPredicate = Predicates.compose(stringIsEmpty, stringLength);
        assert(composeStringPredicate.apply(null) == Integer.MIN_VALUE);
        assert(composeStringPredicate.apply("") == 0);
        assert(composeStringPredicate.apply("hello") == 5);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNull() {
        Predicate<String> stringPredicate = null;
        Function<Integer, Integer> function = Function.identity();
        Predicate<Object> composePredicate = Predicates.compose(stringPredicate, function);
        composePredicate.apply(1);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullFunction() {
        Predicate<Integer> predicate = Predicates.isNull();
        Function<Object, Integer> function = null;
        Predicate<Object> composePredicate = Predicates.compose(predicate, function);
        composePredicate.apply(1);
    }

    @Test
    public void testContainsPattern() {
        Predicate<CharSequence> containsPatternPredicate = Predicates.containsPattern("ab.*c");
        assert(containsPatternPredicate.apply("abcd") == true);
        assert(containsPatternPredicate.apply("abef") == true);
        assert(containsPatternPredicate.apply("ab") == false);

        Predicate<CharSequence> containsPatternFromStringPredicate = Predicates.containsPatternFromString("ab.*c");
        assert(containsPatternFromStringPredicate.apply("abcd") == true);
        assert(containsPatternFromStringPredicate.apply("abef") == true);
        assert(containsPatternFromStringPredicate.apply("ab") == false);
    }

    @Test
    public void testContainsPatternWithFlags() {
        Predicate<CharSequence> containsPatternPredicate = Predicates.containsPattern("(?i)ab.*c");
        assert(containsPatternPredicate.apply("abcd") == true);
        assert(containsPatternPredicate.apply("Abef") == true);
        assert(containsPatternPredicate.apply("ab") == false);

        Predicate<CharSequence> containsPatternFromStringPredicate = Predicates.containsPatternFromString("(?i)ab.*c");
        assert(containsPatternFromStringPredicate.apply("abcd") == true);
        assert(containsPatternFromStringPredicate.apply("Abef") == true);
        assert(containsPatternFromStringPredicate.apply("ab") == false);
    }
}