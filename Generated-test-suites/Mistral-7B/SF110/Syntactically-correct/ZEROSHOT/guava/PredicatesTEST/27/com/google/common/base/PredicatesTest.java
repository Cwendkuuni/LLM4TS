package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PredicatesTest {

 @Test
 public void testAlwaysTrue() {
 Predicate<Integer> alwaysTrue = Predicates.alwaysTrue();
 assertTrue(alwaysTrue.apply(null));
 assertTrue(alwaysTrue.apply(1));
 assertTrue(alwaysTrue.apply(10));
 }

 @Test
 public void testAlwaysFalse() {
 Predicate<Integer> alwaysFalse = Predicates.alwaysFalse();
 assertFalse(alwaysFalse.apply(null));
 assertFalse(alwaysFalse.apply(1));
 assertFalse(alwaysFalse.apply(10));
 }

 @Test
 public void testIsNull() {
 Predicate<Object> isNull = Predicates.isNull();
 assertNull(isNull.apply(null));
 assertNull(isNull.apply(null));
 assertNull(isNull.apply(null));

 assertFalse(isNull.apply(1));
 assertFalse(isNull.apply("Hello"));
 assertFalse(isNull.apply(new Object()));
 }

 @Test
 public void testNotNull() {
 Predicate<Object> notNull = Predicates.notNull();
 assertNotNull(notNull.apply(1));
 assertNotNull(notNull.apply("Hello"));
 assertNotNull(notNull.apply(new Object()));

 assertFalse(notNull.apply(null));
 }

 @Test
 public void testNot() {
 Predicate<Boolean> isTrue = Predicates.alwaysTrue();
 Predicate<Boolean> isFalse = Predicates.alwaysFalse();

 Predicate<Boolean> notTrue = Predicates.not(isTrue);
 Predicate<Boolean> notFalse = Predicates.not(isFalse);

 assertTrue(notTrue.apply(false));
 assertTrue(notFalse.apply(true));
 }

 @Test
 public void testAnd() {
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer integer) {
 return integer % 2 == 0;
 }
 };

 Predicate<Integer> isPositive = Predicates.not(Predicates.isNull())
 .and(new Predicate<Integer>() {
 @Override
 public boolean apply(Integer integer) {
 return integer > 0;
 }
 });

 Predicate<Integer> isEvenAndPositive = Predicates.and(isEven, isPositive);

 List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

 for (Integer number : numbers) {
 assertTrue(isEvenAndPositive.apply(number));
 }

 assertFalse(isEvenAndPositive.apply(null));
 }

 @Test
 public void testOr() {
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer integer) {
 return integer % 2 == 0;
 }
 };

 Predicate<Integer> isPositive = Predicates.not(Predicates.isNull())
 .and(new Predicate<Integer>() {
 @Override
 public boolean apply(Integer integer) {
 return integer > 0;
 }
 });

 Predicate<Integer> isEvenOrPositive = Predicates.or(isEven, isPositive);

 List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

 for (Integer number : numbers) {
 assertTrue(isEvenOrPositive.apply(number));
 assertTrue(isEvenOrPositive.apply(null));
 }
 }

 @Test
 public void testEquals() {
 Predicate<String> isEqualToString = Predicates.equalTo("String");

 assertTrue(isEqualToString.apply("String"));
 assertFalse(isEqualToString.apply("Hello"));
 assertFalse(isEqualToString.apply(null));
 }

 @Test
 public void testInstanceOf() {
 Predicate<Object> isInstanceOfString = Predicates.instanceOf(String.class);

 assertTrue(isInstanceOfString.apply("Hello"));
 assertFalse(isInstanceOfString.apply(1));
 assertFalse(isInstanceOfString.apply(new Object()));
 }

 @Test
 public void testAssignableFrom() {
 Predicate<Class<?>> isAssignableFromString = Predicates.assignableFrom(String.class);

 assertTrue(isAssignableFromString.apply(String.class));
 assertTrue(isAssignableFromString.apply(Object.class));
 assertFalse(isAssignableFromString.apply(Integer.class));
 }

 @Test
 public void testIn() {
 List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
 Predicate<Integer> isInList = Predicates.in(numbers);

 assertTrue(isInList.apply(1));
 assertTrue(isInList.apply(2));
 assertTrue(isInList.apply(3));
 assertTrue(isInList.apply(4));
 assertTrue(isInList.apply(5));
 assertFalse(isInList.apply(6));
 assertFalse(isInList.apply(null));
 }

 @Test
 public void testCompose() {
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer integer) {
 return integer % 2 == 0;
 }
 };

 Function<Integer, Integer> doubleValue = new Function<Integer, Integer>() {
 @Override
 public Integer apply(Integer input) {
 return input * 2;
 }
 };

 Predicate<Integer> isEvenDouble = Predicates.compose(isEven, doubleValue);

 List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

 for (Integer number : numbers) {
 if (number % 2 != 0) {
 continue;
 }
 assertTrue(isEvenDouble.apply(number).equals(number * 2));
 }
 }

 @Test
 public void testContainsPattern() {
 Predicate<String> containsPattern = Predicates.containsPattern("Pattern");

 assertTrue(containsPattern.apply("Pattern"));
 assertFalse(containsPattern.apply("Hello"));
 assertFalse(containsPattern.apply(null));
 }
}