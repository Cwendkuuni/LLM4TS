package com.google.common.base;

import static org.junit.Assert.*;

import java.util.*;
import java.util.function.Function;
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
 assertFalse(Predicates.notNull().apply(null));
 assertTrue(Predicates.notNull().apply(obj));
 }

 @Test
 public void testNot() {
 Predicate<Object> predicate = Predicates.isNull();
 Predicate<Object> notPredicate = Predicates.not(predicate);
 Object obj1 = null;
 Object obj2 = new Object();
 assertFalse(notPredicate.apply(obj1));
 assertTrue(notPredicate.apply(obj2));
 }

 @Test
 public void testAnd() {
 Predicate<Object> predicate1 = Predicates.isNull();
 Predicate<Object> predicate2 = Predicates.notNull();
 Predicate<Object> andPredicate = Predicates.and(predicate1, predicate2);
 Object obj1 = null;
 Object obj2 = new Object();
 assertFalse(andPredicate.apply(obj1));
 assertFalse(andPredicate.apply(obj2));
 }

 @Test
 public void testAndWithIterable() {
 List<Predicate<Object>> predicates = Arrays.asList(Predicates.isNull(), Predicates.notNull());
 Predicate<Object> andPredicate = Predicates.and(predicates);
 Object obj1 = null;
 Object obj2 = new Object();
 assertFalse(andPredicate.apply(obj1));
 assertTrue(andPredicate.apply(obj2));
 }

 @Test
 public void testAndWithArray() {
 Predicate<Object> predicate1 = Predicates.isNull();
 Predicate<Object> predicate2 = Predicates.notNull();
 Predicate<Object> andPredicate = Predicates.and(predicate1, predicate2);
 Object obj1 = null;
 Object obj2 = new Object();
 assertFalse(andPredicate.apply(obj1));
 assertFalse(andPredicate.apply(obj2));
 }

 @Test
 public void testOr() {
 Predicate<Object> predicate1 = Predicates.isNull();
 Predicate<Object> predicate2 = Predicates.notNull();
 Predicate<Object> orPredicate = Predicates.or(predicate1, predicate2);
 Object obj1 = null;
 Object obj2 = new Object();
 assertFalse(orPredicate.apply(obj1));
 assertTrue(orPredicate.apply(obj2));
 }

 @Test
 public void testOrWithIterable() {
 List<Predicate<Object>> predicates = Arrays.asList(Predicates.isNull(), Predicates.notNull());
 Predicate<Object> orPredicate = Predicates.or(predicates);
 Object obj1 = null;
 Object obj2 = new Object();
 assertFalse(orPredicate.apply(obj1));
 assertTrue(orPredicate.apply(obj2));
 }

 @Test
 public void testOrWithArray() {
 Predicate<Object> predicate1 = Predicates.isNull();
 Predicate<Object> predicate2 = Predicates.notNull();
 Predicate<Object> orPredicate = Predicates.or(predicate1, predicate2);
 Object obj1 = null;
 Object obj2 = new Object();
 assertFalse(orPredicate.apply(obj1));
 assertTrue(orPredicate.apply(obj2));
 }

 @Test
 public void testEqualTo() {
 Object obj1 = new Object();
 Object obj2 = obj1;
 Predicate<Object> equalPredicate = Predicates.equalTo(obj1);
 assertTrue(equalPredicate.apply(obj2));
 }

 @Test
 public void testEqualToNull() {
 Predicate<Object> equalPredicate = Predicates.equalTo(null);
 assertFalse(equalPredicate.apply(new Object()));
 assertTrue(equalPredicate.apply(null));
 }

 @Test
 public void testInstanceOf() {
 @SuppressWarnings("unchecked")
 Predicate<Integer> instanceOfPredicate = Predicates.instanceOf(Integer.class);
 Integer integer = new Integer(1);
 assertTrue(instanceOfPredicate.apply(integer));
 }

 @Test
 public void testInstanceOfWrongType() {
 @SuppressWarnings("unchecked")
 Predicate<Integer> instanceOfPredicate = Predicates.instanceOf(String.class);
 Integer integer = new Integer(1);
 assertFalse(instanceOfPredicate.apply(integer));
 }

 @Test
 public void testAssignableFrom() {
 @SuppressWarnings("unchecked")
 Predicate<Class<Number>> assignableFromPredicate = Predicates.assignableFrom(Number.class);
 Predicate<Class<Integer>> assignableFromIntegerPredicate = Predicates.assignableFrom(Integer.class);
 assertTrue(assignableFromPredicate.apply(assignableFromIntegerPredicate));
 }

 @Test
 public void testIn() {
 List<Integer> list = Arrays.asList(1, 2, 3);
 Predicate<Integer> inPredicate = Predicates.in(list);
 assertTrue(inPredicate.apply(1));
 assertFalse(inPredicate.apply(4));
 }

 @Test
 public void testCompose() {
 Function<String, Integer> function = s -> s.length();
 Predicate<String> predicate = Predicates.isNull();
 Predicate<String> composedPredicate = Predicates.compose(predicate, function);
 String str1 = null;
 String str2 = "hello";
 assertFalse(composedPredicate.apply(str1));
 assertTrue(composedPredicate.apply(str2));
 }

 @Test
 public void testContainsPattern() {
 String pattern = "\\w+";
 Predicate<String> containsPatternPredicate = Predicates.containsPattern(pattern);
 assertTrue(containsPatternPredicate.apply("hello world"));
 assertFalse(containsPatternPredicate.apply("123"));
 }

 @Test
 public void testContainsPatternFromString() {
 String pattern = "\\w+";
 Predicate<String> containsPatternFromStringPredicate = Predicates.containsPatternFromString(pattern);
 assertTrue(containsPatternFromStringPredicate.apply("hello world"));
 assertFalse(containsPatternFromStringPredicate.apply("123"));
 }
}