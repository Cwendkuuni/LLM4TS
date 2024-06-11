package com.google.common.base;

import com.google.common.base.Predicates;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

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
 Object notNullObj = new Object();
 assertFalse(Predicates.isNull().apply(notNullObj));
 }

 @Test
 public void testNotNull() {
 Object obj = new Object();
 assertFalse(Predicates.notNull().apply(null));
 assertTrue(Predicates.notNull().apply(obj));
 }

 @Test
 public void testNot() {
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> isOdd = Predicates.not(isEven);

 List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
 int evenIndex = -1;
 int oddIndex = -1;

 for (int i = 0; i < numbers.size(); i++) {
 Integer number = numbers.get(i);
 if (isEven.apply(number)) {
 evenIndex = i;
 }
 if (isOdd.apply(number)) {
 oddIndex = i;
 }
 }

 assertEquals(-1, evenIndex);
 assertEquals(1, oddIndex);
 }

 @Test
 public void testAnd() {
 Predicate<String> isLongerThanThree = new Predicate<String>() {
 @Override
 public boolean apply(String input) {
 return input.length() > 3;
 }
 };
 Predicate<String> iscontainsLetterA = new Predicate<String>() {
 @Override
 public boolean apply(String input) {
 return input.contains("A");
 }
 };
 Predicate<String> andPredicate = Predicates.and(isLongerThanThree, iscontainsLetterA);
 List<String> strings = Arrays.asList("ABC", "DEF", "GHI");
 String result = null;
 for (String string : strings) {
 if (andPredicate.apply(string)) {
 result = string;
 break;
 }
 }
 assertEquals("ABC", result);
 }

 @Test
 public void testOr() {
 Predicate<String> isLongerThanThree = new Predicate<String>() {
 @Override
 public boolean apply(String input) {
 return input.length() > 3;
 }
 };
 Predicate<String> iscontainsLetterA = new Predicate<String>() {
 @Override
 public boolean apply(String input) {
 return input.contains("A");
 }
 };
 Predicate<String> orPredicate = Predicates.or(isLongerThanThree, iscontainsLetterA);
 List<String> strings = Arrays.asList("ABC", "DEF", "GHI", "AB");
 String result = null;
 for (String string : strings) {
 if (orPredicate.apply(string)) {
 result = string;
 break;
 }
 }
 assertEquals("ABC", result);
 assertEquals("AB", result);
 }

 @Test
 public void testEqualsTo() {
 Object obj1 = new Object();
 Object obj2 = obj1;
 Predicate<Object> equalToObj1 = Predicates.equalTo(obj1);
 assertTrue(equalToObj1.apply(obj1));
 assertTrue(equalToObj1.apply(obj2));

 Object obj3 = new Object();
 assertFalse(equalToObj1.apply(obj3));
 }

 @Test
 public void testInstanceOf() {
 Predicate<String> isString = Predicates.instanceOf(String.class);
 List<Object> objects = Arrays.asList("Hello", 123, new String("Hello"));
 String result = null;
 for (Object obj : objects) {
 if (isString.apply(obj)) {
 result = (String)obj;
 break;
 }
 }
 assertEquals("Hello", result);
 }

 @Test
 public void testAssignableFrom() {
 Predicate<Class<?>> isAssignableFromString = Predicates.assignableFrom(String.class);
 List<Class<?>> classes = Arrays.asList(String.class, Integer.class, Number.class);
 Class<?> result = null;
 for (Class<?> clazz : classes) {
 if (isAssignableFromString.apply(clazz)) {
 result = clazz;
 break;
 }
 }
 assertEquals(String.class, result);
 }

 @Test
 public void testIn() {
 Predicate<Integer> inPredicate = Predicates.in(Arrays.asList(1,2,3,4));
 List<Integer> numbers = Arrays.asList(1,2,3,4,5);
 int result = -1;
 for (Integer number : numbers) {
 if (inPredicate.apply(number)) {
 result = number.intValue();
 break;
 }
 }
 assertEquals(1, result.intValue());
 }

 @Test
 public void testCompose() {
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Function<Integer, Integer> addTwo = new Function<Integer, Integer>() {
 @Override
 public Integer apply(Integer input) {
 return input + 2;
 }
 };
 Predicate<Integer> composePredicate = Predicates.compose(isEven, addTwo);
 List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
 int result = -1;
 for (Integer number : numbers) {
 if (composePredicate.apply(number)) {
 result = number.intValue();
 break;
 }
 }
 assertEquals(4, result.intValue());
 }

 @Test
 public void testContainsPattern() {
 Predicate<String> containsPatternPredicate = Predicates.containsPattern(".*A.*");
 List<String> strings = Arrays.asList("ABC", "DEF", "GHI", "AB");
 String result = null;
 for (String string : strings) {
 if (containsPatternPredicate.apply(string)) {
 result = string;
 break;
 }
 }
 assertEquals("AB", result);
 }
}