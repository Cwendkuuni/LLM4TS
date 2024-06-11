package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

	@Test
	public void testAnswer_ReturnsDefaultReturnValue() {
		ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
		Object target = mock(String.class);
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(new Method(String.class.getDeclaredMethod("toString")) {});
		when(invocation.getTarget()).thenReturn(target);

		assertEquals("", returnsSmartNulls.answer(invocation));
	}

	@Test
	public void testAnswer_ReturnsSmartNullForToString() {
		ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
		String target = mock(String.class);
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(new Method(String.class.getDeclaredMethod("toString")) {});
		when(invocation.getTarget()).thenReturn(target);

		String result = (String) returnsSmartNulls.answer(invocation);
		assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
	}

	@Test
	public void testAnswer_ReturnsNullForNonMockableType() {
		ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		Method method = new Method(int[].class.getDeclaredMethod("length")) {};
		when(invocation.getMethod()).thenReturn(method);

		assertNull(returnsSmartNulls.answer(invocation));
	}

	@Test
	public void testAnswer_ReturnsSmartNullForStubbedMethod() {
		ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
		Object target = mock(Object.class);
		Answer<Object> answer = mock(Answer.class);
		InvocationHandler handler = mock(InvocationHandler.class);
		when(handler.invoke(target, any(InvocationOnMock.class))).thenAnswer(answer);
		when(answer.answer(any(InvocationOnMock.class))).thenReturn(null);

		ReturnsSmartNulls spyReturnsSmartNulls = spy(returnsSmartNulls);
		when(spyReturnsSmartNulls.answer(any(InvocationOnMock.class))).thenAnswer(handler);

		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(new Method(Object.class.getDeclaredMethod("toString")) {});

		String result = (String) spyReturnsSmartNulls.answer(invocation);
		assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
		verify(answer).answer(invocation);
	}

	@Test
	public void testAnswer_ReturnsSmartNullForUnstubbedMethodWithThrowingInterceptor() throws Throwable {
		ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
		Object target = mock(Object.class);
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(new Method(Object.class.getDeclaredMethod("toString")) {});
		when(invocation.getTarget()).thenReturn(target);

		ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls.ThrowingInterceptor(invocation);
		ClassImposterizer classImposterizer = mock(ClassImposterizer.class);
		when(classImposterizer.imposterise(eq(throwingInterceptor), any(Class.class))).thenReturn(target);
		when(ClassImposterizer.INSTANCE).thenReturn(classImposterizer);

		Object result = returnsSmartNulls.answer(invocation);
		assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
	}

	@Test(expected = NullPointerException.class)
	public void testAnswer_ThrowsNPEForUnstubbedMethodWithoutThrowingInterceptor() throws Throwable {
		ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
		Object target = mock(Object.class);
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(new Method(Object.class.getDeclaredMethod("toString")) {});
		when(invocation.getTarget()).thenReturn(target);

		ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls.ThrowingInterceptor(invocation);
		ClassImposterizer classImposterizer = mock(ClassImposterizer.class);
		when(classImposterizer.imposterise(eq(throwingInterceptor), any(Class.class))).thenThrow(new UnsupportedOperationException());
		when(ClassImposterizer.INSTANCE).thenReturn(classImposterizer);

		ReturnsSmartNulls spyReturnsSmartNulls = spy(returnsSmartNulls);
		when(spyReturnsSmartNulls.answer(any(InvocationOnMock.class))).thenAnswer(new ReturnsMoreEmptyValues());

		Object result = spyReturnsSmartNulls.answer(invocation);
		assertNull(result);
	}
}