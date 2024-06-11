import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleType() {
        Class<Integer> intClass = Integer.class;
        JavaType intType = new SimpleType(intClass);

        // Test isContainerType
        assertFalse(intType.isContainerType());

        // Test toString
        String intTypeString = intType.toString();
        assertEquals("[simple type, class [I]", intTypeString);

        // Test equals
        JavaType intTypeCopy = new SimpleType(intClass);
        assertTrue(intType.equals(intTypeCopy));

        // Test _narrow
        JavaType intArrayType = new SimpleType(int[].class);
        JavaType intArrayListType = new SimpleType(java.util.ArrayList.class);
        JavaType intArrayTypeNarrowed = intType._narrow(intArrayType.getClass());
        assertNull(intArrayTypeNarrowed);

        JavaType intArrayListTypeNarrowed = intType._narrow(intArrayListType.getClass());
        assertNotNull(intArrayListTypeNarrowed);
        assertEquals(intArrayListType, intArrayListTypeNarrowed);

        // Test withContentType
        try {
            intType.withContentType(new SimpleType(String.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test withTypeHandler
        Object intHandler = new Object() {};
        JavaType intTypeWithHandler = intType.withTypeHandler(intHandler);
        assertNotNull(intTypeWithHandler);
        assertEquals(intHandler, intTypeWithHandler.getTypeHandler());

        // Test withStaticTyping
        JavaType intTypeStatic = intType.withStaticTyping();
        assertFalse(intTypeStatic._asStatic);

        JavaType intTypeStatic2 = intType.withStaticTyping();
        assertEquals(intTypeStatic, intTypeStatic2);
        assertTrue(intTypeStatic._asStatic);
    }
}