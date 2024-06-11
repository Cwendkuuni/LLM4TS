import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.internal.invocation.InvocationMatcher;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructionWithMatchers() {
        Object mock = mock(SomeClass.class);
        Object[] arguments = new Object[]{1, "test"};
        List<Matcher> matchers = Collections.singletonList(Mockito.argThat(new SomeMatcher()));
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        verify(mock).methodUnderTest(arguments[0], arguments[1]); // verify call to methodUnderTest with actual arguments
        // assert matchers
        Matcher matcher = invocationMatcher.getMatchers().get(0);
        assertTrue(matcher.matches(arguments[0]));
        assertTrue(matcher.matches(arguments[1]));
    }

    @Test
    public void testInvocationMatcherConstructionWithoutMatchers() {
        Object mock = mock(SomeClass.class);
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        verify(mock).methodUnderTest(arguments[0], arguments[1]); // verify call to methodUnderTest with actual arguments
    }

    @Test
    public void testMatches_sameInvocation() {
        Object mock = mock(SomeClass.class);
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);

        assertEquals(invocationMatcher, anotherInvocationMatcher); // both instances have the same invocation reference
        assertTrue(invocationMatcher.matches(anotherInvocationMatcher));
    }

    @Test
    public void testMatches_differentInvocations() {
        Object mock = mock(SomeClass.class);
        Object[] arguments1 = new Object[]{1, "test1"};
        Object[] arguments2 = new Object[]{1, "test2"};
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethod_sameMethodNameAndSameArguments() {
        Object mock = mock(SomeClass.class);
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);

        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("methodUnderTest");
        when(method2.getName()).thenReturn("methodUnderTest");
        when(method1.getParameterTypes()).thenReturn(new Class[]{Integer.TYPE, String.class});
        when(method2.getParameterTypes()).thenReturn(new Class[]{Integer.TYPE, String.class});

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethod_sameMethodNameButDifferentArguments() {
        Object mock = mock(SomeClass.class);
        Object[] arguments1 = new Object[]{1, "test1"};
        Object[] arguments2 = new Object[]{1, "test2"};
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);

        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("methodUnderTest");
        when(method2.getName()).thenReturn("methodUnderTest");
        when(method1.getParameterTypes()).thenReturn(new Class[]{Integer.TYPE, String.class});
        when(method2.getParameterTypes()).thenReturn(new Class[]{Integer.TYPE, Double.TYPE});

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList(arguments1));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList(arguments2));

        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethod_differentMethodName() {
        Object mock = mock(SomeClass.class);
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);

        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("methodUnderTest1");
        when(method2.getName()).thenReturn("methodUnderTest2");

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testToString() {
        Object mock = mock(SomeClass.class);
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        String expected = String.format("InvocationMatcher{invocation=%s, matchers=%s}", invocation, Collections.emptyList());
        String actual = invocationMatcher.toString();
        assertEquals(expected, actual);
    }
}