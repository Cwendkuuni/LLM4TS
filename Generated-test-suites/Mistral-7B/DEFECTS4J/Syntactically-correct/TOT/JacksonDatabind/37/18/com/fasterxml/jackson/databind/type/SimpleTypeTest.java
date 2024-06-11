import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);

        assertEquals(String.class, stringType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), stringType.getTypeBindings());
        assertNull(stringType.getSuperClass());
        assertArrayEquals(new Class[0], stringType.getSuperInterfaces());
        assertFalse(stringType.isContainerType());

        String name = "TestString";
        SimpleType namedStringType = new SimpleType(stringClass, TypeBindings.construct(String.class.getTypeName(), name), null, null);
        assertEquals(String.class, namedStringType.getRawClass());
        assertEquals(TypeBindings.construct(String.class.getTypeName(), name), namedStringType.getTypeBindings());
        assertNull(namedStringType.getSuperClass());
        assertArrayEquals(new Class[0], namedStringType.getSuperInterfaces());
        assertFalse(namedStringType.isContainerType());
    }

    @Test
    public void testSimpleTypeNarrowing() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);

        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        SimpleType stringBuilderType = new SimpleType(stringBuilderClass);

        SimpleType narrowedStringBuilderType = stringType.narrow(StringBuilder.class);
        assertEquals(stringBuilderType, narrowedStringBuilderType);

        try {
            SimpleType narrowedStringType = stringBuilderType.narrow(String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSimpleTypeWithHandlers() {
        Class<String> stringClass = String.class;
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};

        SimpleType stringType = new SimpleType(stringClass, null, null, null, valueHandler, typeHandler, false);

        SimpleType stringTypeWithValueHandler = stringType.withValueHandler(valueHandler);
        assertEquals(stringType, stringTypeWithValueHandler);
        assertNotNull(stringTypeWithValueHandler.getValueHandler());
        assertSame(valueHandler, stringTypeWithValueHandler.getValueHandler());

        SimpleType stringTypeWithTypeHandler = stringType.withTypeHandler(typeHandler);
        assertEquals(stringType, stringTypeWithTypeHandler);
        assertNotNull(stringTypeWithTypeHandler.getTypeHandler());
        assertSame(typeHandler, stringTypeWithTypeHandler.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass, null, null, null, null, null, false);

        SimpleType stringTypeWithStaticTyping = stringType.withStaticTyping();
        assertTrue(stringTypeWithStaticTyping.isStatic());

        SimpleType stringTypeWithoutStaticTyping = stringType.withStaticTyping();
        assertFalse(stringTypeWithoutStaticTyping.isStatic());
        assertEquals(stringType, stringTypeWithoutStaticTyping);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructInvalidType() {
        Class<List<String>> listClass = new ArrayList<String>().getClass();
        new SimpleType(listClass);
    }
}