package com.google.common.base;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

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
 }

 @Test
 public void testNotNull() {
 Object obj = new Object();
 assertFalse(Predicates.isNull().apply(obj));
 assertTrue(Predicates.notNull().apply(obj));
 }

 @Test
 public void testNot() {
 Predicate<Object> predicate = Predicates.alwaysTrue();
 Predicate<Object> notPredicate = Predicates.not(predicate);
 Object obj = new Object();
 assertFalse(predicate.apply(obj));
 assertTrue(notPredicate.apply(obj));
 }

 @Test
 public void testAnd() {
 Predicate<Integer> isEven = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> isPositive = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input > 0;
 }
 };
 Predicate<Integer> andPredicate = Predicates.and(isEven, isPositive);
 Integer number = 2;
 assertTrue(andPredicate.apply(number));
 }

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
 Integer number = 1;
 assertFalse(orPredicate.apply(number));
 number = 2;
 assertTrue(orPredicate.apply(number));
 }

 @Test
 public void testEqualTo() {
 Object obj1 = new Object();
 Object obj2 = obj1;
 assertTrue(Predicates.equalTo(obj1).apply(obj1));
 assertTrue(Predicates.equalTo(obj1).apply(obj2));
 Object obj3 = new Object();
 assertFalse(Predicates.equalTo(obj1).apply(obj3));
 }

 @Test
 public void testInstanceOf() {
 Class<String> stringClass = String.class;
 Predicate<Object> instanceOfString = Predicates.instanceOf(stringClass);
 Object strObj = "Test";
 assertTrue(instanceOfString.apply(strObj));
 Object nonStrObj = new Object();
 assertFalse(instanceOfString.apply(nonStrObj));
 }

 @Test
 public void testAssignableFrom() {
 Class<Number> numberClass = Number.class;
 Class<Integer> intClass = Integer.class;
 Predicate<Class<?>> assignableFromNumber = Predicates.assignableFrom(numberClass);
 Predicate<Class<?>> assignableFromInt = Predicates.assignableFrom(intClass);
 assertTrue(assignableFromNumber.apply(intClass));
 assertTrue(assignableFromInt.apply(numberClass));
 Class<String> stringClass = String.class;
 assertFalse(assignableFromNumber.apply(stringClass));
 }

 @Test
 public void testIn() {
 List<Integer> numbers = Arrays.asList(1, 2, 3);
 Predicate<Integer> inNumbers = Predicates.in(numbers);
 Integer number1 = 2;
 assertTrue(inNumbers.apply(number1));
 Integer number2 = 4;
 assertFalse(inNumbers.apply(number2));
 }

 @Test
 public void testCompose() {
 Function<Integer, Boolean> isEven = new Function<Integer, Boolean>() {
 @Override
 public Boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> isEvenPredicate = Predicates.alwaysTrue();
 Predicate<Integer> evenPredicate = Predicates.compose(isEvenPredicate, isEven);
 Integer number = 2;
 assertTrue(evenPredicate.apply(number));
 }

 @Test
 public void testContainsPattern() {
 String pattern = "[A-Z]";
 Predicate<String> containsPattern = Predicates.containsPattern(pattern);
 String string1 = "Test";
 assertFalse(containsPattern.apply(string1));
 String string2 = "TestString";
 assertTrue(containsPattern.apply(string2));
 }

 @Test
 public void testContainsPatternFromString() {
 String pattern = "[A-Z]";
 Predicate<String> containsPattern = Predicates.containsPatternFromString(pattern);
 String string1 = "Test";
 assertFalse(containsPattern.apply(string1));
 String string2 = "TestString";
 assertTrue(containsPattern.apply(string2));
 }
}