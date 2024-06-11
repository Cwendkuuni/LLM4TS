package org.mockito.internal.invocation;
 import org.junit.Test;
 import org.junit.runner.RunWith;
 import org.mockito.Mockito;
 import org.mockito.invocation.InvocationOnMock;
 import org.mockito.invocation.InvocationOnMockImpl;
 import org.mockito.stub.Stubber;
 import static org.hamcrest.CoreMatchers.isA;
 import static org.hamcrest.MatcherAssert.assertThat;
 import static org.hamcrest.Matchers.is;
 import static org.hamcrest.Matchers.notNullValue;
 import static org.mockito.Mockito.mock;
 import static org.mockito.Mockito.spy;
 import static org.mockito.Mockito.verify;
 import static org.mockito.Mockito.when;

 @RunWith(org.mockito.runners.JUnit4.class)
 public class InvocationMatcherTest {

 @Test
 public void shouldCreateNewInvocationMatcherWithEmptyListOfMatchers() {
     Invocation invocation = mock(InvocationOnMock.class);
     InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
     List<Matcher> matchers = invocationMatcher.getMatchers();
     assertThat(matchers, is(Collections.emptyList()));
 }

 @Test
 public void shouldCreateNewInvocationMatcherWithListOfMatchers() {
     Invocation invocation = mock(InvocationOnMock.class);
     List<Matcher> matchers = Collections.singletonList(Mockito.mock(Matcher.class));
     InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
     List<Matcher> actualMatchers = invocationMatcher.getMatchers();
     assertThat(actualMatchers, is(matchers));
 }

 @Test
 public void testGetMethod() {
     Invocation invocation = new InvocationOnMockImpl(mock(Object.class), new Method("testMethod", new Class[0]));
     InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
     Method method = invocationMatcher.getMethod();
     assertThat(method, notNullValue());
     assertThat(method.getName(), is("testMethod"));
 }

 @Test
 public void testGetInvocation() {
     Invocation invocation = mock(InvocationOnMock.class);
     InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
     Invocation actualInvocation = invocationMatcher.getInvocation();
     assertThat(actualInvocation, is(invocation));
 }

 @Test
 public void testGetMatchers() {
     Invocation invocation = mock(InvocationOnMock.class);
     InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
     List<Matcher> matchers = Arrays.asList(Mockito.mock(Matcher.class), Mockito.mock(Matcher.class));
     InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(invocation, matchers);
     List<Matcher> actualMatchers = invocationMatcherWithMatchers.getMatchers();
     assertThat(actualMatchers, is(matchers));
 }

 @Test
 public void testToString() {
     Invocation invocation = mock(InvocationOnMock.class);
     List<Matcher> matchers = Arrays.asList(Mockito.mock(Matcher.class), Mockito.mock(Matcher.class));
     InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
     String actualString = invocationMatcher.toString();
     String expectedString = "Invocation [method=Method testMethod() returns java.lang.Void, arguments=[], invocationSite=InvocationSite [line=0, file=<not available>]] with [Matcher [is <java.lang.Object>], Matcher [is <java.lang.Object>]]";
     assertThat(actualString, is(expectedString));
 }

 @Test
 public void testMatches() {
     Invocation invocation1 = mock(InvocationOnMock.class);
     Invocation invocation2 = mock(InvocationOnMock.class);
     InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
     InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
     assertTrue(invocationMatcher1.matches(invocation1));
     assertFalse(invocationMatcher1.matches(invocation2));
 }

 @Test
 public void testSafelyArgumentsMatch() {
     Invocation invocation = mock(InvocationOnMock.class);
     Object[] arguments = new Object[]{"foo", 42, "bar"};
     InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(isA(String.class), is(42), isA(String.class)));
     assertTrue(invocationMatcher.safelyArgumentsMatch(arguments));
 }

 @Test(expected = Throwable.class)
 public void testSafelyArgumentsMatchFails() {
     Invocation invocation = mock(InvocationOnMock.class);
     Object[] arguments = new Object[]{"foo", 42, "bar"};
     InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(isA(Integer.class), is(42), isA(String.class)));
     invocationMatcher.safelyArgumentsMatch(arguments);
 }

 @Test
 public void testHasSimilarMethod() {
     Invocation invocation1 = new InvocationOnMockImpl(mock(Object.class), new Method("testMethod", new Class[0]));
     Invocation invocation2 = new InvocationOnMockImpl(mock(Object.class), new Method("testMethod", new Class[]{String.class}));
     InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
     InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
     assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
     assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher1.getInvocation()));
     assertFalse(invocationMatcher1.hasSimilarMethod(mock(InvocationMatcher.class)));
 }

 @Test
 public void testHasSameMethod() {
     Invocation invocation1 = new InvocationOnMockImpl(mock(Object.class), new Method("testMethod", new Class[0]));
     Invocation invocation2 = new InvocationOnMockImpl(mock(Object.class), new Method("testMethod", new Class[0]));
     InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
     InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
     assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
 }

 @Test
 public void testCaptureArgumentsFrom() {
     Object[] arguments = new Object[]{"foo", 42, "bar"};
     Invocation invocation = new InvocationOnMockImpl(mock(Object.class), new Method("testMethod", new Class[0]), arguments);
     InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
     Stubber stubber = Mockito.stub(InvocationMatcher.class);
     when(stubber.getMatchers()).thenReturn(Arrays.asList(Mockito.mock(CapturesArguments.class)));
     InvocationMatcher invocationMatcherSpy = spy(invocationMatcher, stubber);
     invocationMatcherSpy.captureArgumentsFrom(invocation);
     for (int i = 0; i < invocation.getRawArguments().length; i++) {
         Object capturedArgument = stubber.getMockitoContext().getCaptor().getAllValues().get(i);
         assertEquals(invocation.getRawArguments()[i], capturedArgument);
     }
 }

 }