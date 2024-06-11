import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.util.reflection.GenericMaster;
import java.lang.reflect.Field;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        Class fieldType = String.class;
        Field field = new Field("name", String.class, null);

        Class expectedResult = String.class;
        Class actualResult = new GenericMaster().getGenericType(field);

        assertEquals(expectedResult, actualResult);
    }
}