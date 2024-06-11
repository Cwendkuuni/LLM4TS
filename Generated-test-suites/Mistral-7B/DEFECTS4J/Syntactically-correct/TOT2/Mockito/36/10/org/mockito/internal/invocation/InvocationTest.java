import org.junit.Test;
import static org.mockito.Mockito.*;

public class InvocationTest {

    @Test
    public void testInvocationConstruction() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] args = new Object[]{null};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, -1, Mockito.mock(RealMethod.class));
        assertNotNull(invocation);
    }

    @Test
    public void testInvocationConstructionWithNullMock() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] args = new Object[]{null};
        Invocation invocation = new Invocation(null, mockitoMethod, args, 1, Mockito.mock(RealMethod.class));
        assertNull(invocation);
    }

    @Test
    public void testInvocationConstructionWithEmptyArray() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] args = new Object[0];
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, Mockito.mock(RealMethod.class));
        assertNotNull(invocation);
    }

    @Test
    public void testInvocationConstructionWithNullArguments() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] args = new Object[]{null};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, Mockito.mock(RealMethod.class));
        assertNotNull(invocation);
    }

    @Test
    public void testInvocationConstructionWithPrimitiveArray() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] args = new Object[]{new int[1], 1};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, Mockito.mock(RealMethod.class));
        assertNotNull(invocation);
    }

    @Test
    public void testInvocationConstructionWithCustomObjectArray() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] args = new Object[]{new CustomObject[1], 1};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, Mockito.mock(RealMethod.class));
        assertNotNull(invocation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvocationConstructionWithNullRealMethod() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] args = new Object[0];
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, null);
    }
}

//