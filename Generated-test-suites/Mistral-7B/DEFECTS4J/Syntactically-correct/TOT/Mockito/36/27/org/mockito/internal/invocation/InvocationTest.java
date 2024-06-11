import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class InvocationTest {

    private MockitoInvocation mockitoInvocation;
    private MockitoMethod mockitoMethod;
    private Object mock;

    @Before
    public void setUp() {
        mock = mock(Object.class);
        mockitoMethod = Mockito.mock(MockitoMethod.class);
        mockitoInvocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object mock, Object[] args) {
                return "Mock method result";
            }
        });
    }

    @Test
    public void testGetMock() {
        Invocation invocation = new Invocation(mockitoInvocation.getMock(), mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object mock, Object[] args) {
                return "Inner mock method result";
            }
        });
        assert (invocation.getMock() == mockitoInvocation.getMock());
    }

    @Test
    public void testGetMethod() {
        assert (mockitoInvocation.getMethod().equals(mockitoMethod));
    }

    @Test
    public void testGetArguments() {
        Object[] arguments = new Object[]{"Argument1", "Argument2"};

        Invocation invocation = new Invocation(mockitoInvocation.getMock(), mockitoMethod, arguments, 2, new RealMethod() {
            @Override
            public Object invoke(Object mock, Object[] args) {
                return "Mock method result";
            }
        });

        assert (Arrays.equals(invocation.getArguments(), arguments));
        assert (Arrays.equals(invocation.getRawArguments(), arguments));
    }

    @Test
    public void testIsVerified() {
        mockitoInvocation.markVerified();
        assertTrue(mockitoInvocation.isVerified());
    }

    @Test
    public void testIsVerifiedInOrder() {
        mockitoInvocation.markVerifiedInOrder();
        assertTrue(mockitoInvocation.isVerifiedInOrder());
    }

    @Test
    public void testToString() {
        assert (mockitoInvocation.toString().contains(mockitoMethod.getName()));
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        when(mockitoMethod.getName()).thenReturn("testMethod");
        String result = mockitoInvocation.callRealMethod();
        assertEquals("Mock method result", result);
    }

    @Test
    public void testVerify() {
        Invocation invocation = Mockito.verify(mockitoInvocation.getMock()).method(mockitoMethod);
        assert (invocation.getSequenceNumber() == mockitoInvocation.getSequenceNumber());
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        mockitoInvocation.markVerified();
        Mockito.verifyNoMoreInteractions(mockitoInvocation.getMock());
    }

    @Test
    public void testEquals() {
        Invocation invocation1 = new Invocation(mockitoInvocation.getMock(), mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object mock, Object[] args) {
                return "Mock method result";
            }
        });

        Invocation invocation2 = new Invocation(mockitoInvocation.getMock(), mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object mock, Object[] args) {
                return "Mock method result";
            }
        });

        assertTrue(invocation1.equals(invocation2));
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Invocation invocation1 = new Invocation(mockitoInvocation.getMock(), mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object mock, Object[] args) {
                return "Mock method result";
            }
        });
        Invocation invocation2 = new Invocation(mockitoInvocation.getMock(), mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object mock, Object[] args) {
                return "Mock method result";
            }
        });
        assertEquals(invocation1.hashCode(), invocation2.hashCode());
    }

    @Test
    public void testToStringWithArgumentsMatchers() {
        Object arg1 = new Object() {
            @Override
            public String toString() {
                return "Argument1";
            }
        };

        Object arg2 = new Object() {
            @Override
            public String toString() {
                return "Argument2";
            }
        };

        when(mockitoMethod.getName()).thenReturn("testMethod");
        when(mockitoMethod.getExceptionTypes()).thenReturn(new Class[0]);
        when(mockitoMethod.isVarArgs()).thenReturn(false);

        Invocation invocation = new Invocation(mockitoInvocation.getMock(), mockitoMethod, new Object[]{arg1, arg2}, 2, new RealMethod() {
            @Override
            public Object invoke(Object mock, Object[] args) throws Throwable {
                throw new RuntimeException();
            }
        });

        verify(mockitoInvocation.getMock()).method(Mockito.eq(mockitoMethod), ArgumentMatchers.arrayContaining(ArgumentMatchers.eq(arg1)), ArgumentMatchers.arrayContaining(ArgumentMatchers.eq(arg2)));

        String result = invocation.toString();
        String expected = String.format("%s(%s, %s)", mockitoMethod.getName(), arg1.toString(), arg2.toString());
        assertTrue(result.contains(expected));
    }

    @Test
    public void testToStringWithVarArgs() {
        Object arg1 = new Object() {
            @Override
            public String toString() {
                return "Argument1";
            }
        };

        Object[] args = new Object[]{arg1, new Object[]{1, 2, 3}};

        when(mockitoMethod.getName()).thenReturn("testMethod");
        when(mockitoMethod.getExceptionTypes()).thenReturn(new Class[0]);
        when(mockitoMethod.isVarArgs()).thenReturn(true);

        Invocation invocation = new Invocation(mockitoInvocation.getMock(), mockitoMethod, args, 2, new RealMethod() {
            @Override
            public Object invoke(Object mock, Object[] args) throws Throwable {
                throw new RuntimeException();
            }
        });

        String result = invocation.toString();
        String expected = String.format("%s(%s, %s)", mockitoMethod.getName(), arg1.toString(), Arrays.toString(args[1]));
        assertTrue(result.contains(expected));
    }

    @Test
    public void testIsToString() {
        assertTrue(Invocation.isToString(mockitoInvocation));
    }
}