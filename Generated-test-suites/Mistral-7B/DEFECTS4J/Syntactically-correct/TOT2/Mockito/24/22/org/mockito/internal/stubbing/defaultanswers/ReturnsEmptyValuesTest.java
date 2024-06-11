import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

    private Object mock;
    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setup() {
        returnsEmptyValues = new ReturnsEmptyValues();
        mock = mock(Object.class);
    }

    @Test
    public void testAnswerToStringMethod() {
        String expected = "Mock for Object, hashCode: " + mock.hashCode();
        assertEquals(expected, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class)));
    }

    @Test
    public void testAnswerCompareToMethod() {
        verify(mock).compareTo(mock);
    }

    @Test
    public void testReturnValueForPrimitiveType() {
        byte expectedPrimitiveByte = (byte) 0;
        byte expectedWrapperByte = Byte.MIN_VALUE;
        assertEquals(expectedPrimitiveByte, returnsEmptyValues.returnValueFor(byte.class).byteValue());
        assertEquals(expectedWrapperByte, returnsEmptyValues.returnValueFor(Byte.class).byteValue());
    }

    @Test
    public void testReturnValueForCollectionType() {
        List<Object> expected = new ArrayList<>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(List.class));
    }

    @Test
    public void testReturnValueForSetType() {
        Set<Object> expected = new HashSet<>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(Set.class));
    }

    @Test
    public void testReturnValueForMapType() {
        Map<Object, Object> expected = new HashMap<>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(Map.class));
    }
}