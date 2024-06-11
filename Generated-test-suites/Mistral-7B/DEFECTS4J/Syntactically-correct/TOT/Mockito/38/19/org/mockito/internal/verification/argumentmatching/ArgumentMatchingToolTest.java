import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchingTools;
import org.mockito.Mockito;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.ArgumentMatchers.contain;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsAreSame_thenReturnEmptyArray() {
     List<Matcher> matchers = Arrays.asList(Mockito.mock(Matcher.class), Mockito.mock(Matcher.class));
     Object[] arguments = new Object[]{new Integer(1), new Integer(1)};

     Assert.assertArrayEquals(new Integer[0], tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsDifferByType_thenReturnIndexes() {
     List<Matcher> matchers = Arrays.asList(Mockito.mock(ContainsExtraTypeInformation.class), Mockito.mock(Matcher.class));
     Object[] arguments = new Object[]{new Integer(1), new String("1")};

     when(matchers.get(0)).thenReturn(Mockito.mock(ContainsExtraTypeInformation.class));
     when(matchers.get(0).matches(eq(arguments[0]))).thenReturn(true);
     when(matchers.get(0).typeMatches(eq(arguments[0]))).thenReturn(false);

     Assert.assertArrayEquals(new Integer[]{0}, tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenToStringRepresentationIsSameButTypesDiffer_thenReturnIndexes() {
     List<Matcher> matchers = Arrays.asList(Mockito.mock(ContainsExtraTypeInformation.class), Mockito.mock(Matcher.class));
     Object[] arguments = new Object[]{new Integer(1), new StringBuilder("1")};

     when(matchers.get(0)).thenReturn(Mockito.mock(ContainsExtraTypeInformation.class));
     when(matchers.get(0).matches(eq(arguments[0]))).thenReturn(true);
     when(matchers.get(0).typeMatches(eq(arguments[0]))).thenReturn(false);

     when(matchers.get(0).toString()).thenReturn("1");

     Assert.assertArrayEquals(new Integer[]{0}, tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatcherListSizeIsNotEqualToArgumentsSize_thenThrowIllegalArgumentException() {
     List<Matcher> matchers = Arrays.asList(Mockito.mock(Matcher.class), Mockito.mock(Matcher.class), Mockito.mock(Matcher.class));
     Object[] arguments = new Object[]{new Integer(1), new Integer(1), new Integer(2)};

     Assert.assertThat(tool, instanceOf(ArgumentMatchingTool.class));
     Assert.assertThat(tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments), is(new Integer[0]));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenNoSuspiciouslyNotMatchingArgs_thenReturnEmptyArray() {
     List<Matcher> matchers = Arrays.asList(Mockito.mock(Matcher.class), Mockito.mock(Matcher.class), Mockito.mock(Matcher.class));
     Object[] arguments = new Object[]{new Integer(1), new Integer(1), new Integer(1)};

     Assert.assertArrayEquals(new Integer[0], tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenNoMatcherInstancesContainsExtraTypeInformation_thenReturnEmptyArray() {
     List<Matcher> matchers = Arrays.asList(Mockito.mock(Matcher.class), Mockito.mock(Matcher.class));
     Object[] arguments = new Object[]{new Integer(1), new Integer(1)};

     Assert.assertArrayEquals(new Integer[0], tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }
}