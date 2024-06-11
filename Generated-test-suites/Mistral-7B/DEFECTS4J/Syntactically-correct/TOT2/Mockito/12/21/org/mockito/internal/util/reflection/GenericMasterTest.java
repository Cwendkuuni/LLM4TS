import org.junit.jupiter.api.*;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.Field;

@SuppressWarnings("unused")
public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        Class fieldType = Integer.TYPE;
        Field field = new Field("testField", fieldType, GenericMaster.class);
        Class expectedType = Integer.class;
        Class actualType = new GenericMaster().getGenericType(field);
        assertSame(expectedType, actualType);
    }
}