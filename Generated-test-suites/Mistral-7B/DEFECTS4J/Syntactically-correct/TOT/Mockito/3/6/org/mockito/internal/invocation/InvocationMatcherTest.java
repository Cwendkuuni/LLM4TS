import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private Object mockObject;
    private List<Matcher> matchers;

    @Before
    public void setUp() {
        mockObject = mock(Object.class);
        matchers = Arrays.asList(
                ArgumentMatchers.eq(1),
                ArgumentMatchers.eq("hello"),
                ArgumentMatchers.not(ArgumentMatchers.isNull())
        );
    }

    @Test
    public void testInvocationMatcherWithEmptyMatcherList() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(Mockito.mock(Invocation.class), Collections.emptyList());
        InvocationMatcher expectedMatcher = new InvocationMatcher(Mockito.mock(Invocation.class));
        assertTrue(invocationMatcher.matches(expectedMatcher));
    }

    @Test
    public void testInvocationMatcherWithCustomMatcherList() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(Mockito.mock(Invocation.class), matchers);
        InvocationMatcher expectedMatcher = new InvocationMatcher(Mockito.mock(Invocation.class), matchers);
        assertTrue(invocationMatcher.matches(expectedMatcher));
    }

    @Test
    public void testInvocationMatcherWithNoMatchingInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(Mockito.mock(Invocation.class), matchers);
        InvocationMatcher noMatchingInvocationMatcher = new InvocationMatcher(Mockito.mock(Invocation.class), Arrays.asList(ArgumentMatchers.eq(2)));
        assertFalse(invocationMatcher.matches(noMatchingInvocationMatcher));
    }

    @Test
    public void testInvocationMatcherWithMatchingInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(Mockito.mock(Invocation.class), matchers);
        InvocationMatcher matchingInvocationMatcher = new InvocationMatcher(Mockito.mock(InvocationOnMock.class), matchers);
        assertTrue(invocationMatcher.matches(matchingInvocationMatcher));
    }

    @Test
    public void testHasSameMethodWithSameMethodNameAndArguments() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        given(invocation1.getMethod()).thenReturn(method);
        given(invocation2.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethodWithDifferentMethods() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        given(invocation1.getMethod()).thenReturn(method1);
        given(invocation2.getMethod()).thenReturn(method2);
        given(method1.getName()).thenReturn("method1");
        given(method2.getName()).thenReturn("method2");

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcherWithVarArgs() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{1, 2, "hello"};
        given(invocation.getArguments()).thenReturn(arguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = Arrays.asList(
                ArgumentMatchers.eq(1),
                ArgumentMatchers.eq("hello")
        );
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(invocation);
        expectedMatcher.captureArgumentsFrom(invocation);

        verify(invocation, VerificationMode.INORDER).<Integer>invoke(ArgumentMatchers.eq(1));
        verify(invocation, VerificationMode.INORDER).<String>invoke(ArgumentMatchers.eq("hello"));
    }
}