import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.mock.MockitoMock;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.reporting.PrintSettings;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class InvocationTest {

    @Test
    public void testInvocationConstruction() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "toString", new Class[0]);
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = spy(new RealMethod() {
            @Override
            public Object invoke(Object o, Object[] objects) throws Throwable {
                return o.toString();
            }
        });
        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        assertEquals(mock, invocation.getMock());
        assertEquals(mockitoMethod, invocation.getMethod());
        assertEquals(args, invocation.getArguments());
        assertEquals(sequenceNumber, invocation.getSequenceNumber());
        assertEquals(realMethod, invocation.getRealMethod());
    }
}