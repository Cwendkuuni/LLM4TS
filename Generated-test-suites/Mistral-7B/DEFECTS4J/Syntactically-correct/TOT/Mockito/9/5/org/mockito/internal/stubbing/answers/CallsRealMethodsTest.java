import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*; // For BDD style testing

public class CallsRealMethodsTest {

    @Test
    public void testCallsRealMethodWithNoArguments() {
        // Given
        Object realObject = new Object() {
            public int methodToMock() {
                return 42;
            }
        };

        CallsRealMethods answer = new CallsRealMethods();

        // When
        Object realResult = answer.answer(Mockito.mock(InvocationOnMock.class).args(realObject));

        // Then
        assertEquals(42, realResult);
    }

    @Test
    public void testCallsRealMethodWithOneArgument() {
        // Given
        Object realObject = new Object() {
            public int methodToMock(int arg) {
                return arg * 2;
            }
        };

        CallsRealMethods answer = new CallsRealMethods();

        // When
        int result = ((Integer) answer.answer(Mockito.mock(InvocationOnMock.class).args(realObject, 5))).intValue();

        // Then
        assertEquals(10, result);
    }

    @Test(expected = RuntimeException.class)
    public void testCallsRealMethodWithError() {
        // Given
        Object realObject = new Object() {
            public void methodToMock() throws RuntimeException {
                throw new RuntimeException("Test error");
            }
        };

        CallsRealMethods answer = new CallsRealMethods();

        try {
            // When
            answer.answer(Mockito.mock(InvocationOnMock.class).args(realObject));
            fail("Method should have thrown an exception");
        } catch (Throwable e) {
            // Then
            assertTrue(e instanceof RuntimeException);
            assertEquals("Test error", e.getMessage());
        }
    }
}