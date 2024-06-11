package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PredicatesTest {

 @Test
 public void testAlwaysTrue() {
 Predicate<Integer> alwaysTruePredicate = Predicates.alwaysTrue();
 assertTrue(alwaysTruePredicate.apply(null));
 assertTrue(alwaysTruePredicate.apply(0));
 assertTrue(alwaysTruePredicate.apply(1));
 }

 @Test
 public void testAlwaysFalse() {
 Predicate<Integer> alwaysFalsePredicate = Predicates.alwaysFalse();
 assertFalse(alwaysFalsePredicate.apply(null));
 assertFalse(alwaysFalsePredicate.apply(0));
 assertFalse(alwaysFalsePredicate.apply(1));
 }

 @Test
 public void testIsNull() {
 Predicate<Integer> isNullPredicate = Predicates.isNull();
 assertFalse(isNullPredicate.apply(null));
 assertFalse(isNullPredicate.apply(0));
 assertTrue(isNullPredicate.apply(null));
 }

 @Test
 public void testNotNull() {
 Predicate<Integer> notNullPredicate = Predicates.notNull();
 assertFalse(notNullPredicate.apply(null));
 assertTrue(notNullPredicate.apply(0));
 assertFalse(notNullPredicate.apply(null));
 }

 @Test
 public void testNot() {
 Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input % 2 == 0;
 }
 };

 Predicate<Integer> notEvenPredicate = Predicates.not(isEvenPredicate);

 assertFalse(notEvenPredicate.apply(0));
 assertTrue(notEvenPredicate.apply(1));
 assertFalse(notEvenPredicate.apply(2));
 }

 @Test
 public void testAnd() {
 Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input % 2 == 0;
 }
 };

 Predicate<Integer> isPositivePredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input > 0;
 }
 };

 Predicate<Integer> andPredicate = Predicates.and(isEvenPredicate, isPositivePredicate);

 assertFalse(andPredicate.apply(-1));
 assertFalse(andPredicate.apply(0));
 assertTrue(andPredicate.apply(2));
 }

 @Test
 public void testAndIterable() {
 Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input % 2 == 0;
 }
 };

 Predicate<Integer> isPositivePredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input > 0;
 }
 };

 List<Predicate<Integer>> predicatesList = Arrays.asList(isEvenPredicate, isPositivePredicate);
 Predicate<Integer> andPredicate = Predicates.and(predicatesList);

 assertFalse(andPredicate.apply(-1));
 assertFalse(andPredicate.apply(0));
 assertTrue(andPredicate.apply(2));
 }

 @Test
 public void testOr() {
 Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input % 2 == 0;
 }
 };

 Predicate<Integer> isOddPredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input % 2 != 0;
 }
 };

 Predicate<Integer> orPredicate = Predicates.or(isEvenPredicate, isOddPredicate);

 assertTrue(orPredicate.apply(0));
 assertTrue(orPredicate.apply(1));
 assertFalse(orPredicate.apply(2));
 }

 @Test
 public void testOrIterable() {
 Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input % 2 == 0;
 }
 };

 Predicate<Integer> isOddPredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input % 2 != 0;
 }
 };

 List<Predicate<Integer>> predicatesList = Arrays.asList(isEvenPredicate, isOddPredicate);
 Predicate<Integer> orPredicate = Predicates.or(predicatesList);

 assertTrue(orPredicate.apply(0));
 assertTrue(orPredicate.apply(1));
 assertFalse(orPredicate.apply(2));
 }

 @Test
 public void testEquals() {
 Predicate<String> isEqualPredicate = Predicates.equalTo("Test String");
 assertFalse(isEqualPredicate.apply(null));
 assertFalse(isEqualPredicate.apply("Different String"));
 assertTrue(isEqualPredicate.apply("Test String"));
 }

 @Test
 public void testInstanceOf() {
 Predicate<Object> isInstanceOfStringPredicate = Predicates.instanceOf(String.class);
 assertFalse(isInstanceOfStringPredicate.apply(null));
 assertFalse(isInstanceOfStringPredicate.apply(123));
 assertTrue(isInstanceOfStringPredicate.apply("Test String"));
 }

 @Test
 public void testAssignableFrom() {
 Predicate<Class<?>> isAssignableFromStringPredicate = Predicates.assignableFrom(String.class);
 assertFalse(isAssignableFromStringPredicate.apply(int.class));
 assertTrue(isAssignableFromStringPredicate.apply(StringBuilder.class));
 }

 @Test
 public void testIn() {
 Predicate<Integer> isInPredicate = Predicates.in(Arrays.asList(1, 2, 3));
 assertFalse(isInPredicate.apply(0));
 assertFalse(isInPredicate.apply(4));
 assertTrue(isInPredicate.apply(2));
 }

 @Test
 public void testCompose() {
 Predicate<String> startsWithPrefixPredicate = new Predicate<String>() {
 @Override
 public boolean apply(@Nullable String input) {
 return input.startsWith("prefix");
 }
 };

 Function<String, Integer> lengthFunction = new Function<String, Integer>() {
 @Override
 public Integer apply(@Nullable String input) {
 return input.length();
 }
 };

 Predicate<String> isLongerThanFiveCharPredicate = Predicates.compose(startsWithPrefixPredicate, lengthFunction);

 assertFalse(isLongerThanFiveCharPredicate.apply("prefix"));
 assertFalse(isLongerThanFiveCharPredicate.apply("prefixTest"));
 assertTrue(isLongerThanFiveCharPredicate.apply("prefixTestLongerString"));
 }

 @Test
 public void testContainsPattern() {
 Predicate<String> containsPatternPredicate = Predicates.containsPattern("pattern");
 assertFalse(containsPatternPredicate.apply(null));
 assertFalse(containsPatternPredicate.apply("Different String"));
 assertTrue(containsPatternPredicate.apply("Test String pattern"));
 }
}