import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<Integer> integerClass = Integer.class;
        JavaType intType = new SimpleType(integerClass, TypeBindings.emptyBindings(), null, null);
        assertEquals(integerClass, intType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), intType.getTypeBindings());
        assertNull(intType.getSuperClass());
        assertNull(intType.getSuperInterfaces());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<Integer> integerClass = Integer.class;
        JavaType intType = new SimpleType(integerClass, TypeBindings.emptyBindings(), null, null);
        JavaType intTypeCopy = new SimpleType(intType);
        assertEquals(integerClass, intTypeCopy.getRawClass());
        assertEquals(intType.getTypeBindings(), intTypeCopy.getTypeBindings());
        assertEquals(intType.getSuperClass(), intTypeCopy.getSuperClass());
        assertArrayEquals(intType.getSuperInterfaces(), intTypeCopy.getSuperInterfaces());
    }

    @Test
    public void testSimpleTypeUnsafeConstructor() {
        Class<Integer> integerClass = Integer.class;
        JavaType intType = new SimpleType(integerClass, TypeBindings.emptyBindings(), null, null);
        JavaType intTypeUnsafe = SimpleType.constructUnsafe(integerClass);
        assertEquals(integerClass, intTypeUnsafe.getRawClass());
        assertNull(intTypeUnsafe.getTypeBindings());
        assertNull(intTypeUnsafe.getSuperClass());
        assertNull(intTypeUnsafe.getSuperInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeInvalidConstruction() {
        Class<List<Integer>> listClass = List.class.getGenericType();
        JavaType intType = new SimpleType(Integer.class, TypeBindings.emptyBindings(), null, null);
        SimpleType listType = new SimpleType(listClass, TypeBindings.emptyBindings(), intType, null);
    }

    @Test
    public void testSimpleTypeNarrowing() {
        Class<Integer> integerClass = Integer.class;
        JavaType intType = new SimpleType(integerClass, TypeBindings.emptyBindings(), null, null);
        JavaType intSubClassType = new SimpleType(intType.getTypeBindings().findConcreteSubtype(Integer.class));
        assertEquals(integerClass, intType.getRawClass());
        assertEquals(intSubClassType.getRawClass(), intSubClassType.getRawClass());
        assertTrue(intSubClassType instanceof SimpleType);
    }
}