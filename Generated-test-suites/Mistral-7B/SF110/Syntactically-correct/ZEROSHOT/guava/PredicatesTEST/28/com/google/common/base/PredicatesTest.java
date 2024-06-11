package com.google.common.base;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

public class PredicatesTest {

 @Test
 public void testAlwaysTrue() {
 Predicate<Integer> predicate = Predicates.alwaysTrue();
 assertTrue(predicate.apply(1));
 assertTrue(predicate.apply(2));
 }

 @Test
 public void testAlwaysFalse() {
 Predicate<Integer> predicate = Predicates.alwaysFalse();
 assertFalse(predicate.apply(1));
 assertFalse(predicate.apply(2));
 }

 @Test
 public void testIsNull() {
 Predicate<Integer> predicate = Predicates.isNull();
 assertNull(predicate.apply(null));
 assertFalse(predicate.apply(1));
 }

 @Test
 public void testNotNull() {
 Predicate<Integer> predicate = Predicates.notNull();
 assertNotNull(predicate.apply(1));
 assertNull(predicate.apply(null));
 }

 @Test
 public void testNot() {
 Predicate<Integer> predicate = Predicates.alwaysTrue();
 Predicate<Integer> negatedPredicate = Predicates.not(predicate);
 assertFalse(negatedPredicate.apply(1));
 assertTrue(negatedPredicate.apply(0));
 }

 @Test
 public void testAnd() {
 Predicate<Integer> even = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> greaterThanZero = Predicates.notNull();
 Predicate<Integer> andPredicate = Predicates.and(even, greaterThanZero);

 List<Integer> inputList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
 for (Integer input : inputList) {
 assertTrue(andPredicate.apply(input));
 }

 List<Integer> nullList = Collections.emptyList();
 assertFalse(andPredicate.apply(nullList));
 }

 @Test
 public void testOr() {
 Predicate<Integer> even = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> odd = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 != 0;
 }
 };
 Predicate<Integer> orPredicate = Predicates.or(even, odd);

 List<Integer> inputList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
 for (Integer input : inputList) {
 assertTrue(orPredicate.apply(input));
 }

 List<Integer> evenList = Arrays.asList(0, 2, 4, 6, 8);
 for (Integer input : evenList) {
 assertTrue(even.apply(input));
 }

 List<Integer> oddList = Arrays.asList(1, 3, 5, 7, 9);
 for (Integer input : oddList) {
 assertTrue(odd.apply(input));
 }

 List<Integer> emptyList = Collections.emptyList();
 assertFalse(orPredicate.apply(emptyList));
 }

 @Test
 public void testEqualTo() {
 Predicate<Integer> equalsFive = Predicates.equalTo(5);
 Predicate<Integer> notEqualsFive = Predicates.not(Predicates.equalTo(5));

 List<Integer> inputList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
 for (Integer input : inputList) {
 if (input.equals(5)) {
 assertTrue(equalsFive.apply(input));
 } else {
 assertTrue(notEqualsFive.apply(input));
 }
 }
 }

 @Test(expected = NullPointerException.class)
 public void testInstanceOfNull() {
 Predicate<Object> instanceOfNull = Predicates.instanceOf(null);
 }

 @Test
 public void testInstanceOfString() {
 Predicate<String> instanceOfString = Predicates.instanceOf(String.class);
 String testString = "Test String";
 assertTrue(instanceOfString.apply(testString));
 }

 @Test
 public void testInstanceOfInteger() {
 Predicate<Integer> instanceOfInteger = Predicates.instanceOf(Integer.class);
 Integer testInteger = 1;
 assertTrue(instanceOfInteger.apply(testInteger));
 }

 @Test
 public void testAssignableFromString() {
 Predicate<Class<?>> assignableFromString = Predicates.assignableFrom(String.class);
 assertTrue(assignableFromString.apply(StringBuilder.class));
 }

 @Test
 public void testAssignableFromInteger() {
 Predicate<Class<?>> assignableFromInteger = Predicates.assignableFrom(Integer.TYPE);
 assertTrue(assignableFromInteger.apply(Integer.class));
 }

 @Test
 public void testIn() {
 Predicate<Integer> inList = Predicates.in(Arrays.asList(1, 2, 3, 4));
 List<Integer> inputList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
 for (Integer input : inputList) {
 if (inList.apply(input)) {
 assertTrue(true);
 } else {
 fail("Expected input to be in the list: " + input);
 }
 }
 }

 @Test
 public void testCompose() {
 Predicate<String> isEmptyString = Predicates.alwaysFalse();
 Function<String, Boolean> isNullFunction = (String s) -> s == null;
 Predicate<String> composePredicate = Predicates.compose(isEmptyString, isNullFunction);
 String nullString = null;
 String emptyString = "";
 String nonEmptyString = "Test";

 assertTrue(composePredicate.apply(nullString));
 assertFalse(composePredicate.apply(emptyString));
 assertFalse(composePredicate.apply(nonEmptyString));
 }

 @Test
 public void testContainsPattern() {
 Pattern pattern = Pattern.compile("test");
 Predicate<String> containsPatternPredicate = Predicates.containsPattern(pattern);
 String testString1 = "Test String 1";
 String testString2 = "Test String 2";
 String testString3 = "Non Test String";

 assertTrue(containsPatternPredicate.apply(testString1));
 assertTrue(containsPatternPredicate.apply(testString2));
 assertFalse(containsPatternPredicate.apply(testString3));
 }
}