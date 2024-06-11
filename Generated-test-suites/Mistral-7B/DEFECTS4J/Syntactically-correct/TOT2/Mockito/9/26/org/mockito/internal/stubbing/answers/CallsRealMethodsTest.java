import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CallsRealMethodsTest {

    @Test
    public void testAnswerMethodWithNullAnswer() {
        CallsRealMethods answer = null;
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        try {
            answer.answer(invocation);
            fail("Method should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testAnswerMethodWithObjectInvocation() {
        CallsRealMethods answer = new CallsRealMethods();
        Object object = new Object();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getTarget()).thenReturn(answer);
        when(invocation.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[0]));
        when(invocation.getArguments()).thenReturn(new Object[0]);

        Object result = answer.answer(invocation);
        assertEquals(object, result);
    }

    @Test(expected = Throwable.class)
    public void testAnswerMethodWithThrowableInvocation() throws Throwable {
        CallsRealMethods answer = new CallsRealMethods();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getTarget()).thenReturn(answer);
        when(invocation.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[0]));
        when(invocation.getArguments()).thenReturn(new Object[0]);
        doThrow(new RuntimeException("Test exception")).when(invocation).callRealMethod();

        answer.answer(invocation);
    }

    @Test
    public void testAnswerMethodWithSerializableAnswer() {
        CallsRealMethods answer = new CallsRealMethods();
        Serializable serializableObject = new Serializable() {
            @Override
            public String toString() {
                return "Serializable object";
            }
        };
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getTarget()).thenReturn(answer);
        when(invocation.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[0]));
        when(invocation.getArguments()).thenReturn(new Object[0]);

        Serializable result = (Serializable) answer.answer(invocation);
        assertEquals(serializableObject, result);
    }

    @Test
    public void testAnswerMethodWithArrayInvocation() {
        CallsRealMethods answer = new CallsRealMethods();
        Object[] array = new Object[1];
        array[0] = new Object();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getTarget()).thenReturn(answer);
        when(invocation.getMethod()).thenReturn(new Method(Object[].class, "set", new Class[] { Object.class }));
        when(invocation.getArguments()).thenReturn(array);

        Object result = answer.answer(invocation);
        assertEquals(array[0], result);
    }
}