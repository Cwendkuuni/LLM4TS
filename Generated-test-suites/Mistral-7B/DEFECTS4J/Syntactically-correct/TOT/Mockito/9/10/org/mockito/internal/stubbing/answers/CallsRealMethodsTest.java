import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

public class CallsRealMethodsTest {

    @Test
    public void testCallRealMethod_whenMethodIsCalled_thenReturnValueFromRealMethodIsReturned() {
        Object realObject = new Object() {
            public String meth() {
                return "Real method return value";
            }
        };

        CallsRealMethods callRealMethods = new CallsRealMethods();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setTarget(realObject);

        String result = (String) callRealMethods.answer(invocation);

        Mockito.verify(realObject).meth();
        Mockito.verifyNoMoreInteractions(realObject);

        assertEquals("Real method return value", result);
    }

    @Test
    public void testCallRealMethodWithArgument_whenMethodIsCalledWithArgument_thenReturnValueFromRealMethodIsReturned() {
        Object realObject = new Object() {
            public int meth(int arg) {
                return arg * 2;
            }
        };

        CallsRealMethods callRealMethods = new CallsRealMethods();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setArgument(0, 3);
        invocation.setTarget(realObject);

        int result = ((Number) callRealMethods.answer(invocation)).intValue();

        Mockito.verify(realObject).meth(3);
        Mockito.verifyNoMoreInteractions(realObject);

        assertEquals(6, result);
    }

    @Test
    public void testCallRealMethod_whenMethodThrowsException_thenExceptionIsThrownByAnswer() {
        Object realObject = new Object() {
            public void meth() throws Exception {
                throw new RuntimeException("Real method throws exception");
            }
        };

        CallsRealMethods callRealMethods = new CallsRealMethods();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setTarget(realObject);

        try {
            callRealMethods.answer(invocation);
            fail("Expected exception");
        } catch (RuntimeException e) {
            Mockito.verify(realObject).meth();
            Mockito.verifyNoMoreInteractions(realObject);
            assertEquals("Real method throws exception", e.getMessage());
        }
    }
}