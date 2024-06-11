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
 assertNull(Predicates.isNull().apply(null));
 }

 @Test(expected = NullPointerException.class)
 public void testIsNullWithObject() {
 Predicates.isNull().apply(new Object());
 }

 @Test
 public void testNotNull() {
 Object obj = new Object();
 assertNotNull(Predicates.notNull().apply(obj));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotWithNullPredicate() {
 Predicates.not(null);
 }

 @Test
 public void testAnd() {
 Predicate<Object> predicateA = Predicates.isNull();
 Predicate<Object> predicateB = Predicates.notNull();
 Predicate<Object> andPredicate = Predicates.and(predicateA, predicateB);

 Object obj1 = null;
 Object obj2 = new Object();

 assertFalse(andPredicate.apply(obj1));
 assertFalse(andPredicate.apply(obj2));

 obj1 = new Object();
 obj2 = null;

 assertFalse(andPredicate.apply(obj1));
 assertFalse(andPredicate.apply(obj2));

 obj1 = new Object();
 obj2 = new Object();

 assertTrue(andPredicate.apply(obj1));
 assertTrue(andPredicate.apply(obj2));
 }

 @Test
 public void testOr() {
 Predicate<Object> predicateA = Predicates.isNull();
 Predicate<Object> predicateB = Predicates.notNull();
 Predicate<Object> orPredicate = Predicates.or(predicateA, predicateB);

 Object obj1 = null;
 Object obj2 = new Object();

 assertFalse(orPredicate.apply(obj1));
 assertTrue(orPredicate.apply(obj2));

 obj1 = new Object();
 obj2 = null;

 assertFalse(orPredicate.apply(obj1));
 assertTrue(orPredicate.apply(obj2));

 obj1 = new Object();
 obj2 = new Object();

 assertTrue(orPredicate.apply(obj1));
 assertTrue(orPredicate.apply(obj2));
 }

 @Test
 public void testEqualTo() {
 Object obj1 = new Object();
 Object obj2 = obj1;
 Predicate<Object> predicate = Predicates.equalTo(obj1);

 assertTrue(predicate.apply(obj1));
 assertTrue(predicate.apply(obj2));
 }

 @Test
 public void testNotEqualTo() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Predicate<Object> predicate = Predicates.not(Predicates.equalTo(obj1));

 assertFalse(predicate.apply(obj1));
 assertTrue(predicate.apply(obj2));
 }

 @Test
 public void testInstanceOf() {
 Class<String> stringClass = String.class;
 Predicate<Object> predicate = Predicates.instanceOf(stringClass);

 Object obj1 = new String("Test");
 Object obj2 = new Integer(1);

 assertTrue(predicate.apply(obj1));
 assertFalse(predicate.apply(obj2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInstanceOfWithNullClass() {
 Predicates.instanceOf(null);
 }

 @Test
 public void testAssignableFrom() {
 Class<String> stringClass = String.class;
 Class<Number> numberClass = Number.class;
 Predicate<Class<?>> predicate = Predicates.assignableFrom(stringClass);

 assertTrue(predicate.apply(stringClass));
 assertTrue(predicate.apply(Number.class));
 }

 @Test
 public void testIn() {
 List<Object> list = Arrays.asList(new Object(), new Object(), new Object());
 Predicate<Object> predicate = Predicates.in(list);

 Object obj1 = new Object();
 Object obj2 = list.get(0);

 assertFalse(predicate.apply(obj1));
 assertTrue(predicate.apply(obj2));
 }

 @Test(expected = NullPointerException.class)
 public void testInWithNullCollection() {
 Predicates.in(null);
 }

 @Test
 public void testContainsPattern() {
 String pattern = "Test";
 Predicate<CharSequence> predicate = Predicates.containsPattern(Pattern.compile(pattern));

 CharSequence obj1 = "Test String";
 CharSequence obj2 = "Different String";

 assertTrue(predicate.apply(obj1));
 assertFalse(predicate.apply(obj2));
 }

 @Test(expected = NullPointerException.class)
 public void testContainsPatternWithNullPattern() {
 Predicates.containsPattern(null);
 }
}