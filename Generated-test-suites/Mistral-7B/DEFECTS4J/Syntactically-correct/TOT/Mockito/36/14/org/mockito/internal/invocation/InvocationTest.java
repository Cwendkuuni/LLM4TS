import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.quality.Degraded;
import org.mockito.quality.Verified;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class InvocationTest {

    private MockitoInvocation mockitoInvocation;
    private Invocation mockInvocation;

    @Before
    public void setup() {
        mockitoInvocation = Mockito.mock(MockitoInvocation.class);
        mockInvocation = new Invocation(mockitoInvocation, new MockitoMethod("testMethod", Void.TYPE, new Class[0]), new Object[0], 1, null);
    }

    @Test
    public void testValidInvocation() {
        Verified invocationVerification = verify(mockitoInvocation).testMethod();
        mockInvocation.markVerifiedInOrder();
        assertEquals(1, invocationVerification.getInvocationOrder());
    }

    @Test(expected = AssertionError.class)
    public void testMismatchedArguments() {
        when(mockitoInvocation.testMethod()).thenReturn(1);
        Invocation invocationWithDifferentArguments = new Invocation(mockitoInvocation, new MockitoMethod("testMethod", Integer.TYPE, new Class[0]), new Object[]{2}, 2, null);
        invocationWithDifferentArguments.markVerified();
        invocationWithDifferentArguments.markVerifiedInOrder();
        invocationWithDifferentArguments.verify();
    }

    @Test
    public void testVoidMethod() {
        doNothing().when(mockitoInvocation).testMethod();
        mockInvocation.markVerified();
        mockInvocation.markVerifiedInOrder();
        mockInvocation.verify();
    }

    @Test(expected = AssertionError.class)
    public void testThrowsException() {
        when(mockitoInvocation.testMethod()).thenThrow(new RuntimeException("Test Exception"));
        mockInvocation.markVerified();
        mockInvocation.markVerifiedInOrder();
        mockInvocation.verify();
    }

    @Test
    public void testThrowsExpectedException() {
        when(mockitoInvocation.testMethod()).thenThrow(new IllegalArgumentException("Test Exception"));
        try {
            Invocation invocationWithUnexpectedException = new Invocation(mockitoInvocation, new MockitoMethod("testMethod", String.class, new Class[0]), new Object[0], 1, null);
            invocationWithUnexpectedException.markVerified();
            invocationWithUnexpectedException.markVerifiedInOrder();
            invocationWithUnexpectedException.verify();
        } catch (AssertionError e) {
            assertEquals("Test Exception", e.getMessage());
        }
    }

    @Test
    public void testVarArgs() {
        when(mockitoInvocation.testMethod((String) ArgumentMatchers.any(), (String) ArgumentMatchers.any())).thenReturn("Test");
        String firstArgument = "First";
        String secondArgument = "Second";
        Invocation invocationWithTwoArguments = new Invocation(mockitoInvocation, new MockitoMethod("testMethod", String.class, new Class[]{String.class, String.class}), new Object[]{firstArgument, secondArgument}, 1, null);
        invocationWithTwoArguments.markVerifiedInOrder();
        invocationWithTwoArguments.verify();
        assertEquals("Test", invocationWithTwoArguments.getArguments()[0]);
        assertEquals("Second", invocationWithTwoArguments.getArguments()[1]);
    }

    @Test
    public void testIsToString() {
        MockitoMethod toStringMethod = new MockitoMethod("toString", String.class, new Class[0]);
        Invocation toStringInvocation = new Invocation(mockitoInvocation, toStringMethod, new Object[0], 1, null);
        assertTrue(Invocation.isToString(toStringInvocation));
    }
}