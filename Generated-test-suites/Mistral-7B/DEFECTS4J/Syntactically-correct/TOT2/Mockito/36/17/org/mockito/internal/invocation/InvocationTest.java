import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.MockitoMethod;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.matchers.Jdk8.instanceOf;

public class InvocationTest {

    @Test
    public void testInvocationCreation() {
        Object mock = mock(String.class);
        MockitoMethod method = new MockitoMethod("testMethod", String[].class, Void.TYPE);
        Object[] args = new Object[]{new String[]{"test"}};
        int sequenceNumber = 1;
        RealMethod realMethod = instanceOf(String.class.getMethod("testMethod", String[].class));

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertEquals(mock, invocation.getMock());
        assertEquals(method, invocation.getMethod());
        assertArrayEquals(args, invocation.getArguments());
        assertEquals(sequenceNumber, invocation.getSequenceNumber());
    }
}