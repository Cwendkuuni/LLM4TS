import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        assertEquals(String.class, stringType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), stringType.getBindings());
        assertNull(stringType.getSuperClass());
        assertNull(stringType.getSuperInterfaces());
        assertFalse(stringType.isStatic());
    }

    @Test
    public void testSimpleTypeCopyConstruction() {
        Class<String> stringClass = String.class;
        SimpleType originalStringType = new SimpleType(stringClass);
        SimpleType copiedStringType = new SimpleType(originalStringType);
        assertEquals(originalStringType, copiedStringType);
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<String> stringClass = String.class;
        Object valueHandler = new Object();
        SimpleType stringType = new SimpleType(stringClass, null, null, null, valueHandler, null, false);
        assertEquals(valueHandler, stringType.getValueHandler());
        assertEquals(stringType.getValueHandler(), stringType.getValueHandler()); // test equality
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        Object typeHandler = new Object();
        SimpleType stringType = new SimpleType(stringClass, null, null, null, null, typeHandler, false);
        assertEquals(typeHandler, stringType.getTypeHandler());
        assertEquals(stringType.getTypeHandler(), stringType.getTypeHandler()); // test equality
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass, null, null, null, null, null, false);
        SimpleType staticStringType = stringType.withStaticTyping();
        assertTrue(staticStringType.isStatic());
        assertFalse(stringType.isStatic());
        assertEquals(stringType, staticStringType.withoutStaticTyping());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructUnsafe() {
        SimpleType.constructUnsafe(String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstruct() {
        SimpleType.construct(String.class);
    }

    @Test
    public void testSimpleTypeWithContentType() {
        JavaType contentType = new SimpleType(Integer.TYPE);
        SimpleType simpleType = new SimpleType(String.class, null, null, null, null, null, false);
        assertNull(simpleType.withContentType(contentType));
    }

    @Test
    public void testSimpleTypeWithContentTypeHandler() {
        Object contentTypeHandler = new Object();
        SimpleType simpleType = new SimpleType(String.class, null, null, null, null, null, false);
        assertNull(simpleType.withContentTypeHandler(contentTypeHandler));
    }

    @Test
    public void testSimpleTypeWithValueHandlerWithStaticTyping() {
        Class<String> stringClass = String.class;
        Object valueHandler = new Object();
        SimpleType stringType = new SimpleType(stringClass, null, null, null, valueHandler, null, false);
        SimpleType staticStringType = stringType.withValueHandler(valueHandler).withStaticTyping();
        assertTrue(staticStringType.isStatic());
        assertFalse(stringType.isStatic());
        assertEquals(stringType, staticStringType.withoutStaticTyping());
        assertEquals(valueHandler, staticStringType.getValueHandler());
    }

    @Test
    public void testSimpleTypeWithTypeHandlerWithStaticTyping() {
        Class<String> stringClass = String.class;
        Object typeHandler = new Object();
        SimpleType stringType = new SimpleType(stringClass, null, null, null, null, typeHandler, false);
        SimpleType staticStringType = stringType.withTypeHandler(typeHandler).withStaticTyping();
        assertTrue(staticStringType.isStatic());
        assertFalse(stringType.isStatic());
        assertEquals(stringType, staticStringType.withoutStaticTyping());
        assertEquals(typeHandler, staticStringType.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithValueHandlerWithoutStaticTyping() {
        Class<String> stringClass = String.class;
        Object valueHandler = new Object();
        SimpleType stringType = new SimpleType(stringClass, null, null, null, valueHandler, null, false);
        SimpleType stringTypeWithoutStaticTyping = stringType.withoutStaticTyping();
        assertFalse(stringTypeWithoutStaticTyping.isStatic());
        assertEquals(valueHandler, stringTypeWithoutStaticTyping.getValueHandler());
    }

    @Test
    public void testSimpleTypeWithTypeHandlerWithoutStaticTyping() {
        Class<String> stringClass = String.class;
        Object typeHandler = new Object();
        SimpleType stringType = new SimpleType(stringClass, null, null, null, null, typeHandler, false);
        SimpleType stringTypeWithoutStaticTyping = stringType.withoutStaticTyping();
        assertFalse(stringTypeWithoutStaticTyping.isStatic());
        assertEquals(typeHandler, stringTypeWithoutStaticTyping.getTypeHandler());
    }

    @Test
    public void testSimpleTypeEquals() {
        Class<String> stringClass = String.class;
        SimpleType stringType1 = new SimpleType(stringClass, null, null, null, null, null, false);
        SimpleType stringType2 = new SimpleType(stringClass, null, null, null, null, null, false);
        SimpleType stringType3 = new SimpleType(String[].class, null, null, null, null, null, false);
        assertEquals(stringType1, stringType1);
        assertEquals(stringType1, stringType2);
        assertNotEquals(stringType1, stringType3);
    }

    @Test
    public void testSimpleTypeGetCanonicalName() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass, null, null, null, null, null, false);
        assertEquals("java.lang.String", stringType.getCanonicalName());
    }

    @Test
    public void testSimpleTypeIsContainerType() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass, null, null, null, null, null, false);
        assertFalse(stringType.isContainerType());
    }
}