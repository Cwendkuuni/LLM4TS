import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;

public class CallsRealMethodsTest {

    @Test
    public void testCallsRealMethod() {
        // Given
        Object mockObject = Mockito.mock(SomeClass.class);
        CallsRealMethods answer = Mockito.spy(new CallsRealMethods());
        // When
        Mockito.doAnswer(answer).when(mockObject).someMethod();
        // Then
        Object result = mockObject.someMethod();
        // Assert
        Mockito.verify(mockObject).callRealMethod(); // Test that the real method was called
        Mockito.verifyNoMoreInteractions(mockObject); // Test that no other interactions occurred
        assertEquals(result, mockObject.someRealMethod()); // Test that the result of the call is as expected
    }

    // ... add more test cases for other methods in the class

    private static class SomeClass {
        public void someMethod() {
            // some implementation
        }

        public Object someRealMethod() {
            // some other implementation
            return "some result";
        }
    }
}