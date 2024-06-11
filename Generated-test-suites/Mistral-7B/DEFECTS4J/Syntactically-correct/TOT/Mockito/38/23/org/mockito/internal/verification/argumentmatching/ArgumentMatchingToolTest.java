import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.JUnit4;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4)
public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool;

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nullArgumentList() {
 tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new LinkedList<>();
 Object[] arguments = null;
 int[] expectedResult = new int[0];
 int[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertThat(result, is(expectedResult));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyArgumentList() {
 tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new LinkedList<>();
 Object[] arguments = new Object[0];
 int[] expectedResult = new int[0];
 int[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertThat(result, is(expectedResult));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_mismatchedSizes() {
 tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new LinkedList<>();
 matchers.add(Mockito.mock(Matcher.class));
 Object[] arguments = new Object[1];
 int[] expectedResult = new int[0];
 int[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertThat(result, is(expectedResult));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousMatchers() {
 tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new LinkedList<>();
 matchers.add(Mockito.mock(Matcher.class));
 matchers.add(Mockito.mock(ContainsExtraTypeInformation.class));
 Object[] arguments = new Object[]{new Integer(1), new Integer(1)};
 int[] expectedResult = new int[0];
 int[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertThat(result, is(expectedResult));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousArgument() {
 tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new LinkedList<>();
 matchers.add(Mockito.mock(Matcher.class));
 matchers.add(Mockito.mock(ContainsExtraTypeInformation.class));
 Object[] arguments = new Object[]{new Integer(1), new Integer(2)};
 int[] expectedResult = new int[0];
 int[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertThat(result, is(expectedResult));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_suspiciousArgument() {
 tool = new ArgumentMatchingTool();
 List<Matcher> matchers = Arrays.asList(
 Mockito.argThat(new ContainsExtraTypeInformation() {
 @Override
 public boolean matches(Object argument) {
 return argument instanceof Integer;
 }

 @Override
 public String description() {
 return "an Integer";
 }

 @Override
 public String toString() {
 return "an Integer matcher";
 }

 @Override
 public boolean typeMatches(Object argument) {
 return false;
 }
 }),
 Mockito.mock(Matcher.class)
 );
 Object[] arguments = new Object[]{new Integer(1), new String("one")};
 int[] expectedResult = new int[]{1};
 int[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertThat(result, arrayContaining(contains(is(1))));
 }
}