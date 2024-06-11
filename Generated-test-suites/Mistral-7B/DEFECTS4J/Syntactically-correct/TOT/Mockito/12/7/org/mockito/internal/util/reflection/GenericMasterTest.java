import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.util.reflection.GenericMaster;
import java.lang.reflect.Field;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();

        try {
            Field field = SomeClass.class.getDeclaredField("fieldName"); // replace with actual class and field name
            Class expectedType = String.class; // replace with actual expected type
            Class actualType = genericMaster.getGenericType(field);

            Assert.assertEquals(expectedType, actualType);
        } catch (NoSuchFieldException e) {
            Assert.fail("Field not found: " + e.getMessage());
        } catch (IllegalAccessException e) {
            Assert.fail("Unable to access field: " + e.getMessage());
        }
    }

    static class SomeClass {
        private String fieldName;
    }
}