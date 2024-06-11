import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.Arrays;
import java.util.List;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SameSize_Matches() {
 List<Matcher> matchers = Arrays.asList(
 new ContainsString("test"),
 new ContainsString("test")
 );
 Object[] arguments = new Object[]{"test", "test"};
 Integer[] expectedResult = new Integer[0];
 Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(expectedResult, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SameSize_NoMatches() {
 List<Matcher> matchers = Arrays.asList(
 new ContainsString("test"),
 new ContainsString("anotherTest")
 );
 Object[] arguments = new Object[]{"test", "test"};
 Integer[] expectedResult = new Integer[0];
 Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(expectedResult, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_DifferentSize() {
 List<Matcher> matchers = Arrays.asList(
 new ContainsString("test")
 );
 Object[] arguments = new Object[]{"test", "test"};
 Integer[] expectedResult = new Integer[0];
 Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(expectedResult, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_DifferentTypes_SameToString_Matches() {
 List<Matcher> matchers = Arrays.asList(
 new ContainsExtraTypeInformation(Integer.class, new StringMatcher("test"))
 );
 Object[] arguments = new Object[]{"1", "test"};
 Integer[] expectedResult = new Integer[0];
 Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(expectedResult, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_DifferentTypes_SameToString_NoMatches() {
 List<Matcher> matchers = Arrays.asList(
 new ContainsExtraTypeInformation(Integer.class, new StringMatcher("test"))
 );
 Object[] arguments = new Object[]{"1", "anotherTest"};
 Integer[] expectedResult = new Integer[0];
 Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(expectedResult, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_DifferentTypes_DifferentToString() {
 List<Matcher> matchers = Arrays.asList(
 new ContainsExtraTypeInformation(Integer.class, new StringMatcher("test")),
 new ContainsExtraTypeInformation(Double.class, new StringMatcher("test"))
 );
 Object[] arguments = new Object[]{"1", 1.0};
 Integer[] expectedResult = new Integer[]{0};
 Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_InvalidArgument() {
 List<Matcher> matchers = Arrays.asList(
 new ContainsString("test")
 );
 Object[] arguments = null;
 argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_InvalidMatcher() {
 List<Matcher> matchers = Arrays.asList(null);
 Object[] arguments = new Object[]{"test", "test"};
 argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }
}