package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.matchers.ContainsExtraTypeInformation.typeEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyArgumentList() {
 List<Matcher> matchers = Collections.emptyList();
 Object[] arguments = new Object[0];
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MismatchedSizes() {
 List<Matcher> matchers = Arrays.asList(typeEquals(String.class), typeEquals(Integer.class));
 Object[] arguments = new Object[]{ "foo", 42 };
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchedArguments() {
 List<Matcher> matchers = Arrays.asList(typeEquals(String.class), typeEquals(String.class));
 Object[] arguments = new Object[]{ "foo", "bar" };
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NotMatchedByType_DifferentTypes() {
 List<Matcher> matchers = Arrays.asList(typeEquals(String.class), typeEquals(Integer.class));
 Object[] arguments = new Object[]{ "foo", 42 };
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NotMatchedByType_SameTypesDifferentValues() {
 List<Matcher> matchers = Arrays.asList(typeEquals(Integer.class), typeEquals(Integer.class));
 Object[] arguments = new Object[]{ 1, 2 };
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NotMatchedByToString() {
 List<Matcher> matchers = Arrays.asList(new ContainsString("foo"), typeEquals(String.class));
 Object[] arguments = new Object[]{ "bar", "foo" };
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchedByToStringButNotType() {
 List<Matcher> matchers = Arrays.asList(typeEquals(String.class), new ContainsString("foo"));
 Object[] arguments = new Object[]{ "bar", "foo" };
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[]{ 1 }, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MultipleNotMatched() {
 List<Matcher> matchers = Arrays.asList(typeEquals(String.class), new ContainsString("foo"), typeEquals(Integer.class));
 Object[] arguments = new Object[]{ "bar", 1, "foo" };
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[]{ 1, 2 }, result);
 }

 @Test(expected = NullPointerException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullMatchers() {
 ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(null, new Object[0]);
 }

 @Test(expected = NullPointerException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArguments() {
 ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(Arrays.asList(typeEquals(String.class)), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MismatchedSizes_ThrowsIllegalArgumentException() {
 List<Matcher> matchers = Arrays.asList(typeEquals(String.class), typeEquals(Integer.class));
 Object[] arguments = new Object[]{ "foo" };
 ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }
}