import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeConstructionWithBindings() {
        Class<List<Integer>> listClass = List.class.getGenericType();
        JavaType javaType = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.builder().arrayBindings(new JavaType[]{javaType}).build();
        SimpleType simpleType = new SimpleType(listClass, bindings, null, null);
        assertEquals(List.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(new JavaType[]{javaType}, simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType originalSimpleType = new SimpleType(stringClass);
        SimpleType copiedSimpleType = new SimpleType(originalSimpleType);
        assertEquals(originalSimpleType.getRawClass(), copiedSimpleType.getRawClass());
        assertEquals(originalSimpleType.getBindings(), copiedSimpleType.getBindings());
        assertEquals(originalSimpleType.getSuperClass(), copiedSimpleType.getSuperClass());
        assertArrayEquals(originalSimpleType.getSuperInterfaces(), copiedSimpleType.getSuperInterfaces());
        assertEquals(originalSimpleType.isStatic(), copiedSimpleType.isStatic());
    }

    @Test
    public void testSimpleTypeNarrowing() {
        Class<List<Integer>> listClass = List.class.getGenericType();
        Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
        SimpleType simpleType = new SimpleType(listClass, TypeBindings.emptyBindings(), null, null);
        JavaType narrowedType = simpleType.narrow(arrayListClass);
        assertEquals(arrayListClass, narrowedType.getRawClass());
        assertEquals(simpleType.getBindings(), narrowedType.getBindings());
        assertNull(narrowedType.getSuperClass());
        assertArrayEquals(new JavaType[0], narrowedType.getSuperInterfaces());
        assertFalse(narrowedType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeInvalidConstruction() {
        Class<Map<String, List<Integer>>> mapClass = Map.class.getGenericType();
        new SimpleType(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructUnsafe() {
        Class<Map<String, List<Integer>>> mapClass = Map.class.getGenericType();
        new SimpleType.constructUnsafe(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructDeprecated() {
        Class<Map<String, List<Integer>>> mapClass = Map.class.getGenericType();
        SimpleType.construct(mapClass);
    }
}