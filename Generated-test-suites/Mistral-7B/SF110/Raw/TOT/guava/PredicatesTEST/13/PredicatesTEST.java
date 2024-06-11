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
 assertNull(Predicates.isNull().apply(null));
 }

 @Test
 public void testIsNotNull() {
 Object obj = new Object();
 assertNotNull(Predicates.notNull().apply(obj));
 }

 @Test
 public void testNot() {
 Predicate<Integer> predicate = Predicates.isNotNull();
 Predicate<Integer> notPredicate = Predicates.not(predicate);
 Integer nullValue = null;
 Integer nonNullValue = 1;

 assertFalse(notPredicate.apply(nullValue));
 assertTrue(notPredicate.apply(nonNullValue));
 }

 @Test
 public void testAnd() {
 Predicate<Integer> evenPredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> positivePredicate = Predicates.not(Predicates.isNull()).and(Predicates.alwaysTrue());

 List<Predicate<Integer>> components = Arrays.asList(evenPredicate, positivePredicate);
 Predicate<Integer> andPredicate = Predicates.and(components);

 Integer input = 10;
 assertTrue(andPredicate.apply(input));
 }

 @Test(expected = NullPointerException.class)
 public void testAndWithNullComponents() {
 Predicate<Integer> andPredicate = Predicates.and((Predicate<Integer>[])null);
 }

 @Test
 public void testOr() {
 Predicate<Integer> evenPredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> oddPredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(@Nullable Integer input) {
 return input % 2 != 0;
 }
 };
 Predicate<Integer> orPredicate = Predicates.or(Arrays.asList(evenPredicate, oddPredicate));

 Integer input = 1;
 assertFalse(orPredicate.apply(input));

 input = 2;
 assertTrue(orPredicate.apply(input));
 }

 @Test
 public void testEqualTo() {
 Predicate<String> predicate = Predicates.equalTo("test");
 String testString = "test";
 assertTrue(predicate.apply(testString));
 }

 @Test
 public void testInstanceOf() {
 try {
 Class<String> stringClass = String.class;
 Predicate<Object> instanceOfPredicate = Predicates.instanceOf(stringClass);
 Object testObject = new Object();
 assertFalse(instanceOfPredicate.apply(testObject));
 } catch (AssertionError e) {
 // expected exception
 }

 try {
 String testString = "test";
 Predicate<Object> instanceOfPredicate = Predicates.instanceOf(String.class);
 assertTrue(instanceOfPredicate.apply(testString));
 } catch (AssertionError e) {
 // expected exception
 }
 }

 @Test(expected = AssertionError.class)
 public void testAssignableFrom() {
 try {
 Predicate<Class<?>> assignableFromPredicate = Predicates.assignableFrom(int[].class);
 Predicate<Class<?>> incompatiblePredicate = Predicates.instanceOf(long[].class);
 Predicate<Class<?>> orPredicate = Predicates.or(Arrays.asList(assignableFromPredicate, incompatiblePredicate));
 } catch (AssertionError e) {
 // expected exception
 }
 }

 @Test
 public void testIn() {
 List<Integer> list = Arrays.asList(1, 2, 3);
 Predicate<Integer> inPredicate = Predicates.in(list);

 Integer input = 2;
 assertTrue(inPredicate.apply(input));

 input = 4;
 assertFalse(inPredicate.apply(input));
 }

 @Test(expected = NullPointerException.class)
 public void testInWithNullList() {
 Predicate<Integer> inPredicate = Predicates.in((List<Integer>)null);
 }

 @Test
 public void testCompose() {
 Function<Integer, Boolean> isEvenFunction = new Function<Integer, Boolean>() {
 @Override
 public Boolean apply(@Nullable Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> evenPredicate = Predicates.alwaysTrue();
 Predicate<Integer> composePredicate = Predicates.compose(evenPredicate, isEvenFunction);

 Integer input = 10;
 assertFalse(composePredicate.apply(input));

 input = 12;
 assertTrue(composePredicate.apply(input));
 }

 @Test(expected = NullPointerException.class)
 public void testComposeWithNullPredicate() {
 Predicate<Integer> composePredicate = Predicates.compose(null, new Function<Integer, Boolean>(){});
 }

 @Test(expected = NullPointerException.class)
 public void testComposeWithNullFunction() {
 Predicate<Integer> composePredicate = Predicates.compose(new Predicate<Integer>(){}, null);
 }
}