import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.spy(new ReturnsSmartNulls());
        InvocationOnMock invocation = Mockito.mock(Object.class);
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswerWithNonnullInvocation() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.spy(new ReturnsSmartNulls());
        InvocationOnMock invocation = Mockito.mock(Object[].class);
        try {
            returnsSmartNulls.answer(invocation);
            fail("Expected smart null, but got an actual return value.");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testAnswerWithMockableReturnType() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.spy(new ReturnsSmartNulls());
        ClassImposterizer classImposterizer = Mockito.spy(ClassImposterizer.INSTANCE);
        Mockito.doReturn(classImposterizer).when(returnsSmartNulls).getClassImposterizer();
        Object target = Mockito.mock(String.class);
        InvocationOnMock invocation = Mockito.mock(Method.class);
        when(invocation.getMethod()).thenReturn(new Method(String.class.getName(), "toString", new Class[0]));
        when(classImposterizer.imposterise(any(MethodInterceptor.class), any(Class.class))).thenReturn(Mockito.mock(String.class));
        Object result = returnsSmartNulls.answer(invocation);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result.toString());
    }

    @Test(expected = Throwable.class)
    public void testAnswerWithThrowsException() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.spy(new ReturnsSmartNulls());
        InvocationOnMock invocation = Mockito.mock(Object.class);
        when(invocation.getMethod()).thenThrow(new RuntimeException());
        returnsSmartNulls.answer(invocation);
    }

    @Test
    public void testAnswerWithEmptyArray() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.spy(new ReturnsSmartNulls());
        InvocationOnMock invocation = Mockito.mock(Object[].class);
        when(invocation.getMethod()).thenReturn(new Method(Object[].class.getName(), "length", new Class[0]));
        when(invocation.getArgumentValues()).thenReturn(ArgumentMatchers.anyArray());
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }
}