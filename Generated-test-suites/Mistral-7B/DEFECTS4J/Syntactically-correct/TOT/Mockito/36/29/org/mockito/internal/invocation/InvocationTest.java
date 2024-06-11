import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.runners.MockitoJUnitRunner;

import java.util.Arrays;

public class InvocationTest {

    private Invocation mockInvocation;
    private MockitoMock mock;

    @Before
    public void setup() {
        mock = Mockito.mock(Object.class);
        mockInvocation = new Invocation(mock, new MockitoMethod(Object.class.getMethod("toString"), null, new Class[0]), new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object mock, Object[] args) throws Throwable {
                return ((String) args[0]).toUpperCase();
            }
        });
    }

    @Test
    public void testInvocationWithValidArguments() {
        String expected = "Hello World";
        String result = mockInvocation.callRealMethod();
        Mockito.verify(mock).toString(ArgumentMatchers.eq(expected));
    }

    @Test(expected = AssertionError.class)
    public void testInvocationWithInvalidArguments() {
        mockInvocation.markVerified();
        mockInvocation.markVerifiedInOrder();
        mockInvocation.verifiedInOrder = false;
        mockInvocation.callRealMethod();
        Mockito.verify(mock).toString("Invalid Argument");
    }

    @Test
    public void testInvocationWithVoidMethod() {
        Mockito.doNothing().when(mock).toString();
        mockInvocation.callRealMethod();
        Mockito.verifyZeroInteractions(mock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvocationWithInvalidReturnType() {
        mockInvocation.callRealMethod();
        Mockito.verify(mock).toString((Object) null);
    }

    @Test
    public void testInvocationWithThrowsException() {
        try {
            mockInvocation.callRealMethod();
            Mockito.fail("Expected exception");
        } catch (Exception e) {
            Mockito.verify(mock).toString("Exception Message");
        }
    }

    @Test
    public void testInvocationWithArrayArguments() {
        String[] array = {"Test", "Array"};
        mockInvocation = new Invocation(mock, new MockitoMethod(Object[].class.getMethod("toString"), null, new Class[]{Object[].class}), array, 1, new RealMethod() {
            @Override
            public Object invoke(Object mock, Object[] args) throws Throwable {
                return Arrays.toString((Object[]) args[0]);
            }
        });

        String expected = "[Test, Array]";
        String result = mockInvocation.callRealMethod();
        Mockito.verify(mock).toString(ArgumentMatchers.arrayContaining(Arrays.asList(expected.split(","))));
    }

    @Test(expected = AssertionError.class)
    public void testInvocationWithMismatchedArguments() {
        mockInvocation.markVerified();
        mockInvocation.markVerifiedInOrder();
        mockInvocation.verifiedInOrder = false;
        mockInvocation.callRealMethod(new Object[]{"Invalid Argument"});
        Mockito.verify(mock).toString((Object) null);
    }
}