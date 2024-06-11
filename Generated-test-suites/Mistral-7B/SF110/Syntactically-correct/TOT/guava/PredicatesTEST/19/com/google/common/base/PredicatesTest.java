package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class PredicatesTest {

 @Test
 public void testAlwaysTrue() {
 Object target = new Object();
 assertTrue(Predicates.alwaysTrue().apply(target));
 }

 @Test
 public void testAlwaysFalse() {
 Object target = new Object();
 assertFalse(Predicates.alwaysFalse().apply(target));
 }

 @Test
 public void testIsNull() {
 Object nullObject = null;
 assertTrue(Predicates.isNull().apply(nullObject));
 Object notNullObject = new Object();
 assertFalse(Predicates.isNull().apply(notNullObject));
 }

 @Test
 public void testNotNull() {
 Object nullObject = null;
 assertFalse(Predicates.notNull().apply(nullObject));
 Object notNullObject = new Object();
 assertTrue(Predicates.notNull().apply(notNullObject));
 }

 @Test
 public void testNot() {
 Predicate<Object> alwaysTrue = Predicates.alwaysTrue();
 Predicate<Object> alwaysFalse = Predicates.alwaysFalse();
 Predicate<Object> notAlwaysTrue = Predicates.not(alwaysTrue);
 Predicate<Object> notAlwaysFalse = Predicates.not(alwaysFalse);

 assertTrue(notAlwaysTrue.apply(new Object()));
 assertFalse(notAlwaysTrue.apply(new Object()));
 assertTrue(notAlwaysFalse.apply(new Object()));
 assertFalse(notAlwaysFalse.apply(new Object()));
 }

 @Test
 public void testAnd() {
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> isPositive = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input > 0;
 }
 };

 List<Predicate<Integer>> components = Arrays.asList(isEven, isPositive);
 Predicate<Integer> andPredicate = Predicates.and(components);

 Integer testCase1 = 4;
 assertTrue(andPredicate.apply(testCase1));

 Integer testCase2 = -2;
 assertTrue(andPredicate.apply(testCase2));

 Integer testCase3 = 0;
 assertFalse(andPredicate.apply(testCase3));

 Integer testCase4 = 1;
 assertFalse(andPredicate.apply(testCase4));
 }

 @Test
 public void testOr() {
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> isOdd = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 != 0;
 }
 };

 List<Predicate<Integer>> components = Arrays.asList(isEven, isOdd);
 Predicate<Integer> orPredicate = Predicates.or(components);

 Integer testCase1 = 4;
 assertTrue(orPredicate.apply(testCase1));

 Integer testCase2 = 1;
 assertTrue(orPredicate.apply(testCase2));

 Integer testCase3 = 0;
 assertFalse(orPredicate.apply(testCase3));
 }

 @Test
 public void testEqualTo() {
 Object target1 = new Object();
 Predicate<Object> isTarget1 = Predicates.equalTo(target1);
 Object target2 = new Object();
 Predicate<Object> isNotTarget1 = Predicates.not(isTarget1);

 assertTrue(isTarget1.apply(target1));
 assertFalse(isTarget1.apply(target2));
 assertFalse(isNotTarget1.apply(target1));
 assertTrue(isNotTarget1.apply(target2));
 }

 @Test
 public void testInstanceOf() {
 Class<String> clazz = String.class;
 Predicate<Object> isString = Predicates.instanceOf(clazz);

 String testString = "test";
 assertTrue(isString.apply(testString));
 assertFalse(isString.apply(new Integer(1)));
 }

 @Test
 public void testAssignableFrom() {
 Class<String> childClass = String.class;
 Class<Object> parentClass = Object.class;
 Predicate<Class<?>> isAssignableFromChildToParent = Predicates.assignableFrom(childClass);

 assertTrue(isAssignableFromChildToParent.apply(childClass));
 assertTrue(isAssignableFromChildToParent.apply(parentClass));
 }

 @Test
 public void testIn() {
 List<Integer> collection = Arrays.asList(1, 2, 3, 4);
 Predicate<Integer> isInCollection = Predicates.in(collection);

 Integer testCase1 = 2;
 assertTrue(isInCollection.apply(testCase1));

 Integer testCase2 = 5;
 assertFalse(isInCollection.apply(testCase2));
 }

 @Test
 public void testCompose() {
 Function<String, Integer> toIntegerFunction = new Function<String, Integer>() {
 @Override
 public Integer apply(String input) {
 return Integer.parseInt(input);
 }
 };
 Predicate<String> isEvenLength = new Predicate<String>() {
 @Override
 public boolean apply(String input) {
 return input.length() % 2 == 0;
 }
 };
 Predicate<String> isEvenLengthAndGreaterThanFive = Predicates.compose(isEvenLength, toIntegerFunction);

 String testCase1 = "test";
 assertFalse(isEvenLengthAndGreaterThanFive.apply(testCase1));

 String testCase2 = "testtest";
 assertTrue(isEvenLengthAndGreaterThanFive.apply(testCase2));
 }

 @Test
 public void testContainsPattern() {
 String testString = "testtesttesttest";
 Pattern pattern = Pattern.compile("test");
 Predicate<CharSequence> containsPattern = Predicates.containsPattern(pattern);

 assertTrue(containsPattern.apply(testString));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsPatternInvalidPattern() {
 Pattern invalidPattern = Pattern.compile(null);
 Predicate<CharSequence> containsPattern = Predicates.containsPattern(invalidPattern);
 }
}