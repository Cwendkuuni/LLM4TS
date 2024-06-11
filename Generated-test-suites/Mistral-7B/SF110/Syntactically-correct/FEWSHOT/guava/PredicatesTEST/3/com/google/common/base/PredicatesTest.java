package com.google.common.base;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class PredicatesTest {

 @Test
 public void testAlwaysTrue() {
 Predicate<Object> alwaysTrue = Predicates.alwaysTrue();
 assertTrue(alwaysTrue.apply(null));
 assertTrue(alwaysTrue.apply(new Object()));
 }

 @Test
 public void testAlwaysFalse() {
 Predicate<Object> alwaysFalse = Predicates.alwaysFalse();
 assertFalse(alwaysFalse.apply(null));
 assertFalse(alwaysFalse.apply(new Object()));
 }

 @Test
 public void testIsNull() {
 Predicate<Object> isNull = Predicates.isNull();
 assertNull(isNull.apply(null));
 assertFalse(isNull.apply(new Object()));
 }

 @Test
 public void testNotNull() {
 Predicate<Object> notNull = Predicates.notNull();
 assertNull(notNull.apply(null));
 assertTrue(notNull.apply(new Object()));
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
 Predicate<Integer> isZero = Predicates.alwaysFalse();
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer integer) {
 return integer != null && integer % 2 == 0;
 }
 };
 Predicate<Integer> andPredicate = Predicates.and(isZero, isEven);
 assertFalse(andPredicate.apply(null));
 assertFalse(andPredicate.apply(-1));
 assertFalse(andPredicate.apply(1));
 assertFalse(andPredicate.apply(3));
 assertTrue(andPredicate.apply(0));
 assertTrue(andPredicate.apply(2));
 }

 @Test
 public void testAndWithPredicatesArray() {
 Predicate<Integer> isZero = Predicates.alwaysFalse();
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer integer) {
 return integer != null && integer % 2 == 0;
 }
 };
 Predicate<Integer> andPredicate = Predicates.and(isZero, isEven);
 assertFalse(andPredicate.apply(null));
 assertFalse(andPredicate.apply(-1));
 assertFalse(andPredicate.apply(1));
 assertFalse(andPredicate.apply(3));
 assertTrue(andPredicate.apply(0));
 assertTrue(andPredicate.apply(2));
 }

 @Test
 public void testAndWithPredicatesIterable() {
 Predicate<Integer> isZero = Predicates.alwaysFalse();
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer integer) {
 return integer != null && integer % 2 == 0;
 }
 };
 List<Predicate<Integer>> predicates = Arrays.asList(isZero, isEven);
 Predicate<Integer> andPredicate = Predicates.and(predicates);
 assertFalse(andPredicate.apply(null));
 assertFalse(andPredicate.apply(-1));
 assertFalse(andPredicate.apply(1));
 assertFalse(andPredicate.apply(3));
 assertTrue(andPredicate.apply(0));
 assertTrue(andPredicate.apply(2));
 }

 @Test
 public void testOr() {
 Predicate<Integer> isZero = Predicates.alwaysFalse();
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer integer) {
 return integer != null && integer % 2 == 0;
 }
 };
 Predicate<Integer> orPredicate = Predicates.or(isZero, isEven);
 assertFalse(orPredicate.apply(null));
 assertFalse(orPredicate.apply(-1));
 assertFalse(orPredicate.apply(0));
 assertTrue(orPredicate.apply(1));
 assertTrue(orPredicate.apply(2));
 assertTrue(orPredicate.apply(3));
 }

 @Test
 public void testOrWithPredicatesArray() {
 Predicate<Integer> isZero = Predicates.alwaysFalse();
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer integer) {
 return integer != null && integer % 2 == 0;
 }
 };
 Predicate<Integer> orPredicate = Predicates.or(isZero, isEven);
 assertFalse(orPredicate.apply(null));
 assertFalse(orPredicate.apply(-1));
 assertFalse(orPredicate.apply(0));
 assertTrue(orPredicate.apply(1));
 assertTrue(orPredicate.apply(2));
 assertTrue(orPredicate.apply(3));
 }

 @Test
 public void testOrWithPredicatesIterable() {
 Predicate<Integer> isZero = Predicates.alwaysFalse();
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer integer) {
 return integer != null && integer % 2 == 0;
 }
 };
 List<Predicate<Integer>> predicates = Arrays.asList(isZero, isEven);
 Predicate<Integer> orPredicate = Predicates.or(predicates);
 assertFalse(orPredicate.apply(null));
 assertFalse(orPredicate.apply(-1));
 assertFalse(orPredicate.apply(0));
 assertTrue(orPredicate.apply(1));
 assertTrue(orPredicate.apply(2));
 assertTrue(orPredicate.apply(3));
 }

 @Test
 public void testEqualTo() {
 Predicate<String> isStringEqualToTarget = Predicates.equalTo("target");
 assertFalse(isStringEqualToTarget.apply(null));
 assertFalse(isStringEqualToTarget.apply("different"));
 assertTrue(isStringEqualToTarget.apply("target"));
 }

 @Test
 public void testInstanceOf() {
 Predicate<String> isInstanceOfString = Predicates.instanceOf(String.class);
 assertFalse(isInstanceOfString.apply(null));
 assertFalse(isInstanceOfString.apply(1));
 assertTrue(isInstanceOfString.apply(""));
 }

 @Test
 public void testAssignableFrom() {
 Predicate<Class<?>> isAssignableFromString = Predicates.assignableFrom(String.class);
 assertFalse(isAssignableFromString.apply(Integer.class));
 assertTrue(isAssignableFromString.apply(StringBuilder.class));
 }

 @Test
 public void testIn() {
 Predicate<Integer> isInList = Predicates.in(Arrays.asList(1, 2, 3));
 assertFalse(isInList.apply(null));
 assertFalse(isInList.apply(0));
 assertTrue(isInList.apply(1));
 assertTrue(isInList.apply(2));
 assertTrue(isInList.apply(3));
 }

 @Test
 public void testCompose() {
 Predicate<String> isEmptyString = Predicates.alwaysFalse();
 Function<String, Integer> stringToLength = new Function<String, Integer>() {
 @Override
 public Integer apply(@Nullable String input) {
 return (input == null) ? null : input.length();
 }
 };
 Predicate<String> stringIsEmpty = Predicates.compose(Predicates.alwaysFalse(), stringToLength);
 assertFalse(stringIsEmpty.apply(null));
 assertFalse(stringIsEmpty.apply(""));
 assertTrue(stringIsEmpty.apply(" "));
 assertTrue(stringIsEmpty.apply("abc"));
 }
}