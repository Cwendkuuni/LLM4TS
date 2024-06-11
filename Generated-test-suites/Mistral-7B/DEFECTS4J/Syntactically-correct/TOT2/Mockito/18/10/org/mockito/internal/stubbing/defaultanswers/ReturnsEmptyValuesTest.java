import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InternalStubbingHelper;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(Object.class);
        Object result = returnsEmptyValues.answer(invocation);
        Assertions.assertNull(result);
    }

    @Test
    public void testAnswerMethodWithNullReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(Object.class);
        Class<?> nullType = null;
        Assertions.assertThrows(NullPointerException.class, () -> returnsEmptyValues.returnValueFor(nullType));
    }

    @Test
    public void testReturnValueForPrimitives() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        int expectedInt = 0;
        char expectedChar = '\u0000';
        double expectedDouble = 0.0;
        float expectedFloat = 0.0f;
        byte expectedByte = (byte) 0;
        short expectedShort = (short) 0;
        long expectedLong = 0L;
        boolean expectedBoolean = false;

        int actualInt = (int) returnsEmptyValues.returnValueFor(int.class);
        Assertions.assertEquals(expectedInt, actualInt);

        char actualChar = (char) returnsEmptyValues.returnValueFor(char.class);
        Assertions.assertEquals(expectedChar, actualChar);

        double actualDouble = returnsEmptyValues.returnValueFor(double.class);
        Assertions.assertEquals(expectedDouble, actualDouble);

        float actualFloat = returnsEmptyValues.returnValueFor(float.class);
        Assertions.assertEquals(expectedFloat, actualFloat);

        byte actualByte = (byte) returnsEmptyValues.returnValueFor(byte.class);
        Assertions.assertEquals(expectedByte, actualByte);

        short actualShort = (short) returnsEmptyValues.returnValueFor(short.class);
        Assertions.assertEquals(expectedShort, actualShort);

        long actualLong = returnsEmptyValues.returnValueFor(long.class);
        Assertions.assertEquals(expectedLong, actualLong);

        boolean actualBoolean = returnsEmptyValues.returnValueFor(boolean.class);
        Assertions.assertEquals(expectedBoolean, actualBoolean);
    }

    @Test
    public void testReturnValueForNonPrimitiveCollections() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Class<?> listType = List.class;
        List<Object> expectedList = new ArrayList<>();

        List<Object> actualList = (List<Object>) returnsEmptyValues.returnValueFor(listType);
        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    public void testReturnValueForNonCollectionReturnTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Class<?> stringType = String.class;

        String actualString = (String) returnsEmptyValues.returnValueFor(stringType);
        Assertions.assertNull(actualString);
    }

    @Test
    public void testToStringMethodWithCustomMockName() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = new Object();
        MockName customMockName = mock(MockName.class);
        when(customMockName.toString()).thenReturn("CustomMockName");
        InvocationOnMock invocation = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getMockName()).thenReturn(customMockName);

        String result = (String) returnsEmptyValues.answer(invocation);
        Assertions.assertEquals("Mock for CustomMockName, hashCode: someHashCode", result);
    }

    @Test
    public void testCompareToMethodWithDifferentObjects() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object object1 = new Object();
        Object object2 = new Object();

        int actualInt = (int) returnsEmptyValues.answer(new InvocationOnMock(new Object(), "compareTo", object1, object2));
        Assertions.assertEquals(1, actualInt);
    }

    @Test
    public void testAnswerMethodForToString() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = new Object();
        InvocationOnMock invocation = mock(Object.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("toString", (Class[]) null));
        when(invocation.getMock()).thenReturn(mock);

        String result = (String) returnsEmptyValues.answer(invocation);
        Assertions.assertNotNull(result);
    }

    @Test
    public void testAnswerMethodForCompareTo() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object object1 = new Object();
        Object object2 = new Object();
        InvocationOnMock invocation = mock(Object.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("compareTo", Object.class));
        when(invocation.getArguments()[0]).thenReturn(object2);

        int result = (int) returnsEmptyValues.answer(invocation);
        Assertions.assertEquals(1, result);
    }
}