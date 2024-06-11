package com.google.common.base;

import com.google.common.base.Predicates;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(JUnit4.class)
public class PredicatesTest {

 @Test
 public void testAlwaysTrue() {
 Object value = new Object();
 assertTrue(Predicates.alwaysTrue().apply(value));
 }

 @Test
 public void testAlwaysFalse() {
 Object value = new Object();
 assertFalse(Predicates.alwaysFalse().apply(value));
 }

 @Test
 public void testIsNull() {
 Object value = null;
 assertTrue(Predicates.isNull().apply(value));

 Object notNullValue = new Object();
 assertFalse(Predicates.isNull().apply(notNullValue));
 }

 @Test
 public void testNotNull() {
 Object value = new Object();
 assertFalse(Predicates.notNull().apply(null));

 Object notNullValue = new Object();
 assertTrue(Predicates.notNull().apply(notNullValue));
 }

 @Test
 public void testNot() {
 Predicate<Boolean> alwaysTrue = Predicates.alwaysTrue();
 Predicate<Boolean> alwaysFalse = Predicates.alwaysFalse();

 Predicate<Boolean> notAlwaysTrue = Predicates.not(alwaysTrue);
 Predicate<Boolean> notAlwaysFalse = Predicates.not(alwaysFalse);

 assertTrue(notAlwaysTrue.apply(false));
 assertFalse(notAlwaysTrue.apply(true));

 assertTrue(notAlwaysFalse.apply(true));
 assertFalse(notAlwaysFalse.apply(false));
 }

 @Test
 public void testAnd() {
 Predicate<Integer> isEven = Predicates.alwaysFalse(); // replace with the implementation that checks if number is even
 Predicate<Integer> isPositive = Predicates.not(Predicates.isNull()).and(Predicates.alwaysTrue().and(Predicates.isInstanceOf(Integer.class)));

 List<Predicate<Integer>> andComponents = Arrays.asList(isEven, isPositive);
 Predicate<Integer> andPredicate = Predicates.and(andComponents);

 int testValue = 4;
 assertTrue(andPredicate.apply(testValue));

 int valueThatDoesntSatisfyCondition = 0;
 assertFalse(andPredicate.apply(valueThatDoesntSatisfyCondition));
 }

 @Test
 public void testOr() {
 Predicate<Integer> isEven = Predicates.alwaysFalse(); // replace with the implementation that checks if number is even
 Predicate<Integer> isOdd = Predicates.not(isEven);

 Predicate<Integer> orComponents1 = Predicates.isNull().or(isEven);
 Predicate<Integer> orComponents2 = Predicates.isNull().or(isOdd);
 Predicate<Integer> orPredicate1 = Predicates.or(orComponents1);
 Predicate<Integer> orPredicate2 = Predicates.or(orComponents2);

 int testValue1 = 4;
 assertFalse(orPredicate1.apply(testValue1));

 int testValue2 = 1;
 assertTrue(orPredicate1.apply(testValue2));

 int valueThatDoesntSatisfyCondition1 = null;
 assertTrue(orPredicate2.apply(valueThatDoesntSatisfyCondition1));

 int valueThatDoesntSatisfyCondition2 = 2; // even number
 assertTrue(orPredicate2.apply(valueThatDoesntSatisfyCondition2));
 }

 @Test
 public void testEqualTo() {
 Object testValue = new Object();
 Predicate<Object> equalsToTestValue = Predicates.equalTo(testValue);

 assertTrue(equalsToTestValue.apply(testValue));
 assertFalse(equalsToTestValue.apply(new Object()));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testContainsPatternWithEmptyList() {
 List<String> emptyList = Collections.emptyList();
 Predicate<List<String>> containsPattern = Predicates.containsPattern("pattern");

 try {
 containsPattern.apply(emptyList);
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testContainsPattern() {
 List<String> testList = Arrays.asList("string1", "string2", "string3");
 Predicate<List<String>> containsPattern = Predicates.containsPattern("string2");

 assertTrue(containsPattern.apply(testList));

 List<String> listWithoutMatchingElement = Arrays.asList("string1", "string3");
 assertFalse(containsPattern.apply(listWithoutMatchingElement));
 }

 @Test
 public void testCompose() {
 Function<Integer, Boolean> isEvenFunction = new Function<Integer, Boolean>() {
 @Override public Boolean apply(Integer input) {
 return input % 2 == 0;
 }
 };

 Predicate<Integer> isEvenPredicate = Predicates.alwaysFalse();
 Predicate<Integer> isEvenWithCompose = Predicates.compose(isEvenPredicate, isEvenFunction);

 int testValue = 4;
 assertTrue(isEvenWithCompose.apply(testValue));

 int valueThatDoesntSatisfyCondition = 1;
 assertFalse(isEvenWithCompose.apply(valueThatDoesntSatisfyCondition));
 }
}