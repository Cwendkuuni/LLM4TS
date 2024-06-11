import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestUtil;

public class GenericMasterTest {

    @Test
    void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = FieldTestUtil.getField(String[].class, Object[].class, "testField");
        Class expectedType = String[].class;
        Class actualType = genericMaster.getGenericType(field);
        assertEquals(expectedType, actualType);
    }
}