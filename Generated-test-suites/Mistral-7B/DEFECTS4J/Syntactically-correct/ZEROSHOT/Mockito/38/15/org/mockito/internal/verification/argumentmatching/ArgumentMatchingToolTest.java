import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool;
 private Stubber stubber;
 private InvocationOnMock invocation;
 private Object[] arguments;
 private List<Matcher> matchers;
 private ContainsExtraTypeInformation extraTypeMatcher;

 @Before
 public void setUp() {
 tool = new ArgumentMatchingTool();
 stubber = mock(Stubber.class);
 invocation = mock(InvocationOnMock.class);
 arguments = new Object[]{1L, "string", 3.0f, new Object() {}};
 matchers = new ArrayList<>();
 extraTypeMatcher = mock(ContainsExtraTypeInformation.class);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsDifferentSizes_thenReturnEmptyArray() {
 matchers.add(is(1L));
 matchers.add(is("string"));
 matchers.add(is(3.0f));
 matchers.add(is(new Object(){}));
 matchers.add(is(new String[]{})); // different size

 verify(stubber).thenAnswer(i -> invocation.getArgument(i.getArgumentIndex()));
 when(extraTypeMatcher.typeMatches(arguments[3])).thenReturn(false);

 int[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 assertThat(result, is(new Integer[0]));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsSameSizes_thenReturnSuspiciousIndexes() {
 matchers.clear();
 matchers.add(is(1L));
 matchers.add(is(1L)); // same object, different reference
 matchers.add(is(1L));
 matchers.add(is(1L));
 matchers.add(is(1L));

 when(extraTypeMatcher.typeMatches(arguments[0])).thenReturn(false);
 when(extraTypeMatcher.typeMatches(arguments[1])).thenReturn(true);
 when(extraTypeMatcher.typeMatches(arguments[2])).thenReturn(false);
 when(extraTypeMatcher.typeMatches(arguments[3])).thenReturn(true);
 when(extraTypeMatcher.typeMatches(arguments[4])).thenReturn(false);

 int[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 List<Integer> expected = new ArrayList<>();
 expected.add(1);
 expected.add(4);

 assertThat(result, is(expected.toArray(new Integer[0])));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenToStringEqualsButTypesDifferent_thenReturnSuspiciousIndexes() {
 matchers.clear();
 matchers.add(contains("string"));
 matchers.add(is(1L));
 matchers.add(is(1L));
 matchers.add(contains("string"));
 matchers.add(is(3.0f));

 when(extraTypeMatcher.typeMatches(arguments[0])).thenReturn(false);
 when(extraTypeMatcher.typeMatches(arguments[1])).thenReturn(true);
 when(extraTypeMatcher.typeMatches(arguments[2])).thenReturn(false);
 when(extraTypeMatcher.typeMatches(arguments[3])).thenReturn(true);
 when(extraTypeMatcher.typeMatches(arguments[4])).thenReturn(false);

 when(stubber.thenAnswer(invocation).getArgument(0)).thenReturn("otherString");

 int[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 List<Integer> expected = new ArrayList<>();
 expected.add(1);
 expected.add(3);

 assertThat(result, is(expected.toArray(new Integer[0])));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenToStringNotEqualsButTypesSame_thenReturnEmptyArray() {
 matchers.clear();
 matchers.add(contains("otherString"));
 matchers.add(is(1L));
 matchers.add(is(1L));
 matchers.add(contains("string"));
 matchers.add(is(3.0f));

 int[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 assertThat(result, is(new Integer[0]));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenToStringAndTypesSame_thenReturnEmptyArray() {
 matchers.clear();
 matchers.add(contains("string"));
 matchers.add(contains("string"));
 matchers.add(contains("string"));
 matchers.add(contains("string"));
 matchers.add(contains("string"));

 int[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 assertThat(result, is(new Integer[0]));
 }
}