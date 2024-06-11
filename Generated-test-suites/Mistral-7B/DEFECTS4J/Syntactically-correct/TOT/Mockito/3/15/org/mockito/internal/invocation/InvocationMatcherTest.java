import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMockAdapter;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_EmptyMatchers() {
        Invocation invocation = mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocation.getMethod();
        assertEquals(method.getName(), invocationMatcher.getMethod().getName());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testInvocationMatcher_CustomMatchers() {
        Invocation invocation = new InvocationAdapter(new InvocationOnMockAdapter(new Object(), new Method(Integer.TYPE, int.class), new Object[0], new Object[]{1}) {
            @Override
            public Object[] getArguments() {
                return new Object[]{new Integer(1), new Integer(2)};
            }
        });

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(argThat(isA(Integer.class)), argThat(isInstanceOf(Integer.class))));

        Method method = invocation.getMethod();
        assertEquals(method.getName(), invocationMatcher.getMethod().getName());
        assertEquals(invocation, invocationMatcher.getInvocation());

        Matcher[] matchers = invocationMatcher.getMatchers().toArray(new Matcher[0]);
        assertThat(matchers[0], isA(ArgThat.class));
        assertThat(matchers[1], isInstanceOf(InstanceOf.class));

        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testInvocationMatcher_NullInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(null);
        assertFalse(invocationMatcher.matches(mock(InvocationOnMock.class)));
    }

    @Test
    public void testInvocationMatcher_DifferentMethods() {
        Invocation invocation1 = mock(InvocationOnMock.class);
        Invocation invocation2 = mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Method method1 = invocation1.getMethod();
        Method method2 = invocation2.getMethod();

        assertFalse(invocationMatcher1.matches(invocation2));
        assertFalse(invocationMatcher2.matches(invocation1));
    }

    @Test
    public void testInvocationMatcher_DifferentMocks() {
        Invocation invocation1 = mock(InvocationOnMock.class);
        Invocation invocation2 = mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertFalse(invocationMatcher1.matches(invocation2));
        assertFalse(invocationMatcher2.matches(invocation1));
    }

    @Test
    public void testInvocationMatcher_SameMethodAndSameArguments() {
        Invocation invocation = mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation);

        Method method = invocation.getMethod();
        assertEquals(method.getName(), invocationMatcher.getMethod().getName());
        assertEquals(method.getName(), invocationMatcher2.getMethod().getName());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation, invocationMatcher2.getInvocation());

        assertTrue(invocationMatcher.matches(invocationMatcher2));
        assertTrue(invocationMatcher2.matches(invocationMatcher));
    }

    @Test
    public void testInvocationMatcher_SameMethodButDifferentArguments() {
        Invocation invocation = mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, Arrays.asList(anyInt(), anyInt()));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, Arrays.asList(anyInt(), anyString()));

        Method method = invocation.getMethod();
        assertEquals(method.getName(), invocationMatcher1.getMethod().getName());
        assertEquals(method.getName(), invocationMatcher2.getMethod().getName());
        assertEquals(invocation, invocationMatcher1.getInvocation());
        assertEquals(invocation, invocationMatcher2.getInvocation());

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
        assertFalse(invocationMatcher2.matches(invocationMatcher1));
    }
}