package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.matchers.ArgumentsMatchers.argThat;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.ArgumentMatchInfo;
import org.mockito.ExposedToMock;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationOnMockAdapter;
import org.mockito.internal.matchers.CapturesArguments;

public class InvocationMatcherTest {

 private Object mock;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 this.mock = mock(TestClass.class);
 }

 @Test
 public void testEmptyMatcher() {
 InvocationMatcher matcher = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method1")),
 Collections.<Matcher>emptyList());

 InvocationMatcher test = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method1")),
 Collections.<Matcher>emptyList());

 assertTrue(matcher.matches(test));
 }

 @Test
 public void testMatcherForSingleArgument() {
 InvocationMatcher matcher = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method1")),
 argThat(new EqualsMatcher(1L)));

 InvocationMatcher test = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method1")),
 argThat(new EqualsMatcher(1L)));

 assertTrue(matcher.matches(test));
 }

 @Test
 public void testMatcherForMultipleArguments() {
 InvocationMatcher matcher = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method1")),
 Arrays.<Matcher>asList(argThat(new EqualsMatcher(1L)), argThat(new EqualsMatcher(2L))));

 InvocationMatcher test = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method1")),
 Arrays.<Matcher>asList(argThat(new EqualsMatcher(1L)), argThat(new EqualsMatcher(2L))));

 assertTrue(matcher.matches(test));
 }

 @Test
 public void testMatcherForCustomMatcher() {
 InvocationMatcher matcher = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method1")),
 argThat(new MyCustomMatcher()));

 InvocationMatcher test = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method1")),
 argThat(new MyCustomMatcher()));

 assertTrue(matcher.matches(test));
 }

 @Test
 public void testMatcherForArgumentsDifferences() {
 InvocationMatcher matcher = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method1")),
 argThat(new EqualsMatcher(1L)));

 InvocationMatcher test = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method1")),
 argThat(new EqualsMatcher(2L)));

 assertFalse(matcher.matches(test));
 }

 @Test
 public void testMatcherForSameMethodButDifferentInvocation() {
 InvocationMatcher matcher = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method1")),
 argThat(new EqualsMatcher(1L)));

 InvocationMatcher test = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method2")),
 argThat(new EqualsMatcher(1L)));

 assertFalse(matcher.matches(test));
 }

 @Test
 public void testMatcherForSameMethodButDifferentMock() {
 InvocationMatcher matcher = new InvocationMatcher(
 Mockito.mockInvocationOnMock(mock1, new MethodTestClass().getMethod("method1")),
 argThat(new EqualsMatcher(1L)));

 InvocationMatcher test = new InvocationMatcher(
 Mockito.mockInvocationOnMock(mock2, new MethodTestClass().getMethod("method1")),
 argThat(new EqualsMatcher(1L)));

 assertFalse(matcher.matches(test));
 }

 @Test
 public void testMatcherForDifferentMethods() {
 InvocationMatcher matcher = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method1")),
 argThat(new EqualsMatcher(1L)));

 InvocationMatcher test = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method2")),
 argThat(new EqualsMatcher(1L)));

 assertFalse(matcher.matches(test));
 }

 @Test
 public void testMatcherForVerifiedInvocation() {
 InvocationMatcher matcher = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method1")),
 argThat(new EqualsMatcher(1L)));

 Mockito.verify(this.mock).method1(1L);

 InvocationMatcher test = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method1")),
 argThat(new EqualsMatcher(1L)));

 assertFalse(matcher.matches(test));
 }

 @Test
 public void testMatcherForSameMethodButOverloaded() {
 InvocationMatcher matcher = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method1", int.class)),
 argThat(new EqualsMatcher(1)));

 InvocationMatcher test = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method1", String.class)),
 argThat(new EqualsMatcher("test")));

 assertFalse(matcher.matches(test));
 }

 @Test
 public void testMatcherForSameMethodButOverloadedAndSameArguments() {
 InvocationMatcher matcher = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method1", int.class)),
 argThat(new EqualsMatcher(1)));

 InvocationMatcher test = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method1", int.class)),
 argThat(new EqualsMatcher(1)));

 assertTrue(matcher.matches(test));
 }

 @Test
 public void testMatcherForSameMethodButOverloadedAndDifferentArguments() {
 InvocationMatcher matcher = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method1", int.class)),
 argThat(new EqualsMatcher(1)));

 InvocationMatcher test = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method1", int.class)),
 argThat(new EqualsMatcher(2)));

 assertFalse(matcher.matches(test));
 }

 @Test
 public void testMatcherForSameMethodButOverloadedAndSameArgumentsDifferentType() {
 InvocationMatcher matcher = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method1", int.class)),
 argThat(new EqualsMatcher(1)));

 InvocationMatcher test = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method1", Integer.class)),
 argThat(new EqualsMatcher(1)));

 assertTrue(matcher.matches(test));
 }

 @Test
 public void testMatcherForSameMethodButOverloadedAndDifferentArgumentsDifferentType() {
 InvocationMatcher matcher = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method1", int.class)),
 argThat(new EqualsMatcher(1)));

 InvocationMatcher test = new InvocationMatcher(
 Mockito.mockInvocationOnMock(this.mock, new MethodTestClass().getMethod("method1", Integer.class)),
 argThat(new EqualsMatcher(2)));

 assertFalse(matcher.matches(test));
 }

 private static class MethodTestClass {
 public Method method1(int arg) {
 return null;
 }

 public Method method1(String arg) {
 return null;
 }
 }

 private static class EqualsMatcher implements Matcher<Object> {
 private final Object expected;

 public EqualsMatcher(Object expected) {
 this.expected = expected;
 }

 @Override
 public boolean matches(Object argument) {
 return expected.equals(argument);
 }
 }

 private static class MyCustomMatcher implements Matcher<Object> {
 @Override
 public boolean matches(Object argument) {
 return true; // replace with your custom matching logic
 }
 }

 @ExposedToMock
 private static class InvocationOnMockAdapter extends InvocationOnMock {
 @Override
 public Object[] getArguments() {
 return arguments;
 }
 }
}