package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

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
 Object obj = null;
 assertTrue(Predicates.isNull().apply(obj));

 Object nonNullObj = new Object();
 assertFalse(Predicates.isNull().apply(nonNullObj));
 }

 @Test
 public void testNotNull() {
 Object obj = new Object();
 assertFalse(Predicates.notNull().apply(null));

 Object nonNullObj = new Object();
 assertTrue(Predicates.notNull().apply(nonNullObj));
 }

 @Test
 public void testNot() {
 Predicate<Object> isNull = Predicates.isNull();
 Predicate<Object> notNull = Predicates.notNull();

 Predicate<Object> notIsNull = Predicates.not(isNull);
 Predicate<Object> notNotnull = Predicates.not(notNull);

 Object obj = new Object();

 assertTrue(notIsNull.apply(obj));
 assertFalse(notNotnull.apply(obj));
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

 Predicate<Integer> andPredicate = Predicates.and(isEven, isPositive);

 List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);

 for (Integer number : numbers) {
 assertTrue(andPredicate.apply(number));
 }

 assertFalse(andPredicate.apply(-1));
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

 Predicate<Integer> orPredicate = Predicates.or(isEven, isOdd);

 List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);

 for (Integer number : numbers) {
 assertTrue(orPredicate.apply(number));
 }
 }

 @Test
 public void testEqualTo() {
 Object obj1 = new Object();
 Object obj2 = obj1;

 assertTrue(Predicates.equalTo(obj1).apply(obj1));
 assertTrue(Predicates.equalTo(obj1).apply(obj2));

 Object obj3 = new Object();
 assertFalse(Predicates.equalTo(obj1).apply(obj3));
 }

 @Test
 public void testInstanceOf() {
 Class<String> stringClass = String.class;
 String str = "test";

 assertTrue(Predicates.instanceOf(stringClass).apply(str));

 List<Object> list = Arrays.asList(1, 2.0, "test", new Object());

 for (Object obj : list) {
 if (obj instanceof String) {
 assertTrue(Predicates.instanceOf(stringClass).apply(obj));
 } else {
 assertFalse(Predicates.instanceOf(stringClass).apply(obj));
 }
 }
 }

 @Test
 public void testAssignableFrom() {
 Class<String> stringClass = String.class;
 Class<Number> numberClass = Number.class;

 assertTrue(Predicates.assignableFrom(numberClass).apply(stringClass));

 Class<List<String>> listStringClass = List.class;
 Class<List<Integer>> listIntegerClass = List.class;

 assertFalse(Predicates.assignableFrom(listStringClass).apply(listIntegerClass));
 }

 @Test
 public void testIn() {
 Collection<Integer> numbers = new HashSet<Integer>();
 numbers.add(1);
 numbers.add(2);
 numbers.add(3);

 Predicate<Integer> inPredicate = Predicates.in(numbers);

 List<Integer> testNumbers = Arrays.asList(1, 2, 3, 4, 5);

 for (Integer number : testNumbers) {
 assertTrue(inPredicate.apply(number));
 }

 assertFalse(inPredicate.apply(4));
 }

 @Test
 public void testCompose() {
 Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
 @Override
 public Integer apply(Integer input) {
 return input + 1;
 }
 };

 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };

 Predicate<Integer> isEvenPlusOne = Predicates.compose(isEven, addOne);

 List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);

 for (Integer number : numbers) {
 if (number % 2 == 0) {
 assertTrue(isEvenPlusOne.apply(number));
 } else {
 assertFalse(isEvenPlusOne.apply(number));
 }
 }
 }

 @Test
 public void testContainsPattern() {
 String pattern = "test";

 Predicate<String> containsPatternPredicate = Predicates.containsPattern(Pattern.compile(pattern));

 List<String> strings = Arrays.asList("test1", "test2", "test3", "test4");

 for (String str : strings) {
 assertTrue(containsPatternPredicate.apply(str));
 }

 assertFalse(containsPatternPredicate.apply("test5"));
 }
}