package com.google.common.base;

import com.google.common.base.Predicates;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class PredicatesTest {

 @Test
 public void testAlwaysTrue() {
 Object obj = new Object();
 assertTrue(Predicates.alwaysTrue().apply(obj));
 }

 @Test
 public void testAlwaysFalse() {
 Object obj = new Object();
 assertFalse(Predicates.alwaysFalse().apply(obj));
 }

 @Test
 public void testIsNull() {
 assertNull(Predicates.isNull().apply(null));
 }

 @Test(expected = NullPointerException.class)
 public void testIsNullWithNotNull() {
 Object obj = new Object();
 assertNull(Predicates.isNull().apply(obj));
 }

 @Test
 public void testNotNull() {
 Object obj = new Object();
 assertNotNull(Predicates.notNull().apply(obj));
 }

 @Test(expected = NullPointerException.class)
 public void testNotNullWithNull() {
 Object obj = null;
 assertNotNull(Predicates.notNull().apply(obj));
 }

 @Test
 public void testNot() {
 Predicate<Boolean> predicate = Predicates.isNull();
 Predicate<Boolean> notPredicate = Predicates.not(predicate);
 Boolean trueValue = Boolean.TRUE;
 Boolean falseValue = Boolean.FALSE;

 assertTrue(notPredicate.apply(falseValue));
 assertTrue(notPredicate.apply(trueValue));
 assertFalse(notPredicate.apply(null));
 assertFalse(notPredicate.apply(trueValue));
 }

 @Test
 public void testAnd() {
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> isPositive = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input > 0;
 }
 };

 Predicate<Integer> andPredicate = Predicates.and(isEven, isPositive);

 int value1 = 10;
 int value2 = 4;
 int value3 = 3;

 assertTrue(andPredicate.apply(value1));
 assertTrue(andPredicate.apply(value2));
 assertFalse(andPredicate.apply(value3));
 }

 @Test
 public void testOr() {
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> isPositive = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input > 0;
 }
 };

 Predicate<Integer> orPredicate = Predicates.or(isEven, isPositive);

 int value1 = 10;
 int value2 = 4;
 int value3 = 3;
 int value4 = 0;

 assertTrue(orPredicate.apply(value1));
 assertTrue(orPredicate.apply(value2));
 assertTrue(orPredicate.apply(value3));
 assertTrue(orPredicate.apply(value4));
 }

 @Test
 public void testInstanceOf() {
 Class<String> stringClass = String.class;
 Predicate<Object> instanceOfPredicate = Predicates.instanceOf(stringClass);

 String testString = new String("test");
 assertTrue(instanceOfPredicate.apply(testString));

 int testInteger = 5;
 assertFalse(instanceOfPredicate.apply(testInteger));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInstanceOfWithInvalidClass() {
 Class<?> invalidClass = new Class<?>(){};
 Predicate<Object> instanceOfPredicate = Predicates.instanceOf(invalidClass);
 }

 @Test
 public void testAssignableFrom() {
 Class<StringBuilder> stringBuilderClass = StringBuilder.class;
 Class<String> stringClass = String.class;
 Predicate<Class<?>> assignableFromPredicate = Predicates.assignableFrom(stringBuilderClass);

 assertTrue(assignableFromPredicate.apply(stringClass));
 }

 @Test
 public void testIn() {
 List<Integer> testList = Arrays.asList(1, 2, 3, 4, 5);
 Predicate<Integer> inPredicate = Predicates.in(testList);

 int value1 = 2;
 int value2 = 6;
 int value3 = 3;

 assertTrue(inPredicate.apply(value1));
 assertFalse(inPredicate.apply(value2));
 assertFalse(inPredicate.apply(value3));
 }

 @Test
 public void testCompose() {
 Function<Integer, Integer> doubleFunction = new Function<Integer, Integer>() {
 @Override
 public Integer apply(@Nullable Integer input) {
 return input * 2;
 }
 };
 Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input % 2 == 0;
 }
 };

 Predicate<Integer> composedPredicate = Predicates.compose(isEvenPredicate, doubleFunction);

 int value1 = 2;
 int value2 = 3;
 int value3 = 6;
 int value4 = 7;

 assertTrue(composedPredicate.apply(value3));
 assertFalse(composedPredicate.apply(value1));
 assertFalse(composedPredicate.apply(value2));
 assertFalse(composedPredicate.apply(value4));
 }

 @Test
 public void testContainsPattern() {
 String pattern = "ab*cd";
 Predicate<String> containsPatternPredicate = Predicates.containsPattern(pattern);

 String testString1 = "abcd";
 String testString2 = "ababcd";
 String testString3 = "abefgh";

 assertTrue(containsPatternPredicate.apply(testString1));
 assertTrue(containsPatternPredicate.apply(testString2));
 assertFalse(containsPatternPredicate.apply(testString3));
 }

 @Test
 public void testContainsPatternFromString() {
 String pattern = "ab*cd";
 Predicate<String> containsPatternPredicate = Predicates.containsPatternFromString(pattern);

 String testString1 = "abcd";
 String testString2 = "ababcd";
 String testString3 = "abefgh";

 assertTrue(containsPatternPredicate.apply(testString1));
 assertTrue(containsPatternPredicate.apply(testString2));
 assertFalse(containsPatternPredicate.apply(testString3));
 }
}