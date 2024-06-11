import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.Field;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        Class fieldType = Integer.TYPE;
        Field field = new Field("testField", fieldType, Integer.class);
        GenericMaster genericMaster = new GenericMaster();
        Class genericType = genericMaster.getGenericType(field);
        assertEquals(fieldType, genericType);
    }

    @Test(expected = NullPointerException.class)
    public void testGetGenericTypeNullField() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = null;
        genericMaster.getGenericType(field);
    }

    @Test
    public void testGetGenericTypeNonGenericField() {
        Class fieldType = String.class;
        Field field = new Field("testField", fieldType, fieldType);
        GenericMaster genericMaster = new GenericMaster();
        Class genericType = genericMaster.getGenericType(field);
        assertEquals(Object.class, genericType);
    }
}