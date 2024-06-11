import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        assertEquals(String.class, stringType.getRawClass());
        assertEquals(SimpleType.class, stringType.getClass());
        assertNull(stringType.getSuperClass());
        assertNull(stringType.getSuperInterfaces());
        assertNull(stringType.getValueHandler());
        assertNull(stringType.getTypeHandler());
        assertFalse(stringType.isStatic());
    }

    @Test
    public void testSimpleTypeCopyConstruction() {
        Class<String> stringClass = String.class;
        SimpleType originalStringType = new SimpleType(stringClass);
        SimpleType copiedStringType = new SimpleType(originalStringType);
        assertEquals(originalStringType.getRawClass(), copiedStringType.getRawClass());
        assertEquals(originalStringType.getClass(), copiedStringType.getClass());
        assertEquals(originalStringType.getSuperClass(), copiedStringType.getSuperClass());
        assertArrayEquals(originalStringType.getSuperInterfaces(), copiedStringType.getSuperInterfaces());
        assertEquals(originalStringType.getValueHandler(), copiedStringType.getValueHandler());
        assertEquals(originalStringType.getTypeHandler(), copiedStringType.getTypeHandler());
        assertEquals(originalStringType.isStatic(), copiedStringType.isStatic());
    }

    @Test
    public void testInvalidTypeConstruction() {
        Class<List<String>> listClass = ArrayList.class;
        try {
            new SimpleType(listClass);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.ArrayList)", e.getMessage());
        }
    }

    @Test
    public void testNarrowType() {
        Class<String> stringClass = String.class;
        Class<StringBuilder> stringBuilderClass = StringBuffer.class;
        SimpleType stringType = new SimpleType(stringClass);
        SimpleType stringBuilderType = new SimpleType(stringBuilderClass);
        SimpleType narrowedStringBuilderType = stringType.narrow(StringBuilder.class);
        assertEquals(stringBuilderType.getRawClass(), narrowedStringBuilderType.getRawClass());
        assertEquals(stringBuilderType.getClass(), narrowedStringBuilderType.getClass());
        assertEquals(stringBuilderType.getSuperClass(), narrowedStringBuilderType.getSuperClass());
        assertArrayEquals(stringBuilderType.getSuperInterfaces(), narrowedStringBuilderType.getSuperInterfaces());
        assertEquals(stringBuilderType.getValueHandler(), narrowedStringBuilderType.getValueHandler());
        assertEquals(stringBuilderType.getTypeHandler(), narrowedStringBuilderType.getTypeHandler());
        assertEquals(stringBuilderType.isStatic(), narrowedStringBuilderType.isStatic());
    }

    @Test
    public void testTypeWithValueHandler() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        Object valueHandler = new Object() {
            @Override
            public String toString() {
                return "valueHandler";
            }
        };
        SimpleType stringWithValueHandlerType = stringType.withValueHandler(valueHandler);
        assertEquals(stringType.getRawClass(), stringWithValueHandlerType.getRawClass());
        assertEquals(stringType.getClass(), stringWithValueHandlerType.getClass());
        assertNull(stringType.getValueHandler());
        assertEquals(valueHandler, stringWithValueHandlerType.getValueHandler());
        assertEquals(stringType.getSuperClass(), stringWithValueHandlerType.getSuperClass());
        assertArrayEquals(stringType.getSuperInterfaces(), stringWithValueHandlerType.getSuperInterfaces());
        assertEquals(stringType.isStatic(), stringWithValueHandlerType.isStatic());
    }

    @Test
    public void testTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        Object typeHandler = new Object() {
            @Override
            public String toString() {
                return "typeHandler";
            }
        };
        SimpleType stringWithTypeHandlerType = stringType.withTypeHandler(typeHandler);
        assertEquals(stringType.getRawClass(), stringWithTypeHandlerType.getRawClass());
        assertEquals(stringType.getClass(), stringWithTypeHandlerType.getClass());
        assertEquals(stringType.getValueHandler(), stringWithTypeHandlerType.getValueHandler());
        assertEquals(typeHandler, stringWithTypeHandlerType.getTypeHandler());
        assertEquals(stringType.getSuperClass(), stringWithTypeHandlerType.getSuperClass());
        assertArrayEquals(stringType.getSuperInterfaces(), stringWithTypeHandlerType.getSuperInterfaces());
        assertEquals(stringType.isStatic(), stringWithTypeHandlerType.isStatic());
    }

    @Test
    public void testTypeWithStaticTyping() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        SimpleType stringWithStaticTypingType = stringType.withStaticTyping();
        assertEquals(stringType.getRawClass(), stringWithStaticTypingType.getRawClass());
        assertEquals(stringType.getClass(), stringWithStaticTypingType.getClass());
        assertEquals(stringType.getValueHandler(), stringWithStaticTypingType.getValueHandler());
        assertEquals(stringType.getTypeHandler(), stringWithStaticTypingType.getTypeHandler());
        assertTrue(stringType.isStatic());
        assertFalse(stringWithStaticTypingType.isStatic());
    }

    @Test
    public void testTypeWithInvalidContentType() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        JavaType invalidContentType = null;
        try {
            SimpleType withInvalidContentTypeType = stringType.withContentType(invalidContentType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testTypeWithContentTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        Object contentTypeHandler = new Object() {
            @Override
            public String toString() {
                return "contentTypeHandler";
            }
        };
        try {
            SimpleType withContentTypeHandlerType = stringType.withContentTypeHandler(contentTypeHandler);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testTypeWithInvalidValueHandler() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        Object invalidValueHandler = null;
        try {
            SimpleType withInvalidValueHandlerType = stringType.withValueHandler(invalidValueHandler);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Value handler can not be null", e.getMessage());
        }
    }

    @Test
    public void testTypeWithContentValueHandler() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        Object contentValueHandler = new Object() {
            @Override
            public String toString() {
                return "contentValueHandler";
            }
        };
        try {
            SimpleType withContentValueHandlerType = stringType.withContentValueHandler(contentValueHandler);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testTypeWithStaticTypingFalseAndThenTrue() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        SimpleType staticTypingFalseType = stringType.withStaticTyping();
        SimpleType staticTypingTrueType = staticTypingFalseType.withStaticTyping();
        assertEquals(stringType.getRawClass(), staticTypingFalseType.getRawClass());
        assertEquals(stringType.getClass(), staticTypingFalseType.getClass());
        assertFalse(staticTypingFalseType.isStatic());
        assertEquals(stringType.getRawClass(), staticTypingTrueType.getRawClass());
        assertEquals(stringType.getClass(), staticTypingTrueType.getClass());
        assertTrue(staticTypingTrueType.isStatic());
    }

    @Test
    public void testRefineType() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        Class<StringBuilder> stringBuilderClass = StringBuffer.class;
        JavaType refinedType = stringType.refine(stringBuilderClass, null, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<List<String>> listClass = ArrayList.class;
        SimpleType listType = new SimpleType(listClass);
        assertEquals("[java.util.ArrayList, List<String>]", listType.buildCanonicalName());
    }

    @Test
    public void testIsContainerType() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        assertFalse(stringType.isContainerType());
    }
}