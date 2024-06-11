import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Annotations;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Arguments;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool;
 private List<Matcher> matchers;
 private Object[] arguments;

 @Before
 public void setup() {
 Annotations.initMocks(this);
 tool = new ArgumentMatchingTool();
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsDontMatch() {
 matchers = Arrays.asList(Mockito.argThat(instanceOf(String.class)), Mockito.anyInt());
 arguments = new Object[]{new Integer(42), "test"};

 Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 assertThat(indexes, containsInAnyOrder(0, 1));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsMatchButTypesDont() {
 matchers = Arrays.asList(Mockito.argThat(isA(Integer.class)), Mockito.anyString());
 arguments = new Object[]{new Integer(42), "test"};

 Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 assertThat(indexes, isA(Integer[].class));
 assertThat(indexes.length, is(0));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsToStringRepresentationAreTheSameButTypesDontMatch() {
 matchers = Arrays.asList(Mockito.argThat(containsString("test")), Mockito.anyInt());
 arguments = new Object[]{new Integer(42), "test"};

 Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 assertThat(indexes, isA(Integer[].class));
 assertThat(indexes.length, is(0));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsCountMismatch() {
 matchers = Arrays.asList(Mockito.argThat(instanceOf(String.class)));
 arguments = new Object[]{new Integer(42)};

 tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersContainExtraTypeInformationMatcher() {
 matchers = Arrays.asList(Mockito.argThat(new ContainsExtraTypeInformation("String") {
 @Override
 public boolean typeMatches(Object argument) {
 return argument instanceof String;
 }
 }), Mockito.anyInt());
 arguments = new Object[]{new Integer(42), "test"};

 Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 assertThat(indexes, isA(Integer[].class));
 assertThat(indexes.length, is(1));
 assertThat(indexes[0], is(0));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsToStringRepresentationAreTheSameAndTypesMatch() {
 matchers = Arrays.asList(Mockito.argThat(containsString("test")), Mockito.anyInt());
 arguments = new Object[]{new StringBuilder("test"), new Integer(42)};

 Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

 assertThat(indexes, isA(Integer[].class));
 assertThat(indexes.length, is(0));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_mockAndInvokedWithArguments() {
 List<Matcher> expectedMatchers = Arrays.asList(Mockito.argThat(isA(String.class)), Mockito.argThat(isA(Integer.class)));
 Object[] expectedArguments = new Object[]{"test", 42};

 MockedObject mockedObject = mock(MockedObject.class);
 InvocationOnMock invocation = Mockito.mockInvoke(mockedObject, "methodWithTwoArgs", expectedArguments);

 Mockito.verify(mockedObject).methodWithTwoArgs(argThat(matchers.get(0)), argThat(matchers.get(1)));
 verify(mockedObject).methodWithTwoArgs(argThat(Matchers.anyString()), argThat(Matchers.anyInt()));

 Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, invocation.getArguments());

 assertThat(indexes, isA(Integer[].class));
 assertThat(indexes.length, is(0));
 }
}

class MockedObject {
 void methodWithTwoArgs(String arg1, Integer arg2) {
 // empty body
 }
}