package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PredicatesTest {

 @Test
 public void testAlwaysTrue() {
 Predicate<Integer> predicate = Predicates.alwaysTrue();
 assertTrue(predicate.apply(null));
 assertTrue(predicate.apply(1));
 assertTrue(predicate.apply(2));
 }

 @Test
 public void testAlwaysFalse() {
 Predicate<Integer> predicate = Predicates.alwaysFalse();
 assertFalse(predicate.apply(null));
 assertFalse(predicate.apply(1));
 assertFalse(predicate.apply(2));
 }

 @Test
 public void testIsNull() {
 Predicate<Object> predicate = Predicates.isNull();
 assertNull(predicate.apply(null));
 assertFalse(predicate.apply(new Object()));
 }

 @Test
 public void testNotNull() {
 Predicate<Object> predicate = Predicates.notNull();
 assertNull(predicate.apply(null));
 assertTrue(predicate.apply(new Object()));
 }

 @Test
 public void testNot() {
 Predicate<Integer> predicateA = Predicates.isNull();
 Predicate<Integer> predicateB = Predicates.alwaysTrue();
 Predicate<Integer> predicateC = Predicates.not(predicateA);
 Predicate<Integer> predicateD = Predicates.not(predicateB);

 assertFalse(predicateC.apply(null));
 assertTrue(predicateC.apply(1));

 assertTrue(predicateD.apply(null));
 assertFalse(predicateD.apply(1));
 }

 @Test
 public void testAnd() {
 Predicate<Integer> predicateA = Predicates.isNull();
 Predicate<Integer> predicateB = Predicates.alwaysFalse();
 Predicate<Integer> predicateC = Predicates.and(predicateA, predicateB);

 assertFalse(predicateC.apply(null));
 assertFalse(predicateC.apply(1));
 }

 @Test
 public void testAndMultiple() {
 Predicate<Integer> predicateA = Predicates.isNull();
 Predicate<Integer> predicateB = Predicates.alwaysFalse();
 Predicate<Integer> predicateC = Predicates.isNotNull();
 Predicate<Integer> predicateD = Predicates.alwaysTrue();
 List<Predicate<Integer>> list = Arrays.asList(predicateA, predicateB, predicateC, predicateD);
 Predicate<Integer> predicateE = Predicates.and(list.toArray(new Predicate[list.size()]));

 assertFalse(predicateE.apply(null));
 assertFalse(predicateE.apply(1));
 assertFalse(predicateE.apply(2));

 assertFalse(predicateE.apply(null));
 assertFalse(predicateE.apply(1));
 assertTrue(predicateE.apply(2));
 }

 @Test
 public void testOr() {
 Predicate<Integer> predicateA = Predicates.isNull();
 Predicate<Integer> predicateB = Predicates.alwaysFalse();
 Predicate<Integer> predicateC = Predicates.isNotNull();
 Predicate<Integer> predicateD = Predicates.alwaysTrue();
 List<Predicate<Integer>> list = Arrays.asList(predicateA, predicateB, predicateC, predicateD);
 Predicate<Integer> predicateE = Predicates.or(list.toArray(new Predicate[list.size()]));

 assertFalse(predicateE.apply(null));
 assertFalse(predicateE.apply(1));
 assertFalse(predicateE.apply(2));

 assertTrue(predicateE.apply(null));
 assertTrue(predicateE.apply(1));
 assertTrue(predicateE.apply(2));
 }

 @Test
 public void testEqualTo() {
 Predicate<Integer> predicateA = Predicates.equalTo(1);
 Predicate<Integer> predicateB = Predicates.equalTo(2);

 assertFalse(predicateA.apply(null));
 assertFalse(predicateA.apply(1));
 assertTrue(predicateA.apply(1));

 assertFalse(predicateB.apply(null));
 assertFalse(predicateB.apply(1));
 assertFalse(predicateB.apply(2));
 }

 @Test
 public void testInstanceOf() {
 Predicate<Object> predicateA = Predicates.instanceOf(String.class);
 Predicate<Object> predicateB = Predicates.instanceOf(Integer.class);

 assertFalse(predicateA.apply(new Object()));
 assertTrue(predicateA.apply(""));

 assertFalse(predicateB.apply(new Object()));
 assertFalse(predicateB.apply(""));
 assertFalse(predicateB.apply(1));
 assertTrue(predicateB.apply(new Integer(1)));
 }

 @Test
 public void testAssignableFrom() {
 Predicate<Class<?>> predicateA = Predicates.assignableFrom(String.class);
 Predicate<Class<?>> predicateB = Predicates.assignableFrom(Number.class);

 assertFalse(predicateA.apply(Integer.class));
 assertTrue(predicateA.apply(String.class));
 assertTrue(predicateA.apply(StringBuilder.class));

 assertFalse(predicateB.apply(String.class));
 assertTrue(predicateB.apply(Integer.class));
 assertTrue(predicateB.apply(Number.class));
 }

 @Test
 public void testIn() {
 Predicate<Integer> predicateA = Predicates.in(Arrays.asList(1, 2, 3));

 assertFalse(predicateA.apply(null));
 assertFalse(predicateA.apply(0));
 assertTrue(predicateA.apply(1));
 assertTrue(predicateA.apply(2));
 assertTrue(predicateA.apply(3));
 }

 @Test
 public void testCompose() {
 Predicate<Integer> predicateA = Predicates.isNotNull();
 Function<Integer, Boolean> function = new Function<Integer, Boolean>() {
 @Override
 public Boolean apply(@Nullable Integer input) {
 return input % 2 == 0;
 }
 };
 Predicate<Integer> predicateB = Predicates.compose(predicateA, function);

 assertFalse(predicateB.apply(null));
 assertFalse(predicateB.apply(1));
 assertFalse(predicateB.apply(3));
 assertTrue(predicateB.apply(2));
 }

 @Test
 public void testContainsPattern() {
 Predicate<String> predicateA = Predicates.containsPattern("pattern");
 Predicate<String> predicateB = Predicates.containsPattern(Pattern.compile("pattern"));

 assertFalse(predicateA.apply(null));
 assertFalse(predicateA.apply(""));
 assertTrue(predicateA.apply("pattern"));
 assertTrue(predicateA.apply("pattern123"));

 assertFalse(predicateB.apply(null));
 assertFalse(predicateB.apply(""));
 assertTrue(predicateB.apply("pattern"));
 assertTrue(predicateB.apply("pattern123"));
 }
}