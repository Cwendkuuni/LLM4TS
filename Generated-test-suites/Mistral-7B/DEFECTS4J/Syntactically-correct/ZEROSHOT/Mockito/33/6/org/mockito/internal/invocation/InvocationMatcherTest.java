package org.mockito.internal.invocation;

 import static org.hamcrest.MatcherAssert.assertThat;
 import static org.hamcrest.Matchers.is;
 import static org.mockito.Mockito.mock;
 import static org.mockito.Mockito.spy;
 import static org.mockito.Mockito.verify;
 import static org.mockito.Mockito.verifyNoMoreInteractions;
 import static org.mockito.Mockito.when;

 import java.lang.reflect.Method;
 import java.util.Collections;
 import java.util.LinkedList;
 import java.util.List;

 import org.hamcrest.Matcher;
 import org.hamcrest.Matchers;
 import org.junit.Test;
 import org.junit.runner.RunWith;
 import org.mockito.ArgumentCaptor;
 import org.mockito.Captor;
 import org.mockito.ExposedForMocking;
 import org.mockito.InvocationOnMock;
 import org.mockito.internal.debugging.Location;
 import org.mockito.internal.matchers.CapturesArguments;
 import org.mockito.internal.reporting.PrintSettings;
 import org.mockito.junit.MockitoJUnitRunner;
 import org.mockito.mock.MockCreationSettings;
 import org.mockito.stub.Answer;

 @SuppressWarnings("unchecked")
 @RunWith(MockitoJUnitRunner.class)
 public class InvocationMatcherTest {

 class TestClass {

 @ExposedForMocking
 public void methodUnderTest(String arg1, int arg2, List<String> arg3) {
 // This method is used for testing purposes only, do not remove it.
 }
 }

 @Test
 public void testCreateInvocationMatcherWithEmptyMatchersList() {
 Invocation invocation = mock(Invocation.class);
 InvocationMatcher matcher = new InvocationMatcher(invocation);
 Method method = invocation.getMethod();
 List<Matcher> matchers = matcher.getMatchers();

 assertThat(matchers, is(Collections.emptyList()));
 assertThat(method, is(invocation.getMethod()));
 }

 @Test
 public void testCreateInvocationMatcherWithMatchersList() {
 Invocation invocation = mock(Invocation.class);
 when(invocation.getMethod()).thenReturn(new Method(TestClass.class, "methodUnderTest", new Class[] { String.class, int.class, List.class }));
 List<Matcher> matchers = Collections.singletonList(Matchers.eq("expectedArg1"));
 InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

 assertThat(matchers, is(Matchers.notNullValue()));
 assertThat(matchers, is(matcher.getMatchers()));
 }

 @Test
 public void testGetInvocation() {
 Invocation invocation = mock(Invocation.class);
 InvocationMatcher matcher = new InvocationMatcher(invocation);

 assertThat(matcher.getInvocation(), is(invocation));
 }

 @Test
 public void testGetMethod() {
 Invocation invocation = mock(Invocation.class);
 InvocationMatcher matcher = new InvocationMatcher(invocation);
 Method method = invocation.getMethod();

 assertThat(matcher.getMethod(), is(method));
 }

 @Test
 public void testToString() {
 Invocation invocation = mock(Invocation.class);
 List<Matcher> matchers = Collections.singletonList(Matchers.eq("expectedArg1"));
 InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
 PrintSettings printSettings = new PrintSettings();

 String actualToString = matcher.toString(printSettings);
 String expectedToString = "Invocation on " + invocation.getMock().getClass().getCanonicalName() + ".";
 expectedToString += " Matcher: " + matchers.get(0).toString();

 assertThat(actualToString, is(expectedToString));
 }

 @Test
 public void testMatches() {
 Invocation invocation1 = mock(Invocation.class);
 Invocation invocation2 = mock(Invocation.class);
 when(invocation1.getMock()).thenReturn(spy(TestClass.class));
 when(invocation2.getMock()).thenReturn(spy(TestClass.class));
 when(invocation1.getMethod()).thenReturn(new Method(TestClass.class, "methodUnderTest", new Class[] { String.class, int.class, List.class }));
 when(invocation2.getMethod()).thenReturn(new Method(TestClass.class, "methodUnderTest", new Class[] { String.class, int.class, List.class }));
 InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
 InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

 assertThat(matcher1, is(matcher1));
 assertThat(matcher1, is(matcher2.matches(matcher1)));
 }

 @Test
 public void testSafelyArgumentsMatch() {
 Invocation invocation = mock(Invocation.class);
 Object[] arguments = new Object[] { "expectedArg1", 42, new LinkedList<String>() };
 InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.singletonList(Matchers.allOf(Matchers.instanceOf(List.class), Matchers.hasSize(1))));

 assertThat(matcher.safelyArgumentsMatch(arguments), is(true));
 }

 @Test(expected = Throwable.class)
 public void testSafelyArgumentsMatchThrowsException() {
 Invocation invocation = mock(Invocation.class);
 Object[] arguments = new Object[] { "expectedArg1", 42, new LinkedList<String>() };
 InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.singletonList(Matchers.instanceOf(String[].class)));

 matcher.safelyArgumentsMatch(arguments);
 }

 @Test
 public void testHasSimilarMethod() {
 Invocation invocation1 = mock(Invocation.class);
 Invocation invocation2 = mock(Invocation.class);
 when(invocation1.getMock()).thenReturn(spy(TestClass.class));
 when(invocation2.getMock()).thenReturn(spy(TestClass.class));
 when(invocation1.getMethod()).thenReturn(new Method(TestClass.class, "methodUnderTest", new Class[] { String.class, int.class, List.class }));
 when(invocation2.getMethod()).thenReturn(new Method(TestClass.class, "methodUnderTest", new Class[] { String.class, int.class, String[].class }));
 InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
 InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

 assertThat(matcher1.hasSimilarMethod(matcher2), is(false));
 }

 @Test
 public void testHasSameMethod() {
 Invocation invocation1 = mock(Invocation.class);
 Invocation invocation2 = mock(Invocation.class);
 when(invocation1.getMock()).thenReturn(spy(TestClass.class));
 when(invocation2.getMock()).thenReturn(spy(TestClass.class));
 when(invocation1.getMethod()).thenReturn(new Method(TestClass.class, "methodUnderTest", new Class[] { String.class, int.class, List.class }));
 when(invocation2.getMethod()).thenReturn(new Method(TestClass.class, "methodUnderTest", new Class[] { String.class, int.class, List.class }));
 InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
 InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

 assertThat(matcher1.hasSameMethod(matcher2), is(true));
 }

 @Test
 public void testCaptureArgumentsFrom() {
 Invocation invocation = mock(Invocation.class);
 when(invocation.getArguments()).thenReturn(new Object[] { "expectedArg1", 42, new LinkedList<String>() });
 InvocationMatcher matcher = new InvocationMatcher(invocation);
 @Captor ArgumentCaptor<List<String>> listCaptor = ArgumentCaptor.forClass(List.class);

 matcher.captureArgumentsFrom(invocation);

 List<String> capturedList = listCaptor.getValue();

 assertThat(capturedList, is(notNullValue()));
 assertThat(capturedList, is(matcher.getInvocation().getArguments()[2]));
 }

 @Test
 public void testCreateFrom() {
 Invocation invocation1 = mock(Invocation.class);
 Invocation invocation2 = mock(Invocation.class);
 when(invocation1.getMock()).thenReturn(spy(TestClass.class));
 when(invocation2.getMock()).thenReturn(spy(TestClass.class));
 when(invocation1.getMethod()).thenReturn(new Method(TestClass.class, "methodUnderTest", new Class[] { String.class, int.class, List.class }));
 when(invocation2.getMethod()).thenReturn(new Method(TestClass.class, "methodUnderTest", new Class[] { String.class, int.class, List.class }));

 List<InvocationMatcher> matchers = InvocationMatcher.createFrom(Arrays.asList(invocation1, invocation2));

 assertThat(matchers, is(notNullValue()));
 assertThat(matchers, hasSize(2));
 assertThat(matchers.get(0), is(instanceOf(InvocationMatcher.class)));
 assertThat(matchers.get(1), is(instanceOf(InvocationMatcher.class)));
 }
 }