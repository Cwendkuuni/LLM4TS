import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerMethod() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(returnsSmartNulls.answer(any(InvocationOnMock.class))).thenReturn("Test Value");

        Object result = returnsSmartNulls.answer(invocation);

        assertEquals("Test Value", result);
    }

    @Test(expected = NullPointerException.class)
    public void testAnswerMethodWithNullInvocation() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = null;

        returnsSmartNulls.answer(invocation);
    }

    @Test
    public void testAnswerMethodWithEmptyArguments() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Object[] emptyArgs = new Object[0];

        when(returnsSmartNulls.answer(eq(invocation), anyArray())).thenReturn(emptyArgs);

        Object result = returnsSmartNulls.answer(invocation);

        assertNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAnswerMethodWithNullReturnType() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = new Method("testMethod", new Class[]{}, null);

        when(returnsSmartNulls.answer(eq(invocation), any(Method.class))).thenReturn((Object) null);

        Object result = returnsSmartNulls.answer(invocation, method);
    }

    @Test(expected = org.mockito.exceptions.SmartNullPointerException.class)
    public void testAnswerMethodWithUnstubbedToStringMethod() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method toStringMethod = new Method("toString", new Class[0], null);

        when(returnsSmartNulls.answer(eq(invocation), any(Method.class))).thenReturn(toStringMethod);

        returnsSmartNulls.answer(invocation);
    }

    @Test(expected = Throwable.class)
    public void testAnswerMethodWithUnstubbedThrowingMethod() {
        ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method throwingMethod = new Method("testThrowingMethod", new Class[0], new Class[]{Throwable.class});

        when(returnsSmartNulls.answer(eq(invocation), any(Method.class))).thenReturn(throwingMethod);

        returnsSmartNulls.answer(invocation);
    }
}

//