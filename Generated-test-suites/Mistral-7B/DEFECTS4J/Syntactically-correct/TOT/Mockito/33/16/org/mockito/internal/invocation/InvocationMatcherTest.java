import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Object mock;

    @Before
    public void setUp() {
        mock = mock(Object.class);
    }

    @Test
    public void testNewInstanceWithMatchers() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("hello"), ArgumentMatchers.eq(1));
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Method method = Mockito.spy(InvocationMatcher.class.getMethod("matches", Invocation.class));
        Matcher expectedMatcher = ArgumentMatchers.argThat(new CapturesArguments<Matcher>() {
            @Override
            public void captureFrom(Matcher argument) {
                assertTrue(argument.matches(matchers.get(0)));
                assertTrue(argument.matches(matchers.get(1)));
            }
        });
        Mockito.doAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocationOnMock) {
                matcher.captureArgumentsFrom(invocationOnMock.getArguments());
                return true;
            }
        }).when(mock).matches(matchers.get(0));
        Mockito.doAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocationOnMock) {
                matcher.captureArgumentsFrom(invocationOnMock.getArguments());
                return true;
            }
        }).when(mock).matches(matchers.get(1));

        assertTrue(matcher.matches(new InvocationOnMock(mock, method, new Object[0])));
        verify(mock).matches(matchers.get(0));
        verify(mock).matches(matchers.get(1));
        assertTrue(matcher.toString().contains("hello"));
        assertTrue(matcher.toString().contains("1"));
    }

    @Test
    public void testNewInstanceWithoutMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        matcher = new InvocationMatcher(invocation);
        Method method = Mockito.spy(InvocationMatcher.class.getMethod("matches", Invocation.class));
        Mockito.doAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocationOnMock) {
                return matcher.invocation.getMock() == invocationOnMock.getMock()
                        && matcher.hasSameMethod(invocationOnMock)
                        && matcher.argumentsMatch(invocationOnMock.getArguments());
            }
        }).when(mock).matches(Mockito.any(Invocation.class));

        assertTrue(matcher.matches(new InvocationOnMock(mock, method, new Object[0])));
        verify(mock).matches(Mockito.any(Invocation.class));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        Method method = Mockito.spy(InvocationMatcher.class.getMethod("hasSameMethod", Invocation.class));

        Mockito.doAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocationOnMock) {
                return true;
            }
        }).when(mock).matches(Mockito.any(Invocation.class));

        assertTrue(matcher1.hasSameMethod(matcher2));
        verify(mock).matches(matcher1);
        verify(mock).matches(matcher2);
        assertFalse(matcher1.hasSameMethod(new InvocationMatcher(mock)));
        assertFalse(matcher1.hasSameMethod(new InvocationMatcher(mock, Collections.singletonList(ArgumentMatchers.any()))));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        Method method1 = Mockito.spy(InvocationMatcher.class.getMethod("hasSimilarMethod", Invocation.class));

        Mockito.doAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocationOnMock) {
                return true;
            }
        }).when(mock).matches(Mockito.any(Invocation.class));

        assertTrue(matcher1.hasSimilarMethod(matcher2));
        verify(mock).matches(matcher1);
        verify(mock).matches(matcher2);
        assertFalse(matcher1.hasSimilarMethod(new InvocationMatcher(mock)));
        assertFalse(matcher1.hasSimilarMethod(new InvocationMatcher(mock, Collections.singletonList(ArgumentMatchers.any()))));

        Object[] arguments1 = new Object[0];
        Object[] arguments2 = new Object[0];
        Invocation invocation3 = Mockito.mock(Invocation.class);
        Invocation invocation4 = Mockito.mock(Invocation.class);

        Mockito.doAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocationOnMock) {
                return matcher1.safelyArgumentsMatch(invocationOnMock.getArguments());
            }
        }).when(mock).matches(Mockito.eq(arguments1));

        Mockito.doAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocationOnMock) {
                return matcher1.safelyArgumentsMatch(arguments2);
            }
        }).when(mock).matches(Mockito.eq(arguments2));

        Mockito.doAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocationOnMock) {
                matcher2.captureArgumentsFrom(invocationOnMock.getArguments());
                return false;
            }
        }).when(mock).matches(Mockito.eq(arguments1));

        Mockito.doAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocationOnMock) {
                matcher2.captureArgumentsFrom(invocationOnMock.getArguments());
                return false;
            }
        }).when(mock).matches(Mockito.eq(arguments2));

        Mockito.doAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocationOnMock) {
                matcher3.captureArgumentsFrom(invocationOnMock.getArguments());
                return true;
            }
        }).when(mock).matches(invocation3);

        Mockito.doAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocationOnMock) {
                matcher4.captureArgumentsFrom(invocationOnMock.getArguments());
                return true;
            }
        }).when(mock).matches(invocation4);

        assertTrue(matcher1.hasSimilarMethod(matcher3));
        assertTrue(matcher1.hasSimilarMethod(matcher4));
        assertFalse(matcher1.hasSimilarMethod(matcher2));
    }
}