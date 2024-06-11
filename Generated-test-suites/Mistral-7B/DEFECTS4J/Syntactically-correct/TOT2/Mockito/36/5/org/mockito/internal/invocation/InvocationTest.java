import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InvocationTest {

    @Test
    public void testInvocationConstructor() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "toString", new Class[0]);
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        // Basic functionality test
        verify(mockitoMethod).match(new Equals(mock));
        verify(realMethod).invoke(mock, args);

        // Edge cases and exception handling tests
        testArgumentsEdgeCases(invocation);
        testExceptionHandling(invocation);
    }

    // Other helper methods for edge cases and exception handling tests go here
}