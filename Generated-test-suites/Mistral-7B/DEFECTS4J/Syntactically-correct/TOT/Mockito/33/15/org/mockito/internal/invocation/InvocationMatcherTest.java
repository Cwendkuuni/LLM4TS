import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.InvocationOnMockImpl;
import org.mockito.stub.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    @Test
    public void testNewInvocationMatcherWithMatchers() {
        Object mock = mock(String.class);
        Method method = Mockito.method(String.class, "length");
        Invocation invocation = new InvocationOnMockImpl(mock, method, new Object[]{new String("test")});
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.isA(String.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.matches(new InvocationOnMockImpl(mock, method, new Object[]{"test"})));
        assertFalse(invocationMatcher.matches(new InvocationOnMockImpl(mock, method, new Object[]{"other"})));
    }

    @Test
    public void testNewInvocationMatcherWithoutMatchers() {
        Object mock = mock(String.class);
        Method method = Mockito.method(String.class, "length");
        Invocation invocation = new InvocationOnMockImpl(mock, method, new Object[]{new String("test")});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.matches(new InvocationOnMockImpl(mock, method, new Object[]{"test"})));
        assertFalse(invocationMatcher.matches(new InvocationOnMockImpl(mock, method, new Object[]{"other"})));
    }

    @Test
    public void testHasSimilarMethod() {
        Object mock = mock(String.class);

        Method method1 = Mockito.method(String.class, "length");
        Method method2 = Mockito.method(String.class, "length");
        Method method3 = Mockito.method(String.class, "substring", Integer.TYPE, Integer.TYPE);

        Invocation invocation1 = new InvocationOnMockImpl(mock, method1, new Object[]{new String("test")});
        Invocation invocation2 = new InvocationOnMockImpl(mock, method1, new Object[]{new String("test")});
        Invocation invocation3 = new InvocationOnMockImpl(mock, method3, new Object[]{new String("test"), 0, 3});

        assertTrue(new InvocationMatcher(invocation1).hasSimilarMethod(invocation2));
        assertFalse(new InvocationMatcher(invocation1).hasSimilarMethod(invocation3));
    }

    @Test
    public void testToString() {
        Object mock = mock(String.class);
        Method method = Mockito.method(String.class, "length");
        Invocation invocation = new InvocationOnMockImpl(mock, method, new Object[]{new String("test")});

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        Answer<String> answer = new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) {
                return invocation.getArgument(0);
            }
        };
        Object argument = "argument";

        Mockito.doAnswer(answer).when(mock).length();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(ArgumentMatchers.eq(argument));

        String result = invocationMatcher.toString(new PrintSettings().suppressDefaultArguments());

        assertEquals("Invocation on mock: [String] length\n" +
                "    Invoked with: [argument]\n", result);
    }
}