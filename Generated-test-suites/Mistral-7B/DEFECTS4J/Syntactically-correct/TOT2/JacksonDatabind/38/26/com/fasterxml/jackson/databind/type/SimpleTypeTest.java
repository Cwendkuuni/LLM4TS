import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertEquals(null, simpleType.getSuperClass());
        assertEquals(new JavaType[0], simpleType.getSuperTypes());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        SimpleType copy = new SimpleType(simpleType);
        assertEquals(String.class, copy.getRawClass());
        assertEquals(simpleType.getBindings(), copy.getBindings());
        assertEquals(simpleType.getSuperClass(), copy.getSuperClass());
        assertArrayEquals(simpleType.getSuperTypes(), copy.getSuperTypes());
    }

    @Test
    public void testSimpleTypeNarrow() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = Object.class;
        SimpleType simpleType1 = new SimpleType(cls1);
        SimpleType simpleType2 = simpleType1._narrow(cls2);
        assertNotNull(simpleType2);
        assertEquals(cls2, simpleType2.getRawClass());
        assertEquals(simpleType1.getBindings(), simpleType2.getBindings());
        assertArrayEquals(simpleType1.getSuperTypes(), simpleType2.getSuperTypes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeNarrowException() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType._narrow(String[].class);
    }

    @Test
    public void testSimpleTypeWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType simpleTypeWithHandler = simpleType.withContentTypeHandler(handler);
        assertNotNull(simpleTypeWithHandler);
        assertNotSame(simpleType, simpleTypeWithHandler);
        assertEquals(handler, simpleTypeWithHandler.getContentHandler());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType simpleTypeWithHandler = simpleType.withValueHandler(handler);
        assertNotNull(simpleTypeWithHandler);
        assertNotSame(simpleType, simpleTypeWithHandler);
        assertEquals(handler, simpleTypeWithHandler.getValueHandler());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType simpleTypeStatic = simpleType.withStaticTyping();
        assertNotNull(simpleTypeStatic);
        assertTrue(simpleTypeStatic.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructException() {
        SimpleType simpleType = SimpleType.construct(String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructUnsafeException() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
    }
}