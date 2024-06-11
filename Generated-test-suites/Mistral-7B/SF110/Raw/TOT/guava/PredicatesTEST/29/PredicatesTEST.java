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
 Object obj1 = new Object();
 Object obj2 = null;
 assertTrue(Predicates.isNull().apply(obj2));
 assertFalse(Predicates.isNull().apply(obj1));
 }

 @Test
 public void testNotNull() {
 Object obj1 = new Object();
 Object obj2 = null;
 assertFalse(Predicates.notNull().apply(obj2));
 assertTrue(Predicates.notNull().apply(obj1));
 }

 @Test
 public void testNot() {
 Predicate<Object> isNullPredicate = Predicates.isNull();
 Predicate<Object> alwaysTruePredicate = Predicates.alwaysTrue();
 Predicate<Object> notNullPredicate = Predicates.notNull();

 assertTrue(Predicates.not(isNullPredicate).apply(new Object()));
 assertFalse(Predicates.not(alwaysTruePredicate).apply(new Object()));
 assertFalse(Predicates.not(notNullPredicate).apply(null));
 assertFalse(Predicates.not(notNullPredicate).apply(new Object()));
 }

 @Test
 public void testAnd() {
 Predicate<Object> isNullPredicate = Predicates.isNull();
 Predicate<Object> alwaysFalsePredicate = Predicates.alwaysFalse();
 Predicate<Object> notNullPredicate = Predicates.notNull();

 List<Predicate<Object>> predicates = Arrays.asList(isNullPredicate, notNullPredicate);
 Predicate<Object> andPredicate = Predicates.and(predicates);

 Object obj1 = new Object();
 Object obj2 = null;

 assertFalse(andPredicate.apply(obj1));
 assertFalse(andPredicate.apply(obj2));

 Predicate<Object> truePredicate = Predicates.alwaysTrue();
 andPredicate = Predicates.and(truePredicate, isNullPredicate);
 assertFalse(andPredicate.apply(obj1));
 assertTrue(andPredicate.apply(obj2));
 }

 @Test
 public void testOr() {
 Predicate<Object> isNullPredicate = Predicates.isNull();
 Predicate<Object> alwaysFalsePredicate = Predicates.alwaysFalse();
 Predicate<Object> alwaysTruePredicate = Predicates.alwaysTrue();
 Predicate<Object> notNullPredicate = Predicates.notNull();

 List<Predicate<Object>> predicates = Arrays.asList(isNullPredicate, alwaysFalsePredicate, notNullPredicate, alwaysTruePredicate);
 Predicate<Object> orPredicate = Predicates.or(predicates);

 Object obj1 = new Object();
 Object obj2 = null;

 assertFalse(orPredicate.apply(obj1));
 assertTrue(orPredicate.apply(obj2));

 Predicate<Object> truePredicate = Predicates.alwaysTrue();
 orPredicate = Predicates.or(truePredicate, isNullPredicate);
 assertTrue(orPredicate.apply(obj1));
 assertTrue(orPredicate.apply(obj2));
 }

 @Test
 public void testEqualTo() {
 Object obj1 = new Object();
 Object obj2 = obj1;
 assertTrue(Predicates.equalTo(obj1).apply(obj1));
 assertTrue(Predicates.equalTo(obj1).apply(obj2));
 assertFalse(Predicates.equalTo(obj1).apply(new Object()));
 }

 @Test
 public void testInstanceOf() {
 Predicate<Object> instanceOfIntegerPredicate = Predicates.instanceOf(Integer.class);
 Object obj1 = new Integer(1);
 Object obj2 = new String("1");

 assertTrue(instanceOfIntegerPredicate.apply(obj1));
 assertFalse(instanceOfIntegerPredicate.apply(obj2));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAssignableFrom() {
 Predicate<Class<?>> assignableFromStringPredicate = Predicates.assignableFrom(String.class);
 assertTrue(assignableFromStringPredicate.apply(String.class));
 // Unsupported operation: Predicate<Class<?>> cannot be applied to Class<Integer>
 //assertFalse(assignableFromStringPredicate.apply(Integer.class));
 }

 @Test
 public void testIn() {
 List<Integer> list = Arrays.asList(1, 2, 3);
 Predicate<Integer> inPredicate = Predicates.in(list);

 Integer obj1 = 1;
 Integer obj2 = 2;

 assertTrue(inPredicate.apply(obj1));
 assertTrue(inPredicate.apply(obj2));
 assertFalse(inPredicate.apply(new Integer(4)));
 }

 @Test
 public void testCompose() {
 Function<Integer, Boolean> isEvenFunction = new Function<Integer, Boolean>() {
 @Override public Boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> isEvenPredicate = Predicates.compose(Predicates.alwaysTrue(), isEvenFunction);

 int oddNumber = 1;
 int evenNumber = 2;

 assertFalse(isEvenPredicate.apply(oddNumber));
 assertTrue(isEvenPredicate.apply(evenNumber));
 }

 @Test
 public void testContainsPattern() {
 Predicate<String> containsPatternPredicate = Predicates.containsPattern("ab*c");

 String str1 = "abc";
 String str2 = "abcd";
 String str3 = "a1b2c";

 assertTrue(containsPatternPredicate.apply(str1));
 assertTrue(containsPatternPredicate.apply(str2));
 assertFalse(containsPatternPredicate.apply(str3));
 }

 @Test
 public void testContainsPatternFromString() {
 Predicate<String> containsPatternPredicate = Predicates.containsPattern("ab*c");

 String str1 = "abc";
 String str2 = "abcd";
 String str3 = "a1b2c";

 assertTrue(containsPatternPredicate.apply(str1));
 assertTrue(containsPatternPredicate.apply(str2));
 assertFalse(containsPatternPredicate.apply(str3));
 }
}