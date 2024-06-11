package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class PredicatesTest {

 @Test
 public void testAlwaysTrue() {
 Object o = new Object();
 assertTrue(Predicates.alwaysTrue().apply(o));
 }

 @Test
 public void testAlwaysFalse() {
 Object o = new Object();
 assertFalse(Predicates.alwaysFalse().apply(o));
 }

 @Test
 public void testIsNull() {
 assertNull(Predicates.IsNull().apply(null));
 }

 @Test(expected = NullPointerException.class)
 public void testIsNullWithNonnullObject() {
 Object o = new Object();
 assertNull(Predicates.IsNull().apply(o));
 }

 @Test
 public void testNotNull() {
 Object o = new Object();
 assertNotNull(Predicates.notNull().apply(o));
 }

 @Test(expected = NullPointerException.class)
 public void testNotNullWithNullObject() {
 Object o = null;
 assertNull(Predicates.notNull().apply(o));
 }

 @Test
 public void testNot() {
 Predicate<Boolean> predicate = Predicates.alwaysTrue();
 Predicate<Boolean> notPredicate = Predicates.not(predicate);
 assertFalse(notPredicate.apply(true));
 assertTrue(notPredicate.apply(false));
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
 List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
 for (Integer number : numbers) {
 assertTrue(andPredicate.apply(number));
 }
 }

 @Test
 public void testAndWithNullComponent() {
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> nullPredicate = null;
 try {
 Predicate<Integer> andPredicate = Predicates.and(isEven, nullPredicate);
 fail("NullPointerException expected");
 } catch (NullPointerException e) {
 // Pass
 }
 }

 @Test
 public void testAndWithInvalidComponentType() {
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<String> stringPredicate = new Predicate<String>() {
 @Override
 public boolean apply(String input) {
 return input.length() > 5;
 }
 };
 try {
 Predicate<Integer> andPredicate = Predicates.and(isEven, stringPredicate);
 fail("ClassCastException expected");
 } catch (ClassCastException e) {
 // Pass
 }
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
 List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
 for (Integer number : numbers) {
 assertTrue(orPredicate.apply(number));
 }
 }

 @Test
 public void testOrWithNullComponent() {
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> nullPredicate = null;
 try {
 Predicate<Integer> orPredicate = Predicates.or(isEven, nullPredicate);
 fail("NullPointerException expected");
 } catch (NullPointerException e) {
 // Pass
 }
 }

 @Test
 public void testOrWithInvalidComponentType() {
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<String> stringPredicate = new Predicate<String>() {
 @Override
 public boolean apply(String input) {
 return input.length() > 5;
 }
 };
 try {
 Predicate<Integer> orPredicate = Predicates.or(isEven, stringPredicate);
 fail("ClassCastException expected");
 } catch (ClassCastException e) {
 // Pass
 }
 }

 @Test
 public void testEquals() {
 String target = "test";
 Predicate<String> predicate = Predicates.equalTo(target);
 List<String> strings = Arrays.asList("test", "test1", "test2");
 for (String string : strings) {
 assertTrue(predicate.apply(string));
 }
 String nonMatch = "test3";
 assertFalse(predicate.apply(nonMatch));
 }

 @Test
 public void testInstanceOf() {
 @SuppressWarnings("unchecked")
 Class<String> clazz = (Class<String>)String.class;
 Predicate<Object> predicate = Predicates.instanceOf(clazz);
 List<Object> objects = Arrays.asList(new String("test"), new Integer(1), new Double(3.14));
 for (Object obj : objects) {
 if (obj instanceof String) {
 assertTrue(predicate.apply(obj));
 } else {
 assertFalse(predicate.apply(obj));
 }
 }
 }

 @Test
 public void testAssignableFrom() {
 @SuppressWarnings("unchecked")
 Class<Number> clazz = (Class<Number>)Number.class;
 Predicate<Class<?>> predicate = Predicates.assignableFrom(clazz);
 List<Class<?>> classes = Arrays.asList(Integer.class, Double.class, String.class);
 for (Class<?> clazz1 : classes) {
 if (clazz.isAssignableFrom(clazz1)) {
 assertTrue(predicate.apply(clazz1));
 } else {
 assertFalse(predicate.apply(clazz1));
 }
 }
 }

 @Test
 public void testIn() {
 List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
 Predicate<Integer> predicate = Predicates.in(numbers);
 List<Integer> numbersToTest = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
 for (Integer number : numbersToTest) {
 if (numbers.contains(number)) {
 assertTrue(predicate.apply(number));
 } else {
 assertFalse(predicate.apply(number));
 }
 }
 }

 @Test
 public void testCompose() {
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Function<Integer, Integer> squareFunction = new Function<Integer, Integer>() {
 @Override
 public Integer apply(Integer input) {
 return input * input;
 }
 };
 Predicate<Integer> composedPredicate = Predicates.compose(isEven, squareFunction);
 List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
 for (Integer number : numbers) {
 if (number % 2 == 0) {
 Integer square = number * number;
 assertTrue(composedPredicate.apply(number));
 assertEqual(square, composedPredicate.apply(number));
 } else {
 assertFalse(composedPredicate.apply(number));
 }
 }
 }

 @Test
 public void testContainsPattern() {
 String pattern = "test";
 Predicate<String> predicate = Predicates.containsPattern(Pattern.compile(pattern));
 List<String> strings = Arrays.asList("test", "test1", "test2", "testtest");
 for (String string : strings) {
 assertTrue(predicate.apply(string));
 }
 String nonMatch = "test123";
 assertFalse(predicate.apply(nonMatch));
 }

 @Test
 public void testContainsPatternFromString() {
 String pattern = "test";
 Predicate<String> predicate = Predicates.containsPattern(Pattern.compile(pattern));
 List<String> strings = Arrays.asList("test", "test1", "test2", "testtest");
 for (String string : strings) {
 assertTrue(predicate.apply(string));
 }
 String nonMatch = "test123";
 assertFalse(predicate.apply(nonMatch));
 }
}