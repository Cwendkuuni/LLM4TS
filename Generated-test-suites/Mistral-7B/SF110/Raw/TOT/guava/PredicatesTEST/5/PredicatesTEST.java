package com.google.common.base;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PredicatesTest {

 // Test alwaysTrue()
 @Test
 public void testAlwaysTrue() {
 Predicate<Integer> alwaysTruePredicate = Predicates.alwaysTrue();
 assertTrue(alwaysTruePredicate.apply(null));
 assertTrue(alwaysTruePredicate.apply(0));
 assertTrue(alwaysTruePredicate.apply(1));
 }

 // Test alwaysFalse()
 @Test
 public void testAlwaysFalse() {
 Predicate<Integer> alwaysFalsePredicate = Predicates.alwaysFalse();
 assertFalse(alwaysFalsePredicate.apply(null));
 assertFalse(alwaysFalsePredicate.apply(0));
 assertFalse(alwaysFalsePredicate.apply(1));
 }

 // Test isNull() and notNull()
 @Test
 public void testIsNullAndNotNull() {
 Object obj = null;
 Predicate<Object> isNullPredicate = Predicates.isNull();
 Predicate<Object> notNullPredicate = Predicates.notNull();

 // Test isNull() with null
 assertTrue(isNullPredicate.apply(null));

 // Test isNull() with non-null
 assertFalse(isNullPredicate.apply(new Object()));

 // Test notNull() with null
 assertFalse(notNullPredicate.apply(null));

 // Test notNull() with non-null
 assertTrue(notNullPredicate.apply(new Object()));
 }

 // Test not()
 @Test
 public void testNot() {
 Predicate<Boolean> isTruePredicate = Predicates.alwaysTrue();
 Predicate<Boolean> isFalsePredicate = Predicates.alwaysFalse();
 Predicate<Boolean> notPredicate = Predicates.not(isTruePredicate);

 // Test not() with true
 assertFalse(notPredicate.apply(true));

 // Test not() with false
 assertTrue(notPredicate.apply(false));
 }

 // Test and()
 @Test
 public void testAnd() {
 Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> isPositivePredicate = Predicates.notNull();
 Predicate<Integer> andPredicate = Predicates.and(isEvenPredicate, isPositivePredicate);

 // Test and() with even and positive numbers
 List<Integer> evenPositiveNumbers = Arrays.asList(2, 4, 6);
 for (Integer number : evenPositiveNumbers) {
 assertTrue(andPredicate.apply(number));
 }

 // Test and() with even and negative number
 assertFalse(andPredicate.apply(-2));

 // Test and() with null number
 assertFalse(andPredicate.apply(null));
 }

 // Test or()
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
 Predicate<Integer> orPredicate = Predicates.or(isEvenPredicate, isOddPredicate);

 // Test or() with even number
 assertTrue(orPredicate.apply(2));

 // Test or() with odd number
 assertTrue(orPredicate.apply(1));

 // Test or() with null number
 assertFalse(orPredicate.apply(null));
 }

 // Test equalsTo()
 @Test
 public void testEqualTo() {
 Predicate<Integer> isEqualToFivePredicate = Predicates.equalTo(5);

 // Test equalsTo() with non-matching numbers
 assertFalse(isEqualToFivePredicate.apply(null));
 assertFalse(isEqualToFivePredicate.apply(1));
 assertFalse(isEqualToFivePredicate.apply(6));

 // Test equalsTo() with matching number
 assertTrue(isEqualToFivePredicate.apply(5));
 }

 // Test instanceOf() (GWT incompatible)
 @Test // Remove this block if using GWT
 public void testInstanceOf() {
 Predicate<Class<?>> isInstanceOfStringPredicate = Predicates.instanceOf(String.class);
 Class<?> clazz = null;

 // Test instanceOf() with String
 assertTrue(isInstanceOfStringPredicate.apply(String.class));
 assertTrue(isInstanceOfStringPredicate.apply(""));

 // Test instanceOf() with non-String
 assertFalse(isInstanceOfStringPredicate.apply(clazz));
 }

 // Test assignableFrom() (GWT incompatible)
 @Test // Remove this block if using GWT
 public void testAssignableFrom() {
 Predicate<Class<?>> isAssignableFromNumberPredicate = Predicates.assignableFrom(Number.class);
 Class<?> clazz = null;

 // Test assignableFrom() with Number
 assertTrue(isAssignableFromNumberPredicate.apply(Number.class));
 assertTrue(isAssignableFromNumberPredicate.apply(Integer.class));

 // Test assignableFrom() with non-Number
 assertFalse(isAssignableFromNumberPredicate.apply(clazz));
 }

 // Test in()
 @Test
 public void testIn() {
 Predicate<Integer> inPredicate = Predicates.in(Arrays.asList(1, 2, 3));

 // Test in() with number in collection
 assertTrue(inPredicate.apply(1));
 assertTrue(inPredicate.apply(2));
 assertTrue(inPredicate.apply(3));

 // Test in() with number not in collection
 assertFalse(inPredicate.apply(4));

 // Test in() with null number
 assertFalse(inPredicate.apply(null));
 }

 // Test compose()
 @Test
 public void testCompose() {
 Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Function<Integer, Integer> multiplyByTwoFunction = new Function<Integer, Integer>() {
 @Override
 public Integer apply(Integer input) {
 return input * 2;
 }
 };
 Predicate<Integer> composePredicate = Predicates.compose(isEvenPredicate, multiplyByTwoFunction);

 // Test compose() with even number
 assertEquals(4, composePredicate.apply(2).intValue());

 // Test compose() with odd number
 assertFalse(composePredicate.apply(1).isPresent());
 }

 // Test containsPattern() (GWT incompatible)
 @Test // Remove this block if using GWT
 public void testContainsPattern() {
 Predicate<String> containsPatternPredicate = Predicates.containsPattern("ab.*c");

 // Test containsPattern() with matching string
 assertTrue(containsPatternPredicate.apply("abc"));
 assertTrue(containsPatternPredicate.apply("abcd"));

 // Test containsPattern() with non-matching string
 assertFalse(containsPatternPredicate.apply("xyz"));

 // Test containsPattern() with null string
 assertFalse(containsPatternPredicate.apply(null));
 }

 // Test containsPatternFromString() (GWT incompatible)
 @Test // Remove this block if using GWT
 public void testContainsPatternFromString() {
 Predicate<String> containsPatternPredicate = Predicates.containsPattern("ab.*c");

 // Test containsPatternFromString() with matching string
 assertTrue(containsPatternPredicate.apply("abc"));
 assertTrue(containsPatternPredicate.apply("abcd"));

 // Test containsPatternFromString() with non-matching string
 assertFalse(containsPatternPredicate.apply("xyz"));
 }
}