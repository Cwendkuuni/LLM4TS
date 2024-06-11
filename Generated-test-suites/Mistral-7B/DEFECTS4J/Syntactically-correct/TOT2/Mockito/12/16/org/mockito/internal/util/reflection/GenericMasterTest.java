import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldType;

public class GenericMasterTest {

    @Test
    void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = new FieldType("testField", Integer.class).getDeclaredField();

        Class expectedGenericType = Integer.class;
        Class actualGenericType = genericMaster.getGenericType(field);

        assertEquals(expectedGenericType, actualGenericType);
    }
}