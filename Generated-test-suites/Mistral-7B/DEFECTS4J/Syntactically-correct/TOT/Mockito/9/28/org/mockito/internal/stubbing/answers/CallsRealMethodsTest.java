import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class CallsRealMethodsTest {
    private CallsRealMethods callRealMethods;
    private Object mockObject;

    @Before
    public void setUp() {
        callRealMethods = new CallsRealMethods();
        mockObject = mock(Object.class);
    }

    @Test
    public void testCallRealMethod() {
        // Test call real method with valid argument
        Object result = callRealMethods.answer(Mockito.argThat(inv -> inv.getTarget() == mockObject));
        verify(mockObject).callRealMethod();
        assertNotNull(result);

        // Test call real method with null argument
        try {
            callRealMethods.answer(Mockito.argThat(inv -> inv.getArgument(0) == null));
            fail("Expected IllegalAccessException");
        } catch (IllegalAccessException e) {
            // Expected
        }

        // Test call real method with String argument
        String arg = "test";
        Object result = callRealMethods.answer(Mockito.argThat(inv -> inv.getArgument(0).equals(arg)));
        verify(mockObject).callRealMethod();
        assertEquals(arg, result);

        // Test call real method with Integer argument
        int arg = 42;
        Object result = callRealMethods.answer(Mockito.argThat(inv -> inv.getArgument(0).equals(arg)));
        verify(mockObject).callRealMethod();
        assertEquals(arg, result);

        // Test call real method with Object argument
        Object arg = new Object();
        Object result = callRealMethods.answer(Mockito.argThat(inv -> inv.getArgument(0).equals(arg)));
        verify(mockObject).callRealMethod();
        assertEquals(arg, result);

        // Test call real method with List argument
        List<String> arg = Arrays.asList("test1", "test2");
        Object result = callRealMethods.answer(Mockito.argThat(inv -> inv.getArgument(0).equals(arg)));
        verify(mockObject).callRealMethod();
        assertEquals(arg, result);

        // Test call real method with Array argument
        String[] arg = new String[]{"test1", "test2"};
        Object result = callRealMethods.answer(Mockito.argThat(inv -> inv.getArgument(0).getClass() == arg.getClass()));
        verify(mockObject).callRealMethod();
        assertArrayEquals(arg, (String[])result);

        // Test call real method with Empty List argument
        List<String> arg = Collections.emptyList();
        Object result = callRealMethods.answer(Mockito.argThat(inv -> inv.getArgument(0).equals(arg)));
        verify(mockObject).callRealMethod();
        assertEquals(arg, result);

        // Test call real method with Empty Array argument
        String[] arg = new String[0];
        Object result = callRealMethods.answer(Mockito.argThat(inv -> inv.getArgument(0).getClass() == arg.getClass()));
        verify(mockObject).callRealMethod();
        assertArrayEquals(arg, (String[])result);

        // Test call real method with private method
        Object privateMethodMock = spy(new PrivateMethodClass());
        callRealMethods.answer(Mockito.argThat(inv -> inv.getTarget() == privateMethodMock));
        privateMethodMock.privateMethod();
        verify(privateMethodMock).privateMethod();

        // Test call real method with exception
        Object exceptionMock = mock(Exception.class);
        try {
            callRealMethods.answer(Mockito.argThat(inv -> inv.getTarget() instanceof Exception));
            fail("Expected InvocationTargetException");
        } catch (InvocationTargetException e) {
            assertEquals(exceptionMock, e.getCause());
        }
    }

    @Test
    public void testSerializability() {
        CallsRealMethods serializedCallsRealMethods = new CallsRealMethods();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(serializedCallsRealMethods);
        objectOutputStream.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Object deserializedCallsRealMethods = objectInputStream.readObject();

        assertEquals(serializedCallsRealMethods, deserializedCallsRealMethods);
    }

    @Test
    public void testCallRealMethodWithPrivateMethodClass() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        PrivateMethodClass privateMethodClass = new PrivateMethodClass();

        Object result = callRealMethods.answer(Mockito.argThat(inv -> inv.getTarget() == privateMethodClass));
        verifyNoMoreInteractions(privateMethodClass);
        assertEquals(privateMethodClass, result);
    }

    class PrivateMethodClass {
        private void privateMethod() {
            // Private method
        }
    }
}