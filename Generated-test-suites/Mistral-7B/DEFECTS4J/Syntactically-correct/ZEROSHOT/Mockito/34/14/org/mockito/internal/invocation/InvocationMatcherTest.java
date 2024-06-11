package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.reflection.Reflector;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util. collections.List;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class InvocationMatcherTest {

 private Invocation invocation;
 private InvocationMatcher invocationMatcher;
 private Object mock;
 private Method method;
 private List<Matcher> matchers;

 @Before
 public void setUp() {
 mock = mock(Object.class);
 method = Reflector.findMethod(Object.class, "toString", (Class[]) null);
 invocation = new Invocation(new Object[]{}, new Object[]{}, method, null, new Location(1, 1));
 matchers = Arrays.asList(Matchers.is("test"), new CapturesArguments());
 invocationMatcher = new InvocationMatcher(invocation, matchers);
 }

 @Test(expected = MockitoException.class)
 public void testInvocationMatcherNullInvocation() {
 new InvocationMatcher(null, matchers);
 }

 @Test
 public void testInvocationMatcherEmptyMatchers() {
 InvocationMatcher invocationMatcherEmpty = new InvocationMatcher(invocation);
 assertThat(invocationMatcherEmpty.getMatchers(), is(invocation.argumentsToMatchers()));
 }

 @Test
 public void testGetMethod() {
 Method expectedMethod = method;
 Method actualMethod = invocationMatcher.getMethod();
 assertThat(actualMethod, is(expectedMethod));
 }

 @Test
 public void testGetInvocation() {
 Invocation expectedInvocation = invocation;
 Invocation actualInvocation = invocationMatcher.getInvocation();
 assertThat(actualInvocation, is(expectedInvocation));
 }

 @Test
 public void testGetMatchers() {
 List<Matcher> expectedMatchers = matchers;
 List<Matcher> actualMatchers = invocationMatcher.getMatchers();
 assertThat(actualMatchers, is(expectedMatchers));
 }

 @Test
 public void testToString() {
 String expectedToString = invocation.toString(matchers, new PrintSettings());
 String actualToString = invocationMatcher.toString();
 assertThat(actualToString, is(expectedToString));
 }

 @Test
 public void testMatches() {
 Object[] invocationArguments = new Object[0];
 Invocation actualInvocation = new InvocationOnMock(mock);{ actualInvocation.setArguments(invocationArguments); }
 InvocationMatcher invocationMatcherToTest = new InvocationMatcher(invocation, matchers);
 assertThat(invocationMatcherToTest.matches(actualInvocation), is(true));
 }

 @Test
 public void testSafelyArgumentsMatch() {
 Object[] actualArguments = new Object[0];
 InvocationMatcher invocationMatcherToTest = new InvocationMatcher(invocation, matchers);
 boolean result = invocationMatcherToTest.safelyArgumentsMatch(actualArguments);
 assertThat(result, is(true));
 }

 @Test
 public void testHasSameMethod() {
 Invocation candidateInvocation = new InvocationOnMock(mock);{ candidateInvocation.setMethod(method); }
 InvocationMatcher invocationMatcherToTest = new InvocationMatcher(invocation, matchers);
 InvocationMatcher candidateMatcher = new InvocationMatcher(candidateInvocation, Collections.<Matcher>emptyList());
 boolean result = invocationMatcherToTest.hasSameMethod(candidateMatcher);
 assertThat(result, is(true));
 }

 @Test
 public void testHasSimilarMethod() {
 Invocation candidateInvocation1 = new InvocationOnMock(mock);{ candidateInvocation1.setMethod(Reflector.findMethod(Object.class, "toString", (Class[]) null)); }
 Invocation candidateInvocation2 = new InvocationOnMock(mock);{ candidateInvocation2.setMethod(Reflector.findMethod(Object.class, "toString", new Class[]{String.class})); }
 InvocationMatcher invocationMatcherToTest = new InvocationMatcher(invocation, matchers);
 InvocationMatcher candidateMatcher1 = new InvocationMatcher(candidateInvocation1, Collections.<Matcher>emptyList());
 InvocationMatcher candidateMatcher2 = new InvocationMatcher(candidateInvocation2, Collections.<Matcher>emptyList());
 boolean result1 = invocationMatcherToTest.hasSimilarMethod(candidateMatcher1);
 boolean result2 = invocationMatcherToTest.hasSimilarMethod(candidateMatcher2);
 assertThat(result1, is(true));
 assertThat(result2, is(false));
 }

 @Test
 public void testCaptureArgumentsFrom() {
 Object[] invocationArguments = new Object[0];
 Object[] expectedCapturedArguments = new Object[1];
 Object[] actualCapturedArguments = new Object[1];
 Object argToCapture = new Object();
 matchers.get(0).forEachArgument(new ArgumentCaptor<Object>() {
 @Override public void capture(Object argument) {
 expectedCapturedArguments[0] = argument;
 }

 @Override public void setValue(Object value) {
 // Not used
 }
 });
 invocation.setArguments(invocationArguments);
 invocationMatcher = new InvocationMatcher(invocation, matchers);
 invocationMatcher.captureArgumentsFrom(invocation);
 actualCapturedArguments = (Object[]) invocation.getArguments()[0];
 assertThat(actualCapturedArguments[0], is(argToCapture));
 }

 @Test
 public void testCaptureArgumentsFromThrowsExceptionForNonCaptureArguments() {
 Object[] invocationArguments = new Object[0];
 Matcher nonCaptureMatcher = is("test");
 invocation.setArguments(invocationArguments);
 invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(nonCaptureMatcher));
 try {
 invocationMatcher.captureArgumentsFrom(invocation);
 fail();
 } catch (MockitoException e) {
 // Expected
 }
 }
}