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
 assertTrue(alwaysTruePredicate.apply(1));
 assertTrue(alwaysTruePredicate.apply(2));
 assertTrue(alwaysTruePredicate.apply(3));
 }

 @Test
 public void testAlwaysFalse() {
 Predicate<Integer> alwaysFalsePredicate = Predicates.alwaysFalse();
 assertFalse(alwaysFalsePredicate.apply(1));
 assertFalse(alwaysFalsePredicate.apply(2));
 assertFalse(alwaysFalsePredicate.apply(3));
 }

 @Test
 public void testIsNull() {
 Predicate<Integer> isNullPredicate = Predicates.isNull();
 assertNull(isNullPredicate.apply(null));
 assertFalse(isNullPredicate.apply(1));
 assertFalse(isNullPredicate.apply(2));
 assertFalse(isNullPredicate.apply(3));
 }

 @Test
 public void testNotNull() {
 Predicate<Integer> notNullPredicate = Predicates.notNull();
 assertNull(notNullPredicate.apply(null));
 assertTrue(notNullPredicate.apply(1));
 assertTrue(notNullPredicate.apply(2));
 assertTrue(notNullPredicate.apply(3));
 }

 @Test
 public void testNot() {
 Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> notEvenPredicate = Predicates.not(isEvenPredicate);

 assertFalse(notEvenPredicate.apply(2));
 assertTrue(notEvenPredicate.apply(1));
 assertTrue(notEvenPredicate.apply(3));
 }

 @Test
 public void testAnd() {
 Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> isPositivePredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input > 0;
 }
 };
 Predicate<Integer> evenAndPositivePredicate = Predicates.and(isEvenPredicate, isPositivePredicate);

 assertTrue(evenAndPositivePredicate.apply(2));
 assertTrue(evenAndPositivePredicate.apply(4));
 assertFalse(evenAndPositivePredicate.apply(0));
 assertFalse(evenAndPositivePredicate.apply(-1));
 }

 @Test
 public void testOr() {
 Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> isOddPredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 != 0;
 }
 };
 Predicate<Integer> evenOrOddPredicate = Predicates.or(isEvenPredicate, isOddPredicate);

 assertTrue(evenOrOddPredicate.apply(1));
 assertTrue(evenOrOddPredicate.apply(2));
 assertTrue(evenOrOddPredicate.apply(3));
 }

 @Test
 public void testEqualTo() {
 Predicate<Integer> isEqualToThreePredicate = Predicates.equalTo(3);
 assertTrue(isEqualToThreePredicate.apply(3));
 assertFalse(isEqualToThreePredicate.apply(1));
 assertFalse(isEqualToThreePredicate.apply(2));
 }

 @Test
 public void testInstanceOf() {
 Predicate<Object> isInstanceOfStringPredicate = Predicates.instanceOf(String.class);
 assertTrue(isInstanceOfStringPredicate.apply("Hello"));
 assertFalse(isInstanceOfStringPredicate.apply(1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testInstanceOfIllegalArgumentException() {
 Predicate<Object> isInstanceOfNullPredicate = Predicates.instanceOf(null);
 }

 @Test
 public void testAssignableFrom() {
 Predicate<Class<?>> isAssignableFromNumberPredicate = Predicates.assignableFrom(Number.class);
 assertTrue(isAssignableFromNumberPredicate.apply(Integer.class));
 assertTrue(isAssignableFromNumberPredicate.apply(Double.class));
 assertFalse(isAssignableFromNumberPredicate.apply(String.class));
 }

 @Test
 public void testIn() {
 List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
 Predicate<Integer> isInNumbersPredicate = Predicates.in(numbers);

 assertTrue(isInNumbersPredicate.apply(1));
 assertTrue(isInNumbersPredicate.apply(2));
 assertTrue(isInNumbersPredicate.apply(3));
 assertFalse(isInNumbersPredicate.apply(4));
 assertFalse(isInNumbersPredicate.apply(5));
 assertFalse(isInNumbersPredicate.apply(6));
 }

 @Test
 public void testCompose() {
 Function<Integer, Boolean> isEvenFunction = new Function<Integer, Boolean>() {
 @Override
 public Boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> isEvenPredicate = Predicates.compose(Predicates.alwaysTrue(), isEvenFunction);

 assertTrue(isEvenPredicate.apply(2));
 assertTrue(isEvenPredicate.apply(4));
 assertFalse(isEvenPredicate.apply(1));
 assertFalse(isEvenPredicate.apply(3));
 }

 @Test
 public void testContainsPattern() {
 Predicate<String> containsPatternPredicate = Predicates.containsPattern("ab");
 assertTrue(containsPatternPredicate.apply("hello world"));
 assertTrue(containsPatternPredicate.apply("good bye ab"));
 assertFalse(containsPatternPredicate.apply("hello there"));
 }

 @Test
 public void testContainsPatternFromString() {
 Predicate<String> containsPatternPredicate = Predicates.containsPattern("ab");
 Predicate<String> containsPatternFromStringPredicate = Predicates.containsPattern("the string \"ab\"");

 assertTrue(containsPatternPredicate.apply("hello world"));
 assertTrue(containsPatternPredicate.apply("good bye ab"));
 assertFalse(containsPatternPredicate.apply("hello there"));
 assertTrue(containsPatternFromStringPredicate.apply("hello world"));
 assertTrue(containsPatternFromStringPredicate.apply("good bye ab"));
 assertFalse(containsPatternFromStringPredicate.apply("hello there"));
 }
}