import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.util.StringUtils;
import org.mockito.internal.util.StringUtils.*;

import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;
    private Object mock;

    @Before
    public void setUp() {
        returnsSmartNulls = new ReturnsSmartNulls();
        mock = Mockito.mock(Object.class);
    }

    @Test
    public void testAnswerMethodReturnsSmartNull() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(mock.toString()).thenReturn(invocation);

        Object result = returnsSmartNulls.answer(invocation);

        verify(mock).toString();
        assertNotSame(null, result);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result.toString());
    }

    @Test
    public void testAnswerNullInvocation() {
        InvocationOnMock invocation = null;

        try {
            returnsSmartNulls.answer(invocation);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testAnswerUnsupportedReturnType() {
        Class<?> unsupportedReturnType = byte[].class;

        try {
            ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
            when(mock.getClass()).thenReturn(unsupportedReturnType);
            Object result = returnsSmartNulls.answer(mock(InvocationOnMock.class));

            fail("Expected NullPointerException or SmartNull, but got " + result);
        } catch (NullPointerException e) {
            // Expected exception for unsupported return types
        }
    }

    @Test
    public void testAnswerToStringMethodOfSupportedReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(mock.toString()).thenReturn("MockObject");

        Object result = returnsSmartNulls.answer(invocation);

        assertEquals("SmartNull returned by unstubbed toString() method on mock", result.toString());
    }

    @Test
    public void testAnswerInterceptorThrowsException() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        ThrowingInterceptor interceptor = new ThrowingInterceptor(invocation) {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                throw new RuntimeException("Interceptor threw an exception");
            }
        };

        ClassImposterizer classImposterizer = spy(ClassImposterizer.INSTANCE);
        Class<?> throwableClass = throwable.class;

        doReturn(interceptor).when(classImposterizer).imposterise(eq(ThrowingInterceptor.class), eq(throwableClass));

        Object result = returnsSmartNulls.answer(invocation);

        assertEquals("SmartNull returned by unstubbed throws RuntimeException Interceptor threw an exception() method on mock", result.toString());
    }
}