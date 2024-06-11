import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool argumentMatchingTool;
 private List<Matcher> matchers;
 private Object[] arguments;
 private ContainsExtraTypeInformation containsExtraTypeInformationMock;

 @Before
 public void setUp() {
 argumentMatchingTool = new ArgumentMatchingTool();
 matchers = new ArrayList<>();
 arguments = new Object[2];
 containsExtraTypeInformationMock = mock(ContainsExtraTypeInformation.class);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsAreDifferentTypesButToStringRepresentationsAreEqual_thenIndexesAreReturned() {
 matchers.add(contains(is("expectedString")));
 arguments[0] = "differentTypeString";
 matchers.add(contains(is(1)));
 arguments[1] = 1;

 when(containsExtraTypeInformationMock.typeMatches(1)).thenAnswer(new Answer<Boolean>() {
 @Override
 public Boolean answer(InvocationOnMock invocation) throws Throwable {
 return false;
 }
 });

 int[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 List<Integer> expected = new ArrayList<>();
 expected.add(0);

 assertThat(actual, is(instanceOf(Integer[].class)));
 assertThat(actual.length, is(1));
 assertThat(actual[0], is(0));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsAreSameTypesButToStringRepresentationsAreDifferent_thenNoIndexesAreReturned() {
 matchers.add(contains(is("expectedString")));
 arguments[0] = "differentToStringRepresentation";
 matchers.add(contains(is(1)));
 arguments[1] = 1;

 int[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 assertThat(actual, is(instanceOf(Integer[].class)));
 assertThat(actual.length, is(0));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsAreSameTypesAndToStringRepresentationsAreEqual_thenNoIndexesAreReturned() {
 matchers.add(contains(is("expectedString")));
 arguments[0] = "expectedToStringRepresentation";
 matchers.add(contains(is(1)));
 arguments[1] = 1;

 int[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 assertThat(actual, is(instanceOf(Integer[].class)));
 assertThat(actual.length, is(0));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersNumberIsNotEqualArgumentsNumber_thenNoIndexesAreReturned() {
 matchers.add(contains(is("expectedString")));
 matchers.add(contains(is(1)));
 arguments[0] = "string";
 arguments[1] = 1;
 arguments[2] = 2;

 int[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 assertThat(actual, is(instanceOf(Integer[].class)));
 assertThat(actual.length, is(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersNumberIsNotEqualArgumentsNumber_thenExceptionIsThrown() {
 matchers.add(contains(is("expectedString")));
 arguments[0] = "string";
 arguments[1] = 1;
 matchers.add(contains(is(2)));

 try {
 argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 } catch (IllegalArgumentException e) {
 throw new RuntimeException(e);
 }
 }
}