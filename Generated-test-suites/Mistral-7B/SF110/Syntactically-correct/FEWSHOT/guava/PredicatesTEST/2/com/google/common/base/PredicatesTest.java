package com.google.common.base;

import static org.junit.Assert.*;
import com.google.common.base.Predicates.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PredicatesTest {

    @Test
    public void testAlwaysTrueAndFalse() {
        assertTrue(alwaysTrue().apply(null));
        assertFalse(alwaysFalse().apply(null));
    }

    @Test
    public void testIsNullAndNotNull() {
        assertNull(isNull().apply(null));
        assertNotNull(notNull().apply(new Object()));
    }

    @Test
    public void testNotPredicate() {
        Predicate<Integer> predicate = isNull();
        Predicate<Integer> notPredicate = not(predicate);
        assertFalse(notPredicate.apply(null));
        assertTrue(notPredicate.apply(new Integer(42)));
    }

    @Test
    public void testAndPredicate() {
        Predicate<String> isEmpty = isNull();
        Predicate<String> isEmptyOrStartsWithWith = startsWith("With");
        Predicate<String> andPredicate = and(isEmpty, isEmptyOrStartsWithWith);

        List<String> strings = Arrays.asList(null, "", "Empty", "With", "WithEmpty", "WithEmptyWith");
        for (String string : strings) {
            System.out.println("String: " + string);
            System.out.println("Predicate: " + andPredicate);
            assertFalse(andPredicate.apply(string));
        }

        String emptyString = "";
        String withString = "With";
        String withEmptyString = "WithEmpty";
        String withEmptyWithString = "WithEmptyWith";

        List<String> validStrings = Arrays.asList(emptyString, withString, withEmptyString, withEmptyWithString);
        for (String string : validStrings) {
            System.out.println("String: " + string);
            System.out.println("Predicate: " + andPredicate);
            assertTrue(andPredicate.apply(string));
        }
    }

    @Test
    public void testOrPredicate() {
        Predicate<String> isEmpty = isNull();
        Predicate<String> isEmptyOrStartsWithWith = startsWith("With");
        Predicate<String> orPredicate = or(isEmpty, isEmptyOrStartsWithWith);

        List<String> strings = Arrays.asList(null, "", "Empty", "With", "WithEmpty", "WithEmptyWith");
        for (String string : strings) {
            System.out.println("String: " + string);
            System.out.println("Predicate: " + orPredicate);
            assertTrue(orPredicate.apply(string) || isEmpty.apply(string) || isEmptyOrStartsWithWith.apply(string));
        }

        List<String> validStrings = Arrays.asList(null, "", "Empty", "With", "WithEmpty", "WithEmptyWith");
        Predicate<String> isEmptyOrStartsWithEmpty = startsWith("Empty");
        Predicate<String> validPredicate = or(isEmpty, isEmptyOrStartsWithWith, isEmptyOrStartsWithEmpty);
        for (String string : validStrings) {
            System.out.println("String: " + string);
            System.out.println("Predicate: " + validPredicate);
            assertTrue(validPredicate.apply(string) || isEmpty.apply(string) || isEmptyOrStartsWithWith.apply(string) || isEmptyOrStartsWithEmpty.apply(string));
        }
    }

    @Test
    public void testEqualsTo() {
        Object target = new Object();
        Predicate<Object> equalsToTarget = equalTo(target);
        assertFalse(equalsToTarget.apply(null));
        assertTrue(equalsToTarget.apply(target));
    }

    @Test
    public void testInstanceOf() {
        Class<String> stringClass = String.class;
        Predicate<Object> instanceOfString = instanceOf(stringClass);
        assertFalse(instanceOfString.apply(new Integer(42)));
        assertTrue(instanceOfString.apply("Hello"));
    }

    @Test
    public void testAssignableFrom() {
        Class<String> stringClass = String.class;
        Class<Number> numberClass = Number.class;
        Predicate<Class<?>> assignableFromNumber = assignableFrom(numberClass);
        assertTrue(assignableFromNumber.apply(stringClass));
    }

    @Test
    public void testInPredicate() {
        List<Integer> ints = Arrays.asList(1, 2, 3, 4, 5);
        Predicate<Integer> inInts = in(ints);
        assertFalse(inInts.apply(0));
        assertFalse(inInts.apply(6));
        assertTrue(inInts.apply(1));
        assertTrue(inInts.apply(5));
    }

    @Test
    public void testComposePredicate() {   
        Function<Integer, Integer> function = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input * 2;
            }
        };
        Predicate<Integer> isEven = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        };
        Predicate<Integer> composePredicate = compose(isEven, function);
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        for (Integer number : numbers) {
            System.out.println("Number: " + number);
            System.out.println("Predicate: " + composePredicate);
            assertFalse(composePredicate.apply(number));
        }

        List<Integer> evenNumbers = Arrays.asList(2, 4, 6);
        for (Integer number : evenNumbers) {
            System.out.println("Number: " + number);
            System.out.println("Predicate: " + composePredicate);
            assertTrue(composePredicate.apply(number));
            System.out.println("Function applied to number: " + function.apply(number));
            assertEquals(number * 2, function.apply(number));
        }
    }

    @Test
    public void testContainsPatternPredicate() {
        Predicate<String> containsPattern = containsPattern("pattern");
        String[] strings = {"HelloWorld", "HelloPattern", "HelloEmpty", "PatternHello", "Pattern"};
        for (String string : strings) {
            System.out.println("String: " + string);
            System.out.println("Predicate: " + containsPattern);
            assertTrue(containsPattern.apply(string) || containsPattern.apply(string.toUpperCase()));
        }
    }
}