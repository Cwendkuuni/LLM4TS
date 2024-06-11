import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

public class InvocationTest {

    @Test
    public void testInvocationConstructor_ValidArguments() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] args = new Object[]{"abc"};
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        Assert.assertEquals(mock, invocation.getMock());
        Assert.assertEquals(mockitoMethod, invocation.getMethod());
        Assert.assertArrayEquals(args, invocation.getArguments());
        Assert.assertEquals(sequenceNumber, invocation.getSequenceNumber());
        Assert.assertEquals(realMethod, invocation.realMethod);
    }
}