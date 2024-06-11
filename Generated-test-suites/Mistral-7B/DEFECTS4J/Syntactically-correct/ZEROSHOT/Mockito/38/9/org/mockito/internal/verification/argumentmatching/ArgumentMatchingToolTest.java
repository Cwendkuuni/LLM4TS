import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.JUnit4;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.Times;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.isA;
import static org.mockito.ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexesWithEmptyList() {
     List<Matcher> matchers = new ArrayList<>();
     Object[] arguments = new Object[0];
     Integer[] expected = new Integer[0];
     ArgumentMatchingTool tool = new ArgumentMatchingTool();
     Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assert actual != null;
     assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexesWithNullArguments() {
     List<Matcher> matchers = Arrays.asList(mock(ContainsExtraTypeInformation.class));
     Object[] arguments = null;
     ArgumentMatchingTool tool = new ArgumentMatchingTool();
     Integer[] expected = new Integer[0];
     Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexesWithDifferentSizeListAndArray() {
     List<Matcher> matchers = Arrays.asList(mock(ContainsExtraTypeInformation.class));
     Object[] arguments = new Object[2];
     ArgumentMatchingTool tool = new ArgumentMatchingTool();
     Integer[] expected = new Integer[0];
     Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexesWithEmptyListAndEmptyArray() {
     List<Matcher> matchers = new ArrayList<>();
     Object[] arguments = new Object[0];
     ArgumentMatchingTool tool = new ArgumentMatchingTool();
     Integer[] expected = new Integer[0];
     Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexesWithEmptyListAndNonEmptyArray() {
     List<Matcher> matchers = new ArrayList<>();
     Object[] arguments = new Object[]{1};
     ArgumentMatchingTool tool = new ArgumentMatchingTool();
     Integer[] expected = new Integer[0];
     Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexesWithNonEmptyListAndEmptyArray() {
     List<Matcher> matchers = Arrays.asList(mock(ContainsExtraTypeInformation.class));
     Object[] arguments = new Object[0];
     ArgumentMatchingTool tool = new ArgumentMatchingTool();
     Integer[] expected = new Integer[0];
     Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexesWithNonEmptyListAndNonEmptyArrayButDifferentSize() {
     List<Matcher> matchers = Arrays.asList(mock(ContainsExtraTypeInformation.class), mock(ContainsExtraTypeInformation.class));
     Object[] arguments = new Object[]{1, 2};
     ArgumentMatchingTool tool = new ArgumentMatchingTool();
     Integer[] expected = new Integer[0];
     Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexesWithNonEmptyListAndNonEmptyArrayButNoSuspiciousArgs() {
     List<Matcher> matchers = Arrays.asList(mock(ContainsExtraTypeInformation.class), mock(ContainsExtraTypeInformation.class));
     Object[] arguments = new Object[]{1, 1};
     ArgumentMatchingTool tool = new ArgumentMatchingTool();
     Integer[] expected = new Integer[0];
     Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexesWithNonEmptyListAndNonEmptyArrayButSuspiciousArgs() {
     List<Matcher> matchers = Arrays.asList(mock(ContainsExtraTypeInformation.class), mock(ContainsExtraTypeInformation.class));
     Object[] arguments = new Object[]{1, "String"};
     ArgumentMatchingTool tool = new ArgumentMatchingTool();
     Integer[] expected = new Integer[]{1};
     Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(expected, actual);
 }

 @Test
 public void testSafelyMatches() throws Exception {
     Matcher mockMatcher = mock(Matcher.class);
     Object mockArgument = mock(Object.class);
     ArgumentMatchingTool tool = new ArgumentMatchingTool();
     boolean expected = true;
     boolean actual = tool.safelyMatches(mockMatcher, mockArgument);
     assertThat(actual, is(expected));
 }

 @Test
 public void testToStringEquals() throws Exception {
     Matcher mockMatcher = mock(Matcher.class);
     Object mockArgument = mock(Object.class);
     ArgumentMatchingTool tool = new ArgumentMatchingTool();
     String expected = "Matcher{}";
     String actual = tool.toString(mockMatcher);
     String description = StringDescription.toString(mockMatcher);
     assertThat(actual, is(description));
     String argString = mockArgument.toString();
     assertThat(argString, is(argString));
     assertThat(tool.toStringEquals(mockMatcher, mockArgument), is(true));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexesWithCustomMatcher() throws Exception {
     List<Matcher> matchers = Arrays.asList(mock(CustomMatcher.class));
     Object[] arguments = new Object[]{1, "String"};
     ArgumentMatchingTool tool = new ArgumentMatchingTool();
     Integer[] expected = new Integer[]{1};
     Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(expected, actual);
 }

 private static class CustomMatcher implements Matcher<Object> {
     @Override
     public boolean matches(Object argument) {
         return true;
     }

     @Override
     public void describeTo(StringDescription description) {
         description.appendText("CustomMatcher{}");
     }

     @Override
     public String toString() {
         return "CustomMatcher{}";
     }

     @Override
     public void describeMismatch(Object item, StringDescription description) {
         description.appendText("CustomMatcherMismatch{}");
     }

     @Override
     public void describeNegatedMismatch(Object item, StringDescription description) {
         description.appendText("CustomMatcherNegatedMismatch{}");
     }
 }

 private void assertArrayEquals(Integer[] expected, Integer[] actual) {
     if (expected.length != actual.length) {
         throw new AssertionError("Expected and actual arrays have different lengths.");
     }
     for (int i = 0; i < expected.length; i++) {
         if (expected[i].intValue() != actual[i].intValue()) {
             throw new AssertionError("Expected and actual arrays have different values at index " + i);
         }
     }
 }

 @Before
 public void setUp() throws Exception {
     PowerMockito.mockStatic(Whitebox.class);
 }

 @After
 public void tearDown() throws Exception {
     PowerMockito.resetAll();
 }
}