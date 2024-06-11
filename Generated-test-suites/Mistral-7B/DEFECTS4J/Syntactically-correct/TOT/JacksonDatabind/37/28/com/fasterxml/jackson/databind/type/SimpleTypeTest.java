import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test default constructor with null arguments
        SimpleType simpleType1 = new SimpleType(null);
        assertNull(simpleType1.getClass());
        assertNull(simpleType1.getBindings());
        assertNull(simpleType1.getSuperClass());
        assertNull(simpleType1.getSuperInterfaces());
        assertFalse(simpleType1.isStatic());

        // Test constructor with valid arguments
        Class<Integer> intClass = Integer.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType simpleType2 = new SimpleType(intClass, bindings, superClass, superInterfaces);
        assertEquals(intClass, simpleType2.getClass());
        assertSame(bindings, simpleType2.getBindings());
        assertSame(superClass, simpleType2.getSuperClass());
        assertArrayEquals(superInterfaces, simpleType2.getSuperInterfaces());
        assertFalse(simpleType2.isStatic());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<Integer> intClass = Integer.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType simpleType1 = new SimpleType(intClass, bindings, superClass, superInterfaces);

        SimpleType simpleType2 = new SimpleType(simpleType1);
        assertEquals(intClass, simpleType2.getClass());
        assertSame(bindings, simpleType2.getBindings());
        assertSame(superClass, simpleType2.getSuperClass());
        assertArrayEquals(superInterfaces, simpleType2.getSuperInterfaces());
        assertFalse(simpleType2.isStatic());
    }

    @Test
    public void testInvalidTypeConstruction() {
        try {
            SimpleType simpleType = new SimpleType(Map.class);
            fail("Expected IllegalArgumentException when constructing SimpleType with a Map class");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            SimpleType simpleType = new SimpleType(Collection.class);
            fail("Expected IllegalArgumentException when constructing SimpleType with a Collection class");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            SimpleType simpleType = new SimpleType(int[].class);
            fail("Expected IllegalArgumentException when constructing SimpleType with an array class");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNarrowingType() {
        Class<Integer> intClass = Integer.class;
        Class<Integer[]> intArrayClass = int[].class;
        SimpleType simpleTypeInt = new SimpleType(intClass);
        SimpleType simpleTypeIntArray = new SimpleType(intArrayClass);

        SimpleType narrowedTypeInt = simpleTypeInt._narrow(intClass);
        SimpleType narrowedTypeIntArray = simpleTypeIntArray._narrow(intClass);

        assertEquals(simpleTypeInt, narrowedTypeInt);
        assertNotSame(simpleTypeInt, narrowedTypeIntArray);
        assertEquals(intClass, narrowedTypeInt.getClass());
        assertEquals(intClass, narrowedTypeIntArray.getClass());
    }

    @Test
    public void testInvalidNarrowingType() {
        try {
            SimpleType simpleType = new SimpleType(Object.class);
            SimpleType narrowedType = simpleType._narrow(String.class);
            fail("Expected IllegalArgumentException when narrowing a SimpleType to an unrelated type");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}