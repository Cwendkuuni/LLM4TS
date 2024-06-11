import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.Field;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        Field sampleField = new Field(String.class, "sampleField", String.class);
        Class expectedType = String.class;
        Class actualType = genericMaster.getGenericType(sampleField);

        assertEquals(expectedType, actualType);
    }
}