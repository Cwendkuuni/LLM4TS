import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.Invocation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationTest {

    @Test
    public void testInvocationWithNoArguments() {
        Object mock = mock(String.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(new MockUtil().getMockName(mock) + ".someMethod", new Class[0]), new Object[0], 0, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) {
                return "Mocked method invoked";
            }
        });

        Mockito.doReturn("Mocked method return value").when(mock).someMethod();
        String result = ((InvocationOnMock) invocation).callRealMethod();
        verify(mock).someMethod();
        verify(mock).someMethod(); // verify that method was called only once
        verifyNoMoreInteractions(mock);
        assertNotNull(result);
        assertEquals("Mocked method return value", result);
    }

    @Test
    public void testInvocationWithOneArgument() {
        Object mock = mock(String.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(new MockUtil().getMockName(mock) + ".someMethod", String.class), new Object[]{"Test argument"}, 0, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) {
                String argument = (String) args[0];
                return argument.toUpperCase();
            }
        });

        Mockito.when(mock.someMethod((String) Mockito.anyString())).thenReturn("Mocked method return value");
        String result = ((InvocationOnMock) invocation).callRealMethod("Test argument");
        verify(mock).someMethod("Test argument");
        verify(mock).someMethod("Test argument"); // verify that method was called only once
        verifyNoMoreInteractions(mock);
        assertNotNull(result);
        assertEquals("TEST ARGUMENT", result);
    }

    @Test
    public void testInvocationWithMultipleArguments() {
        Object mock = mock(String[].class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(new MockUtil().getMockName(mock) + ".someMethod", String[].class), new Object[]{"Test array", "Test element"}, 0, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) {
                String[] arguments = (String[]) args[0];
                for (String element : arguments) {
                    System.out.println(element);
                }
                return null;
            }
        });

        Mockito.when(mock.someMethod((String[]) Mockito.any())).thenReturn(null);
        ((InvocationOnMock) invocation).callRealMethod(new String[]{"Test array element 1", "Test array element 2"});
        verify(mock).someMethod(Mockito.any(String[].class));
        verify(mock).someMethod(Mockito.any(String[].class)); // verify that method was called only once
        verifyNoMoreInteractions(mock);
    }

    @Test(expected = AssertionError.class)
    public void testInvocationWithInvalidReturnType() {
        Object mock = mock(String.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(new MockUtil().getMockName(mock) + ".someMethod", int.class), new Object[0], 0, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) {
                return 42;
            }
        });

        try {
            ((InvocationOnMock) invocation).callRealMethod();
            fail("Expected AssertionError");
        } catch (AssertionError e) {
            // expected
        }

        verifyZeroInteractions(mock);
    }

    @Test(expected = AssertionError.class)
    public void testInvocationWithInvalidException() {
        Object mock = mock(String.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(new MockUtil().getMockName(mock) + ".someMethod", String.class), new Object[0], 0, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Exception {
                throw new RuntimeException("Unexpected exception");
            }
        });

        try {
            ((InvocationOnMock) invocation).callRealMethod();
            fail("Expected AssertionError");
        } catch (AssertionError e) {
            // expected
        }

        verifyZeroInteractions(mock);
    }
}