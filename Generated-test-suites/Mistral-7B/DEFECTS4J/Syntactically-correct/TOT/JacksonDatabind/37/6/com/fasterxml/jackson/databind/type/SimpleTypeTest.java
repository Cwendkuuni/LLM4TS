import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test constructing a SimpleType using the constructor with a Class
        Class<Integer> intClass = Integer.class;
        SimpleType intSimpleType = new SimpleType(intClass);
        assertEquals(intClass, intSimpleType.getRawClass());
        assertNull(intSimpleType.getSuperClass());
        assertArrayEquals(new JavaType[0], intSimpleType.getSuperTypes());
        assertFalse(intSimpleType.isContainerType());

        // Test constructing a SimpleType using the copy constructor
        SimpleType intSimpleTypeCopy = new SimpleType(intSimpleType);
        assertEquals(intSimpleType, intSimpleTypeCopy);
    }

    @Test
    public void testNarrowing() {
        // Test narrowing a SimpleType to a subclass
        SimpleType intSimpleType = new SimpleType(Integer.class);
        SimpleType intWrapperType = intSimpleType.narrow(Integer.TYPE);
        assertEquals(intWrapperType.getRawClass(), Integer.TYPE);
        assertNotNull(intWrapperType.getSuperClass());
        assertEquals(intWrapperType.getSuperClass().getRawClass(), Integer.TYPE);
        assertArrayEquals(new JavaType[0], intWrapperType.getSuperTypes());
        assertTrue(intWrapperType.isContainerType());

        // Test narrowing a SimpleType to a superclass (should return the original type)
        SimpleType intWrapperType2 = intWrapperType.narrow(Integer.class);
        assertEquals(intSimpleType, intWrapperType2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTypeConstruction() {
        // Test constructing a SimpleType with an invalid class
        Class<List<String>> listClass = new ArrayList<String>().getClass();
        new SimpleType(listClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeMethod() {
        // Test calling the deprecated constructUnsafe method
        new SimpleType.constructUnsafe(String.class);
    }
}