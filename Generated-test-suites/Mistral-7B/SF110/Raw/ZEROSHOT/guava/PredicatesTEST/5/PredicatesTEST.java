package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PredicatesTest {

 @Test
 public void testAlwaysTrue() {
 Predicate<Integer> alwaysTruePredicate = Predicates.alwaysTrue();
 assertTrue(alwaysTruePredicate.apply(null));
 assertTrue(alwaysTruePredicate.apply(1));
 assertTrue(alwaysTruePredicate.apply(2));
 }

 @Test
 public void testAlwaysFalse() {
 Predicate<Integer> alwaysFalsePredicate = Predicates.alwaysFalse();
 assertFalse(alwaysFalsePredicate.apply(null));
 assertFalse(alwaysFalsePredicate.apply(1));
 assertFalse(alwaysFalsePredicate.apply(2));
 }

 @Test
 public void testIsNull() {
 Predicate<Integer> isNullPredicate = Predicates.isNull();
 assertFalse(isNullPredicate.apply(null));
 assertFalse(isNullPredicate.apply(1));
 assertTrue(isNullPredicate.apply(null));
 }

 @Test
 public void testNotNull() {
 Predicate<Integer> notNullPredicate = Predicates.notNull();
 assertFalse(notNullPredicate.apply(null));
 assertTrue(notNullPredicate.apply(1));
 assertTrue(notNullPredicate.apply(2));
 }

 @Test
 public void testNot() {
 Predicate<Integer> predicate = Predicates.alwaysTrue();
 Predicate<Integer> notPredicate = Predicates.not(predicate);
 assertFalse(notPredicate.apply(1));
 assertTrue(notPredicate.apply(false));
 }

 @Test
 public void testAnd() {
 Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> isPositivePredicate = Predicates.not(Predicates.isNull());
 Predicate<Integer> andPredicate = Predicates.and(isEvenPredicate, isPositivePredicate);

 int[] testCases = { 2, 4, 0, -1 };
 for (int testCase : testCases) {
 assertTrue(andPredicate.apply(testCase));
 }
 }

 @Test
 public void testOr() {
 Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> isPositivePredicate = Predicates.not(Predicates.isNull());
 Predicate<Integer> orPredicate = Predicates.or(isEvenPredicate, isPositivePredicate);

 int[] testCases = { null, 2, 4, 1 };
 for (int testCase : testCases) {
 assertTrue(orPredicate.apply(testCase) || orPredicate.apply(null));
 }
 }

 @Test
 public void testEqualTo() {
 Predicate<Integer> equalsPredicateForOne = Predicates.equalTo(1);
 Predicate<Integer> equalsPredicateForTwo = Predicates.equalTo(2);

 Predicate<Integer> notEqualsPredicateForOne = Predicates.not(Predicates.equalTo(1));
 Predicate<Integer> notEqualsPredicateForTwo = Predicates.not(Predicates.equalTo(2));

 List<Predicate<Integer>> testCases = Arrays.asList(
 Predicates.and(equalsPredicateForOne, notEqualsPredicateForTwo),
 Predicates.and(notEqualsPredicateForOne, equalsPredicateForTwo),
 Predicates.or(equalsPredicateForOne, equalsPredicateForTwo)
 );

 for (Predicate<Integer> predicate : testCases) {
 int testCase = 1;
 assertFalse(predicate.apply(testCase));
 testCase = 2;
 assertTrue(predicate.apply(testCase));
 }
 }

 @Test
 public void testIn() {
 List<Integer> list = Arrays.asList(1, 2, 3);
 Predicate<Integer> inPredicate = Predicates.in(list);

 int[] testCases = { null, 1, 2, 3, 4 };
 for (int testCase : testCases) {
 if (list.contains(testCase)) {
 assertTrue(inPredicate.apply(testCase));
 } else {
 assertFalse(inPredicate.apply(testCase));
 }
 }
 }

 @Test
 public void testInstanceOf() {
 @SuppressWarnings("unchecked")
 Predicate<Object> instanceOfStringPredicate = Predicates.instanceOf(String.class);
 assertTrue(instanceOfStringPredicate.apply(""));
 assertFalse(instanceOfStringPredicate.apply(new Object()));
 }

 @Test
 public void testAssignableFrom() {
 @SuppressWarnings("unchecked")
 Predicate<Class<?>> assignableFromStringPredicate = Predicates.assignableFrom(String.class);
 assertTrue(assignableFromStringPredicate.apply(StringBuffer.class));
 assertFalse(assignableFromStringPredicate.apply(Integer.class));
 }

 @Test
 public void testCompose() {
 Function<Integer, Integer> squareFunction = new Function<Integer, Integer>() {
 @Override
 public Integer apply(@Nullable Integer input) {
 return input * input;
 }
 };
 Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> composedPredicate = Predicates.compose(isEvenPredicate, squareFunction);

 int[] testCases = { 1, 2, 3, 4 };
 for (int testCase : testCases) {
 assertTrue(composedPredicate.apply(testCase) || composedPredicate.apply(null));
 }
 }

 @Test
 public void testContainsPattern() {
 Predicate<String> containsPatternPredicate = Predicates.containsPattern("ab*");

 String[] testCases = { "apple", "banana", "abacus", "application" };
 for (String testCase : testCases) {
 assertTrue(containsPatternPredicate.apply(testCase));
 }
 }
}