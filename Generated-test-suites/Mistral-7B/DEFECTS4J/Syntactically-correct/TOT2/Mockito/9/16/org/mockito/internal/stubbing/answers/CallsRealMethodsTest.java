import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class CallsRealMethodsTest {

    @Test
    public void testAnswerMethodWithNullArgument() {
        CallsRealMethods callsRealMethods = Mockito.spy(new CallsRealMethods());
        Object result = callsRealMethods.answer(ArgumentMatchers.nullArg());
        // Verify that the real method was called
        Mockito.verify(callsRealMethods, Mockito.times(1)).answer(ArgumentMatchers.any(InvocationOnMock.class));
        assertNull(result);
    }

    @Test
    public void testAnswerMethodWithValidArgument() {
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        String arg = "test";
        String result = (String) callsRealMethods.answer(ArgumentMatchers.argThat(invocation -> invocation.getMethod().getName().equals("answer"))).invokeArguments(arg);
        assertEquals("test", result);
    }
}