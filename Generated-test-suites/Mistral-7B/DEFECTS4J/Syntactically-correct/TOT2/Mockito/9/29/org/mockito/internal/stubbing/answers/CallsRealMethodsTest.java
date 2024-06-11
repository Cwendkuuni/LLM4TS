
import org.junit.*;
import org.mockito.*;
import org.mockito.junit.*;

public class CallsRealMethodsTest {

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        CallsRealMethods callsRealMethods = new CallsRealMethods();

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);

        objStream.writeObject(callsRealMethods);
        objStream.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        CallsRealMethods deserializedCallsRealMethods = (CallsRealMethods) objectInputStream.readObject();
        objectInputStream.close();

        Assert.assertNotNull(deserializedCallsRealMethods);
    }

    @Test
    public void testAnswerMethod() {
        CallsRealMethods callsRealMethods = Mockito.mock(CallsRealMethods.class);
        InvocationOnMock invocationMock = Mockito.mock(InvocationOnMock.class);

        Object expectedResult = new Object();
        Mockito.when(invocationMock.getMock()).thenReturn(callsRealMethods);
        Mockito.when(invocationMock.getMethod()).thenReturn(Mockito.method(String.class, "testMethod"));
        Mockito.when(callsRealMethods.answer(invocationMock)).thenReturn(expectedResult);

        Object actualResult = callsRealMethods.answer(invocationMock);

        Mockito.verify(callsRealMethods).answer(invocationMock);
        Mockito.verifyNoMoreInteractions(invocationMock);
        Mockito.verify(invocationMock).callRealMethod();

        Assert.assertEquals(expectedResult, actualResult);
    }
}