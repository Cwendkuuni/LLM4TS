package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.matchers.argthat.ArgThat.argThat;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

 private Object mock;
 private InvocationMatcher invocationMatcher;
 private Invocation invocation;
 private List<Matcher> matchers;
 private Method method;
 private Location location;

 @Before
 public void setUp() {
 mock = mock(TestClass.class);
 invocation = new Invocation(mock, new MethodTestClass().getMethod("test"), new Object[]{"arg1"}, new Location(1, 1), new Object[0]);
 matchers = new LinkedList<>();
 method = new MethodTestClass().getMethod("test");
 location = new Location(1, 1);
 }

 @Test
 public void test_InvocationMatcher_constructor_with_Invocation_and_empty_List_of_matchers() {
 invocationMatcher = new InvocationMatcher(invocation);
 assertEquals(invocation, invocationMatcher.getInvocation());
 assertNotNull(invocationMatcher.getMatchers());
 assertFalse(invocationMatcher.getMatchers().isEmpty());
 assertEquals(method, invocationMatcher.getMethod());
 }

 @Test
 public void test_InvocationMatcher_constructor_with_Invocation_and_List_of_matchers() {
 matchers.add(argThat(new TypeSafeMatcher<Integer>() {
 @Override
 public boolean matchesSafely(Integer item) {
 return item.intValue() > 5;
 }
 }));
 invocationMatcher = new InvocationMatcher(invocation, matchers);
 assertEquals(invocation, invocationMatcher.getInvocation());
 assertNotNull(invocationMatcher.getMatchers());
 assertFalse(invocationMatcher.getMatchers().isEmpty());
 assertEquals(method, invocationMatcher.getMethod());
 assertEquals(matchers, invocationMatcher.getMatchers());
 }

 @Test
 public void test_matches_when_invocations_are_equal() {
 InvocationMatcher otherInvocationMatcher = new InvocationMatcher(invocation);
 assertTrue(invocationMatcher.matches(otherInvocationMatcher));
 }

 @Test
 public void test_matches_when_invocations_have_different_mocks() {
 InvocationMatcher otherInvocationMatcher = new InvocationMatcher(mock("otherMock"), invocation.getMethod(), invocation.getArguments(), location, invocation.getCallSite());
 assertFalse(invocationMatcher.matches(otherInvocationMatcher));
 }

 @Test
 public void test_matches_when_invocations_have_different_methods() {
 InvocationMatcher otherInvocationMatcher = new InvocationMatcher(invocation, matchers);
 Invocation otherInvocation = new Invocation(mock, new MethodTestClass().getMethod("test2"), new Object[]{"arg2"}, location, invocation.getCallSite());
 otherInvocationMatcher.setInvocation(otherInvocation);
 assertFalse(invocationMatcher.matches(otherInvocationMatcher));
 }

 @Test
 public void test_matches_when_invocations_have_same_method_but_different_arguments() {
 InvocationMatcher otherInvocationMatcher = new InvocationMatcher(invocation, matchers);
 Invocation otherInvocation = new Invocation(mock, method, new Object[]{"arg2"}, location, invocation.getCallSite());
 otherInvocationMatcher.setInvocation(otherInvocation);
 assertFalse(invocationMatcher.matches(otherInvocationMatcher));
 }

 @Test
 public void test_matches_when_invocations_have_same_method_but_same_arguments() {
 Invocation otherInvocation = new Invocation(mock, method, new Object[]{"arg1"}, location, invocation.getCallSite());
 InvocationMatcher otherInvocationMatcher = new InvocationMatcher(otherInvocation, matchers);
 assertTrue(invocationMatcher.matches(otherInvocationMatcher));
 }

 @Test
 public void test_matches_when_invocations_have_same_method_but_arguments_are_not_comparable() {
 Invocation otherInvocation = new Invocation(mock, method, new Object[]{new Object()}, location, invocation.getCallSite());
 InvocationMatcher otherInvocationMatcher = new InvocationMatcher(otherInvocation, matchers);
 assertFalse(invocationMatcher.matches(otherInvocationMatcher));
 }

 @Test
 public void test_matches_when_invocations_have_same_method_but_arguments_match() {
 matchers.add(argThat(arg -> arg.equals("arg1")));
 Invocation otherInvocation = new Invocation(mock, method, new Object[]{"arg1"}, location, invocation.getCallSite());
 InvocationMatcher otherInvocationMatcher = new InvocationMatcher(otherInvocation, matchers);
 assertTrue(invocationMatcher.matches(otherInvocationMatcher));
 }

 @Test
 public void test_hasSameMethod_when_invocations_have_same_method() {
 assertTrue(invocationMatcher.hasSameMethod(invocationMatcher));
 }

 @Test
 public void test_hasSameMethod_when_invocations_have_different_methods() {
 InvocationMatcher otherInvocationMatcher = new InvocationMatcher(new Invocation(mock, new MethodTestClass().getMethod("test2"), new Object[]{"arg2"}, location, invocation.getCallSite()));
 assertFalse(invocationMatcher.hasSameMethod(otherInvocationMatcher));
 }

 @Test
 public void test_toString_when_no_matchers() {
 assertEquals("Invocation: test(Object) -> [mock]\n", invocationMatcher.toString());
 }

 @Test
 public void test_toString_when_matchers_are_present() {
 matchers.add(argThat(arg -> arg.equals("arg1")));
 assertEquals("Invocation: test(Object) -> [mock]\n"
 + "Arguments:\n"
 + "0: equals(Object)\n"
 + "  expected: <arg1>\n"
 + "  is : <[...]>\n", invocationMatcher.toString());
 }

 @Test
 public void test_captureArgumentsFrom_when_matchers_are_present() {
 ArgumentCaptor<String> argumentCaptor = argThat(arg -> arg.equals("arg1"));
 invocationMatcher.captureArgumentsFrom(new InvocationOnMock(mock));
 assertEquals("arg1", argumentCaptor.getValue());
 }

 @Test
 public void test_captureArgumentsFrom_when_matchers_are_not_present() {
 invocationMatcher = new InvocationMatcher(invocation);
 ArgumentCaptor<String> argumentCaptor = argThat(arg -> arg.equals("arg1"));
 invocationMatcher.captureArgumentsFrom(new InvocationOnMock(mock));
 assertNull(argumentCaptor.getValue());
 }

 @Test
 public void test_createFrom_when_invocations_list_is_empty() {
 List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(Collections.emptyList());
 assertEquals(0, invocationMatchers.size());
 }

 @Test
 public void test_createFrom_when_invocations_list_has_elements() {
 List<Invocation> invocations = new LinkedList<>();
 invocations.add(invocation);
 List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
 assertEquals(1, invocationMatchers.size());
 }
}

class MethodTestClass {
 Method test;
 Method test2;

 Method getMethod(String methodName) {
 if (methodName.equals("test")) {
 return test;
 } else {
 return test2;
 }
 }
}