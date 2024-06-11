package test.junit4;

import org.junit.*; 
import static org.junit.Assert.*;
import org.mockito.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;

    @Before
    public void setup() {
        this.returnsSmartNulls = new ReturnsSmartNulls();
    }

    @Test
    public void testAnswerMethodWithNullArgument() {
        Answer<Object> answer = returnsSmartNulls;
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(new Method("testMethod", new Class[0]));
        when(answer.answer(ArgumentMatchers.eq(invocation))).thenCallRealMethod();
        Object result = answer.answer(invocation);
        assertEquals("SmartNull returned by unstubbed testMethod() method on mock", result.toString());
    }

    @Test
    public void testAnswerMethodWithNonNullArgument() {
        Answer<Object> answer = returnsSmartNulls;
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(new Method("testMethod", new Class[0]));
        when(answer.answer(ArgumentMatchers.eq(invocation))).thenCallRealMethod();
        Object result = answer.answer(invocation);
        assertNotNull(result);
    }

    @Test
    public void testAnswerMethodWithStaticMethod() {
        Answer<Object> answer = returnsSmartNulls;
        Class mockClass = mock(Class.class);
        when(mockClass.getMethod("testMethod", new Class[0])).thenCallRealMethod();
        when(answer.answer(ArgumentMatchers.eq(ArgumentMatchers.any(Class.class)))).thenCallRealMethod();
        Object result = answer.answer(mockClass);
        assertEquals("SmartNull returned by unstubbed testMethod() method on mock", result.toString());
    }

    @Test(expected = Throwable.class)
    public void testAnswerMethodWithExceptionThrowingMethod() {
        Answer<Object> answer = returnsSmartNulls;
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenThrow(new RuntimeException());
        when(answer.answer(ArgumentMatchers.eq(invocation))).thenCallRealMethod();
        answer.answer(invocation);
    }

    @Test
    public void testAnswerMethodWithMultipleReturnTypes() {
        Answer<Object> answer = returnsSmartNulls;
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = new Method("testMethod", new Class[]{Integer.class, String.class});
        when(invocation.getMethod()).thenReturn(method);
        Object[] arguments = new Object[]{1, "test"};
        when(answer.answer(ArgumentMatchers.eq(invocation))).thenCallRealMethod();
        Object[] result = answer.answer(invocation, arguments);
        assertEquals(0, result.length);
        assertEquals(null, result[0]);
        assertEquals(null, result[1]);
    }

    @Test
    public void testFormatMethodCallMethod() {
        String result = returnsSmartNulls.formatMethodCall();
        assertEquals("testMethod()", result);
    }
}