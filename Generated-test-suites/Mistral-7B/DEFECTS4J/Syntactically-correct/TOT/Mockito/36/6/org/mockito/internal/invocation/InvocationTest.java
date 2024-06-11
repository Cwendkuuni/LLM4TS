import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.Invocation;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class InvocationTest {

    private Invocation mockInvocation;

    @Before
    public void setUp() {
        Class<?> mockClass = Mockito.class;
        try {
            Method method = mockClass.getDeclaredMethod("methodWithTwoIntsAndOneObjectArg", int.class, int.class, Object.class);
            mockInvocation = new Invocation(Mockito.mock(mockClass), new MockitoMethod(method), new Object[]{1, 2, "test"}, 0, new RealMethod() {
                @Override
                public Object invoke(Object mock, Object[] args) throws Throwable {
                    return null;
                }
            });
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetMock() {
        assertNotNull(mockInvocation.getMock());
    }

    @Test
    public void testGetMethod() {
        assertNotNull(mockInvocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object[] expectedArguments = new Object[]{1, 2, "test"};
        assertArrayEquals(expectedArguments, mockInvocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        assertFalse(mockInvocation.isVerified());
    }

    @Test
    public void testMarkVerified() {
        mockInvocation.markVerified();
        assertTrue(mockInvocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        assertTrue(mockInvocation.getSequenceNumber() > 0);
    }

    @Test
    public void testIsVerifiedInOrder() {
        assertFalse(mockInvocation.isVerifiedInOrder());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        mockInvocation.markVerified();
        mockInvocation.markVerifiedInOrder();
        assertTrue(mockInvocation.isVerifiedInOrder());
    }

    @Test
    public void testToString() {
        String expectedToString = "org.mockito.internal.invocation.Invocation@5d4d6c6d [MockitoMock, method = methodWithTwoIntsAndOneObjectArg(int, int, java.lang.Object), arguments = [1, 2, test]]";
        assertTrue(expectedToString.equals(mockInvocation.toString()));
    }

    @Test
    public void testEquals() {
        Invocation anotherInvocation = new Invocation(mockInvocation.getMock(), mockInvocation.getMethod(), mockInvocation.getArguments(), 1, new RealMethod() {
            @Override
            public Object invoke(Object mock, Object[] args) throws Throwable {
                return null;
            }
        });
        assertTrue(mockInvocation.equals(anotherInvocation));
    }

    @Test(expected = MockitoException.class)
    public void testInvokeRealMethodWithInvalidArguments() {
        try {
            mockInvocation.callRealMethod(new Object[]{1, "test"});
            fail();
        } catch (MockitoException e) {
            // expected
        }
    }

    @Test
    public void testIsToStringMethod() {
        assertTrue(Invocation.isToString(mockInvocation));
    }

    @Test
    public void testIsValidException() {
        Throwable throwable = new RuntimeException();
        assertTrue(mockInvocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType() {
        assertTrue(mockInvocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsVoid() {
        assertFalse(mockInvocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        assertEquals("String", mockInvocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        assertEquals("methodWithTwoIntsAndOneObjectArg", mockInvocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        assertFalse(mockInvocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        assertNull(mockInvocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        assertEquals(3, mockInvocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        Object[] rawArguments = new Object[]{1, 2, "test"};
        assertArrayEquals(rawArguments, mockInvocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        // no implementation in this test case
    }

    @Test
    public void testToStringWithPrintSettings() {
        String[] lines = mockInvocation.toString(new PrintSettings()).split("\n");
        assertTrue(lines[0].contains("Invocation"));
        assertTrue(lines[1].contains("methodWithTwoIntsAndOneObjectArg"));
        assertTrue(lines[1].contains("(int, int, java.lang.Object)"));
        assertTrue(lines[2].contains("arguments = [1, 2, test]"));
    }
}