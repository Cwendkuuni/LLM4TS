import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testReturnsSmartNullsInstance() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        assertNotNull(returnsSmartNulls);
    }

    @Test
    public void testReturnsDefaultValueWhenMethodIsStubbed() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Mockito.when(mock.methodName()).thenReturn("mockValue");
        assertEquals("mockValue", returnsSmartNulls.answer(new InvocationOnMock(mock.class, "methodName", new Object[0])));
    }

    @Test
    public void testReturnsSmartNullWhenMethodIsStubbedButNotImposterised() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Mockito.when(mock.methodName()).then(new ReturnsSmartNulls());
        assertEquals("SmartNull returned by unstubbed methodName() method on mock", returnsSmartNulls.answer(new InvocationOnMock(mock.class, "methodName", new Object[0])));
    }

    @Test(expected = NullPointerException.class)
    public void testReturnsNullWhenMethodIsUnstubbedAndReturnTypeIsNotMockable() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        returnsSmartNulls.answer(new InvocationOnMock(String.class, "valueOf", new Object[]{"Hello"}));
    }

    @Test
    public void testReturnsSmartNullWhenMethodIsUnstubbed() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Mockito.doThrow(new RuntimeException()).when(mock).methodName();
        assertEquals("SmartNull returned by unstubbed methodName() method on mock", returnsSmartNulls.answer(new InvocationOnMock(mock.class, "methodName", new Object[0])));
    }
}