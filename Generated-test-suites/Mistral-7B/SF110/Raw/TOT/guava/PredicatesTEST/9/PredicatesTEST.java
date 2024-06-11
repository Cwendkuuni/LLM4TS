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
 Predicate<Object> predicate = Predicates.isNull();
 Predicate<Object> notPredicate = Predicates.not(predicate);
 Object obj1 = null;
 assertFalse(notPredicate.apply(obj1));
 Object obj2 = new Object();
 assertTrue(notPredicate.apply(obj2));
 }

 @Test
 public void testAnd() {
 Predicate<Integer> evenPredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> positivePredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input > 0;
 }
 };
 Predicate<Integer> andPredicate = Predicates.and(evenPredicate, positivePredicate);
 int input = 10;
 assertTrue(andPredicate.apply(input));
 input = 5;
 assertFalse(andPredicate.apply(input));
 }

 @Test
 public void testOr() {
 Predicate<Integer> evenPredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> oddPredicate = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input % 2 != 0;
 }
 };
 Predicate<Integer> orPredicate = Predicates.or(evenPredicate, oddPredicate);
 int input = 10;
 assertTrue(orPredicate.apply(input));
 input = 5;
 assertTrue(orPredicate.apply(input));
 }

 @Test(expected = NullPointerException.class)
 public void testAndWithNull() {
 Predicate<Object> nullPredicate = null;
 Predicate<Object> anotherPredicate = Predicates.alwaysTrue();
 Predicate<Object> andPredicate = Predicates.and(nullPredicate, anotherPredicate);
 Object obj = new Object();
 andPredicate.apply(obj);
 }

 @Test(expected = NullPointerException.class)
 public void testOrWithNull() {
 Predicate<Object> nullPredicate = null;
 Predicate<Object> anotherPredicate = Predicates.alwaysTrue();
 Predicate<Object> orPredicate = Predicates.or(nullPredicate, anotherPredicate);
 Object obj = new Object();
 orPredicate.apply(obj);
 }
}