import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.Anything;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.invocation.MockitoMethod;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockitoJUnitRunner;

public class InvocationTest {

    @Test
    public void testConstructor() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] args = new Object[] {"arg1", 42};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        assertEquals(mock, invocation.getMock());
        assertEquals(mockitoMethod, invocation.getMethod());
        assertArrayEquals(args, invocation.getArguments());
        assertEquals(sequenceNumber, invocation.getSequenceNumber());
        assertEquals(realMethod, invocation.realMethod);
    }
}