package org.mockito.internal.verification.argumentmatching;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class ArgumentMatchingToolTest {

 private List<Matcher> createMockMatchers() {
 return Arrays.asList(
 new ContainsRegex("foo"),
 new ContainsString("bar"),
 new ContainsString("baz"),
 new IsInstanceOf(String.class)
 );
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyListAndArguments_ReturnsEmptyArray() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = createMockMatchers();
 Object[] arguments = new Object[0];
 Integer[] expected = new Integer[0];
 Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_DifferentSize_ReturnsEmptyArray() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = createMockMatchers();
 Object[] arguments = new Object[1];
 Integer[] expected = new Integer[0];
 Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspiciousArgs_ReturnsEmptyArray() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = createMockMatchers();
 Object[] arguments = new Object[]{"foo", "bar", "baz"};
 Integer[] expected = new Integer[0];
 Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_OneSuspiciousArg_ReturnsCorrectIndex() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = Arrays.asList(
 new ContainsRegex("foo"),
 new ContainsString("bar"),
 new ContainsString("baz"),
 new ContainsString("qux")
 );
 Object[] arguments = new Object[]{"foo", "bar", "baz", "qux"};
 Integer[] expected = new Integer[]{3};
 Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MultipleSuspiciousArgs_ReturnsCorrectIndices() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = Arrays.asList(
 new ContainsRegex("foo"),
 new ContainsRegex("bar"),
 new ContainsString("baz"),
 new ContainsString("qux")
 );
 Object[] arguments = new Object[]{"qux", "foo", "bar", "baz", "qux"};
 Integer[] expected = new Integer[]{0, 4};
 Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullMatchers_ThrowsIllegalArgumentException() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Object[] arguments = new Object[]{"foo", "bar", "baz"};
 tool.getSuspiciouslyNotMatchingArgsIndexes(null, arguments);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArguments_ThrowsIllegalArgumentException() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = createMockMatchers();
 tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, null);
 }
}