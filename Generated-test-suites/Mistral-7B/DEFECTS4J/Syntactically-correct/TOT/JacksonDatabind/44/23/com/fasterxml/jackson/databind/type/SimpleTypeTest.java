import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        assertEquals(Integer.class, intType.getRawClass());
        assertNull(intType.getSuperClass());
        assertNull(intType.getSuperInterfaces());
        assertFalse(intType.isContainerType());

        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = new JavaType[0];
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        SimpleType intTypeWithBindings = new SimpleType(intClass, bindings, superClass, superInterfaces, valueHandler, typeHandler, asStatic);
        assertEquals(Integer.class, intTypeWithBindings.getRawClass());
        assertEquals(superClass, intTypeWithBindings.getSuperClass());
        assertArrayEquals(superInterfaces, intTypeWithBindings.getSuperInterfaces());
        assertFalse(intTypeWithBindings.isContainerType());
    }

    @Test
    public void testSimpleTypeClassCast() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        SimpleType intType2 = new SimpleType(intType.getRawClass());
        assertEquals(intType, intType2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeInvalidConstruction() {
        Class<Map<Integer, String>> mapClass = Map.class;
        new SimpleType(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeDeprecatedConstruction() {
        Class<List<Integer>> listClass = ArrayList.class;
        SimpleType listType = SimpleType.construct(listClass);
    }

    @Test
    public void testSimpleTypeRefine() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        SimpleType intTypeRefined = intType.refine(Integer[].class, TypeBindings.emptyBindings(), null, null);
        assertNull(intTypeRefined);
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        Object stringHandler = new StringHandler();
        SimpleType stringTypeWithHandler = stringType.withTypeHandler(stringHandler);
        assertEquals(stringType, stringTypeWithHandler);
        assertEquals(stringHandler, stringTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        Object intHandler = new IntHandler();
        SimpleType intTypeWithHandler = intType.withValueHandler(intHandler);
        assertEquals(intType, intTypeWithHandler);
        assertEquals(intHandler, intTypeWithHandler.getValueHandler());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType intTypeWithStaticTyping = intType.withStaticTyping();
        assertTrue(intTypeWithStaticTyping.isStatic());
        assertFalse(intType.isStatic());
    }

    @Test
    public void testSimpleTypeEquals() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType1 = new SimpleType(intClass);
        SimpleType intType2 = new SimpleType(intClass);
        SimpleType intType3 = new SimpleType(String.class);
        assertNotNull(intType1);
        assertNotNull(intType2);
        assertNotSame(intType1, intType2);
        assertNotSame(intType1, intType3);
        assertNotSame(intType2, intType3);
        assertTrue(intType1.equals(intType1));
        assertTrue(intType1.equals(intType2));
        assertTrue(intType2.equals(intType1));
        assertFalse(intType1.equals(intType3));
        assertFalse(intType2.equals(intType3));
    }
}