import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.Field;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        Class expectedType = String.class;
        Field field = new Field(String[].class, "myField", String[].class);
        GenericMaster genericMaster = new GenericMaster();
        Class actualType = genericMaster.getGenericType(field);

        assertEquals(expectedType, actualType);
    }
}