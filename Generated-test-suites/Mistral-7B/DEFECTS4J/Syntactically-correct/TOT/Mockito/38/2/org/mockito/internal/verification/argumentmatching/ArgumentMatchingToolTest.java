import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;

public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SameSize() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new LinkedList<>();
 matchers.add(contains("expected1"));
 matchers.add(contains("expected2"));
 Object[] arguments = new Object[]{"actual1", "actual2"};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[]{0, 1}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_DifferentSize() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new LinkedList<>();
 Object[] arguments = new Object[]{"actual1"};
 matchThreadThrowable(()-> tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments), ArrayIndexOutOfBoundsException.class);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_DifferentTypesButNotToStringEquals() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new LinkedList<>();
 matchers.add(contains(1));
 Object[] arguments = new Object[]{1.0};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_ToStringEqualsButTypesNotMatch() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new LinkedList<>();
 matchers.add(new ContainsExtraTypeInformation(String.class, "expectedString"));
 Object[] arguments = new Object[]{"expectedString1"};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_ToStringEqualsAndTypesMatch() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new LinkedList<>();
 matchers.add(new ContainsExtraTypeInformation(String.class, "expectedString"));
 Object[] arguments = new Object[]{"expectedString"};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousMatch() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new LinkedList<>();
 matchers.add(new ContainsExtraTypeInformation(Integer[].class, "expectedListOfIntegers"));
 Object[] arguments = new Object[]{new Integer[]{1, 2, 3}};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[]{0}, result);
 }
}