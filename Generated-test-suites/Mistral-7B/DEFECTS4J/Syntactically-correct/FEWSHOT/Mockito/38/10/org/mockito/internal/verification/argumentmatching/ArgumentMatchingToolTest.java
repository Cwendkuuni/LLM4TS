package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.matchers.ContainsExtraTypeInformation.typeMatches;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes() {
 String description = "Description";
 Matcher<String> matcher1 = new TypeSafeMatcher<String>() {
 @Override
 public boolean matchesSafely(String item) {
 return item.equals("foo");
 }

 @Override
 public void describeTo(StringDescription description) {
 description.appendText("matches 'foo'");
 }
 };
 Matcher<Integer> matcher2 = new TypeSafeMatcher<Integer>() {
 @Override
 public boolean matchesSafely(Integer item) {
 return item.equals(1);
 }

 @Override
 public void describeTo(StringDescription description) {
 description.appendText("matches 1");
 }
 };
 Matcher<String> matcher3 = new ContainsExtraTypeInformation("Contains \"bar\"", String.class) {
 @Override
 public boolean matchesSafely(Object item) {
 String str = (String) item;
 return str.contains("bar");
 }

 @Override
 public void describeTo(StringDescription description) {
 description.appendText("contains 'bar' in String");
 }
 };
 List<Matcher<Object>> matchers = Arrays.asList(matcher1, matcher2, matcher3);
 Object[] arguments = new Object[]{"foo", 1, "bar"};

 Integer[] expected = new Integer[]{1, 2};
 Integer[] actual = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyList() {
 List<Matcher<Object>> matchers = Arrays.asList();
 Object[] arguments = new Object[]{1, "foo"};

 assertNull(ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_differentSize() {
 List<Matcher<Object>> matchers = Arrays.asList(matcher1, matcher2, matcher3);
 Object[] arguments = new Object[]{"foo", 1, "bar", 42};

 assertNull(ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousArgs() {
 List<Matcher<Object>> matchers = Arrays.asList(matcher1, matcher2, matcher3);
 Object[] arguments = new Object[]{"foo", 1, "foo"};

 assertNull(ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_sameTypesAndValues() {
 List<Matcher<Object>> matchers = Arrays.asList(matcher1, matcher1, matcher3);
 Object[] arguments = new Object[]{"foo", "foo", "bar"};

 assertNull(ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_differentTypesButSameValues() {
 List<Matcher<Object>> matchers = Arrays.asList(matcher1, matcher2, matcher3);
 Object[] arguments = new Object[]{"foo", 1, "bar"};

 Integer[] actual = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Integer[] expected = new Integer[]{1};

 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_sameTypesButDifferentValues() {
 List<Matcher<Object>> matchers = Arrays.asList(matcher1, matcher1, matcher3);
 Object[] arguments = new Object[]{"foo", "bar", "bar"};

 Integer[] actual = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Integer[] expected = new Integer[]{2};

 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_differentTypesAndDifferentValues() {
 List<Matcher<Object>> matchers = Arrays.asList(matcher1, matcher2, matcher3);
 Object[] arguments = new Object[]{"foo", 1, "bar"};

 Integer[] actual = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Integer[] expected = new Integer[]{1, 2};

 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_stringMatcherWithDifferentTypes() {
 List<Matcher<Object>> matchers = Arrays.asList(matcher1, matcher3, matcher3);
 Object[] arguments = new Object[]{"foo", 1, "bar"};

 Integer[] actual = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Integer[] expected = new Integer[]{1, 2};

 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationMatcherWithDifferentTypes() {
 List<Matcher<Object>> matchers = Arrays.asList(matcher1, matcher3, new ContainsExtraTypeInformation("Contains 'bar'", Integer.class) {
 @Override
 public boolean matchesSafely(Object item) {
 return ((Number) item).intValue() > 0;
 }

 @Override
 public void describeTo(StringDescription description) {
 description.appendText("contains positive integer");
 }
 });
 Object[] arguments = new Object[]{"foo", 1, "bar"};

 Integer[] actual = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Integer[] expected = new Integer[]{1, 2};

 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationMatcherWithSameTypeButDifferentValues() {
 List<Matcher<Object>> matchers = Arrays.asList(matcher1, matcher3, new ContainsExtraTypeInformation("Contains 'bar'", String.class) {
 @Override
 public boolean matchesSafely(Object item) {
 return ((String) item).contains("bar");
 }

 @Override
 public void describeTo(StringDescription description) {
 description.appendText("contains 'bar' in String");
 }
 });
 Object[] arguments = new Object[]{"foo", "foo", "bar"};

 Integer[] actual = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Integer[] expected = new Integer[]{2};

 assertArrayEquals(expected, actual);
 }
}