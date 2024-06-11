import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.invocation.Invocation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class InvocationTest {

    @Test
    public void testConstructorWithMockMethodAndArguments() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] arguments = new Object[]{"abc"};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod() {
            @Override
            public Object invoke(Object o, Object[] objects) throws Throwable {
                return null;
            }
        });

        assertEquals(mock, invocation.getMock());
        assertEquals(mockitoMethod, invocation.getMethod());
        assertEquals(arguments, invocation.getArguments());
        assertEquals(1, invocation.getSequenceNumber());
    }

    @Test
    public void testToString() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] arguments = new Object[]{"abc"};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod() {
            @Override
            public Object invoke(Object o, Object[] objects) throws Throwable {
                return null;
            }
        });

        String expectedToString = "MockitoMock$1.length(String)['abc']";
        String actualToString = invocation.toString();
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testIsVerified() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] arguments = new Object[]{"abc"};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod() {
            @Override
            public Object invoke(Object o, Object[] objects) throws Throwable {
                return null;
            }
        });

        assertFalse(invocation.isVerified());

        verify(mock).length();

        assertTrue(invocation.isVerified());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] arguments = new Object[]{"abc"};
        Invocation invocation1 = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod() {
            @Override
            public Object invoke(Object o, Object[] objects) throws Throwable {
                return null;
            }
        });
        Invocation invocation2 = new Invocation(mock, mockitoMethod, arguments, 2, new RealMethod() {
            @Override
            public Object invoke(Object o, Object[] objects) throws Throwable {
                return null;
            }
        });

        verify(mock).length();
        verify(mock).length();

        assertFalse(invocation1.isVerifiedInOrder());
        assertFalse(invocation2.isVerifiedInOrder());

        invocation1.markVerifiedInOrder();
        invocation2.markVerifiedInOrder();

        assertTrue(invocation1.isVerifiedInOrder());
        assertTrue(invocation2.isVerifiedInOrder());
    }

    @Test(expected = AssertionError.class)
    public void testIsVerifiedInOrderWithDifferentArguments() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] arguments1 = new Object[]{"abc"};
        Object[] arguments2 = new Object[]{"def"};
        Invocation invocation1 = new Invocation(mock, mockitoMethod, arguments1, 1, new RealMethod() {
            @Override
            public Object invoke(Object o, Object[] objects) throws Throwable {
                return null;
            }
        });
        Invocation invocation2 = new Invocation(mock, mockitoMethod, arguments2, 2, new RealMethod() {
            @Override
            public Object invoke(Object o, Object[] objects) throws Throwable {
                return null;
            }
        });

        verify(mock).length();
        verify(mock).length();

        invocation1.markVerifiedInOrder();
        invocation2.markVerifiedInOrder();
    }

    @Test
    public void testCallRealMethod() throws Exception {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] arguments = new Object[]{"abc"};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod() {
            @Override
            public Object invoke(Object o, Object[] objects) throws Throwable {
                return null;
            }
        });

        assertEquals(3, invocation.callRealMethod());
    }
}