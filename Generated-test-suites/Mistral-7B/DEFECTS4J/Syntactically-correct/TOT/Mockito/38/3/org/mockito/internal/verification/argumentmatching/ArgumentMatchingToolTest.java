import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_sameNumberOfArguments() {
 Matcher[] matchers = new Matcher[2];
 matchers[0] = mock(Matcher.class);
 matchers[1] = mock(ContainsExtraTypeInformation.class);
 Object[] arguments = new Object[2];
 arguments[0] = "expectedString1";
 arguments[1] = 123;

 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_differentNumberOfArguments() {
 Matcher[] matchers = new Matcher[3];
 matchers[0] = mock(Matcher.class);
 matchers[1] = mock(ContainsExtraTypeInformation.class);
 Object[] arguments = new Object[2];
 arguments[0] = "expectedString1";
 arguments[1] = 123;

 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_matchingArguments() {
 Matcher[] matchers = new Matcher[2];
 matchers[0] = mock(Matcher.class);
 matchers[1] = mock(ContainsExtraTypeInformation.class);
 Object[] arguments = new Object[2];
 arguments[0] = "expectedString1";
 arguments[1] = "expectedString2";

 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nonMatchingStringArguments() {
 Matcher[] matchers = new Matcher[2];
 matchers[0] = mock(Matcher.class);
 matchers[1] = spy(new ContainsExtraTypeInformation<String>("expectedType") {
 @Override
 public boolean typeMatches(Object argument) {
 return argument instanceof String;
 }
 });
 Object[] arguments = new Object[2];
 arguments[0] = "expectedString1";
 arguments[1] = 123;

 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nonMatchingIntegerArguments() {
 Matcher[] matchers = new Matcher[2];
 matchers[0] = mock(Matcher.class);
 matchers[1] = spy(new ContainsExtraTypeInformation<Integer>(Integer.class.getName()) {
 @Override
 public boolean typeMatches(Object argument) {
 return argument instanceof Integer;
 }
 });
 Object[] arguments = new Object[2];
 arguments[0] = "expectedString1";
 arguments[1] = 123;

 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nonMatchingStringAndIntegerArguments() {
 Matcher[] matchers = new Matcher[3];
 matchers[0] = mock(Matcher.class);
 matchers[1] = mock(ContainsExtraTypeInformation.class);
 Object[] arguments = new Object[3];
 arguments[0] = "expectedString1";
 arguments[1] = "expectedString2";
 arguments[2] = 123;

 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[]{2}, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nullMatchers() {
 Object[] arguments = new Object[2];
 arguments[0] = "expectedString1";
 arguments[1] = 123;

 tool.getSuspiciouslyNotMatchingArgsIndexes(null, arguments);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nullArguments() {
 Matcher[] matchers = new Matcher[2];
 matchers[0] = mock(Matcher.class);
 matchers[1] = mock(ContainsExtraTypeInformation.class);

 tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, null);
 }

}