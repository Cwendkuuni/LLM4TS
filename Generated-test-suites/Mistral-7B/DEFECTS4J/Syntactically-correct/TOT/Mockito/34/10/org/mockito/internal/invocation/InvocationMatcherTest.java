import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.InvocationOnMockActual;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    private Object mock;
    private InvocationMatcher matcher;

    @Before
    public void setup() {
        mock = mock(SomeClass.class);
    }

    @Test
    public void testSimpleInvocationMatcher() {
        Invocation invocation = new Invocation(mock, SomeClass.class.getMethod("methodWithOneArgument", int.class), new Object[]{1});
        List<Matcher> matchers = Arrays.asList(is(1));
        matcher = new InvocationMatcher(invocation, matchers);

        InvocationMatcher testMatcher = Mockito.spy(matcher);

        ArgumentCaptor<InvocationMatcher> invocationMatcherCaptor = ArgumentCaptor.forClass(InvocationMatcher.class);
        InvocationMatcher expectedMatcher = Mockito.argThat(invocationMatcherCaptor);

        Mockito.doAnswer(invocation -> {
            InvocationMatcher argMatcher = invocationMatcherCaptor.getValue();
            argMatcher.captureArgumentsFrom(invocation);
            return null;
        }).when(mock).thenAnswer(invocation -> null);

        testMatcher.matches(new InvocationOnMockActual(mock, SomeClass.class.getMethod("methodWithOneArgument", int.class), new Object[]{1}));

        verify(mock).methodWithOneArgument(is(1));
    }

    @Test
    public void testEmptyMatcherList() {
        Invocation invocation = new Invocation(mock, SomeClass.class.getMethod("methodWithNoArguments"), null);
        matcher = new InvocationMatcher(invocation);

        InvocationMatcher testMatcher = Mockito.spy(matcher);

        ArgumentCaptor<Invocation> invocationCaptor = ArgumentCaptor.forClass(Invocation.class);
        Invocation expectedInvocation = invocationCaptor.capture(testMatcher.getInvocation());

        Mockito.doAnswer(invocation -> null).when(mock).methodWithNoArguments();

        testMatcher.matches(new InvocationOnMockActual(mock, SomeClass.class.getMethod("methodWithNoArguments"), null));

        verify(mock).methodWithNoArguments();
        assertThat(expectedInvocation, instanceOf(Invocation.class));
    }

    @Test(expected = AssertionError.class)
    public void testMatcherMismatch() {
        Invocation invocation = new Invocation(mock, SomeClass.class.getMethod("methodWithOneArgument", int.class), new Object[]{1});
        List<Matcher> matchers = Arrays.asList(is(2));
        matcher = new InvocationMatcher(invocation, matchers);

        InvocationMatcher testMatcher = Mockito.spy(matcher);

        testMatcher.matches(new InvocationOnMockActual(mock, SomeClass.class.getMethod("methodWithOneArgument", String.class), new Object[]{"test"}));
    }

    @Test
    public void testLocation() {
        Invocation invocation = new Invocation(mock, SomeClass.class.getMethod("methodWithOneArgument", int.class), new Object[]{1});
        Location location = new Location("TestClass.java", 10, 20);
        invocation.setLocation(location);

        List<Matcher> matchers = Arrays.asList(is(1));
        matcher = new InvocationMatcher(invocation, matchers);

        InvocationMatcher testMatcher = Mockito.spy(matcher);

        testMatcher.matches(new InvocationOnMockActual(mock, SomeClass.class.getMethod("methodWithOneArgument", int.class), new Object[]{1}));

        verify(mock).methodWithOneArgument(is(1));
        assertThat(testMatcher.getLocation(), is(location));
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation(mock, SomeClass.class.getMethod("methodWithOneArgument", int.class), new Object[]{1});
        Location location = new Location("TestClass.java", 10, 20);
        invocation.setLocation(location);

        List<Matcher> matchers = Arrays.asList(is(1));
        matcher = new InvocationMatcher(invocation, matchers);

        String expectedToString = "InvocationMatcher [invocation=Invocation Proxy: MockitoMock$MockitoMock$$EnhancerByMockitoWithCGLIB$$15a46a35 at TestClass.java:10:20, method=Method public int someClass.methodWithOneArgument(int), arguments=[1], matchers=[Matcher [is = 1]]]";
        String actualToString = matcher.toString();

        assertThat(actualToString, is(expectedToString));
    }
}