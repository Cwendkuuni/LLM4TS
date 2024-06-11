package org.mockito.internal.invocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.mock.MockitoMock;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

 private @Mock InvocationOnMock mockInvocation;
 private InvocationMatcher invocationMatcher;

 @Test
 public void testMatchesSimpleInvocation() {
 Object[] arguments = new Object[]{"testString", 1};
 Invocation invocation = mock(Invocation.class);
 when(invocation.getArguments()).thenReturn(arguments);
 when(invocation.getMock()).thenReturn(mockInvocation);
 InvocationMatcher im = new InvocationMatcher(invocation);

 InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, Arrays.asList(is("testString"), is(1)));

 assertThat(im, is(equalTo(expectedMatcher)));
 }

 @Test
 public void testMatchesInvocationWithEmptyMatchersList() {
 Object[] arguments = new Object[]{"testString", 1};
 Invocation invocation = mock(Invocation.class);
 when(invocation.getArguments()).thenReturn(arguments);
 when(invocation.getMock()).thenReturn(mockInvocation);
 InvocationMatcher im = new InvocationMatcher(invocation);

 InvocationMatcher expectedMatcher = new InvocationMatcher(invocation);

 assertThat(im, is(equalTo(expectedMatcher)));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCaptureArgumentsFromVarArgsThrowsException() {
 Object[] varargs = new Object[]{"testString1", "testString2", "testString3"};
 Invocation invocation = mock(Invocation.class);
 when(invocation.getMethod()).thenReturn(mock(Method.class));
 when(invocation.getArguments()).thenReturn(varargs);
 when(invocation.getMock()).thenReturn(mockInvocation);

 InvocationMatcher im = new InvocationMatcher(invocation);
 im.captureArgumentsFrom(invocation);
 }

 @Test
 public void testMatchesInvocationWithMatchers() {
 Object[] arguments = new Object[]{"testString", 1, new Object[]{1, 2}};
 Invocation invocation = mock(Invocation.class);
 when(invocation.getArguments()).thenReturn(arguments);
 when(invocation.getMock()).thenReturn(mockInvocation);

 InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, Arrays.asList(is("testString"), is(1), is(Arrays.asList(1, 2))));
 InvocationMatcher im = new InvocationMatcher(invocation, Arrays.asList(is("testString"), is(1), ArgumentCaptor.forClass(int[].class).capture()));

 List<Matcher> actualMatchers = im.getMatchers();
 Matcher capturedMatcher = actualMatchers.get(2);

 assertThat(capturedMatcher, is(instanceOf(ArgumentCaptor.class)));
 ArgumentCaptor<int[]> intArgumentCaptor = (ArgumentCaptor<int[]>) capturedMatcher;
 int[] capturedValues = intArgumentCaptor.getValue();

 assertThat(capturedValues, is(Arrays.asList(1, 2).toArray(new int[0])));

 assertThat(im, is(equalTo(expectedMatcher)));
 }
}