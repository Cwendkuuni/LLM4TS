import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.OngoingStubbing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {
    private InvocationMatcher matcher;
    private Invocation invocation;

    @Before
    public void setUp() {
        invocation = mock(InvocationOnMock.class);
        matcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testGetMethod() {
        Method actual = matcher.getMethod();
        Method expected = invocation.getMethod();
        assertThat(actual, equalTo(expected));
    }
}