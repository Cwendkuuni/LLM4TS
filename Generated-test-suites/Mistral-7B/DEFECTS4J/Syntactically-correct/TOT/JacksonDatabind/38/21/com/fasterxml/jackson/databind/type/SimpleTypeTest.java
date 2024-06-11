import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructor() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        assertEquals(String.class, stringType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), stringType.getBindings());
        assertEquals(null, stringType.getSuperClass());
        assertEquals(null, stringType.getSuperInterfaces());
        assertEquals(false, stringType.isStatic());

        JavaType stringTypeWithValueHandler = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, new StdValueHandler(), false);
        assertEquals(String.class, stringTypeWithValueHandler.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), stringTypeWithValueHandler.getBindings());
        assertEquals(null, stringTypeWithValueHandler.getSuperClass());
        assertEquals(null, stringTypeWithValueHandler.getSuperInterfaces());
        assertNotNull(stringTypeWithValueHandler.getValueHandler());
        assertEquals(false, stringTypeWithValueHandler.isStatic());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType originalStringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType copiedStringType = new SimpleType(originalStringType);

        assertEquals(String.class, copiedStringType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), copiedStringType.getBindings());
        assertEquals(null, copiedStringType.getSuperClass());
        assertEquals(null, copiedStringType.getSuperInterfaces());
        assertEquals(false, copiedStringType.isStatic());

        // Check that the original and copied instances are not the same
        assertNotSame(originalStringType, copiedStringType);
    }

    @Test
    public void testSimpleTypeConstructUnsafe() {
        Class<String> stringClass = String.class;
        SimpleType stringType = SimpleType.constructUnsafe(stringClass);
        assertEquals(String.class, stringType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), stringType.getBindings());
        assertEquals(null, stringType.getSuperClass());
        assertEquals(null, stringType.getSuperInterfaces());
        assertEquals(false, stringType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructDeprecated() {
        Class<String> stringClass = String.class;
        SimpleType.construct(stringClass);
    }

    @Test
    public void testSimpleTypeNarrow() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        SimpleType narrowedStringType = stringType._narrow(String[].class);
        assertEquals(String[].class, narrowedStringType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), narrowedStringType.getBindings());
        assertEquals(null, narrowedStringType.getSuperClass());
        assertEquals(null, narrowedStringType.getSuperInterfaces());
        assertEquals(false, narrowedStringType.isStatic());
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType stringTypeWithTypeHandler = new SimpleType(stringType, null, null, null, null, new StdTypeHandler(), false);

        assertEquals(String.class, stringTypeWithTypeHandler.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), stringTypeWithTypeHandler.getBindings());
        assertEquals(null, stringTypeWithTypeHandler.getSuperClass());
        assertEquals(null, stringTypeWithTypeHandler.getSuperInterfaces());
        assertNotNull(stringTypeWithTypeHandler.getTypeHandler());
        assertEquals(false, stringTypeWithTypeHandler.isStatic());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType stringTypeWithValueHandler = new SimpleType(stringType, null, null, null, new StdValueHandler(), null, false);

        assertEquals(String.class, stringTypeWithValueHandler.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), stringTypeWithValueHandler.getBindings());
        assertEquals(null, stringTypeWithValueHandler.getSuperClass());
        assertEquals(null, stringTypeWithValueHandler.getSuperInterfaces());
        assertNotNull(stringTypeWithValueHandler.getValueHandler());
        assertEquals(false, stringTypeWithValueHandler.isStatic());
    }

    @Test
    public void testSimpleTypeWithContentTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType stringTypeWithContentTypeHandler = new SimpleType(stringType, null, null, null, null, new StdContentTypeHandler(), false);

        assertEquals(String.class, stringTypeWithContentTypeHandler.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), stringTypeWithContentTypeHandler.getBindings());
        assertEquals(null, stringTypeWithContentTypeHandler.getSuperClass());
        assertEquals(null, stringTypeWithContentTypeHandler.getSuperInterfaces());
        assertNotNull(stringTypeWithContentTypeHandler.getContentTypeHandler());
        assertEquals(false, stringTypeWithContentTypeHandler.isStatic());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType stringTypeWithStaticTyping = stringType.withStaticTyping();

        assertEquals(String.class, stringTypeWithStaticTyping.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), stringTypeWithStaticTyping.getBindings());
        assertEquals(null, stringTypeWithStaticTyping.getSuperClass());
        assertEquals(null, stringTypeWithStaticTyping.getSuperInterfaces());
        assertEquals(true, stringTypeWithStaticTyping.isStatic());
    }
}