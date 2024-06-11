package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

 @Test
 public void testReturnsSmartNullForUnstubbedMethodCallWithToStringReturnType() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
     Object mock = mock(String.class);

     when(mock.toString()).then(returnsSmartNulls);

     String smartNull = (String) returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class));

     verify(mock).toString();
     assertEquals("SmartNull returned by unstubbed toString method on mock", smartNull.toString());
 }

 @Test
 public void testReturnsDefaultValueIfMethodIsStubbed() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
     Object mock = mock(String.class);

     when(mock.toString()).thenReturn("foo");

     String result = (String) returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class));

     assertEquals("foo", result);
 }

 @Test
 public void testReturnsNullIfMethodReturnTypeIsNotMockable() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
     Object mock = mock(Long.class);

     ClassImposterizer classImposterizer = ClassImposterizer.INSTANCE;

     assertNull(returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class)));
     assertNull(classImposterizer.imposterise(new ThrowingInterceptor(Mockito.mock(InvocationOnMock.class)), Long.class));
 }

 @Test(expected = NullPointerException.class)
 public void testThrowsNullPointerExceptionForUnstubbedMethodCallWithNonToStringReturnType() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
     Object mock = mock(Integer.class);

     when(mock.hashCode()).thenAnswer(Mockito.callRealMethod());
     returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class));
     mock.hashCode();
 }

 @Test
 public void testThrowsSmartNullPointerExceptionForUnstubbedMethodCallWithToStringReturnType() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
     Object mock = mock(StringBuilder.class);

     when(mock.toString()).thenAnswer(returnsSmartNulls);
     String smartNull = (String) returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class));

     assertEquals("SmartNull returned by unstubbed toString method on mock", smartNull);
 }
}