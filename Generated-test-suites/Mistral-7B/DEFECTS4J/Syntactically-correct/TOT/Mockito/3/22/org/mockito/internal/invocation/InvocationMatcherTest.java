import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class InvocationMatcherTest {

    private InvocationMatcher testSubject;
    private Object mock;
    private Stubber stubber;

    @Before
    public void setup() {
        mock = mock(Object.class);
        stubber = Mockito.stub(mock);
    }

    @Test
    public void testMatcherWithEmptyList() {
        // given
        List<Matcher> matchers = Collections.emptyList();
        Invocation invocation = new InvocationOnMock(mock, Mockito.method(Object.class, "method", int.class), Arrays.asList(1));

        // when
        testSubject = new InvocationMatcher(invocation, matchers);

        // then
        assertThat(testSubject.matches(invocation), is(true));
    }

    @Test
    public void testMatcherWithMatchers() {
        // given
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.isA(String.class));
        Invocation invocation = new InvocationOnMock(mock, Mockito.method(Object.class, "method", int.class, String.class), Arrays.asList(1, "Hello World"));

        // when
        testSubject = new InvocationMatcher(invocation, matchers);

        // then
        assertThat(testSubject.matches(invocation), is(true));
    }

    @Test
    public void testMatcherWithMethodOnly() {
        // given
        Invocation invocation = new InvocationOnMock(mock, Mockito.method(Object.class, "method", int.class), Arrays.asList(1));

        // when
        testSubject = new InvocationMatcher(invocation);

        // then
        assertThat(testSubject.matches(invocation), is(true));
    }

    @Test
    public void testMatcherWithDifferentMethod() {
        // given
        Invocation otherInvocation = new InvocationOnMock(mock, Mockito.method(Object.class, "differentMethod", String.class), Arrays.asList("Hello World"));

        // when
        testSubject = new InvocationMatcher(Mockito.mock(Invocation.class), Collections.emptyList());

        // then
        assertThat(testSubject.matches(otherInvocation), is(false));
    }

    @Test
    public void testCaptureArguments() {
        // given
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.argThat(new MyCustomMatcher()));
        Invocation invocation = new InvocationOnMock(mock, Mockito.method(Object.class, "method", int.class, String.class), Arrays.asList(1, "Hello World"));

        // when
        testSubject = new InvocationMatcher(invocation, matchers);
        testSubject.captureArgumentsFrom(invocation);

        // then
        verify(stubber).argThat(Mockito.argThat(new MyCapturedMatcher()));
    }

    @Test
    public void testCaptureArgumentsWithVarargs() {
        // given
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.argThat(new MyCustomMatcher()), ArgumentMatchers.argThat(new MyCustomMatcher()));
        Invocation invocation = new InvocationOnMock(mock, Mockito.method(Object.class, "method", int.class, String[].class), Arrays.asList(1, new String[]{"Hello World", "World Hello"}));

        // when
        testSubject = new InvocationMatcher(invocation, matchers);
        testSubject.captureArgumentsFrom(invocation);

        // then
        verify(stubber).argThat(Mockito.argThat(new MyCapturedMatcher()));
        verify(stubber).argThat(Mockito.argThat(new MyCapturedMatcher()));
    }

    @Test
    public void testFailureWhenArgumentsDoNotMatch() {
        // given
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1));
        Invocation invocation = new InvocationOnMock(mock, Mockito.method(Object.class, "method", int.class), Arrays.asList(2));

        // when, then
        assertFalse(testSubject.matches(invocation));
    }

    private static class MyCustomMatcher implements ArgumentMatcher<Object> {
        @Override
        public boolean matches(Object argument) {
            return argument.equals("Custom Match");
        }
    }

    private static class MyCapturedMatcher implements ArgumentMatcher<Object> {
        private Object capturedArgument;

        @Override
        public void match(Object argument) {
            this.capturedArgument = argument;
        }

        @Override
        public boolean matches(Object argument) {
            return true;
        }

        public Object getCapturedArgument() {
            return capturedArgument;
        }
    }
}