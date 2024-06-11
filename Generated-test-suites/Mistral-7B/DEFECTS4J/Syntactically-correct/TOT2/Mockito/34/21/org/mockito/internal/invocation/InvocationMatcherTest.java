import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testMatches() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(Mockito.any());
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, matchers);

        Invocation actualInvocation = Mockito.mock(Invocation.class);
        List<Matcher> actualMatchers = Arrays.asList(Mockito.any(), Mockito.any());
        InvocationMatcher actualMatcher = new InvocationMatcher(actualInvocation, actualMatchers);

        assertTrue(invocationMatcher1.matches(actualMatcher));
    }
}