import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.internal.invocation.InvocationMatcher;
import java.lang.reflect.Method;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testGetMethod() throws Exception {
        Invocation invocation = mock(Invocation.class);
        Method method = new Method("someMethod", new Class[] { int.class, String.class });
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
       assertEquals(method, invocationMatcher.getMethod());
    }
}