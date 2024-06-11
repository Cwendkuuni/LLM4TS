import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import static org.junit.Assert.*;

public class InvocationTest {

    @Test
    public void testToString() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(new Class[]{}, String.class);
        Object[] arguments = new Object[]{};
        Invocation invocation = new Invocation(mock, method, arguments, 1, null);

        String expectedInvocation = "mock.toString()";
        String actualInvocation = invocation.toString();

        assertEquals(expectedInvocation, actualInvocation);
    }
}