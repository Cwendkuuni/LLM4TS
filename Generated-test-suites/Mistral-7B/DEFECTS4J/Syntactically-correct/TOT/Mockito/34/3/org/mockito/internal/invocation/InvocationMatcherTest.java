import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.Arguments;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.Capture;
import org.mockito.internal.util.collections.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

 private InvocationMatcher invocationMatcher;
 private Object mock;

 @Before
 public void setup() {
 mock = Mockito.mock(Object.class);
 invocationMatcher = new InvocationMatcher(new InvocationOnMock(mock), Arrays.asList(argThat(isA(Integer.class))));
 }

 @Test
 public void testGetMethod() {
 Method method = invocationMatcher.getMethod();
 assertTrue(method.isAccessible());
 assertNotNull(method);
 }

 @Test
 public void testGetInvocation() {
 Invocation invocation = invocationMatcher.getInvocation();
 assertNotNull(invocation);
 assertEquals(mock, invocation.getMock());
 }

 @Test
 public void testGetMatchers() {
 List<Matcher> matchers = invocationMatcher.getMatchers();
 assertEquals(1, matchers.size());
 assertTrue(matchers.get(0) instanceof ArgumentCaptor);
 }

 @Test
 public void testMatches() {
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock), Arrays.asList(argThat(isA(Integer.class))));
 Invocation invocation = new InvocationOnMock(mock);
 invocation.setMethod(new Method("testMethod", int[].class));
 invocation.setArguments(new Object[]{new Integer(1)});

 assertTrue(invocationMatcher.matches(invocation));
 }

 @Test
 public void testHasSimilarMethod_sameMethod() {
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock), Arrays.asList(argThat(isA(Integer.class))));
 Invocation invocation = new InvocationOnMock(mock);
 invocation.setMethod(new Method("testMethod", int.class));
 invocation.setArguments(new Object[]{new Integer(1)});

 assertTrue(invocationMatcher.hasSimilarMethod(invocation));
 }

 @Test
 public void testHasSimilarMethod_differentMethod() {
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock), Arrays.asList(argThat(isA(Integer.class))));
 Invocation invocation = new InvocationOnMock(mock);
 invocation.setMethod(new Method("testMethod2", int.class));
 invocation.setArguments(new Object[]{new Integer(1)});

 assertFalse(invocationMatcher.hasSimilarMethod(invocation));
 }

 @Test
 public void testHasSimilarMethod_unverified() {
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock), Arrays.asList(argThat(isA(Integer.class))));
 Invocation invocation = new InvocationOnMock(mock);
 invocation.setMethod(new Method("testMethod", int.class));
 invocation.setArguments(new Object[]{new Integer(1)});
 invocation.reset(); // unverified

 assertTrue(invocationMatcher.hasSimilarMethod(invocation));
 }

 @Test
 public void testHasSimilarMethod_differentMock() {
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock), Arrays.asList(argThat(isA(Integer.class))));
 InvocationMatcher invocationMatcher3 = new InvocationMatcher(new InvocationOnMock(new Object()), Arrays.asList(argThat(isA(Integer.class))));
 Invocation invocation = new InvocationOnMock(mock);
 invocation.setMethod(new Method("testMethod", int.class));
 invocation.setArguments(new Object[]{new Integer(1)});

 assertFalse(invocationMatcher.hasSimilarMethod(invocationMatcher3));
 }

 @Test
 public void testHasSimilarMethod_overloaded_sameArguments() {
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock), Arrays.asList(argThat(isA(Integer.class))));
 InvocationMatcher invocationMatcher3 = new InvocationMatcher(new InvocationOnMock(mock), Arrays.asList(argThat(isA(Integer[].class))));
 Invocation invocation = new InvocationOnMock(mock);
 invocation.setMethod(new Method("testMethod", int.class));
 invocation.setArguments(new Object[]{new Integer(1)});

 assertFalse(invocationMatcher.hasSimilarMethod(invocationMatcher3));
 }

 @Test
 public void testHasSimilarMethod_overloaded_differentArguments() {
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock), Arrays.asList(argThat(isA(Integer.class))));
 InvocationMatcher invocationMatcher3 = new InvocationMatcher(new InvocationOnMock(mock), Arrays.asList(argThat(isA(Integer[].class))));
 Invocation invocation = new InvocationOnMock(mock);
 invocation.setMethod(new Method("testMethod", int[].class));
 invocation.setArguments(new Object[]{new Integer[]{new Integer(1)}});

 assertFalse(invocationMatcher.hasSimilarMethod(invocationMatcher3));
 }

 @Test
 public void testSafelyArgumentsMatch() {
 InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(mock), Arrays.asList(argThat(isA(Integer[].class))));
 Invocation invocation = new InvocationOnMock(mock);
 invocation.setMethod(new Method("testMethod", int[].class));
 invocation.setArguments(new Object[]{new Integer[]{new Integer(1), new Integer(2)}});

 assertTrue(invocationMatcher.safelyArgumentsMatch(invocation.getArguments()));
 }

 @Test(expected = Throwable.class)
 public void testSafelyArgumentsMatch_throwable() {
 InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(mock), Arrays.asList(argThat(isA(Float.class))));
 Invocation invocation = new InvocationOnMock(mock);
 invocation.setMethod(new Method("testMethod", int.class));
 invocation.setArguments(new Object[]{new Float(1.0f)});

 // throws exception
 invocationMatcher.safelyArgumentsMatch(invocation.getArguments());
 }

 @Test
 public void testToString() {
 String toString = invocationMatcher.toString();
 assertTrue(toString.contains("InvocationMatcher"));
 assertTrue(toString.contains("Method"));
 assertTrue(toString.contains("List"));
 }

 @Test
 public void testCaptureArgumentsFrom() {
 ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
 when(mock.testMethod((Capture<Integer>) argumentCaptor.capture("integerCaptor"))).thenReturn(1);
 invocationMatcher.captureArgumentsFrom(new InvocationOnMock(mock));
 Integer capturedArgument = argumentCaptor.getValue();
 assertTrue(capturedArgument != null);
 assertEquals(1, capturedArgument.intValue());
 }

 @Test
 public void testCaptureArgumentsFrom_nullArguments() {
 ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
 when(mock.testMethod((Capture<Integer>) argumentCaptor.capture("integerCaptor"))).thenReturn(1);
 invocationMatcher.captureArgumentsFrom(new InvocationOnMock(mock, new Object[0]));
 assertNull(argumentCaptor.getValue());
 }
}