import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockitoCore;
import org.mockito.stub.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private InvocationMatcher.Invocation invocation;
    private InvocationMatcher matcher;
    private Object mock;

    @Before
    public void setup() {
        mock = mock(SomeClass.class);
        invocation = new Invocation(mock, new FakeLocation(), new Object[]{}, new Method(SomeClass.class, "someMethod", new Class[0]));
        matcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testSimpleMatcher() {
        // Given
        List<Matcher> matchers = Arrays.asList(ArgumentCaptor.forClass(String.class));
        InvocationMatcher matcherWithCaptor = new InvocationMatcher(invocation, matchers);

        // When
        Invocation invocationWithArg = new Invocation(mock, new FakeLocation(), new Object[]{"expectedValue"}, new Method(SomeClass.class, "someMethod", new Class[0]));
        matcherWithCaptor.matches(invocationWithArg);

        // Then
        ArgumentCaptor<String> argumentCaptor = matcherWithCaptor.getMatchers().get(0);
        Object capturedArgument = argumentCaptor.getValue();
        assertThat(capturedArgument, is(instanceOf(String.class)));
        assertThat(capturedArgument, is(notNullValue()));
        assertThat(capturedArgument, is("expectedValue"));
    }

    @Test
    public void testEmptyMatcherList() {
        // Given
        InvocationMatcher emptyMatcher = new InvocationMatcher(invocation);

        // When
        Invocation emptyInvocation = new Invocation(mock, new FakeLocation(), new Object[0], new Method(SomeClass.class, "someMethod", new Class[0]));
        boolean result = emptyMatcher.matches(emptyInvocation);

        // Then
        assertTrue(result);
    }

    @Test
    public void testDifferentMethod() {
        // Given
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation otherInvocation = new Invocation(mock, new FakeLocation(), new Object[0], new Method(AnotherClass.class, "otherMethod", new Class[0]));

        // When
        boolean result = matcher.matches(otherInvocation);

        // Then
        assertFalse(result);
    }

    @Test
    public void testDifferentMock() {
        // Given
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Object anotherMock = mock(AnotherClass.class);
        Invocation anotherInvocation = new Invocation(anotherMock, new FakeLocation(), new Object[0], invocation.getMethod());

        // When
        boolean result = matcher.matches(anotherInvocation);

        // Then
        assertFalse(result);
    }

    @Test
    public void testVerifiedInvocation() {
        // Given
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return null;
            }
        }).when(mock).someMethod();

        Invocation verifiedInvocation = new Invocation(mock, new FakeLocation(), new Object[0], invocation.getMethod());
        verifiedInvocation.markVerified();

        // When
        boolean result = matcher.matches(verifiedInvocation);

        // Then
        assertFalse(result);
    }

    @Test
    public void testSameMethodDifferentArguments() {
        // Given
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Object[] differentArguments = new Object[]{1, 2};
        Invocation differentInvocation = new Invocation(mock, new FakeLocation(), differentArguments, invocation.getMethod());

        // When
        boolean result = matcher.matches(differentInvocation);

        // Then
        assertFalse(result);
    }

    @Test
    public void testSameMethodSameArguments() {
        // Given
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // When
        boolean result = matcher.matches(invocation);

        // Then
        assertTrue(result);
    }

    @Test
    public void testLocation() {
        // Given
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation invocationWithDifferentLocation = new Invocation(mock, new FakeLocation(1, 1), new Object[0], invocation.getMethod());

        // When
        boolean result = matcher.matches(invocationWithDifferentLocation);

        // Then
        assertFalse(result);
    }

    @Test
    public void testToString() {
        // Given
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // When
        String toString = matcher.toString();

        // Then
        assertThat(toString, notNullValue());
    }

    @Test
    public void testCaptureArguments() {
        // Given
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Object arg = new Object();

        // When
        matcher.captureArgumentsFrom(new Invocation(mock, new FakeLocation(), new Object[]{arg}, invocation.getMethod()));

        // Then
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        verify(matcher).captureArgumentsFrom(argumentCaptor.capture());
        Object capturedArgument = argumentCaptor.getValue();
        assertThat(capturedArgument, is(arg));
    }

    @Test
    public void testEmptyArgumentsCapture() {
        // Given
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // When
        matcher.captureArgumentsFrom(new Invocation(mock, new FakeLocation(), new Object[0], invocation.getMethod()));

        // Then
        ArgumentCaptor<Invocation> argumentCaptor = ArgumentCaptor.forClass(Invocation.class);
        verify(matcher).captureArgumentsFrom(argumentCaptor.capture());
        Invocation capturedInvocation = argumentCaptor.getValue();
        assertThat(capturedInvocation.getArguments(), is(Collections.EMPTY_LIST));
    }

    private static class FakeLocation implements Location {
        private final int line;
        private final int column;

        public FakeLocation() {
            this(0, 0);
        }

        public FakeLocation(int line, int column) {
            this.line = line;
            this.column = column;
        }

        @Override
        public int getLine() {
            return line;
        }

        @Override
        public int getColumn() {
            return column;
        }
    }

    private static class SomeClass {
        public void someMethod() {
        }
    }

    private static class AnotherClass {
        public void otherMethod() {
        }
    }
}