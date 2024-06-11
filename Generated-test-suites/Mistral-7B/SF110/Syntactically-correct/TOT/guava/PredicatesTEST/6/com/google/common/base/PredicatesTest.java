package com.google.common.base;

import com.google.common.base.Predicates;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

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
 Object obj = null;
 assertTrue(Predicates.isNull().apply(obj));

 Object nonNullObj = new Object();
 assertFalse(Predicates.isNull().apply(nonNullObj));
 }

 @Test
 public void testNotNull() {
 Object obj = new Object();
 assertFalse(Predicates.notNull().apply(null));

 Object nonNullObj = new Object();
 assertTrue(Predicates.notNull().apply(nonNullObj));
 }

 @Test
 public void testNot() {
 Predicate<Object> predicate = Predicates.isNull();
 Predicate<Object> notPredicate = Predicates.not(predicate);

 Object obj = new Object();
 assertFalse(notPredicate.apply(obj));
 assertNull(notPredicate.apply(null));
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

 Integer input = 10;
 assertTrue(andPredicate.apply(input));

 input = 5;
 assertFalse(andPredicate.apply(input));
 }

 @Test
 public void testAndMultiplePredicates() {
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
 Predicate<Integer> isFive = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input == 5;
 }
 };

 Predicate<Integer> andPredicate = Predicates.and(isEven, isPositive, isFive);

 Integer input = 10;
 assertFalse(andPredicate.apply(input));

 input = 5;
 assertTrue(andPredicate.apply(input));
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

 Integer input = 10;
 assertTrue(orPredicate.apply(input));

 input = 6;
 assertTrue(orPredicate.apply(input));
 }

 @Test
 public void testOrMultiplePredicates() {
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
 Predicate<Integer> isPositive = new Predicate<Integer>() {
 @Override
 public boolean apply(Integer input) {
 return input > 0;
 }
 };

 Predicate<Integer> orPredicate = Predicates.or(isEven, isOdd, isPositive);

 Integer input = -5;
 assertFalse(orPredicate.apply(input));

 input = 5;
 assertTrue(orPredicate.apply(input));
 }

 @Test
 public void testEqualsTo() {
 Object target = new Object();
 Predicate<Object> equalsToTarget = Predicates.equalTo(target);

 Object equalObject = target;
 assertTrue(equalsToTarget.apply(equalObject));

 Object notEqualObject = new Object();
 assertFalse(equalsToTarget.apply(notEqualObject));
 }

 @Test
 public void testInstanceOf() {
 Class<?> stringClass = String.class;
 Predicate<Object> instanceOfString = Predicates.instanceOf(stringClass);

 String string = "Test";
 assertTrue(instanceOfString.apply(string));

 int integer = 5;
 assertFalse(instanceOfString.apply(integer));
 }

 @Test
 public void testAssignableFrom() {
 Class<?> stringClass = String.class;
 Class<?> stringBufferClass = StringBuffer.class;
 Predicate<Class<?>> assignableFromString = Predicates.assignableFrom(stringClass);

 assertTrue(assignableFromString.apply(stringBufferClass));
 }

 @Test
 public void testIn() {
 List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
 Predicate<Integer> inNumbers = Predicates.in(numbers);

 Integer input = 2;
 assertTrue(inNumbers.apply(input));

 input = 6;
 assertFalse(inNumbers.apply(input));
 }

 @Test
 public void testCompose() {
 Function<Integer, Boolean> isEvenFunction = new Function<Integer, Boolean>() {
 @Override
 public Boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> isEvenPredicate = Predicates.alwaysTrue();

 Predicate<Integer> composePredicate = Predicates.compose(isEvenPredicate, isEvenFunction);

 Integer input = 10;
 assertTrue(composePredicate.apply(input));
 }

 @Test
 public void testContainsPattern() {
 String pattern = "[a-z]+";
 Predicate<CharSequence> containsPattern = Predicates.containsPattern(Pattern.compile(pattern));

 String input = "How are you?";
 assertTrue(containsPattern.apply(input));

 String invalidInput = "1234";
 assertFalse(containsPattern.apply(invalidInput));
 }
}