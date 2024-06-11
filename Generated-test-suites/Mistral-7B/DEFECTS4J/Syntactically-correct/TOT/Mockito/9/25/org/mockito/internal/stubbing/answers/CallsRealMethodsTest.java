import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;

public class CallsRealMethodsTest {

    @Test
    public void testCallRealMethod() {
        // Given
        Object mockObject = Mockito.mock(Object.class, new CallsRealMethods());
        // When
        Object result = mockObject.methodToTest(); // assume methodToTest is a method in the mocked object
        // Then
        Object expectedResult = new Object(); // replace this with the expected result for the method call
        Mockito.verify(mockObject).callRealMethod();
        Mockito.verify(mockObject).methodToTest();
        assertSame(expectedResult, result);
    }

    // Other test cases can be added here
}