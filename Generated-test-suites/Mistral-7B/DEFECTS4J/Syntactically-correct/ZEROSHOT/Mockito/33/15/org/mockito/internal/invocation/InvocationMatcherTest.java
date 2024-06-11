import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private InvocationMatcher emptyMatcherInvocation;
    private InvocationMatcher singleMatcherInvocation;
    private InvocationMatcher multipleMatchersInvocation;

    @Before
    public void setup() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);

        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.argThat(new MyMatcher()), ArgumentMatchers.eq("test"));
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        emptyMatcherInvocation = new InvocationMatcher(invocation);
        singleMatcherInvocation = new InvocationMatcher(invocation, Collections.singletonList(ArgumentMatchers.eq("test")));
        multipleMatchersInvocation = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testEmptyMatcher() {
        InvocationMatcher emptyMatcher = emptyMatcherInvocation;

        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> emptyMatchers = Collections.emptyList();

        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, emptyMatchers);

        assertThat(emptyMatcher.getMatchers(), is(emptyMatchers));
        assertThat(emptyMatcher, is(sameInstance(expectedMatcher)));
    }

    @Test
    public void testSingleMatcher() {
        InvocationMatcher singleMatcher = singleMatcherInvocation;

        Object test = "test";
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> singleMatchers = Collections.singletonList(ArgumentMatchers.eq(test));

        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, singleMatchers);

        assertThat(singleMatcher.getMatchers(), is(singleMatchers));
        assertThat(singleMatcher, is(sameInstance(expectedMatcher)));
        assertThat(singleMatcher.matches(Mockito.mock(InvocationOnMock.class)), is(true));
    }

    @Test
    public void testMultipleMatchers() {
        InvocationMatcher multipleMatchers = multipleMatchersInvocation;

        Object arg1 = "arg1";
        Object arg2 = "arg2";
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.argThat(new MyMatcher()), ArgumentMatchers.eq("test"));

        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, matchers);

        assertThat(multipleMatchers.getMatchers(), is(matchers));
        assertThat(multipleMatchers, is(sameInstance(expectedMatcher)));
        InvocationOnMock mock = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = new Object[]{arg1, arg2};

        Mockito.when(invocation.getArguments()).thenReturn(arguments);

        // Verify that the matchers are evaluated in order
        Answer<Boolean> answer = Mockito.answer(inv -> {
            boolean matches = false;
            for (Matcher matcher : matchers) {
                matches |= matcher.matches(inv.getArguments()[0]);
            }
            return matches;
        });

        Mockito.when(invocation.getMock()).thenAnswer(answer);

        boolean result = multipleMatchers.matches(invocation);

        Mockito.verify(invocation, Mockito.times(1)).getArguments();
        Mockito.verify(invocation, Mockito.times(1)).getMock();

        Mockito.verify(matchers.get(0), Mockito.times(1)).matches(arg1);
        Mockito.verify(matchers.get(1), Mockito.times(1)).matches(arg2);

        assertThat(result, is(true));
    }

    @Test
    public void testMatches() {
        InvocationMatcher invocationMatcher = this.invocationMatcher;

        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.argThat(new MyMatcher()), ArgumentMatchers.eq("test"));
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, matchers);

        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);

        Mockito.when(invocation.getMock()).thenReturn(invocation);

        Object[] arguments = new Object[]{1, 2};
        Mockito.when(invocation.getArguments()).thenReturn(arguments);

        Answer<Boolean> answer = Mockito.answer(inv -> {
            return invocationMatcher.matches(inv);
        });

        Mockito.when(invocation.getMock()).thenAnswer(answer);

        boolean result = invocationMatcher.matches(invocation);

        Mockito.verify(invocation, Mockito.times(1)).getArguments();
        Mockito.verify(invocation, Mockito.times(1)).getMock();

        Mockito.verify(matchers.get(0), Mockito.times(1)).matches(arguments[0]);
        Mockito.verify(matchers.get(1), Mockito.times(1)).matches("test");

        assertThat(result, is(true));
    }

    @Test
    public void testHasSameMethod() {
        InvocationMatcher invocationMatcher = this.invocationMatcher;

        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);

        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation1.getMethod()).thenReturn(method1);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);

        Mockito.when(invocation1.getMock()).thenReturn(invocation1);
        Mockito.when(invocation2.getMock()).thenReturn(invocation2);

        boolean result = invocationMatcher.hasSameMethod(invocation1);

        Mockito.verify(invocation1, Mockito.times(1)).getMethod();
        Mockito.verify(invocation2, Mockito.times(1)).getMethod();

        Mockito.verifyNoMoreInteractions(method1, method2);

        result = invocationMatcher.hasSameMethod(invocation2);

        Mockito.verify(invocation1, Mockito.times(1)).getMethod();
        Mockito.verify(invocation2, Mockito.times(1)).getMethod();

        Mockito.verifyNoMoreInteractions(method1, method2);

        assertThat(result, is(false));

        Mockito.when(method1.getName()).thenReturn("testMethod");
        Mockito.when(method2.getName()).thenReturn("testMethod");

        result = invocationMatcher.hasSameMethod(invocation1);

        Mockito.verify(invocation1, Mockito.times(1)).getMethod();
        Mockito.verify(invocation2, Mockito.times(1)).getMethod();

        Mockito.verifyNoMoreInteractions(method1, method2);

        result = invocationMatcher.hasSameMethod(invocation2);

        Mockito.verify(invocation1, Mockito.times(1)).getMethod();
        Mockito.verify(invocation2, Mockito.times(1)).getMethod();

        Mockito.verifyNoMoreInteractions(method1, method2);

        assertThat(result, is(true));
    }

    @Test
    public void testToString() {
        InvocationMatcher invocationMatcher = this.invocationMatcher;

        String expectedString = "InvocationMatcher{method=<not proxied> [Class InvocationMatcher], invocation=<not proxied> [Invocation], matchers=[ArgThat method matches <MyMatcher>], printSettings=PrintSettings{}}";

        String result = invocationMatcher.toString();

        assertThat(result, is(notNullValue()));
        assertThat(result, is(expectedString));
    }
}

class MyMatcher implements ArgumentMatcher<Object> {
    @Override
    public boolean matches(Object argument) {
        return true;
    }
}