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
 Predicate<Integer> alwaysTrue = Predicates.alwaysTrue();
 assertTrue(alwaysTrue.apply(null));
 assertTrue(alwaysTrue.apply(1));
 assertTrue(alwaysTrue.apply(2.5));
 assertTrue(alwaysTrue.apply("hello"));
 }

 @Test
 public void testAlwaysFalse() {
 Predicate<Integer> alwaysFalse = Predicates.alwaysFalse();
 assertFalse(alwaysFalse.apply(null));
 assertFalse(alwaysFalse.apply(1));
 assertFalse(alwaysFalse.apply(2.5));
 assertFalse(alwaysFalse.apply("hello"));
 }

 @Test
 public void testIsNull() {
 Predicate<Object> isNull = Predicates.isNull();
 assertNull(isNull.apply(null));
 assertNull(isNull.apply(null));
 assertNull(isNull.apply(null));
 assertNull(isNull.apply(null));
 }

 @Test
 public void testNotNull() {
 Predicate<Object> notNull = Predicates.notNull();
 assertNotNull(notNull.apply(new Object()));
 assertNotNull(notNull.apply(new String("test")));
 assertNotNull(notNull.apply(Arrays.asList(1, 2, 3)));
 assertNotNull(notNull.apply(new Pattern("test")));
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
 public boolean apply(@Nullable Integer t) {
 return t % 2 == 0;
 }
 };

 Predicate<Integer> isPositive = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer t) {
 return t > 0;
 }
 };

 Predicate<Integer> andPredicate = Predicates.and(isEven, isPositive);

 int[] evenPositiveNumbers = { 2, 4, 6, 8 };
 for (int i : evenPositiveNumbers) {
 assertTrue(andPredicate.apply(i));
 }

 int[] oddPositiveNumbers = { 1, 3, 5 };
 for (int i : oddPositiveNumbers) {
 assertFalse(andPredicate.apply(i));
 }
 }

 @Test
 public void testAndWithNulls() {
 Predicate<Object> isNull = Predicates.isNull();
 Predicate<Object> notNull = Predicates.notNull();
 Predicate<Object> andPredicate = Predicates.and(isNull, notNull);

 assertFalse(andPredicate.apply(null));
 assertFalse(andPredicate.apply(null));
 assertFalse(andPredicate.apply(null));
 assertFalse(andPredicate.apply(null));
 }

 @Test
 public void testAndWithDifferentTypes() {
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer t) {
 return t % 2 == 0;
 }
 };

 Predicate<String> containsString = new Predicate<String>() {
 @Override
 public boolean apply(@Nullable String s) {
 return s.contains("test");
 }
 };

 Predicate<Object> andPredicate = Predicates.and(isEven, containsString);

 String testString = "test test test";
 int testNumber = 4;

 assertTrue(andPredicate.apply(testNumber));
 assertFalse(andPredicate.apply(testString));
 }

 @Test
 public void testOr() {
 Predicate<Boolean> isTrue = Predicates.alwaysTrue();
 Predicate<Boolean> isFalse = Predicates.alwaysFalse();
 Predicate<Boolean> orPredicate = Predicates.or(isTrue, isFalse);

 assertTrue(orPredicate.apply(true));
 assertTrue(orPredicate.apply(false));
 }

 @Test
 public void testOrWithNulls() {
 Predicate<Object> isNull = Predicates.isNull();
 Predicate<Object> notNull = Predicates.notNull();
 Predicate<Object> orPredicate = Predicates.or(isNull, notNull);

 assertTrue(orPredicate.apply(null));
 assertTrue(orPredicate.apply(new Object()));
 }

 @Test
 public void testOrWithDifferentTypes() {
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer t) {
 return t % 2 == 0;
 }
 };

 Predicate<String> isLongerThanFiveChars = new Predicate<String>() {
 @Override
 public boolean apply(@Nullable String s) {
 return s.length() > 5;
 }
 };

 Predicate<Object> orPredicate = Predicates.or(isEven, isLongerThanFiveChars);

 int evenNumber = 4;
 String longerString = "testtesttesttesttest";

 assertTrue(orPredicate.apply(evenNumber));
 assertTrue(orPredicate.apply(longerString));
 }

 @Test
 public void testEqualsTo() {
 Predicate<Object> equalsToNull = Predicates.equalTo(null);
 Predicate<Object> notEqualsToNull = Predicates.not(Predicates.equalTo(null));
 Predicate<String> equalsToTest = Predicates.equalTo("test");
 Predicate<String> notEqualsToTest = Predicates.not(Predicates.equalTo("test"));

 assertFalse(equalsToNull.apply(new Object()));
 assertTrue(notEqualsToNull.apply(new Object()));
 assertFalse(equalsToNull.apply(null));
 assertTrue(notEqualsToNull.apply(null));
 assertFalse(equalsToNull.apply("test"));
 assertFalse(equalsToNull.apply(new String("test")));
 assertFalse(equalsToNull.apply(new StringBuilder("test")));
 assertTrue(notEqualsToNull.apply(new Object()));
 assertTrue(notEqualsToNull.apply(new String("test")));
 assertTrue(notEqualsToNull.apply(new StringBuilder("test")));
 assertTrue(equalsToTest.apply("test"));
 assertFalse(equalsToTest.apply("hello"));
 assertFalse(equalsToTest.apply("test test"));
 assertFalse(equalsToTest.apply(new Object()));
 assertFalse(equalsToTest.apply(new StringBuilder("test")));
 assertTrue(notEqualsToTest.apply("test"));
 assertTrue(notEqualsToTest.apply("hello"));
 assertFalse(notEqualsToTest.apply("test test"));
 assertTrue(notEqualsToTest.apply(new Object()));
 assertTrue(notEqualsToTest.apply(new StringBuilder("test")));
 }

 @Test
 public void testInstanceOf() {
 Predicate<Object> instanceOfString = Predicates.instanceOf(String.class);
 Predicate<Object> notInstanceOfString = Predicates.not(Predicates.instanceOf(String.class));

 assertTrue(instanceOfString.apply("test"));
 assertFalse(instanceOfString.apply(42));
 assertTrue(notInstanceOfString.apply("test"));
 assertFalse(notInstanceOfString.apply(42));
 assertFalse(instanceOfString.apply(new StringBuilder("test")));
 assertFalse(instanceOfString.apply(new StringBuilder()));
 }

 @Test
 public void testAssignableFrom() {
 Predicate<Class<?>> assignableFromString = Predicates.assignableFrom(String.class);
 Predicate<Class<?>> notAssignableFromString = Predicates.not(Predicates.assignableFrom(String.class));

 assertTrue(assignableFromString.apply(String.class));
 assertFalse(assignableFromString.apply(Integer.class));
 assertTrue(notAssignableFromString.apply(String.class));
 assertFalse(notAssignableFromString.apply(Integer.class));
 assertFalse(assignableFromString.apply(ArrayList.class));
 assertFalse(assignableFromString.apply(List.class));
 assertTrue(notAssignableFromString.apply(ArrayList.class));
 assertTrue(notAssignableFromString.apply(List.class));
 }

 @Test
 public void testIn() {
 Predicate<Integer> inList = Predicates.in(Arrays.asList(1, 2, 3));

 assertTrue(inList.apply(1));
 assertTrue(inList.apply(2));
 assertTrue(inList.apply(3));
 assertFalse(inList.apply(4));
 }

 @Test
 public void testCompose() {
 Predicate<Boolean> isTrue = Predicates.alwaysTrue();
 Predicate<Integer> isPositive = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer t) {
 return t > 0;
 }
 };
 Function<Integer, Boolean> isPositiveFunction = new Function<Integer, Boolean>() {
 @Override
 public Boolean apply(@Nullable Integer t) {
 return isPositive.apply(t);
 }
 };
 Predicate<Integer> composePredicate = Predicates.compose(isTrue, isPositiveFunction);

 assertTrue(composePredicate.apply(1));
 assertFalse(composePredicate.apply(-1));
 }

 @Test
 public void testContainsPattern() {
 Predicate<CharSequence> containsPattern = Predicates.containsPattern("test");

 assertTrue(containsPattern.apply("thetest"));
 assertTrue(containsPattern.apply("testtest"));
 assertTrue(containsPattern.apply("testtesttesttesttest"));
 assertFalse(containsPattern.apply("hello"));
 assertFalse(containsPattern.apply("testtesttesttest"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDefensiveCopyWhenArrayIsEmpty() {
 Predicate<Object> emptyPredicate = Predicates.alwaysTrue();
 List<Predicate<? super Object>> emptyList = new ArrayList<>();
 List<Predicate<? super Object>> defensiveCopy = Predicates.defensiveCopy(emptyPredicate);
 defensiveCopy.add(emptyPredicate);
 defensiveCopy.add(emptyPredicate);
 }

 @Test(expected = NullPointerException.class)
 public void testDefensiveCopyWhenElementIsNull() {
 Predicate<Object> emptyPredicate = Predicates.alwaysTrue();
 List<Predicate<? super Object>> list = new ArrayList<>();
 list.add(emptyPredicate);
 list.add(null);
 List<Predicate<? super Object>> defensiveCopy = Predicates.defensiveCopy(list);
 }
}