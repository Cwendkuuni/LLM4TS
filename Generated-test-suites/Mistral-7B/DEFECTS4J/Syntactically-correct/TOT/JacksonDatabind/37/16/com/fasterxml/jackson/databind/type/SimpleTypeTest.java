import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getTypeBindings());
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(new Class[0], simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType originalSimpleType = new SimpleType(stringClass);
        SimpleType copiedSimpleType = new SimpleType(originalSimpleType);
        assertEquals(originalSimpleType, copiedSimpleType);
        assertEquals(String.class, copiedSimpleType.getRawClass());
        assertEquals(originalSimpleType.getTypeBindings(), copiedSimpleType.getTypeBindings());
        assertSame(originalSimpleType.getSuperClass(), copiedSimpleType.getSuperClass());
        assertArrayEquals(originalSimpleType.getSuperInterfaces(), copiedSimpleType.getSuperInterfaces());
        assertFalse(copiedSimpleType.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructorCall() {
        Class<List<String>> listClass = List.class;
        new SimpleType(listClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStaticConstructorCall() {
        SimpleType simpleType = new SimpleType(String[].class);
    }

    @Test
    public void testUnsafeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getTypeBindings());
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(new Class[0], simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeprecatedConstructMethod() {
        Class<List<String>> listClass = List.class;
        SimpleType simpleType = SimpleType.construct(listClass);
    }

    @Test
    public void testNarrowingType() {
        Class<String> stringClass = String.class;
        Class<StringBuffer> stringBufferClass = StringBuffer.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        SimpleType stringBufferSimpleType = stringSimpleType._narrow(StringBuffer.class);
        assertEquals(StringBuffer.class, stringBufferSimpleType.getRawClass());
        assertEquals(stringSimpleType.getTypeBindings(), stringBufferSimpleType.getTypeBindings());
        assertNull(stringBufferSimpleType.getSuperClass());
        assertArrayEquals(new Class[0], stringBufferSimpleType.getSuperInterfaces());
        assertFalse(stringBufferSimpleType.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNarrowing() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        stringSimpleType._narrow(Integer.class);
    }
}