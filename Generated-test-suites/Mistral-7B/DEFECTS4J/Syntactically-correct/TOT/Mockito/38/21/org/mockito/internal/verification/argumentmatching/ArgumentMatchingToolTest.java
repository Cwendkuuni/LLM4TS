import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.verification.argumentmatching.ContainsExtraTypeInformation;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes;

public class ArgumentMatchingToolTest {

 private Object[] createArgumentsWithDifferentTypes(Object[] baseArguments) {
 Object[] argumentsWithDifferentTypes = Arrays.copyOf(baseArguments, baseArguments.length);
 argumentsWithDifferentTypes[0] = new Integer(1);
 return argumentsWithDifferentTypes;
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nullArguments_returnsEmptyArray() {
 List<Matcher<Object>> matchers = null;
 Object[] arguments = new Object[0];
 Integer[] expected = new Integer[0];
 assertArrayEquals(expected, getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_mismatchingSize_returnsEmptyArray() {
 List<Matcher<Object>> matchers = Arrays.asList(Matchers.anyInt());
 Object[] arguments = new Object[1];
 Integer[] expected = new Integer[0];
 assertArrayEquals(expected, getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousMatchers_returnsEmptyArray() {
 List<Matcher<Object>> matchers = Arrays.asList(Matchers.contains("abc"), Matchers.contains("def"));
 Object[] arguments = new Object[]{"abc", "def"};
 Integer[] expected = new Integer[0];
 assertArrayEquals(expected, getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_suspiciousMatcher_returnsCorrectIndex() {
 List<Matcher<Object>> matchers = Arrays.asList(
 new ContainsExtraTypeInformation(Matchers.contains("abc"), Integer.class),
 Matchers.contains("def")
 );
 Object[] arguments = new Object[]{"abc", new Integer(1)};
 Integer[] expected = new Integer[]{0};
 assertArrayEquals(expected, getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test(expected = AssertionError.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_matcherThrowsException_throwsAssertionError() {
 List<Matcher<Object>> matchers = Arrays.asList(
 new ContainsExtraTypeInformation(
 Matchers.contains("abc"),
 new Throwable() {
 @Override public String toString() {
 throw new RuntimeException("Test exception");
 }
 }.getClass()),
 Matchers.contains("def")
 );
 Object[] arguments = new Object[]{"abc", "def"};
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_toStringMatcher_returnsCorrectIndex() {
 List<Matcher<Object>> matchers = Arrays.asList(
 new ContainsExtraTypeInformation(Matchers.stringContains("abc"), String.class),
 Matchers.contains("def")
 );
 Object[] arguments = new Object[]{"abcd", "def"};
 Integer[] expected = new Integer[]{0};
 assertArrayEquals(expected, getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_multipleSuspiciousMatchers_returnsCorrectIndices() {
 List<Matcher<Object>> matchers = Arrays.asList(
 new ContainsExtraTypeInformation(Matchers.contains("abc"), Integer.class),
 new ContainsExtraTypeInformation(Matchers.contains("xyz"), Double.class),
 Matchers.contains("def")
 );
 Object[] arguments = createArgumentsWithDifferentTypes(new Object[]{"abc", "def", new Integer(1), new Double(1.5)});
 Integer[] expected = new Integer[]{0, 2};
 assertArrayEquals(expected, getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_toStringEquals_returnsCorrectIndices() {
 List<Matcher<Object>> matchers = Arrays.asList(
 new ContainsExtraTypeInformation(Matchers.stringContains("abc"), String.class),
 new ContainsExtraTypeInformation(Matchers.contains("abc"), Integer.class)
 );
 Object[] arguments = new Object[]{"abcd", new Integer(1)};
 Integer[] expected = new Integer[]{0, 1};
 assertArrayEquals(expected, getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }
}