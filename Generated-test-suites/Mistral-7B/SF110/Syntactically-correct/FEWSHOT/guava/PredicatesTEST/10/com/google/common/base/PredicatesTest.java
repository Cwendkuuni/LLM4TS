package com.google.common.base;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.*;
import java.util.regex.*;

import org.junit.Test;

public class PredicatesTest {

 // Test for alwaysTrue()
 @Test
 public void testAlwaysTrue() {
 Object obj = new Object();
 assertTrue(Predicates.alwaysTrue().apply(obj));
 }

 // Test for alwaysFalse()
 @Test
 public void testAlwaysFalse() {
 Object obj = new Object();
 assertFalse(Predicates.alwaysFalse().apply(obj));
 }

 // Test for isNull()
 @Test
 public void testIsNull() {
 Object obj1 = new Object();
 assertFalse(Predicates.isNull().apply(obj1));
 Object obj2 = null;
 assertTrue(Predicates.isNull().apply(obj2));
 }

 // Test for notNull()
 @Test
 public void testNotNULL() {
 Object obj1 = new Object();
 assertFalse(Predicates.notNull().apply(null));
 assertTrue(Predicates.notNull().apply(obj1));
 }

 // Test for not()
 @Test
 public void testNot() {
 Predicate<Boolean> predicate = Predicates.alwaysTrue();
 Predicate<Boolean> notPredicate = Predicates.not(predicate);
 Boolean input = Boolean.FALSE;
 assertFalse(predicate.apply(input));
 assertTrue(notPredicate.apply(input));

 input = Boolean.TRUE;
 assertFalse(notPredicate.apply(input));
 assertTrue(predicate.apply(input));
 }

 // Test for and() with Iterable
 @Test
 public void testAndWithIterable() {
 Predicate<Integer> evenNumber = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer integer) {
 return integer % 2 == 0;
 }
 };
 Predicate<Integer> oddNumber = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer integer) {
 return integer % 2 != 0;
 }
 };
 List<Predicate<Integer>> predicates = Arrays.asList(evenNumber, Predicates.not(oddNumber));
 Predicate<Integer> andPredicate = Predicates.and(predicates);

 Integer input1 = 2;
 assertTrue(andPredicate.apply(input1));

 Integer input2 = 3;
 assertFalse(andPredicate.apply(input2));
 }

 // Test for and() with Predicate...
 @Test
 public void testAndWithPredicates() {
 Predicate<Integer> evenNumber = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer integer) {
 return integer % 2 == 0;
 }
 };
 Predicate<Integer> oddNumber = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer integer) {
 return integer % 2 != 0;
 }
 };
 Predicate<Integer> andPredicate = Predicates.and(evenNumber, oddNumber);

 Integer input1 = 2;
 assertFalse(andPredicate.apply(input1));

 Integer input2 = 4;
 assertTrue(andPredicate.apply(input2));
 }

 // Test for or() with Iterable
 @Test
 public void testOrWithIterable() {
 Predicate<Integer> evenNumber = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer integer) {
 return integer % 2 == 0;
 }
 };
 Predicate<Integer> oddNumber = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer integer) {
 return integer % 2 != 0;
 }
 };
 List<Predicate<Integer>> predicates = Arrays.asList(evenNumber, oddNumber);
 Predicate<Integer> orPredicate = Predicates.or(predicates);

 Integer input1 = 2;
 assertTrue(orPredicate.apply(input1));

 Integer input2 = 3;
 assertTrue(orPredicate.apply(input2));
 }

 // Test for or() with Predicate...
 @Test
 public void testOrWithPredicates() {
 Predicate<Integer> evenNumber = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer integer) {
 return integer % 2 == 0;
 }
 };
 Predicate<Integer> oddNumber = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer integer) {
 return integer % 2 != 0;
 }
 };
 Predicate<Integer> orPredicate = Predicates.or(evenNumber, oddNumber);

 Integer input1 = 2;
 assertTrue(orPredicate.apply(input1));

 Integer input2 = 4;
 assertTrue(orPredicate.apply(input2));

 Integer input3 = 6;
 assertTrue(orPredicate.apply(input3));
 }

 // Test for equalTo()
 @Test
 public void testEqualTo() {
 Object target1 = new Object();
 Predicate<Object> equalsToTarget1 = Predicates.equalTo(target1);
 assertTrue(equalsToTarget1.apply(target1));
 assertFalse(equalsToTarget1.apply(new Object()));

 Object target2 = new Object();
 Predicate<Object> equalsToTarget2 = Predicates.equalTo(target2);
 assertFalse(equalsToTarget2.apply(target1));
 assertFalse(equalsToTarget2.apply(target2));
 assertFalse(equalsToTarget2.apply(new Object()));
 }

 // Test for instanceOf()
 @Test(expected = IllegalArgumentException.class)
 public void testInstanceOfWithInvalidType() {
 Predicate<String> instanceOfInteger = Predicates.instanceOf(Integer.class);
 }

 @Test
 public void testInstanceOfWithCorrectType() {
 Predicate<Integer> instanceOfInteger = Predicates.instanceOf(Integer.class);
 Integer input = 10;
 assertTrue(instanceOfInteger.apply(input));
 }

 // Test for assignableFrom()
 @Test(expected = IllegalArgumentException.class)
 public void testAssignableFromWithInvalidType() {
 Predicate<Class<String>> assignableFromInteger = Predicates.assignableFrom(Integer.class);
 }

 @Test
 public void testAssignableFromWithCorrectType() {
 Predicate<Class<Integer>> assignableFromInteger = Predicates.assignableFrom(Integer.class);
 assertTrue(assignableFromInteger.apply(Integer.TYPE));
 }

 // Test for in()
 @Test
 public void testIn() {
 List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
 Predicate<Integer> inPredicate = Predicates.in(numbers);

 Integer input1 = 1;
 assertTrue(inPredicate.apply(input1));

 Integer input2 = 6;
 assertFalse(inPredicate.apply(input2));
 }

 // Test for compose()
 @Test
 public void testCompose() {
 Predicate<String> isEmptyString = new Predicate<String>() {
 @Override
 public boolean apply(@Nullable String string) {
 return string == null || string.isEmpty();
 }
 };
 Function<String, Integer> stringToLength = new Function<String, Integer>() {
 @Override
 public Integer apply(@Nullable String string) {
 return string == null ? null : string.length();
 }
 };
 Predicate<String> stringLengthIsZero = Predicates.compose(isEmptyString, stringToLength);
 String input1 = "";
 assertTrue(stringLengthIsZero.apply(input1));

 String input2 = "Test";
 assertFalse(stringLengthIsZero.apply(input2));
 }

 // Test for containsPattern()
 @Test
 public void testContainsPattern() {
 String pattern = "Test";
 Predicate<String> containsPatternPredicate = Predicates.containsPattern(Pattern.compile(pattern));
 String input1 = "This is a test";
 assertTrue(containsPatternPredicate.apply(input1));

 String input2 = "This is not a test";
 assertFalse(containsPatternPredicate.apply(input2));
 }
}