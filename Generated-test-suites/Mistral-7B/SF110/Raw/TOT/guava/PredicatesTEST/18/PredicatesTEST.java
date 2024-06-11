package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

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
 public void testNotNull() {
 Object obj = new Object();
 assertNotNull(Predicates.notNull().apply(obj));
 }

 @Test
 public void testNot() {
 Predicate<Boolean> predicate = Predicates.isNull();
 Predicate<Boolean> notPredicate = Predicates.not(predicate);
 assertFalse(notPredicate.apply(null));
 assertTrue(notPredicate.apply(new Boolean(true)));
 }

 @Test
 public void testAnd() {
 Predicate<String> stringPredicate1 = Predicates.isNull();
 Predicate<String> stringPredicate2 = Predicates.instanceOf(String.class);
 List<Predicate<String>> predicates = Arrays.asList(stringPredicate1, stringPredicate2);
 Predicate<String> andPredicate = Predicates.and(predicates.toArray(new Predicate[0]));
 assertNull(andPredicate.apply(null));
 String str = "test";
 assertTrue(andPredicate.apply(str));
 }

 @Test
 public void testOr() {
 Predicate<Integer> intPredicate1 = Predicates.isNull();
 Predicate<Integer> intPredicate2 = Predicates.instanceOf(Integer.class);
 Predicate<Integer> intPredicate3 = Predicates.notNull();
 List<Predicate<Integer>> predicates = Arrays.asList(intPredicate1, intPredicate2, intPredicate3);
 Predicate<Integer> orPredicate = Predicates.or(predicates.toArray(new Predicate[0]));
 assertNull(orPredicate.apply(null));
 Integer intObj = null;
 assertFalse(orPredicate.apply(intObj));
 Integer intObj2 = new Integer(10);
 assertTrue(orPredicate.apply(intObj2));
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
 public void testIn() {
 List<Integer> list = Arrays.asList(1, 2, 3, 4);
 Predicate<Integer> inPredicate = Predicates.in(list);
 assertTrue(inPredicate.apply(1));
 assertTrue(inPredicate.apply(2));
 assertTrue(inPredicate.apply(3));
 assertTrue(inPredicate.apply(4));
 assertFalse(inPredicate.apply(5));
 }

 @Test
 public void testContainsPattern() {
 String text = "test test test test test";
 Pattern pattern = Pattern.compile("test");
 Predicate<CharSequence> containsPatternPredicate = Predicates.containsPattern(pattern);
 assertTrue(containsPatternPredicate.apply(text));
 }

 @Test(expected = AssertionError.class)
 public void testComposeWithNullFunction() {
 Predicate<String> predicate = Predicates.isNull();
 Function<String, Integer> function = null;
 Predicate<String> compositionPredicate = Predicates.compose(predicate, function);
 String str = "test";
 compositionPredicate.apply(str);
 }

 @Test(expected = NullPointerException.class)
 public void testContainsPatternWithNullInput() {
 Predicate<CharSequence> containsPatternPredicate = Predicates.containsPattern(Pattern.compile("test"));
 containsPatternPredicate.apply(null);
 }
}