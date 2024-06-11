import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        JavaType javaType = simpleType;

        // Testing the class of the constructed type
        assertEquals(String.class, javaType.getRawClass());

        // Testing the superclass of the constructed type
        assertNull(javaType.getSuperClass());

        // Testing if the type is static
        assertFalse(javaType.isStatic());

        // Testing the bounds of the constructed type
        assertNull(javaType.getSuperTypes());

        // Testing the type name
        assertEquals("java.lang.String", javaType.toString());

        // Testing the type with a value handler
        Object handler = new Object() {};
        JavaType typeWithHandler = simpleType.withValueHandler(handler);
        assertTrue(typeWithHandler.getRawClass().isInstance(handler));
    }

    @Test
    public void testSimpleTypeRefine() {
        Class<List<String>> listClass = List.class.getTypeParameters()[0].getRawType();
        JavaType listType = TypeFactory.defaultInstance().constructType(listClass);

        SimpleType simpleType = new SimpleType(listType.getRawClass(), TypeBindings.emptyBindings(), null, null);

        JavaType refinedType = simpleType.refine(List.class, TypeBindings.emptyBindings(), null, null);
        assertNotNull(refinedType);

        JavaType expectedType = TypeFactory.defaultInstance().constructType(List.class, String.class);
        assertEquals(expectedType, refinedType);
    }

    @Test
    public void testSimpleTypeWithContentHandler() {
        Class<String[]> arrayClass = String[].class;

        SimpleType simpleType = new SimpleType(arrayClass, TypeBindings.emptyBindings(), null, null);

        Object handler = new Object() {};
        JavaType typeWithHandler = simpleType.withContentTypeHandler(handler);

        // This should throw an IllegalArgumentException since SimpleType has no content type
        try {
            simpleType.withContentValueHandler(handler);
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }
}