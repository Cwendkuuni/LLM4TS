import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.Invocation;
import java.util.Arrays;

public class InvocationTest {

    @Test
    public void testInvocationWithNoArguments() {
        // Given
        Object mock = Mockito.mock(String.class);
        MockitoMethod method = Mockito.method(String.class, "length", new Class[0]);
        Invocation invocation = new Invocation(mock, method, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return "Mocked String".length();
            }
        });

        // When
        String result = (String) invocation.callRealMethod();

        // Then
        assertEquals(5, result.length());
        Assert.assertTrue(invocation.isVerified());
    }

    @Test
    public void testInvocationWithSingleArgument() {
        // Given
        Object mock = Mockito.mock(String.class);
        MockitoMethod method = Mockito.method(String.class, "charAt", new Class[] { int.class });
        Invocation invocation = new Invocation(mock, method, new Object[] { 0 }, 1, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return ((String) proxy).charAt((Integer) args[0]);
            }
        });

        // When
        char result = (char) invocation.callRealMethod();

        // Then
        assertEquals('M', result);
        Assert.assertTrue(invocation.isVerified());
    }

    @Test
    public void testInvocationWithMultipleArguments() {
        // Given
        Object mock = Mockito.mock(String.class);
        MockitoMethod method = Mockito.method(String.class, "substring", new Class[] { int.class, int.class });
        Invocation invocation = new Invocation(mock, method, new Object[] { 0, 3 }, 1, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return ((String) proxy).substring((Integer) args[0], (Integer) args[1]);
            }
        });

        // When
        String result = (String) invocation.callRealMethod();

        // Then
        assertEquals("Mock", result);
        Assert.assertTrue(invocation.isVerified());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvocationWithInvalidArguments() {
        // Given
        Object mock = Mockito.mock(String.class);
        MockitoMethod method = Mockito.method(String.class, "substring", new Class[] { int.class, int.class });
        Invocation invocation = new Invocation(mock, method, new Object[] { 5, 3 }, 1, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return ((String) proxy).substring((Integer) args[0], (Integer) args[1]);
            }
        });

        // When and Then
        invocation.callRealMethod();
        Assert.assertFalse(invocation.isVerified());
    }

    @Test
    public void testInvocationWithVoidReturnType() {
        // Given
        Object mock = Mockito.mock(PrintStream.class);
        MockitoMethod method = Mockito.method(PrintStream.class, "println", new Class[0]);
        Invocation invocation = new Invocation(mock, method, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                ((PrintStream) proxy).println("Test message");
                return null;
            }
        });

        // When
        invocation.callRealMethod();

        // Then
        // In this case, there's no way to assert on the result,
        // as void methods don't return anything. However, we can
        // check the side effect.
        String testMessage = "Test message";
        Mockito.verify(mock).println(testMessage);
        Assert.assertTrue(invocation.isVerified());
    }

    @Test(expected = AssertionError.class)
    public void testInvocationWithMismatchedReturnType() {
        // Given
        Object mock = Mockito.mock(String.class);
        MockitoMethod method = Mockito.method(String.class, "length", new Class[0]);
        Invocation invocation = new Invocation(mock, method, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return "Mocked String".length();
            }
        });

        // When and Then (expecting an AssertionError)
        Integer result = (Integer) invocation.callRealMethod();
        Assert.fail("Invocation should not have returned an Integer.");
    }

    @Test
    public void testInvocationWithException() {
        // Given
        Object mock = Mockito.mock(String.class);
        MockitoMethod method = Mockito.method(String.class, "substring", new Class[] { int.class, int.class });
        Invocation invocation = new Invocation(mock, method, new Object[] { 5, 3 }, 1, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                throw new RuntimeException("Test exception");
            }
        });

        // When and Then
        try {
            invocation.callRealMethod();
            fail("Invocation should have thrown an exception.");
        } catch (RuntimeException e) {
            assertEquals("Test exception", e.getMessage());
            Assert.assertTrue(invocation.isVerified());
        }
    }

    @Test
    public void testHashCodeAndEquals() {
        // Given
        Object mock = Mockito.mock(String.class);
        MockitoMethod method = Mockito.method(String.class, "length", new Class[0]);
        Invocation invocation1 = new Invocation(mock, method, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return "Mocked String 1".length();
            }
        });
        Invocation invocation2 = new Invocation(mock, method, new Object[0], 2, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return "Mocked String 2".length();
            }
        });

        // When and Then
        Assert.assertNotSame(invocation1, invocation2);
        Assert.assertFalse(invocation1.equals(invocation2));
        Assert.assertTrue(invocation1.equals(invocation1));
        Assert.assertTrue(invocation2.equals(invocation2));
    }
}