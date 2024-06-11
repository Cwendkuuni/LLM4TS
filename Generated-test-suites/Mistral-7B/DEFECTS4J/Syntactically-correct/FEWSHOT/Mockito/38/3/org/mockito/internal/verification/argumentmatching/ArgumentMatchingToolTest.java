package org.mockito.internal.verification.argumentmatching;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_Empty() {
     List<Matcher> matchers = Arrays.asList();
     Object[] arguments = new Object[0];
     Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MismatchedSize() {
     List<Matcher> matchers = Arrays.asList(contains("foo"));
     Object[] arguments = new Object[2];
     Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyMatchers() {
     List<Matcher> matchers = Arrays.asList();
     Object[] arguments = new Object[]{1, "bar"};
     Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchedArgs() {
     List<Matcher> matchers = Arrays.asList(contains("foo"), contains("bar"));
     Object[] arguments = new Object[]{1, "foo"};
     Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciouslyNotMatchingArgs() {
     List<Matcher> matchers = Arrays.asList(contains("String"), contains(123L));
     Object[] arguments = new Object[]{"String", 123};
     Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciouslyNotMatchingArgs_SingleMismatch() {
     List<Matcher> matchers = Arrays.asList(contains("String"), contains(123.0));
     Object[] arguments = new Object[]{"String", 123};
     Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[]{0}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciouslyNotMatchingArgs_MultipleMismatches() {
     List<Matcher> matchers = Arrays.asList(contains("String"), contains(123.0), contains("foo"));
     Object[] arguments = new Object[]{"String", 123, "bar"};
     Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[]{1}, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullMatchers() {
     Object[] arguments = new Object[]{"String", 123};
     ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(null, arguments);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArguments() {
     List<Matcher> matchers = Arrays.asList(contains("String"));
     ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullMatchersAndArguments() {
     ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(null, null);
 }

 @Test
 public void testToStringEquals_NullMatcher() {
     Object arg = new Object(){};
     assertFalse(ArgumentMatchingTool.toStringEquals(null, arg));
 }

 @Test
 public void testToStringEquals_NullArgument() {
     Matcher m = contains("foo");
     assertFalse(ArgumentMatchingTool.toStringEquals(m, null));
 }

 @Test
 public void testToStringEquals_StringMatcher_StringArgument() {
     Matcher m = contains("foo");
     String arg = "foo";
     assertTrue(ArgumentMatchingTool.toStringEquals(m, arg));
 }

 @Test
 public void testToStringEquals_StringMatcher_IntegerArgument() {
     Matcher m = contains("foo");
     Integer arg = 42;
     assertFalse(ArgumentMatchingTool.toStringEquals(m, arg));
 }

 @Test
 public void testToStringEquals_StringMatcher_ObjectArgument() {
     Matcher m = contains("foo");
     Object arg = new Object(){};
     assertFalse(ArgumentMatchingTool.toStringEquals(m, arg));
 }

 @Test
 public void testToStringEquals_StringMatcher_DifferentStringArgument() {
     Matcher m = contains("foo");
     String arg = "bar";
     assertFalse(ArgumentMatchingTool.toStringEquals(m, arg));
 }

 @Test
 public void testToStringEquals_NumberMatcher_NumberArgument() {
     Matcher m = contains(42);
     Integer arg = 42;
     assertTrue(ArgumentMatchingTool.toStringEquals(m, arg));
 }

 @Test
 public void testToStringEquals_NumberMatcher_StringArgument() {
     Matcher m = contains(42);
     String arg = "42";
     assertTrue(ArgumentMatchingTool.toStringEquals(m, arg));
 }

 @Test
 public void testToStringEquals_NumberMatcher_ObjectArgument() {
     Matcher m = contains(42);
     Object arg = new Object(){};
     assertFalse(ArgumentMatchingTool.toStringEquals(m, arg));
 }

 @Test
 public void testToStringEquals_NullMatcher_NullArgument() {
     Object arg = null;
     assertFalse(ArgumentMatchingTool.toStringEquals(null, arg));
 }

 @Test
 public void testToStringEquals_NullMatcher_StringArgument() {
     String arg = "foo";
     assertFalse(ArgumentMatchingTool.toStringEquals(null, arg));
 }

 @Test
 public void testToStringEquals_NullMatcher_NumberArgument() {
     Integer arg = 42;
     assertFalse(ArgumentMatchingTool.toStringEquals(null, arg));
 }

 @Test
 public void testToStringEquals_NullMatcher_ObjectArgument() {
     Object arg = new Object(){};
     assertFalse(ArgumentMatchingTool.toStringEquals(null, arg));
 }

 @Test
 public void testToStringEquals_DifferentMatchers_SameArguments() {
     Matcher m1 = contains("foo");
     Matcher m2 = contains("bar");
     Object arg = "foo";
     assertFalse(ArgumentMatchingTool.toStringEquals(m1, arg));
     assertFalse(ArgumentMatchingTool.toStringEquals(m2, arg));
     assertTrue(ArgumentMatchingTool.toStringEquals(m1, m2));
 }

 @Test
 public void testToStringEquals_SameMatcher_DifferentArguments() {
     Matcher m = contains("foo");
     Object arg1 = "foo";
     Object arg2 = new Object(){};
     assertFalse(ArgumentMatchingTool.toStringEquals(m, arg1));
     assertFalse(ArgumentMatchingTool.toStringEquals(m, arg2));
 }

 @Test
 public void testToStringEquals_SameMatcher_SameArguments() {
     Matcher m = contains("foo");
     Object arg = "foo";
     assertTrue(ArgumentMatchingTool.toStringEquals(m, arg));
 }

 @Test
 public void testToStringEquals_SameMatcher_SameArguments_DifferentType() {
     Matcher m = contains("foo");
     Object arg = new Object(){};
     assertFalse(ArgumentMatchingTool.toStringEquals(m, arg));
 }

 @Test
 public void testSafelyMatches_Matcher_NullArgument() {
     Matcher m = contains("foo");
     assertFalse(ArgumentMatchingTool.safelyMatches(m, null));
 }

 @Test
 public void testSafelyMatches_Matcher_StringArgument() {
     Matcher m = contains("foo");
     String arg = "foo";
     assertTrue(ArgumentMatchingTool.safelyMatches(m, arg));
 }

 @Test
 public void testSafelyMatches_Matcher_IntegerArgument() {
     Matcher m = contains(42);
     Integer arg = 42;
     assertTrue(ArgumentMatchingTool.safelyMatches(m, arg));
 }

 @Test
 public void testSafelyMatches_Matcher_ObjectArgument() {
     Matcher m = contains("foo");
     Object arg = new Object(){};
     assertFalse(ArgumentMatchingTool.safelyMatches(m, arg));
 }

 @Test
 public void testSafelyMatches_Matcher_DifferentTypes() {
     Matcher m = contains("foo");
     Integer arg = 42;
     assertFalse(ArgumentMatchingTool.safelyMatches(m, arg));
 }

 @Test(expected = RuntimeException.class)
 public void testSafelyMatches_Matcher_NotMatchingArgument() {
     Matcher m = contains("foo");
     Object arg = "bar";
     ArgumentMatchingTool.safelyMatches(m, arg);
 }

 @Test(expected = RuntimeException.class)
 public void testSafelyMatches_Matcher_ThrowingArgumentMatcher() {
     Matcher m = new ArgumentMatcher() {
         @Override
         public boolean matches(Object argument) throws Exception {
             throw new Exception("testException");
         }
     };
     Object arg = new Object(){};
     ArgumentMatchingTool.safelyMatches(m, arg);
 }
}