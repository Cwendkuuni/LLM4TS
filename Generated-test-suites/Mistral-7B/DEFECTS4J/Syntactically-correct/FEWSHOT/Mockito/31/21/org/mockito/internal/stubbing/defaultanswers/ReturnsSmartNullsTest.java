package org.mockito.internal.stubbing.defaultanswers.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;
import org.mockito.Answer;
import org.mockito.invocation.InvocationOnMock;

import org.mockito.internal.stubbing.defaultanswers.ReturnsMoreEmptyValues;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;

public class ReturnsSmartNullsTest {

	@Test
	public void testAnswerReturnsNull() {
		Object mock = mock(Object.class, new ReturnsSmartNulls());
		assertNull(ReturnsSmartNulls.class.cast(mock).answer(mock(InvocationOnMock.class)));
	}

	@Test
	public void testAnswerReturnsDefaultValueWhenReturnTypeIsMockable() {
		Object mock = mock(Object.class, new ReturnsMoreEmptyValues());
		when(mock.foo()).thenReturn("default value");
		assertEquals("default value", ReturnsSmartNulls.class.cast(mock).answer(mock(InvocationOnMock.class)));
	}

	@Test
	public void testAnswerReturnsSmartNullWhenReturnTypeIsNotMockable() {
		Object mock = mock(Object.class, new ReturnsMoreEmptyValues());
		assertNull(ReturnsSmartNulls.class.cast(mock).answer(mock(InvocationOnMock.class)));
		
		String smartNull = ReturnsSmartNulls.class.cast(mock).answer(spy(String.class));
		assertEquals("SmartNull returned by unstubbed foo() method on mock", smartNull.toString());
	}

	@Test
	public void testAnswerReturnsSmartNullWhenToStringIsCalled() {
		Object mock = mock(Object.class, new ReturnsSmartNulls());
		String smartNull = ReturnsSmartNulls.class.cast(mock).answer(mock(InvocationOnMock.class));
		assertEquals("SmartNull returned by unstubbed method on mock", smartNull.toString());
	}

	@Test
	public void testAnswerDoesNotRedirectInvocationToSpy() {
		Object mock = mock(Object.class, new ReturnsMoreEmptyValues());
		Object spy = spy(mock);
		ReturnsSmartNulls returnsSmartNulls = ReturnsSmartNulls.class.cast(mock);

		Method method = ReflectionUtils.getMethod(ReturnsSmartNulls.class, "answer", InvocationOnMock.class);
		Object[] arguments = new Object[] { mock(InvocationOnMock.class) };

		verifyNoMoreInteractions(spy);
		verifyZeroInteractions(spy);
		verify(returnsSmartNulls).delegate.answer(arguments[0]);
	}

	@Test
	public void testThrowingInterceptorThrowsExceptionWhenToStringIsCalled() {
		Object mock = mock(Object.class, new ReturnsSmartNulls());
		ReturnsSmartNulls returnsSmartNulls = ReturnsSmartNulls.class.cast(mock);
		InvocationOnMock invocation = mock(InvocationOnMock.class);

		try {
			ReturnsSmartNulls.ReturnsSmartNullsInterceptor interceptor = ReturnsSmartNulls.class.cast(ReturnsSmartNulls.class.getDeclaredField("delegate").get(returnsSmartNulls)).cast(ReturnsSmartNulls.ReturnsSmartNullsInterceptor.class);
			Method method = interceptor.getClass().getMethod("intercept", Object.class, Method.class, Object[].class, MethodProxy.class);
			method.setAccessible(true);
			method.invoke(interceptor, mock, mock(Method.class), new Object[] { mock(Object[].class) }, mock(MethodProxy.class));
		} catch (Exception e) {
			assertEquals("SmartNull returned by unstubbed method on mock", e.getCause().getCause().getMessage());
			verify(ReturnsSmartNulls.class.cast(mock)).new Reporter().smartNullPointerException(any(Location.class));
		}
	}

	private static Object mock(Class<?> clazz) {
		return mock(clazz, new ReturnsMoreEmptyValues());
	}

	private static Object spy(Object obj) {
		return spy(obj.getClass(), new ReturnsMoreEmptyValues());
	}

	private static <T> T mock(Class<T> clazz, Answer<Object> answer) {
		return mockitoMock(clazz, answer);
	}

	private static <T> T spy(Class<T> clazz, Answer<Object> answer) {
		return mockitoSpy(clazz, answer);
	}

	private static <T> T mockitoMock(Class<T> clazz, Answer<Object> answer) {
		return Mockito.mock(clazz, answer);
	}

	private static <T> T mockitoSpy(Class<T> clazz, Answer<Object> answer) {
		return Mockito.spy(clazz, answer);
	}

	private static <T> Method getMethod(Class<T> clazz, String methodName, Class<?>... parameterTypes) {
		try {
			return clazz.getMethod(methodName, parameterTypes);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	private static Object[] any(Object... objects) {
		return Arrays.asList(objects).toArray();
	}
}

class ReflectionUtils {
	public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
		try {
			return clazz.getMethod(methodName, parameterTypes);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
}