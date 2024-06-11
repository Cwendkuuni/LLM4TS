package com.example.predicates; // replace with your package name

import static org.junit.Assert.*;
import com.google.common.base.*;
import org.junit.Test;

public class PredicatesTest {

 // Testing alwaysTrue and alwaysFalse methods
 @Test
 public void testAlwaysTrueAndAlwaysFalse() {
 Predicate<Object> alwaysTrue = Predicates.alwaysTrue();
 Predicate<Object> alwaysFalse = Predicates.alwaysFalse();

 assertTrue(alwaysTrue.apply(new Object()));
 assertFalse(alwaysFalse.apply(new Object()));
 }

 // Testing isNull and notNull methods
 @Test
 public void testIsNullAndNotNull() {
 Object obj = new Object();
 Predicate<Object> isNull = Predicates.isNull();
 Predicate<Object> notNull = Predicates.notNull();

 assertFalse(isNull.apply(obj));
 assertTrue(notNull.apply(obj));
 }

 // Testing not method
 @Test
 public void testNot() {
 Predicate<Boolean> isTrue = Predicates.alwaysTrue();
 Predicate<Boolean> isFalse = Predicates.alwaysFalse();

 Predicate<Boolean> notTrue = Predicates.not(isTrue);
 Predicate<Boolean> notFalse = Predicates.not(isFalse);

 assertFalse(notTrue.apply(true));
 assertTrue(notTrue.apply(false));

 assertTrue(notFalse.apply(false));
 assertFalse(notFalse.apply(true));
 }

 // Testing and method
 @Test
 public void testAnd() {
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> isPositive = Predicates.not(Predicates.isNull())
 .and(new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input > 0;
 }
 });

 Predicate<Integer> andPredicate = Predicates.and(isEven, isPositive);

 assertTrue(andPredicate.apply(2));
 assertFalse(andPredicate.apply(1));
 assertFalse(andPredicate.apply(null));
 }

 // Testing or method
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

 assertTrue(orPredicate.apply(2));
 assertTrue(orPredicate.apply(1));
 assertFalse(orPredicate.apply(0));
 }

 // Testing instanceOf method
 @Test
 public void testInstanceOf() {
 Predicate<Object> isString = Predicates.instanceOf(String.class);

 assertTrue(isString.apply("Test"));
 assertFalse(isString.apply(new Object()));
 assertFalse(isString.apply(1));
 }

 // Testing assignableFrom method
 @Test
 public void testAssignableFrom() {
 Predicate<Class<?>> isString = Predicates.assignableFrom(String.class);
 Predicate<Class<?>> isNumber = Predicates.assignableFrom(Number.class);

 assertTrue(isString.apply(String[].class));
 assertTrue(isString.apply(StringBuffer.class));

 assertTrue(isNumber.apply(Double.class));
 assertTrue(isNumber.apply(Integer.class));
 }

 // Testing equalTo method
 @Test
 public void testEqualTo() {
 Predicate<Object> isEqual = Predicates.equalTo("Test");

 assertTrue(isEqual.apply("Test"));
 assertFalse(isEqual.apply("Different"));
 assertFalse(isEqual.apply(new Object()));
 }

 // Testing in method
 @Test
 public void testIn() {
 Collection<Integer> numbers = Arrays.asList(1, 2, 3);
 Predicate<Integer> inPredicate = Predicates.in(numbers);

 assertTrue(inPredicate.apply(2));
 assertFalse(inPredicate.apply(4));
 assertFalse(inPredicate.apply(null));
 }

 // Testing compose method
 @Test
 public void testCompose() {
 Function<Integer, Boolean> isEven = new Function<Integer, Boolean>() {
 @Override
 public Boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> isPositive = Predicates.not(Predicates.isNull())
 .and(new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input > 0;
 }
 });
 Predicate<Integer> isEvenAndPositive = Predicates.compose(isPositive, isEven);

 assertTrue(isEvenAndPositive.apply(2));
 assertFalse(isEvenAndPositive.apply(0));
 assertFalse(isEvenAndPositive.apply(null));
 }

 // Testing containsPattern method
 @Test
 public void testContainsPattern() {
 Predicate<String> containsPattern = Predicates.containsPattern("Test%");

 assertTrue(containsPattern.apply("Test123"));
 assertFalse(containsPattern.apply("Test456"));
 assertFalse(containsPattern.apply(null));
 }

 // Testing containsPatternFromString method
 @Test
 public void testContainsPatternFromString() {
 Predicate<String> containsPattern = Predicates.containsPatternFromString("%Test%");

 assertTrue(containsPattern.apply("Test123Test"));
 assertFalse(containsPattern.apply("Test456"));
 assertFalse(containsPattern.apply(null));
 }
}