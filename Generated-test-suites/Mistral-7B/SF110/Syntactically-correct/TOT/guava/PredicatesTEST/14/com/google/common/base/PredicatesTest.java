package com.google.common.base;

import com.google.common.base.Predicates;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class PredicatesTest {

 @Test
 public void testAlwaysTrue() {
 Object obj = new Object();
 assertTrue(Predicates.alwaysTrue().apply(obj));
 }

 @Test(expected = NullPointerException.class)
 public void testAlwaysTrueWithNull() {
 Predicates.alwaysTrue().apply(null);
 }

 @Test
 public void testAlwaysFalse() {
 Object obj = new Object();
 assertFalse(Predicates.alwaysFalse().apply(obj));
 }

 @Test
 public void testIsNull() {
 assertNull(Predicates.IsNull().apply(null));
 }

 @Test
 public void testIsNullWithNotNullObject() {
 Object obj = new Object();
 assertFalse(Predicates.IsNull().apply(obj));
 }

 @Test
 public void testNot() {
 Predicate<Boolean> predicate = Predicates.alwaysTrue();
 Predicate<Boolean> notPredicate = Predicates.not(predicate);
 assertFalse(notPredicate.apply(true));
 assertTrue(notPredicate.apply(false));
 }

 @Test(expected = NullPointerException.class)
 public void testNotWithNullPredicate() {
 Predicates.not(null);
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
 int number = 10;
 assertTrue(andPredicate.apply(number));
 }

 @Test
 public void testAndWithEmptyList() {
 Predicate<Integer> andPredicate = Predicates.and();
 assertFalse(andPredicate.apply(1));
 }

 @Test(expected = NullPointerException.class)
 public void testAndWithNullComponent() {
 Predicate<Integer> andPredicate = Predicates.and(null, Predicates.alwaysTrue());
 }

 @Test
 public void testAndWithNullComponent2() {
 Predicate<Integer> andPredicate = Predicates.and(Predicates.alwaysTrue(), null);
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
 int number = 10;
 assertTrue(orPredicate.apply(number));
 }

 @Test
 public void testOrWithEmptyList() {
 Predicate<Integer> orPredicate = Predicates.or();
 assertFalse(orPredicate.apply(1));
 }

 @Test(expected = NullPointerException.class)
 public void testOrWithNullComponent() {
 Predicate<Integer> orPredicate = Predicates.or(null, Predicates.alwaysTrue());
 }

 @Test
 public void testOrWithNullComponent2() {
 Predicate<Integer> orPredicate = Predicates.or(Predicates.alwaysTrue(), null);
 }

 @Test
 public void testEqualTo() {
 Predicate<String> isEqualToString = Predicates.equalTo("test");
 String string = "test";
 assertTrue(isEqualToString.apply(string));
 }

 @Test
 public void testEqualToWithNotEqual() {
 Predicate<String> isEqualToString = Predicates.equalTo("test");
 String string2 = "test2";
 assertFalse(isEqualToString.apply(string2));
 }

 @Test
 public void testInstanceOf() {
 Predicate<String> isString = Predicates.instanceOf(String.class);
 String string = new String("test");
 assertTrue(isString.apply(string));
 }

 @Test(expected = ClassCastException.class)
 public void testInstanceOfWithObject() {
 Predicate<String> isString = Predicates.instanceOf(String.class);
 Object obj = new Object();
 isString.apply(obj);
 }

 @Test(expected = NullPointerException.class)
 public void testInstanceOfWithNullClass() {
 Predicate<String> isString = Predicates.instanceOf(null);
 }

 @Test
 public void testAssignableFrom() {
 Predicate<Class<?>> isAssignableFromNumber = Predicates.assignableFrom(Number.class);
 Predicate<Class<?>> isAssignableFromString = Predicates.assignableFrom(String.class);
 assertTrue(isAssignableFromNumber.apply(Integer.class));
 assertTrue(isAssignableFromString.apply(StringBuilder.class));
 }

 @Test
 public void testIn() {
 Collection<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
 Predicate<Integer> inPredicate = Predicates.in(numbers);
 int number = 2;
 assertTrue(inPredicate.apply(number));
 }

 @Test
 public void testInWithOutsideElement() {
 Collection<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
 Predicate<Integer> inPredicate = Predicates.in(numbers);
 int number = 6;
 assertFalse(inPredicate.apply(number));
 }

 @Test
 public void testInWithEmptyCollection() {
 Predicate<Integer> inPredicate = Predicates.in(Collections.emptyList());
 int number = 1;
 assertFalse(inPredicate.apply(number));
 }

 @Test
 public void testCompose() {
 Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Function<Integer, Integer> addOneFunction = new Function<Integer, Integer>() {
 @Override
 public Integer apply(Integer input) {
 return input + 1;
 }
 };
 Predicate<Integer> composePredicate = Predicates.compose(isEvenPredicate, addOneFunction);
 int number = 4;
 assertTrue(composePredicate.apply(number));
 }

 @Test(expected = NullPointerException.class)
 public void testComposeWithNullPredicate() {
 Predicate<Integer> composePredicate = Predicates.compose(null, new Function<Integer, Integer>() {
 @Override
 public Integer apply(Integer input) {
 return input + 1;
 }
 });
 }

 @Test(expected = NullPointerException.class)
 public void testComposeWithNullFunction() {
 Predicate<Integer> composePredicate = Predicates.compose(new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 == 0;
 }
 }, null);
 }
}