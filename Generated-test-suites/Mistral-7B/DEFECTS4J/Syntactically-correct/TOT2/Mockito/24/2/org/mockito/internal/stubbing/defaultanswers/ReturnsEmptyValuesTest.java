import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {
    private ReturnsEmptyValues returnsEmptyValues;

    @BeforeEach
    public void setup() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testAnswerToStringMethod() {
        Object mock = Mockito.mock(String.class);
        InvocationOnMock invocation = Mockito.methodCall(Mockito.isA(String.class), Mockito.isA(String.class));
        String expected = "Mock for String, hashCode: " + mock.hashCode();
        String actual = (String) returnsEmptyValues.answer(invocation);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testAnswerCompareToMethod() {
        Object mock1 = Mockito.mock(Comparable.class);
        Object mock2 = Mockito.mock(Comparable.class);
        InvocationOnMock invocation1 = Mockito.methodCall(mock1, "compareTo", mock2);
        int expected = 1;
        int actual = (int) returnsEmptyValues.answer(invocation1);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testAnswerOtherMethod() {
        Class<?> type = int[].class;
        Object actual = returnsEmptyValues.answer(Mockito.methodCall(Mockito.isA(int[].class), Mockito.any()));
        Assertions.assertNull(actual);
    }

    @Test
    public void testReturnValueForPrimitiveType() {
        Class<?> intType = int.class;
        Object actual = new ReturnsEmptyValues().returnValueFor(intType);
        int expected = 0;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testReturnValueForCollectionType() {
        Class<?> listType = LinkedList.class;
        Object actual = new ReturnsEmptyValues().returnValueFor(listType);
        Assertions.assertInstanceOf(LinkedList.class, actual);
        Assertions.assertTrue(((LinkedList) actual).isEmpty());
    }

    @Test
    public void testReturnValueForSetType() {
        Class<?> setType = LinkedHashSet.class;
        Object actual = new ReturnsEmptyValues().returnValueFor(setType);
        Assertions.assertInstanceOf(LinkedHashSet.class, actual);
        Assertions.assertTrue(((LinkedHashSet) actual).isEmpty());
    }

    @Test
    public void testReturnValueForMapType() {
        Class<?> mapType = LinkedHashMap.class;
        Object actual = new ReturnsEmptyValues().returnValueFor(mapType);
        Assertions.assertInstanceOf(LinkedHashMap.class, actual);
        Assertions.assertTrue(((LinkedHashMap) actual).isEmpty());
    }

    @Test
    public void testReturnValueForCustomType() {
        Class<?> customType = MyCustomClass.class;
        Object actual = new ReturnsEmptyValues().returnValueFor(customType);
        Assertions.assertNull(actual);
    }

    @Test
    public void testReturnValueForNullType() {
        Class<?> nullType = null;
        Assertions.assertNull(new ReturnsEmptyValues().returnValueFor(nullType));
    }

    @Test
    public void testAnswerForNullMethod() {
        Object mock = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.methodCall(null, Mockito.any());
        Assertions.assertThrows(NullPointerException.class, () -> returnsEmptyValues.answer(invocation));
    }
}