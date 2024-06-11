import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InvocationTest {

    @Test
    public void testBasicInvocation() {
        Object mock = mock(String.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(String.class, "length", new Class[0]), new Object[0], 1, null);

        when(mock.length()).thenReturn(5);
        String result = mock.toString();
        verify(mock).length();
        verify(mock).toString();
        verifyNoMoreInteractions(mock);

        assert(invocation.getMock() == mock);
        assert(invocation.getMethod().getName().equals("length"));
        assert(invocation.getArguments().length == 0);
        assert(invocation.isVerified());
        assert(invocation.isVerifiedInOrder());
    }

    @Test
    public void testInvocationWithArguments() {
        Object mock = mock(String.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(String.class, "concat", new Class[]{String.class, String.class}), new Object[]{"hello", "world"}, 1, null);

        when(mock.concat(ArgumentMatchers.anyString())).thenAnswer(invocationCall -> invocationCall.getArguments()[0] + invocationCall.getArguments()[1]);
        String result = mock.concat("hello", "world");
        verify(mock).concat(ArgumentMatchers.eq("hello"));
        verify(mock).concat(ArgumentMatchers.eq("world"));
        verifyNoMoreInteractions(mock);

        assert(invocation.getMock() == mock);
        assert(invocation.getMethod().getName().equals("concat"));
        assert(Arrays.equals(invocation.getArguments(), new Object[]{"hello", "world"}));
        assert(invocation.isVerified());
        assert(invocation.isVerifiedInOrder());
    }

    @Test
    public void testInvocationWithException() {
        Object mock = mock(String.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(String.class, "length", new Class[0]), new Object[0], 1, null);

        when(mock.length()).thenThrow(new RuntimeException("Unexpected exception"));

        try {
            mock.length();
        } catch (RuntimeException e) {
            verify(mock).length();
            verifyThrownException(RuntimeException.class);
        }

        assert(invocation.isVerified());
        assert(invocation.isVerifiedInOrder());
    }

    @Test
    public void testInvocationWithVoidMethod() {
        Object mock = mock(PrintStream.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(PrintStream.class, "println", new Class[]{String.class}), new Object[]{"Hello"}, 1, null);

        mock.println("Hello");
        verify(mock).println(ArgumentMatchers.eq("Hello"));
        verifyNoMoreInteractions(mock);

        assert(invocation.isVoid());
        assert(invocation.isVerified());
        assert(invocation.isVerifiedInOrder());
    }

    @Test
    public void testInvocationWithVarArgs() {
        Object mock = mock(String.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(String.class, "format", new Class[]{String.class, Object[].class}), new Object[0], 1, null);

        when(mock.format(ArgumentMatchers.anyString(), ArgumentMatchers.anyArray(Object.class))).thenAnswer(invocationCall -> String.format(invocationCall.getArguments()[0], invocationCall.getArguments()[1]));
        String result = mock.format("%s %d", "Hello", new Object[]{42});
        verify(mock).format(ArgumentMatchers.eq("%s %d"), ArgumentMatchers.anyArray(Object.class));
        verifyNoMoreInteractions(mock);

        assert(invocation.getMock() == mock);
        assert(invocation.getMethod().getName().equals("format"));
        assert(Arrays.equals(invocation.getArguments(), new Object[]{"%s %d", new Object[]{null, new Object[]{42}}}));
        assert(invocation.isVerified());
        assert(invocation.isVerifiedInOrder());
    }

    @Test
    public void testInvocationWithArrayArgument() {
        Object mock = mock(String.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(String.class, "format", new Class[]{String.class, Object[].class}), new Object[]{"%s %s", new Object[]{"foo", new Object[]{"bar", "baz"}}}, 1, null);

        when(mock.format(ArgumentMatchers.anyString(), ArgumentMatchers.anyArray(Object.class))).thenAnswer(invocationCall -> String.format(invocationCall.getArguments()[0], invocationCall.getArguments()[1]));
        String result = mock.format("%s %s", new Object[]{"Hello", new Object[]{"foo", "bar", "baz"}});
        verify(mock).format(ArgumentMatchers.eq("%s %s"), new Object[]{new Object[]{"Hello", new Object[]{"foo", "bar", "baz"}}});
        verifyNoMoreInteractions(mock);

        assert(invocation.getMock() == mock);
        assert(invocation.getMethod().getName().equals("format"));
        assert(Arrays.deepEquals(invocation.getArguments(), new Object[]{"%s %s", new Object[]{"Hello", new Object[]{"foo", "bar", "baz"}}}));
        assert(invocation.isVerified());
        assert(invocation.isVerifiedInOrder());
    }

    @Test
    public void testInvocationWithCustomMatcher() {
        Object mock = mock(String.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(String.class, "toLowerCase", new Class[0]), new Object[0], 1, null);

        when(mock.toLowerCase()).thenAnswer(invocationCall -> new StringBuilder(invocationCall.getArguments()[0].toString()).reverse().toString());

        String result = mock.toLowerCase();
        verify(mock).toLowerCase();
        verify(mock).toString();
        verifyNoMoreInteractions(mock);

        assert(invocation.toString().contains("mockito.org.mockito.internal.invocation.Invocation@"));
        assert(invocation.toString().contains("toString"));
        assert(invocation.toString().contains("toLowerCase"));
        assert(invocation.toString().contains("java.lang.String"));
        assert(invocation.toString().contains("org.mockito.internal.invocation.realmethod.RealMethod"));
        assert(invocation.toString().contains("getArguments"));
        assert(invocation.toString().contains("isVerified"));
        assert(invocation.toString().contains("isVerifiedInOrder"));
        assert(invocation.toString().contains("equals"));
        assert(invocation.toString().contains("hashCode"));
        assert(invocation.toString().contains("toString"));
        assert(invocation.toString().contains("toString(PrintSettings)"));
        assert(invocation.toString().contains("markVerified"));
        assert(invocation.toString().contains("markVerifiedInOrder"));

        assert(invocation.isVerified());
        assert(invocation.isVerifiedInOrder());
    }
}