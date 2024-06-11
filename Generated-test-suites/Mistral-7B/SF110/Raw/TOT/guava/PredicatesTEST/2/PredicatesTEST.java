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
 Predicate<Integer> predicate = Predicates.isNull();
 Predicate<Integer> notPredicate = Predicates.not(predicate);

 Integer value1 = null;
 assertFalse(notPredicate.apply(value1));
 Integer value2 = 1;
 assertTrue(notPredicate.apply(value2));
 }

 @Test
 public void testAnd() {
 Predicate<Integer> predicate1 = Predicates.isNull();
 Predicate<Integer> predicate2 = Predicates.notNull();
 Predicate<Integer> andPredicate = Predicates.and(predicate1, predicate2);

 Integer value1 = null;
 assertFalse(andPredicate.apply(value1));
 Integer value2 = 1;
 assertFalse(andPredicate.apply(value2));
 Integer value3 = null;
 assertFalse(andPredicate.apply(value3));
 Integer value4 = 1;
 assertTrue(andPredicate.apply(value4));
 }

 @Test
 public void testOr() {
 Predicate<Integer> predicate1 = Predicates.isNull();
 Predicate<Integer> predicate2 = Predicates.notNull();
 Predicate<Integer> orPredicate = Predicates.or(predicate1, predicate2);

 Integer value1 = null;
 assertTrue(orPredicate.apply(value1));
 Integer value2 = 1;
 assertTrue(orPredicate.apply(value2));
 Integer value3 = null;
 assertTrue(orPredicate.apply(value3));
 Integer value4 = 1;
 assertTrue(orPredicate.apply(value4));
 }

 @Test
 public void testEqualTo() {
 Integer value1 = 1;
 Predicate<Integer> predicate = Predicates.equalTo(value1);
 assertTrue(predicate.apply(value1));
 assertFalse(predicate.apply(2));
 }

 @Test
 public void testInstanceOf() {
 Class<Number> clazz = Number.class;
 Predicate<Object> predicate = Predicates.instanceOf(clazz);
 assertTrue(predicate.apply(1));
 assertFalse(predicate.apply("test"));
 }

 @Test
 public void testAssignableFrom() {
 Class<Number> clazz1 = Integer.class;
 Class<Number> clazz2 = Number.class;
 Predicate<Class<?>> predicate = Predicates.assignableFrom(clazz1);
 assertTrue(predicate.apply(clazz2));
 assertFalse(predicate.apply(String.class));
 }

 @Test
 public void testIn() {
 List<Integer> list = Arrays.asList(1, 2, 3);
 Predicate<Integer> predicate = Predicates.in(list);
 assertTrue(predicate.apply(1));
 assertTrue(predicate.apply(2));
 assertFalse(predicate.apply(4));
 }

 @Test
 public void testCompose() {
 Predicate<String> predicate1 = Predicates.isNull();
 Function<String, Boolean> function = s -> s.isEmpty();
 Predicate<String> predicate2 = Predicates.compose(predicate1, function);

 String value1 = "";
 assertTrue(predicate2.apply(value1));
 String value2 = "test";
 assertFalse(predicate2.apply(value2));
 }

 @Test(expected = NullPointerException.class)
 public void testContainsPatternNull() {
 Predicate<CharSequence> predicate = Predicates.containsPattern(null);
 }

 @Test
 public void testContainsPattern() {
 Predicate<String> predicate = Predicates.containsPattern("ab");
 String value1 = "test";
 assertFalse(predicate.apply(value1));
 String value2 = "testabtest";
 assertTrue(predicate.apply(value2));
 }

 @Test
 public void testContainsPatternFromString() {
 Predicate<String> predicate = Predicates.containsPattern("ab");
 String value = "testabtest";
 assertTrue(predicate.apply(value));
 }
}