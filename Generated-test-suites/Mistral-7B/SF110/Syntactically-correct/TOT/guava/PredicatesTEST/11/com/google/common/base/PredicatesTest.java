package com.google.common.base;

import com.google.common.base.Predicates;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PredicatesTest {

 @Test
 public void testAlwaysTrue() {
 Object testObject = new Object();
 assertTrue(Predicates.alwaysTrue().apply(testObject));
 }

 @Test
 public void testAlwaysFalse() {
 Object testObject = new Object();
 assertFalse(Predicates.alwaysFalse().apply(testObject));
 }

 @Test
 public void testIsNull() {
 Object nullObject = null;
 assertTrue(Predicates.isNull().apply(nullObject));

 Object nonNullObject = new Object();
 assertFalse(Predicates.isNull().apply(nonNullObject));
 }

 @Test
 public void testNotNull() {
 Object nullObject = null;
 assertFalse(Predicates.notNull().apply(nullObject));

 Object nonNullObject = new Object();
 assertTrue(Predicates.notNull().apply(nonNullObject));
 }

 @Test
 public void testNot() {
 Predicate<Boolean> truePredicate = Predicates.alwaysTrue();
 Predicate<Boolean> falsePredicate = Predicates.alwaysFalse();

 Predicate<Boolean> notTrue = Predicates.not(truePredicate);
 Predicate<Boolean> notFalse = Predicates.not(falsePredicate);

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

 Predicate<Integer> isPositive = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer integer) {
 return integer > 0;
 }
 };

 Predicate<Integer> andPredicate = Predicates.and(isEven, isPositive);
 int evenPositiveNumber = 10;
 int oddPositiveNumber = 5;
 int evenNegativeNumber = -10;
 int oddNegativeNumber = -5;

 assertTrue(andPredicate.apply(evenPositiveNumber));
 assertFalse(andPredicate.apply(oddPositiveNumber));
 assertFalse(andPredicate.apply(evenNegativeNumber));
 assertFalse(andPredicate.apply(oddNegativeNumber));
 }

 @Test
 public void testAndIterable() {
 Predicate<Integer> isPositive = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer integer) {
 return integer > 0;
 }
 };

 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer integer) {
 return integer % 2 == 0;
 }
 };

 Predicate<Integer> andPredicate = Predicates.and(Arrays.asList(isPositive, isEven));
 int evenPositiveNumber = 10;
 int oddPositiveNumber = 5;
 int evenNegativeNumber = -10;
 int oddNegativeNumber = -5;

 assertTrue(andPredicate.apply(evenPositiveNumber));
 assertFalse(andPredicate.apply(oddPositiveNumber));
 assertFalse(andPredicate.apply(evenNegativeNumber));
 assertFalse(andPredicate.apply(oddNegativeNumber));
 }

 @Test
 public void testAndArray() {
 Predicate<Integer> isPositive = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer integer) {
 return integer > 0;
 }
 };

 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer integer) {
 return integer % 2 == 0;
 }
 };

 Predicate<Integer> andPredicate = Predicates.and(isPositive, isEven);
 int evenPositiveNumber = 10;
 int oddPositiveNumber = 5;
 int evenNegativeNumber = -10;
 int oddNegativeNumber = -5;

 assertTrue(andPredicate.apply(evenPositiveNumber));
 assertFalse(andPredicate.apply(oddPositiveNumber));
 assertFalse(andPredicate.apply(evenNegativeNumber));
 assertFalse(andPredicate.apply(oddNegativeNumber));
 }

 @Test
 public void testOr() {
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer integer) {
 return integer % 2 == 0;
 }
 };

 Predicate<Integer> isOdd = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer integer) {
 return integer % 2 != 0;
 }
 };

 Predicate<Integer> orPredicate = Predicates.or(isEven, isOdd);
 int evenNumber = 10;
 int oddNumber = 5;

 assertTrue(orPredicate.apply(evenNumber));
 assertTrue(orPredicate.apply(oddNumber));
 }

 @Test
 public void testOrIterable() {
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer integer) {
 return integer % 2 == 0;
 }
 };

 Predicate<Integer> isOdd = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer integer) {
 return integer % 2 != 0;
 }
 };

 List<Predicate<Integer>> predicates = Arrays.asList(isEven, isOdd);
 Predicate<Integer> orPredicate = Predicates.or(predicates);
 int evenNumber = 10;
 int oddNumber = 5;

 assertTrue(orPredicate.apply(evenNumber));
 assertTrue(orPredicate.apply(oddNumber));
 }

 @Test
 public void testOrArray() {
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer integer) {
 return integer % 2 == 0;
 }
 };

 Predicate<Integer> isOdd = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer integer) {
 return integer % 2 != 0;
 }
 };

 Predicate<Integer> orPredicate = Predicates.or(isEven, isOdd
);
 int evenNumber = 10;
 int oddNumber = 5;

 assertTrue(orPredicate.apply(evenNumber));
 assertTrue(orPredicate.apply(oddNumber));
 }

 @Test
 public void testEqualToNull() {
 Predicate<Object> equalToNullPredicate = Predicates.equalTo(null);
 Object nullObject = null;
 assertTrue(equalToNullPredicate.apply(nullObject));
 }

 @Test
 public void testEqualToNotNull() {
 Predicate<Object> equalToNotNullPredicate = Predicates.equalTo(new Object());
 Object nonNullObject = new Object();
 assertTrue(equalToNotNullPredicate.apply(nonNullObject));
 }

 @Test
 public void testContainsPattern() {
 Predicate<CharSequence> containsPatternPredicate = Predicates.containsPattern("ab");
 CharSequence testString1 = "abcd";
 CharSequence testString2 = "efgh";

 assertTrue(containsPatternPredicate.apply(testString1));
 assertFalse(containsPatternPredicate.apply(testString2));
 }

 @Test(expected = AssertionError.class)
 public void testInstanceOfNull() {
 Predicate<Object> instanceOfNullPredicate = Predicates.instanceOf(null.getClass());
 Object nullObject = null;
 assertTrue(instanceOfNullPredicate.apply(nullObject));
 }

 @Test
 public void testInstanceOfString() {
 Predicate<Object> instanceOfStringPredicate = Predicates.instanceOf(String.class);
 String testString = "test";
 assertTrue(instanceOfStringPredicate.apply(testString));
 }

 @Test
 public void testAssignableFromString() {
 Predicate<Class<?>> assignableFromStringPredicate = Predicates.assignableFrom(String.class);
 Class<String> stringClass = String.class;
 assertTrue(assignableFromStringPredicate.apply(stringClass));
 }
}