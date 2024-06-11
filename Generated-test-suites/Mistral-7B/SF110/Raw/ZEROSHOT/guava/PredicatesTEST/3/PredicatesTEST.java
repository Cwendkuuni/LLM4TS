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
 assertTrue(alwaysTrue.apply(2.0));
 assertTrue(alwaysTrue.apply("test"));
 }

 @Test
 public void testAlwaysFalse() {
 Predicate<Integer> alwaysFalse = Predicates.alwaysFalse();
 assertFalse(alwaysFalse.apply(null));
 assertFalse(alwaysFalse.apply(1));
 assertFalse(alwaysFalse.apply(2.0));
 assertFalse(alwaysFalse.apply("test"));
 }

 @Test
 public void testIsNull() {
 Predicate<Integer> isNull = Predicates.isNull();
 assertFalse(isNull.apply(1));
 assertNull(isNull.apply(null));
 assertFalse(isNull.apply(new Object()));
 }

 @Test
 public void testNotNull() {
 Predicate<Integer> notNull = Predicates.notNull();
 assertFalse(notNull.apply(null));
 assertTrue(notNull.apply(1));
 assertNotNull(notNull.apply(new Object()));
 }

 @Test
 public void testNot() {
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> notEven = Predicates.not(isEven);

 assertFalse(notEven.apply(1));
 assertFalse(notEven.apply(2));
 assertTrue(notEven.apply(3));
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
 Predicate<Integer> and = Predicates.and(isEven, isPositive);

 assertTrue(and.apply(2));
 assertTrue(and.apply(3));
 assertFalse(and.apply(0));
 assertFalse(and.apply(-1));
 }

 @Test
 public void testAndWithIterable() {
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
 Predicate<Integer> and = Predicates.and(Arrays.asList(isEven, isPositive));

 assertTrue(and.apply(2));
 assertTrue(and.apply(3));
 assertFalse(and.apply(0));
 assertFalse(and.apply(-1));
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
 Predicate<Integer> or = Predicates.or(isEven, isPositive);

 assertTrue(or.apply(2));
 assertTrue(or.apply(3));
 assertTrue(or.apply(1));
 }

 @Test
 public void testOrWithIterable() {
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
 Predicate<Integer> or = Predicates.or(Arrays.asList(isEven, isPositive));

 assertTrue(or.apply(2));
 assertTrue(or.apply(3));
 assertTrue(or.apply(1));
 }

 @Test
 public void testEqualTo() {
 Predicate<Integer> equalToFive = Predicates.equalTo(5);
 Predicate<Integer> equalToSeven = Predicates.equalTo(7);

 assertFalse(equalToFive.apply(1));
 assertFalse(equalToFive.apply(2));
 assertTrue(equalToFive.apply(5));
 assertFalse(equalToFive.apply(new Object()));

 assertFalse(equalToSeven.apply(1));
 assertFalse(equalToSeven.apply(2));
 assertFalse(equalToSeven.apply(5));
 assertFalse(equalToSeven.apply(new Object()));
 }

 @Test
 public void testInstanceOf() {
 Predicate<Object> instanceOfString = Predicates.instanceOf(String.class);

 assertFalse(instanceOfString.apply(1));
 assertFalse(instanceOfString.apply(2));
 assertFalse(instanceOfString.apply(3));
 assertTrue(instanceOfString.apply("test"));
 }

 @Test
 public void testAssignableFrom() {
 @SuppressWarnings("unchecked")
 Predicate<Class<?>> assignableFromList = Predicates.assignableFrom((Class)List.class);

 assertFalse(assignableFromList.apply(String.class));
 assertTrue(assignableFromList.apply(ArrayList.class));
 assertTrue(assignableFromList.apply(LinkedList.class));
 }

 @Test
 public void testIn() {
 List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
 Predicate<Integer> inNumbers = Predicates.in(numbers);

 assertFalse(inNumbers.apply(0));
 assertFalse(inNumbers.apply(-1));
 assertTrue(inNumbers.apply(1));
 assertTrue(inNumbers.apply(2));
 assertTrue(inNumbers.apply(3));
 assertTrue(inNumbers.apply(4));
 assertTrue(inNumbers.apply(5));
 }

 @Test
 public void testCompose() {
 Predicate<String> startsWithX = new Predicate<String>() {
 @Override
 public boolean apply(@Nullable String input) {
 return input != null && input.startsWith("x");
 }
 };
 Function<String, Integer> stringToInt = new Function<String, Integer>() {
 @Override
 public Integer apply(@Nullable String input) {
 return input.length();
 }
 };
 Predicate<String> compose = Predicates.compose(startsWithX, stringToInt);

 assertFalse(compose.apply("test"));
 assertFalse(compose.apply(""));
 assertFalse(compose.apply("abc"));
 assertTrue(compose.apply("x"));
 assertTrue(compose.apply("xx"));
 assertTrue(compose.apply("xxx"));
 }

 @Test
 public void testContainsPattern() {
 Predicate<String> containsPattern = Predicates.containsPattern("x%");

 assertFalse(containsPattern.apply("test"));
 assertFalse(containsPattern.apply(""));
 assertFalse(containsPattern.apply("abc"));
 assertTrue(containsPattern.apply("xabcx"));
 assertTrue(containsPattern.apply("testx"));
 }

 @Test
 public void testContainsPatternFromString() {
 Predicate<String> containsPattern = Predicates.containsPattern("x%");

 assertFalse(containsPattern.apply("test"));
 assertFalse(containsPattern.apply(""));
 assertFalse(containsPattern.apply("abc"));
 assertTrue(containsPattern.apply("xabcx"));
 assertTrue(containsPattern.apply("testx"));
 }
}