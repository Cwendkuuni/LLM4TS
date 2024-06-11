import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import static org.junit.Assert.*;

public class CallsRealMethodsTest {

    @Test
    public void testCallRealMethod() {
        // Test 1
        Object realObject = new Object() {
            public String methodToBeCalled() {
                return "Real method return value";
            }
        };
        CallsRealMethods answer = new CallsRealMethods();
        Object mockObject = Mockito.mock(Object.class);

        // When
        Mockito.doAnswer(answer).when(mockObject).methodToBeCalled();
        String result = (String) mockObject.methodToBeCalled();

        // Then
        Mockito.verify(mockObject).callRealMethod();
        Mockito.verifyNoMoreInteractions(mockObject);
        assertNotNull(result);
        assertEquals("Real method return value", result);

        // Testing with the real object
        String realResult = realObject.methodToBeCalled();
        assertNotNull(realResult);
        assertEquals("Real method return value", realResult);
    }

    @Test
    public void testCallRealMethodWithArguments() {
        // Test 2
        Object realObject = new Object() {
            public String methodToBeCalled(String arg1, int arg2) {
                return arg1 + " " + arg2;
            }
        };
        CallsRealMethods answer = new CallsRealMethods();
        Object mockObject = Mockito.mock(Object.class);

        // When
        Mockito.doAnswer(answer).when(mockObject).methodToBeCalled(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
        String result = (String) mockObject.methodToBeCalled("Test", 123);

        // Then
        Mockito.verify(mockObject).callRealMethod();
        Mockito.verifyNoMoreInteractions(mockObject);
        assertNotNull(result);
        assertEquals("Test 123", result);

        // Testing with the real object
        String realResult = realObject.methodToBeCalled("Test", 123);
        assertNotNull(realResult);
        assertEquals("Test 123", realResult);
    }

    @Test(expected = RuntimeException.class)
    public void testCallRealMethodWithException() {
        // Test 3
        Object realObject = new Object() {
            public String methodToBeCalled() throws RuntimeException {
                throw new RuntimeException("Real method exception");
            }
        };
        CallsRealMethods answer = new CallsRealMethods();
        Object mockObject = Mockito.mock(Object.class);

        // When
        Mockito.doAnswer(answer).when(mockObject).answer(Mockito.any(InvocationOnMock.class));

        // Then
        Mockito.verify(mockObject).callRealMethod();
        Mockito.verifyNoMoreInteractions(mockObject);

        // Call the real method and expect an exception
        try {
            mockObject.answer(Mockito.mock(InvocationOnMock.class));
            fail("Exception not thrown");
        } catch (Throwable e) {
            assertEquals("Real method exception", e.getCause().getMessage());
        }

        // Testing with the real object
        try {
            realObject.methodToBeCalled();
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("Real method exception", e.getMessage());
        }
    }

    @Test
    public void testCallRealMethodWithReflection() {
        // Test 4
        Object mockObject = Mockito.mock(Object.class);
        CallsRealMethods answer = new CallsRealMethods();
        Mockito.doAnswer(answer).when(mockObject).answer(Mockito.any(InvocationOnMock.class));

        assertEquals(mockObject.getClass().getMethod("callRealMethod"), mockObject.methodCall(new Method("callRealMethod")));
    }

    @Test
    public void testCallRealMethodWithArgumentsReflection() {
        // Test 5
        Object mockObject = Mockito.mock(Object.class);
        CallsRealMethods answer = new CallsRealMethods();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.doAnswer(answer).when(mockObject).answer(invocation);
        Object[] args = new Object[]{"arg1", 123};
        Mockito.doReturn(args).when(invocation).getArguments();

        assertEquals(args, mockObject.methodCall(invocation));
    }

    @Test(expected = RuntimeException.class)
    public void testCallRealMethodWithExceptionReflection() {
        // Test 6
        Object mockObject = Mockito.mock(Object.class);
        CallsRealMethods answer = new CallsRealMethods();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.doAnswer(answer).when(mockObject).answer(invocation);
        Mockito.doThrow(new RuntimeException("Exception message")).when(mockObject).callRealMethod();

        try {
            mockObject.answer(invocation);
            fail("Exception not thrown");
        } catch (Throwable e) {
            assertEquals("Exception message", e.getMessage());
        }
    }
}