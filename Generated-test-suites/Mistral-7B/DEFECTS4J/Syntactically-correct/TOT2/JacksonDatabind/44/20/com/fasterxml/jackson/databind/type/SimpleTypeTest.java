import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {
    // Test case for the default constructor
    @Test
    public void testDefaultConstructor() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertFalse(simpleType.isContainerType());
    }

    // Test case for the constructor with bindings, superClass, and superInterfaces
    @Test
    public void testConstructWithBindingsSuperClassSuperInterfaces() {
        JavaType superClass = new SimpleType(Integer.class);
        SimpleType simpleType = new SimpleType(String.class, null, superClass, new JavaType[0]);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(superClass, simpleType.getSuperClass());
        assertArrayEquals(new JavaType[0], simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    // Test case for the copy constructor
    @Test
    public void testCopyConstructor() {
        SimpleType original = new SimpleType(String.class);
        SimpleType copy = new SimpleType(original);
        assertEquals(original, copy);
    }

    // Test case for the constructor with valueHandler and typeHandler
    @Test
    public void testConstructWithValueHandlerTypeHandler() {
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        SimpleType simpleType = new SimpleType(String.class, null, null, null, valueHandler, typeHandler, false);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertFalse(simpleType.isContainerType());
    }

    // Test case for the constructor with valueHandler, typeHandler, and asStatic
    @Test
    public void testConstructWithValueHandlerTypeHandlerAsStatic() {
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        SimpleType simpleType = new SimpleType(String.class, null, null, null, valueHandler, typeHandler, true);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertTrue(simpleType.isStatic());
        assertFalse(simpleType.isContainerType());
    }

    // Test case for the constructUnsafe method
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertFalse(simpleType.isContainerType());
    }

    // Test case for the deprecated construct method
    @Test
    public void testConstructDeprecated() {
        try {
            SimpleType simpleType = SimpleType.construct(String.class);
            fail("Should not reach this point");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    // Test case for the _narrow method
    @Test
    public void testNarrow() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(Integer.class);
        SimpleType result = simpleType1._narrow(Integer.class);
        assertNotNull(result);
        assertEquals(simpleType2, result);
    }

    // Test case for the withContentType, withTypeHandler, withValueHandler, and withStaticTyping methods
    @Test
    public void testMethods() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType contentType = new SimpleType(Integer.class);
        Object typeHandler = new Object();
        Object valueHandler = new Object();
        SimpleType result1 = simpleType.withContentType(contentType);
        SimpleType result2 = simpleType.withTypeHandler(typeHandler);
        SimpleType result3 = simpleType.withValueHandler(valueHandler);
        SimpleType result4 = simpleType.withStaticTyping();
        assertEquals(simpleType, result1);
        assertEquals(simpleType, result2);
        assertEquals(simpleType, result3);
        assertEquals(simpleType, result4);
    }
}