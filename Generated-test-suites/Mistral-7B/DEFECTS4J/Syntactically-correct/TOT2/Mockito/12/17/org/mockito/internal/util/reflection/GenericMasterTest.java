// getGenericTypeTest.java
import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

public class GetGenericTypeTest {
    @Test
    public void testGetGenericType_ReturnsCorrectType() {
        Field field = getClassField("List<Integer> list");
        Class expectedType = Integer.class;
        Class actualType = new GenericMaster().getGenericType(field);
        assertEquals(expectedType, actualType);
    }

    private Field getClassField(String fieldName) {
        try {
            return this.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}