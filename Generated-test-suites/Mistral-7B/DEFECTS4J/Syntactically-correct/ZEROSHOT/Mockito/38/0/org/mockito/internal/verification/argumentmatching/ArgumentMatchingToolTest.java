import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.verification.VerificationMode;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool argumentMatchingTool;

 @Before
 public void setup() {
 argumentMatchingTool = new ArgumentMatchingTool();
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchesAndTypesDontMatch_thenReturnIndexes() {
 List<Matcher> matchers = Arrays.asList(
 Mockito.argThat(new ContainsExtraTypeInformation<>("String", String.class)),
 Mockito.argThat(new ContainsExtraTypeInformation<>("Integer", Integer.class))
 );
 Object[] arguments = new Object[]{ "mockito", 1 };
 Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 assertArrayEquals(new Integer[]{ 0 }, indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchesButToStringDoesntMatch_thenReturnIndexes() {
 List<Matcher> matchers = Arrays.asList(
 Mockito.argThat(new ContainsExtraTypeInformation<>("String", String.class)),
 Mockito.argThat(new ContainsExtraTypeInformation<>("Integer", Integer.class))
 );
 Object[] arguments = new Object[]{ "mockito", 1L };
 Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 assertArrayEquals(new Integer[]{ 1 }, indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchesAndToStringAndTypesMatch_thenReturnEmptyArray() {
 List<Matcher> matchers = Arrays.asList(
 Mockito.argThat(new ContainsExtraTypeInformation<>("String", String.class)),
 Mockito.argThat(new ContainsExtraTypeInformation<>("String", String.class))
 );
 Object[] arguments = new Object[]{ "mockito", "mockito" };
 Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 assertArrayEquals(new Integer[0], indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenNullArguments_thenReturnEmptyArray() {
 List<Matcher> matchers = Arrays.asList(
 Mockito.argThat(new ContainsExtraTypeInformation<>("String", String.class)),
 Mockito.argThat(new ContainsExtraTypeInformation<>("Integer", Integer.class))
 );
 Object[] arguments = null;
 Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 assertArrayEquals(new Integer[0], indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenDifferentSizes_thenReturnEmptyArray() {
 List<Matcher> matchers = Arrays.asList(
 Mockito.argThat(new ContainsExtraTypeInformation<>("String", String.class)),
 Mockito.argThat(new ContainsExtraTypeInformation<>("Integer", Integer.class))
 );
 Object[] arguments = new Object[]{ "mockito" };
 Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 assertArrayEquals(new Integer[0], indexes);
 }

 @Test
 public void testSafelyMatches_whenMatcherMatchesArgument_thenReturnTrue() {
 Matcher mockMatcher = Mockito.argThat(new ContainsExtraTypeInformation<>("String", String.class));
 Object mockArgument = "mockito";

 assertTrue(argumentMatchingTool.safelyMatches(mockMatcher, mockArgument));
 }

 @Test
 public void testSafelyMatches_whenMatcherDoesntMatchArgument_thenReturnFalse() {
 Matcher mockMatcher = Mockito.argThat(new ContainsExtraTypeInformation<>("String", String.class));
 Object mockArgument = 1;

 assertFalse(argumentMatchingTool.safelyMatches(mockMatcher, mockArgument));
 }

 @Test
 public void testToStringEquals_whenToStringOfMatcherAndArgumentAreEqual_thenReturnTrue() {
 Matcher mockMatcher = Mockito.argThat(new ContainsExtraTypeInformation<>("String", String.class));
 Object mockArgument = "mockito";

 assertTrue(argumentMatchingTool.toStringEquals(mockMatcher, mockArgument));
 }

 @Test
 public void testToStringEquals_whenToStringOfMatcherAndArgumentAreNotEqual_thenReturnFalse() {
 Matcher mockMatcher = Mockito.argThat(new ContainsExtraTypeInformation<>("String", String.class));
 Object mockArgument = "not mockito";

 assertFalse(argumentMatchingTool.toStringEquals(mockMatcher, mockArgument));
 }

 @Test
 public void testToStringEquals_whenMatcherIsNull_thenThrowNPE() {
 Matcher mockMatcher = null;
 Object mockArgument = "mockito";

 try {
 argumentMatchingTool.toStringEquals(mockMatcher, mockArgument);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testToStringEquals_whenArgumentIsNull_thenThrowNPE() {
 Matcher mockMatcher = Mockito.argThat(new ContainsExtraTypeInformation<>("String", String.class));
 Object mockArgument = null;

 try {
 argumentMatchingTool.toStringEquals(mockMatcher, mockArgument);
 } catch (NullPointerException e) {
 // expected
 }
 }
}