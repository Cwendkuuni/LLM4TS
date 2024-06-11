import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.matchers.ArrayEquals.arrayEquals;
import static org.mockito.internal.matchers.Equals.equalTo;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationTest {

    @Test
    public void testInvocationWithNoArguments() {
        Object mock = mock(String.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod("toString"), new Object[0], 1, null);

        verify(mock).toString();
        assertThat(invocation, is(notNullValue()));
        assertThat(invocation.getMock(), is(mock));
        assertThat(invocation.getMethod().getName(), is("toString"));
        assertThat(invocation.getArguments(), is(new Object[0]));
        assertThat(invocation.isVerified(), is(false));
        assertThat(invocation.getSequenceNumber(), is(1));
        assertThat(invocation.isVerifiedInOrder(), is(false));
        assertThat(invocation.isValidReturnType(String.class), is(true));
        assertThat(invocation.isVoid(), is(false));
        assertThat(invocation.printMethodReturnType(), is("String"));
        assertThat(invocation.getLocation().getClassName(), is(""));
        assertThat(invocation.getLocation().getLineNumber(), is(0));

        // Calling real method
        String result = invocation.callRealMethod();
        assertThat(result, is(""));
    }

    @Test
    public void testInvocationWithOneArgument() {
        Object mock = mock(String.class);
        String argument = "test";
        Invocation invocation = new Invocation(mock, new MockitoMethod("toString", String.class), new Object[]{argument}, 1, null);

        when(mock.toString(argument)).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getRawArguments();
                return args[0].toString();
            }
        });

        verify(mock).toString(argument);
        assertThat(invocation, is(notNullValue()));
        assertThat(invocation.getMock(), is(mock));
        assertThat(invocation.getMethod().getName(), is("toString"));
        assertThat(invocation.getArguments(), is(new Object[]{argument}));
        assertThat(invocation.isVerified(), is(false));
        assertThat(invocation.getSequenceNumber(), is(1));
        assertThat(invocation.isVerifiedInOrder(), is(false));
        assertThat(invocation.isValidReturnType(String.class), is(true));
        assertThat(invocation.isVoid(), is(false));
        assertThat(invocation.printMethodReturnType(), is("String"));
        assertThat(invocation.getLocation().getClassName(), is(""));
        assertThat(invocation.getLocation().getLineNumber(), is(0));

        String result = invocation.callRealMethod();
        assertThat(result, is(argument));
    }

    @Test
    public void testInvocationWithVarArgs() {
        Object mock = mock(String.class);
        String[] arguments = {"test1", "test2"};
        Invocation invocation = new Invocation(mock, new MockitoMethod("format", String.class, String[].class), arguments, 1, null);

        when(mock.format(arguments)).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] rawArguments = invocation.getRawArguments();
                return String.format(rawArguments[0].toString(), rawArguments[1]);
            }
        });

        verify(mock).format(arguments);
        assertThat(invocation, is(notNullValue()));
        assertThat(invocation.getMock(), is(mock));
        assertThat(invocation.getMethod().getName(), is("format"));
        assertThat(invocation.getArguments(), is(arguments));
        assertThat(invocation.isVerified(), is(false));
        assertThat(invocation.getSequenceNumber(), is(1));
        assertThat(invocation.isVerifiedInOrder(), is(false));
        assertThat(invocation.isValidReturnType(String.class), is(true));
        assertThat(invocation.isVoid(), is(false));
        assertThat(invocation.printMethodReturnType(), is("String"));
        assertThat(invocation.getLocation().getClassName(), is(""));
        assertThat(invocation.getLocation().getLineNumber(), is(0));

        String result = invocation.callRealMethod();
        String expected = String.format("%s %s", arguments[0], arguments[1]);
        assertThat(result, is(expected));
    }

    @Test(expected = AssertionError.class)
    public void testInvocationWithInvalidArguments() {
        Object mock = mock(String.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod("toString"), new Object[]{new Object()}, 1, null);

        verify(mock).toString(new Object());
        assertThat(invocation, is(notNullValue()));
        assertThat(invocation.getMock(), is(mock));
        assertThat(invocation.getMethod().getName(), is("toString"));
        assertThat(invocation.getArguments(), is(new Object[]{new Object()}));
        assertThat(invocation.isVerified(), is(false));
        assertThat(invocation.getSequenceNumber(), is(1));
        assertThat(invocation.isVerifiedInOrder(), is(false));
        assertThat(invocation.isValidReturnType(String.class), is(true));
        assertThat(invocation.isVoid(), is(false));
        assertThat(invocation.printMethodReturnType(), is("String"));
        assertThat(invocation.getLocation().getClassName(), is(""));
        assertThat(invocation.getLocation().getLineNumber(), is(0));

        // Invoke real method with an exception
        try {
            invocation.callRealMethod();
        } catch (Exception e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test(expected = AssertionError.class)
    public void testInvocationWithInvalidReturnType() {
        Object mock = mock(Number.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod("toString", String.class), new Object[0], 1, null);

        verify(mock).toString();
        assertThat(invocation, is(notNullValue()));
        assertThat(invocation.getMock(), is(mock));
        assertThat(invocation.getMethod().getName(), is("toString"));
        assertThat(invocation.getArguments(), is(new Object[0]));
        assertThat(invocation.isVerified(), is(false));
        assertThat(invocation.getSequenceNumber(), is(1));
        assertThat(invocation.isVerifiedInOrder(), is(false));
        assertThat(invocation.isValidReturnType(Boolean.class), is(false));
        assertThat(invocation.isVoid(), is(false));
        assertThat(invocation.printMethodReturnType(), is("String"));
        assertThat(invocation.getLocation().getClassName(), is(""));
        assertThat(invocation.getLocation().getLineNumber(), is(0));

        // Invoke real method
        try {
            invocation.callRealMethod();
            fail("Expecting AssertionError");
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test
    public void testInvocationWithVoidReturnType() {
        Object mock = mock(VoidConsumer.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod("accept", Void.TYPE, Object.class), new Object[]{null}, 1, null);

        verify(mock).accept(null);
        assertThat(invocation, is(notNullValue()));
        assertThat(invocation.getMock(), is(mock));
        assertThat(invocation.getMethod().getName(), is("accept"));
        assertThat(invocation.getArguments(), is(new Object[]{null}));
        assertThat(invocation.isVerified(), is(false));
        assertThat(invocation.getSequenceNumber(), is(1));
        assertThat(invocation.isVerifiedInOrder(), is(false));
        assertThat(invocation.isValidReturnType(Void.TYPE), is(true));
        assertThat(invocation.isVoid(), is(true));
        assertThat(invocation.printMethodReturnType(), is("void"));
        assertThat(invocation.getLocation().getClassName(), is(""));
        assertThat(invocation.getLocation().getLineNumber(), is(0));

        // Calling real method
        invocation.callRealMethod();
    }
}