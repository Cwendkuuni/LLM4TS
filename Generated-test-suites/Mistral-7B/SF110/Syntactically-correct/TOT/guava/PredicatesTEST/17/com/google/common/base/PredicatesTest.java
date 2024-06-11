package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
 Object obj1 = null;
 assertTrue(Predicates.isNull().apply(obj1));
 Object obj2 = new Object();
 assertFalse(Predicates.isNull().apply(obj2));
 }

 @Test
 public void testNotNull() {
 Object obj1 = null;
 assertFalse(Predicates.notNull().apply(obj1));
 Object obj2 = new Object();
 assertTrue(Predicates.notNull().apply(obj2));
 }

 @Test
 public void testNot() {
 Predicate<Object> predicateAlwaysTrue = Predicates.alwaysTrue();
 Predicate<Object> predicateNotAlwaysTrue = Predicates.not(predicateAlwaysTrue);
 Object obj1 = new Object();
 assertTrue(predicateNotAlwaysTrue.apply(obj1));
 assertFalse(predicateAlwaysTrue.apply(obj1));
 }

 @Test
 public void testAnd() {
 Predicate<String> isEmpty = Predicates.isNull();
 Predicate<String> isEmptyOrLengthZero = Predicates.and(Predicates.isNull(), Predicates.equalTo(""));
 List<Predicate<String>> predicatesList = Arrays.asList(Predicates.isNull(), Predicates.equalTo(""));
 Predicate<String> andPredicate = Predicates.and(predicatesList);
 String emptyString = null;
 String zeroLengthString = "";
 String nonEmptyString = "NonEmpty";

 assertTrue(isEmpty.apply(emptyString));
 assertTrue(isEmptyOrLengthZero.apply(emptyString));
 assertTrue(andPredicate.apply(emptyString));

 assertFalse(isEmpty.apply(zeroLengthString));
 assertFalse(isEmptyOrLengthZero.apply(zeroLengthString));
 assertFalse(andPredicate.apply(zeroLengthString));

 assertTrue(isEmpty.apply(nonEmptyString));
 assertFalse(isEmptyOrLengthZero.apply(nonEmptyString));
 assertFalse(andPredicate.apply(nonEmptyString));
 }

 @Test
 public void testOr() {
 Predicate<String> isEmpty = Predicates.isNull();
 Predicate<String> isEmptyOrLengthZero = Predicates.or(Predicates.isNull(), Predicates.equalTo(""));
 List<Predicate<String>> predicatesList = Arrays.asList(Predicates.isNull(), Predicates.equalTo(""));
 Predicate<String> orPredicate = Predicates.or(predicatesList);
 String emptyString = null;
 String zeroLengthString = "";
 String nonEmptyString = "NonEmpty";

 assertTrue(isEmpty.apply(emptyString));
 assertTrue(isEmptyOrLengthZero.apply(emptyString));
 assertTrue(orPredicate.apply(emptyString));

 assertFalse(isEmpty.apply(zeroLengthString));
 assertFalse(isEmptyOrLengthZero.apply(zeroLengthString));
 assertTrue(orPredicate.apply(zeroLengthString));

 assertTrue(isEmpty.apply(nonEmptyString));
 assertFalse(isEmptyOrLengthZero.apply(nonEmptyString));
 assertFalse(orPredicate.apply(nonEmptyString));
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

 @Test(expected = NullPointerException.class)
 public void testInstanceOfWithNull() {
 Predicate<Object> predicate = Predicates.instanceOf(String.class);
 assertFalse(predicate.apply(null));
 }

 @Test
 public void testInstanceOf() {
 Object obj1 = new String("String1");
 Object obj2 = new Integer(1);
 Predicate<Object> predicateString = Predicates.instanceOf(String.class);
 Predicate<Object> predicateInteger = Predicates.instanceOf(Integer.class);

 assertTrue(predicateString.apply(obj1));
 assertFalse(predicateString.apply(obj2));
 assertTrue(predicateInteger.apply(obj2));
 assertFalse(predicateInteger.apply(obj1));
 }

 @Test
 public void testAssignableFrom() {
 Class<?> superClass = String.class;
 Class<?> subClass = "StringBuilder".getClass();
 Predicate<Class<?>> predicateSuperClass = Predicates.assignableFrom(superClass);
 Predicate<Class<?>> predicateSubClass = Predicates.assignableFrom(subClass);

 assertTrue(predicateSuperClass.apply(String.class));
 assertTrue(predicateSuperClass.apply(subClass));
 assertFalse(predicateSubClass.apply(String.class));
 assertFalse(predicateSubClass.apply(Integer.class));
 }

 @Test
 public void testIn() {
 List<Integer> numbersList = Arrays.asList(1, 2, 3);
 Predicate<Integer> predicateInList = Predicates.in(numbersList);
 Integer number1 = 1;
 Integer number2 = 2;
 Integer number3 = 4;

 assertTrue(predicateInList.apply(number1));
 assertTrue(predicateInList.apply(number2));
 assertFalse(predicateInList.apply(number3));
 }

 @Test
 public void testCompose() {
 Function<String, Integer> stringToInteger = new Function<String, Integer>() {
 @Override public Integer apply(String input) { return Integer.parseInt(input); }
 };
 Predicate<String> predicateIsEmpty = Predicates.isNull();
 Predicate<Integer> predicateIsEven = new Predicate<Integer>() {
 @Override public boolean apply(Integer input) { return input % 2 == 0; }
 };
 Predicate<String> predicateIsEvenLength = Predicates.compose(predicateIsEmpty, stringToInteger);
 Predicate<String> predicateIsOddLength = new Predicate<String>() {
 @Override public boolean apply(String input) { return input.length() % 2 != 0; }
 };
 String emptyString = "";
 String evenLengthString = "evenLengthString";
 String oddLengthString = "oddLengthString";

 assertTrue(predicateIsEmpty.apply(emptyString));
 assertFalse(predicateIsEvenLength.apply(emptyString));
 assertFalse(predicateIsEvenLength.apply(evenLengthString));

 assertTrue(predicateIsEmpty.apply(null));
 assertFalse(predicateIsEvenLength.apply(null));

 assertTrue(predicateIsOddLength.apply(oddLengthString));
 assertFalse(predicateIsOddLength.apply(evenLengthString));
 }

 @Test
 public void testContainsPattern() {
 Predicate<String> containsPatternPredicate = Predicates.containsPattern(".*Google.*");
 String string1 = "GoogleCommonBase";
 String string2 = "AnotherClass";
 String string3 = "GoogleCommonBasePredicates";

 assertTrue(containsPatternPredicate.apply(string1));
 assertFalse(containsPatternPredicate.apply(string2));
 assertTrue(containsPatternPredicate.apply(string3));
 }

 @Test
 public void testContainsPatternFromString() {
 Predicate<String> containsPatternPredicate = Predicates.containsPattern("Google");
 String string1 = "GoogleCommonBase";
 String string2 = "AnotherClass";
 String string3 = "GoogleCommonBasePredicates";

 assertTrue(containsPatternPredicate.apply(string1));
 assertFalse(containsPatternPredicate.apply(string2));
 assertTrue(containsPatternPredicate.apply(string3));
 }
}