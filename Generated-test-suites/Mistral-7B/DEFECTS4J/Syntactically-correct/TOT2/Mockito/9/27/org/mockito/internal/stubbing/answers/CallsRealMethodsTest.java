import org.junit.Assert;
import org.mockito.Mockito;

public class CallsRealMethodsTest {

    @Test
    public void testDefaultConstructor() {
        // This test case is not needed as there is no logic in the constructor
    }

    @Test
    public void testAnswerMethodWithNullMockedMethod() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        InvocationOnMock invocationMock = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationMock.getMethod()).thenReturn(null);

        Object result = callRealMethods.answer(invocationMock);

        Assert.assertNull(result);
    }

    @Test
    public void testAnswerMethodWithException() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        Object throwable = new RuntimeException();
        InvocationOnMock invocationMock = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationMock.getMethod()).thenThrow(throwable);

        try {
            callRealMethods.answer(invocationMock);
            fail("An exception should have been thrown");
        } catch (Throwable e) {
            Assert.assertEquals(throwable.getClass(), e.getClass());
        }
    }

    @Test
    public void testAnswerMethodWithNoArguments() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        InvocationOnMock invocationMock = Mockito.mock(InvocationOnMock.class);

        Object result = callRealMethods.answer(invocationMock);

        Assert.assertNotNull(result);
    }

    @Test
    public void testAnswerMethodWithArguments() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        Object[] arguments = new Object[3];
        InvocationOnMock invocationMock = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationMock.getArguments()).thenReturn(arguments);

        Object result = callRealMethods.answer(invocationMock);

        Assert.assertNotNull(result);
    }

    @Test(expected = IncompatibleTypeException.class)
    public void testAnswerMethodWithIncompatibleReturnType() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        InvocationOnMock invocationMock = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationMock.getMethod()).thenReturn(new Method("testMethod"));
        Mockito.when(invocationMock.getTarget()).thenReturn(new Object());
        Mockito.when(invocationMock.getArguments()).thenReturn(new Object[0]);

        Mockito.when(invocationMock.getTarget().getClass()).thenReturn(String.class);
        Object result = callRealMethods.answer(invocationMock);

        fail("An IncompatibleTypeException should have been thrown");
    }

    @Test
    public void testGetSerializableMethod() {
        Serializable serializable = new Serializable() {
            @Override
            public String toString() {
                return "SerializableObject";
            }
        };
        CallsRealMethods callRealMethods = new CallsRealMethods();

        Serializable result = callRealMethods.getSerializable();

        Assert.assertEquals(serializable, result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetSerializableMethodWithNullObject() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        CallsRealMethods nullCallsRealMethods = null;

        try {
            callRealMethods.getSerializable();
        } catch (NullPointerException e) {
            callRealMethods = nullCallsRealMethods;
        }

        Assert.assertSame(null, callRealMethods);
    }
}