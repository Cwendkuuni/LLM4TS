import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndMatchers() {
        Invocation invocationMock = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(argThat(isA(String.class)), argThat(isA(Integer.class)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock, matchers);
        // Perform assertions on the InvocationMatcher instance
    }

    @Test(expected = NullPointerException.class)
    public void testInvocationMatcherNullMatchers() {
        Invocation invocationMock = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock, null);
    }

    @Test(expected = NullPointerException.class)
    public void testInvocationMatcherNullInvocation() {
        List<Matcher> matchers = Arrays.asList(argThat(isA(String.class)), argThat(isA(Integer.class)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(null, matchers);
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocation() {
        Invocation invocationMock = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock);
        // Perform assertions on the InvocationMatcher instance
    }

    @Test(expected = NullPointerException.class)
    public void testInvocationMatcherNullInvocationInConstructorWithOneArgument() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(null);
    }

    // Add more test cases and assertions as needed

    private Matcher argThat(Class<?> clazz) {
        return ArgumentMatchers.instanceOf(clazz);
    }
}