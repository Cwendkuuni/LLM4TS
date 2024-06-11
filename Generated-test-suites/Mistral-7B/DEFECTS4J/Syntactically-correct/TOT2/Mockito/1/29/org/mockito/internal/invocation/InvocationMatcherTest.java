import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.junit.Runners;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class InvocationMatcherTest {

    @Test
    public void testMatches() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Object mock = Mockito.mock(Object.class);

        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        List<Matcher> matchers1 = Arrays.asList(Mockito.any(), Mockito.anyInt());
        List<Matcher> matchers2 = Arrays.asList(Mockito.any(), Mockito.anyInt());

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers2);

        assertThat(invocationMatcher1.matches(invocation1), is(true));
        assertThat(invocationMatcher1.matches(invocation2), is(false));
        assertThat(invocationMatcher1.matches(invocationMatcher1), is(true));

        assertThat(invocationMatcher2.matches(invocation1), is(false));
        assertThat(invocationMatcher2.matches(invocationMatcher1), is(false));
        assertThat(invocationMatcher2.matches(invocationMatcher2), is(true));

        assertThat(invocationMatcher1, is(not(sameInstance(invocationMatcher2))));

        // Test with mocks having the same method and arguments
        when(method1.getName()).thenReturn("testMethod");
        when(method2.getName()).thenReturn("testMethod");
        when(method1.getMock()).thenReturn(mock);
        when(method2.getMock()).thenReturn(mock);

        Object[] args1 = new Object[]{1, 2};
        Object[] args2 = new Object[]{1, 2};

        invocation1 = Mockito.mock(Invocation.class);
        invocation2 = Mockito.mock(Invocation.class);

        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation1.getArguments()).thenReturn(args1);
        when(invocation2.getMethod()).thenReturn(method2);
        when(invocation2.getArguments()).thenReturn(args2);

        assertThat(invocationMatcher1.matches(invocationMatcher2), is(true));
    }
}