import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.Field;
import org.mockito.internal.util.reflection.GenericMaster;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = new Field(String.class, "name", String.class);

        // Test with a generic field
        Class expectedType = String.class;
        Class actualType = genericMaster.getGenericType(field);
        assertEquals(expectedType, actualType);

        // Test with a non-generic field
        field = new Field(int.class, "age", int.class);
        actualType = genericMaster.getGenericType(field);
        assertEquals(Object.class, actualType);

        // Test with null Field
        assertThrows(NullPointerException.class, () -> genericMaster.getGenericType(null));

        // Test with a private field
        field = new Field(String.class, "privateField", String.class);
        field.setAccessible(true); // Make it accessible
        actualType = genericMaster.getGenericType(field);
        assertEquals(Object.class, actualType);

        // Test with a non-existent field
        field = new Field(String.class, "nonExistentField", String.class);
        assertThrows(NoSuchFieldException.class, () -> genericMaster.getGenericType(field));
    }

    @Test
    public void testGetGenericTypeWithNestedGenerics() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = new Field(List.class, "list", new ParameterizedType() {{
            setOwnerType(List.class);
            setName("list");
            setRawType(ArrayList.class);
            setTypeArguments(new Type[] { Integer.class, Integer.class });
        }});

        Class expectedType = ArrayList.class;
        Class actualType = genericMaster.getGenericType(field);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetGenericTypeWithArray() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = new Field(int[].class, "intArray", int[].class);

        Class expectedType = int[].class;
        Class actualType = genericMaster.getGenericType(field);
        assertEquals(expectedType, actualType);
    }
}