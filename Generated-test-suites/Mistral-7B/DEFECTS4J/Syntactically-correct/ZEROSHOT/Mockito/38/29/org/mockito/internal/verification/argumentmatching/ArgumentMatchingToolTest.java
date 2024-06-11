import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.Arrays;
import java.util.List;

public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyList() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Object[] arguments = new Object[0];
 List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation("string", "class1"));
 int[] expected = new int[0];
 int[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousMatch() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Object[] arguments = new Object[]{new Integer(1), new String("hello")};
 List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation("Integer", "Integer"), new ContainsExtraTypeInformation("String", "String"));
 int[] expected = new int[0];
 int[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_suspiciousMatch() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Object[] arguments = new Object[]{new Integer(1), new String("1")};
 List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation("Integer", "Integer"), new ContainsExtraTypeInformation("String", "String"));
 int[] expected = new int[]{1};
 int[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_multipleSuspiciousMatches() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Object[] arguments = new Object[]{new Integer(1), new Integer(2), new String("1"), new String("hello")};
 List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation("Integer", "Integer"), new ContainsExtraTypeInformation("String", "String"), new ContainsExtraTypeInformation("Integer", "Integer"), new ContainsExtraTypeInformation("String", "String"));
 int[] expected = new int[]{1, 3};
 int[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_mismatchedSizes() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Object[] arguments = new Object[]{new Integer(1)};
 List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation("Integer", "Integer"), new ContainsExtraTypeInformation("String", "String"));
 int[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }
}