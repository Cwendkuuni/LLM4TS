import org.junit.jupiters.javasupport.testisolation. isolation.Isolated;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

@Isolated
public class InvocationMatcherTest {

    @org.junit.Test
    public void testInvocationMatcherWithNoMatchers() {
        Stubber stubber = Mockito.mock(Stubber.class);
        Invocation invocation = new InvocationOnMock(stubber.createMock(Object.class), new Object[0], new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocation.getMethod();
        List<Matcher> matchers = invocationMatcher.getMatchers();

        assertThat(matchers, is(notNullValue()));
        assertThat(matchers.size(), is(0));
        assertThat(invocationMatcher.matches(invocation), is(true));
    }

    @org.junit.Test
    public void testInvocationMatcherWithCustomMatchers() {
        Stubber stubber = Mockito.mock(Stubber.class);
        Invocation invocation = new InvocationOnMock(stubber.createMock(Object.class), new Object[]{1, 2, "abc"}, new Object[0]);
        Matcher m1 = ArgumentMatchers.equalTo(1);
        Matcher m2 = ArgumentMatchers.equalTo("abc");
        List<Matcher> matchers = Arrays.asList(m1, m2);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocation.getMethod();
        List<Matcher> matchersFromMatcher = invocationMatcher.getMatchers();

        assertThat(matchersFromMatcher, is(sameInstance(matchers)));
        assertThat(invocationMatcher.matches(invocation), is(true));
    }

    @org.junit.Test
    public void testInvocationMatcherWithDifferentMethod() {
        Stubber stubber = Mockito.mock(Stubber.class);
        Invocation invocation1 = new InvocationOnMock(stubber.createMock(Object.class), new Object[]{1}, new Object[0]);
        Invocation invocation2 = new InvocationOnMock(stubber.createMock(Object.class), new Object[]{2}, new Object[0]);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        List<Matcher> matchers1 = invocationMatcher1.getMatchers();
        List<Matcher> matchers2 = invocationMatcher1.getMatchers();

        assertThat(matchers1, is(notNullValue()));
        assertThat(invocationMatcher1.matches(invocation1), is(true));
        assertThat(invocationMatcher1.matches(invocation2), is(false));
    }

    @org.junit.Test
    public void testInvocationMatcherWithSameMethodAndSameArguments() {
        Stubber stubber = Mockito.mock(Stubber.class);
        Invocation invocation1 = new InvocationOnMock(stubber.createMock(Object.class), new Object[]{1, 2, "abc"}, new Object[0]);
        Invocation invocation2 = new InvocationOnMock(stubber.createMock(Object.class), new Object[]{1, 2, "abc"}, new Object[0]);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        List<Matcher> matchers1 = invocationMatcher1.getMatchers();
        List<Matcher> matchers2 = invocationMatcher1.getMatchers();

        assertThat(matchers1, is(notNullValue()));
        assertThat(invocationMatcher1.matches(invocation1), is(true));
        assertThat(invocationMatcher1.matches(invocation2), is(true));
    }

    @org.junit.Test
    public void testInvocationMatcherWithSameMethodButDifferentArguments() {
        Stubber stubber = Mockito.mock(Stubber.class);
        Invocation invocation1 = new InvocationOnMock(stubber.createMock(Object.class), new Object[]{1}, new Object[0]);
        Invocation invocation2 = new InvocationOnMock(stubber.createMock(Object.class), new Object[]{2}, new Object[0]);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        List<Matcher> matchers1 = invocationMatcher1.getMatchers();

        assertThat(matchers1, is(notNullValue()));
        assertThat(invocationMatcher1.matches(invocation1), is(true));
        assertThat(invocationMatcher1.matches(invocation2), is(false));
    }

    @org.junit.Test
    public void testInvocationMatcherWithSameMethodButNullArguments() {
        Stubber stubber = Mockito.mock(Stubber.class);
        Invocation invocation1 = new InvocationOnMock(stubber.createMock(Object.class), new Object[0], new Object[0]);
        Invocation invocation2 = new InvocationOnMock(stubber.createMock(Object.class), null, new Object[0]);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        List<Matcher> matchers1 = invocationMatcher1.getMatchers();

        assertThat(matchers1, is(notNullValue()));
        assertThat(invocationMatcher1.matches(invocation1), is(true));
        assertThat(invocationMatcher1.matches(invocation2), is(false));
    }

    @org.junit.Test
    public void testInvocationMatcherWithVarargs() {
        Stubber stubber = Mockito.mock(Stubber.class);
        Invocation invocation = new InvocationOnMock(stubber.createMock(Object.class), new Object[]{1, 2, 3, "abc"}, new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> matchers = invocationMatcher.getMatchers();
        Matcher m = matchers.get(1);

        if (m instanceof CapturesArguments) {
            CapturesArguments capturedArguments = (CapturesArguments) m;
            Object[] capturedArgumentsArray = capturedArguments.getArguments();
            assertThat(capturedArgumentsArray[0], is(sameInstance("abc")));
            assertThat(capturedArgumentsArray[1], is(nullValue()));
            assertThat(capturedArgumentsArray.length, is(invocation.getRawArguments().length - 1));
        }

        Object[] rawArguments = invocation.getRawArguments();

        assertThat(invocationMatcher.matches(invocation), is(true));
    }
}