package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PredicatesTest {

 @Test
 public void testAlwaysTrue() {
 Predicate<Integer> predicate = Predicates.alwaysTrue();
 assertTrue(predicate.apply(null));
 assertTrue(predicate.apply(1));
 assertTrue(predicate.apply(2.0));
 }

 @Test
 public void testAlwaysFalse() {
 Predicate<Integer> predicate = Predicates.alwaysFalse();
 assertFalse(predicate.apply(null));
 assertFalse(predicate.apply(1));
 assertFalse(predicate.apply(2.0));
 }

 @Test
 public void testIsNull() {
 Predicate<String> predicate = Predicates.isNull();
 assertFalse(predicate.apply("abc"));
 assertNull(predicate.apply(null));
 }

 @Test
 public void testNotNull() {
 Predicate<String> predicate = Predicates.notNull();
 assertTrue(predicate.apply("abc"));
 assertNull(predicate.apply(null));
 }

 @Test
 public void testNot() {
 Predicate<Boolean> predicateTrue = Predicates.alwaysTrue();
 Predicate<Boolean> predicateFalse = Predicates.alwaysFalse();
 Predicate<Boolean> predicateNotFalse = Predicates.not(predicateFalse);

 assertTrue(predicateNotFalse.apply(true));
 assertFalse(predicateNotFalse.apply(false));

 Predicate<Integer> predicateOne = Predicates.alwaysTrue();
 Predicate<Integer> predicateZero = Predicates.alwaysFalse();
 Predicate<Integer> predicateNotZero = Predicates.not(predicateZero);

 assertTrue(predicateNotZero.apply(1));
 assertFalse(predicateNotZero.apply(0));
 }

 @Test
 public void testAnd() {
 Predicate<Integer> predicateEven = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> predicatePositive = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input > 0;
 }
 };
 Predicate<Integer> predicateAnd = Predicates.and(predicateEven, predicatePositive);

 List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

 for (Integer num : list) {
 assertTrue(predicateAnd.apply(num));
 }

 assertFalse(predicateAnd.apply(-1));
 assertFalse(predicateAnd.apply(0));
 }

 @SafeVarargs
 @Test
 public <T> void testAndVarargs(Predicate<T>... predicates) {
 Predicate<T> predicateAnd = Predicates.and(predicates);
 List<T> list = Collections.emptyList();

 for (T element : list) {
 assertTrue(predicateAnd.apply(element));
 }

 assertFalse(predicateAnd.apply(Collections.singletonMap("key", "value").toString().toCharArray()));
 }

 @Test
 public void testOr() {
 Predicate<Integer> predicateEven = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> predicateOdd = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input % 2 != 0;
 }
 };
 Predicate<Integer> predicateOr = Predicates.or(predicateEven, predicateOdd);

 List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

 for (Integer num : list) {
 if (num % 2 == 0) {
 assertTrue(predicateEven.apply(num));
 } else {
 assertTrue(predicateOdd.apply(num));
 }

 assertTrue(predicateOr.apply(num));
 }

 assertFalse(predicateOr.apply(-1));
 assertFalse(predicateOr.apply(0));
 }

 @SafeVarargs
 @Test
 public <T> void testOrVarargs(Predicate<T>... predicates) {
 Predicate<T> predicateOr = Predicates.or(predicates);
 List<T> list = Collections.emptyList();

 for (T element : list) {
 assertTrue(predicateOr.apply(element));
 }

 assertFalse(predicateOr.apply(Collections.singletonMap("key", "value").toString().toCharArray()));
 }

 @Test
 public void testEqualTo() {
 Predicate<String> predicateEqualToNull = Predicates.equalTo(null);
 Predicate<String> predicateEqualToEmptyString = Predicates.equalTo("");
 Predicate<String> predicateEqualToHello = Predicates.equalTo("Hello");

 assertFalse(predicateEqualToNull.apply("abc"));
 assertFalse(predicateEqualToNull.apply(null));
 assertTrue(predicateEqualToNull.apply(null));

 assertFalse(predicateEqualToEmptyString.apply("abc"));
 assertFalse(predicateEqualToEmptyString.apply(""));
 assertTrue(predicateEqualToEmptyString.apply(""));

 assertTrue(predicateEqualToHello.apply("Hello"));
 assertFalse(predicateEqualToHello.apply("Hi"));
 }

 @Test
 public void testInstanceOf() {
 Predicate<String> predicateInstanceOfString = Predicates.instanceOf(String.class);

 assertTrue(predicateInstanceOfString.apply("abc"));
 assertFalse(predicateInstanceOfString.apply(123));
 }

 @Test
 public void testAssignableFrom() {
 Predicate<Class<?>> predicateAssignableFromString = Predicates.assignableFrom(String.class);

 assertTrue(predicateAssignableFromString.apply(StringBuilder.class));
 assertFalse(predicateAssignableFromString.apply(Integer.class));
 }

 @Test
 public void testIn() {
 Predicate<String> predicateInList = Predicates.in(Arrays.asList("apple", "banana", "cherry"));

 assertTrue(predicateInList.apply("apple"));
 assertTrue(predicateInList.apply("banana"));
 assertTrue(predicateInList.apply("cherry"));
 assertFalse(predicateInList.apply("orange"));
 }

 @Test
 public void testCompose() {
 Predicate<Integer> predicateEven = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input % 2 == 0;
 }
 };
 Function<Integer, Integer> functionAddOne = new Function<Integer, Integer>() {
 @Override
 public Integer apply(@Nullable Integer input) {
 return input + 1;
 }
 };
 Predicate<Integer> predicateCompose = Predicates.compose(predicateEven, functionAddOne);

 List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

 for (Integer num : list) {
 if (num % 2 == 0) {
 assertTrue(predicateEven.apply(num));
 } else {
 continue;
 }

 assertTrue(predicateCompose.apply(num));
 }

 assertFalse(predicateCompose.apply(-1));
 assertFalse(predicateCompose.apply(0));
 }

 @Test
 public void testContainsPattern() {
 Predicate<String> predicateContainsPattern = Predicates.containsPattern("ab.*c");

 assertTrue(predicateContainsPattern.apply("apple"));
 assertTrue(predicateContainsPattern.apply("banana"));
 assertTrue(predicateContainsPattern.apply("abc"));
 assertFalse(predicateContainsPattern.apply("orange"));
 }

 @Test
 public void testContainsPatternFromString() {
 Predicate<String> predicateContainsPatternFromString = Predicates.containsPattern("ab.*c");

 assertTrue(predicateContainsPatternFromString.apply("apple"));
 assertTrue(predicateContainsPatternFromString.apply("banana"));
 assertTrue(predicateContainsContainsPatternFromString.apply("abc"));
 assertFalse(predicateContainsPatternFromString.apply("orange"));
 }
}