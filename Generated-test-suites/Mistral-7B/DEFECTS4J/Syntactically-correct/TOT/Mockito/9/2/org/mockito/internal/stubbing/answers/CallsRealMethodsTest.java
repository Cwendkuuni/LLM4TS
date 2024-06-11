import org.junit.Before;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;

public class CallsRealMethodsTest {
    private CallsRealMethods callsRealMethods;

    @Before
    public void setup() {
        callsRealMethods = new CallsRealMethods();
    }

    @Test
    public void testCallsRealMethodWithNullArguments() throws Throwable {
        // Given
        Object realObject = new Object() {
            public void methodToBeCalledWithNull() {
                System.out.println("Real method called with null");
            }
        };
        Mockito.doNothing().when(realObject).methodToBeCalledWithNull(); // Stubbing the side effect

        // When
        callsRealMethods.answer(Mockito.mock(InvocationOnMock.class, realObject));

        // Then
        // No exception should be thrown and the real method should be called
        realObject.methodToBeCalledWithNull();
    }

    @Test
    public void testCallsRealMethodWithValidArguments() throws Throwable {
        // Given
        Object realObject = new Object() {
            public int methodToBeCalledWithValidArguments(int a, int b) {
                return a + b;
            }
        };

        // When
        int result = (int) callsRealMethods.answer(Mockito.mock(InvocationOnMock.class, realObject, new Object[] {1, 2}));

        // Then
        // The real method should be called and the result should be the sum of the arguments
        Mockito.verify(realObject).methodToBeCalledWithValidArguments(1, 2);
        Mockito.verifyNoMoreInteractions(realObject);
        assertEquals(3, result);
    }
}