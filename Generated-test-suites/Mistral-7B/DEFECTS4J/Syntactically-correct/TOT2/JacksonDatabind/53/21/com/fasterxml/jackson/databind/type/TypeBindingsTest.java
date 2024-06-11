import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals("", bindings.toString());
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testCreateWithNoTypeArgs() {
        TypeBindings bindings = TypeBindings.create(ArrayList.class, null);
        assertEquals("<>", bindings.toString());
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testCreateWithOneTypeArg() {
        TypeBindings bindings = TypeBindings.create(ArrayList.class, JavaType.TYPE_INT_ARRAY);
        assertEquals("<Integer[]>", bindings.toString());
        assertEquals(1, bindings.size());
        assertEquals(JavaType.TYPE_INT_ARRAY, bindings.findBoundType(""));
    }

    @Test
    public void testCreateWithTwoTypeArgs() {
        TypeBindings bindings = TypeBindings.create(LinkedHashMap.class, JavaType.TYPE_INT_ARRAY, JavaType.TYPE_STRING);
        assertEquals("<Integer[], String>", bindings.toString());
        assertEquals(2, bindings.size());
        assertEquals(JavaType.TYPE_INT_ARRAY, bindings.findBoundType(""));
        assertEquals(JavaType.TYPE_STRING, bindings.findBoundType(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedTypeArgs() {
        TypeBindings bindings = TypeBindings.create(LinkedHashMap.class, JavaType.TYPE_INT_ARRAY, JavaType.TYPE_STRING_ARRAY);
        assertNull(bindings);
    }

    @Test
    public void testCreateWithUnboundTypeVariable() {
        TypeBindings bindings = TypeBindings.createIfNeeded(LinkedHashMap.class, JavaType.TYPE_INT_ARRAY);
        assertEquals("<Integer[], E>", bindings.toString());
        assertEquals(1, bindings.size());
        assertEquals(JavaType.TYPE_INT_ARRAY, bindings.findBoundType(""));
        assertNull(bindings.findBoundType("E"));
    }

    @Test
    public void testCreateWithBoundTypeVariable() {
        TypeBindings bindings = TypeBindings.createIfNeeded(LinkedHashMap.class, JavaType.constructParametricType(LinkedHashMap.class, JavaType.TYPE_INT, JavaType.constructParametricType(List.class, JavaType.TYPE_STRING)));
        assertEquals("<Integer, List<String>>", bindings.toString());
        assertEquals(2, bindings.size());
        assertEquals(JavaType.TYPE_INT, bindings.findBoundType(""));
        assertEquals(JavaType.constructParametricType(List.class, JavaType.TYPE_STRING), bindings.findBoundType(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNegativeIndex() {
        Class<?> clazz = AbstractList.class;
        TypeBindings bindings = TypeBindings.create(clazz, null);
        assertNull(bindings.findBoundType(""));
        assertThrows(IllegalArgumentException.class, () -> bindings.getBoundType(-1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidName() {
        Class<?> clazz = AbstractList.class;
        TypeBindings bindings = TypeBindings.create(clazz, null);
        assertNull(bindings.findBoundType(""));
        assertThrows(IllegalArgumentException.class, () -> bindings.getBoundType("InvalidName"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullNameArray() {
        Class<?> clazz = AbstractList.class;
        assertThrows(IllegalArgumentException.class, () -> TypeBindings.create(clazz, null, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullTypeArray() {
        Class<?> clazz = AbstractList.class;
        String[] names = new String[1];
        assertThrows(IllegalArgumentException.class, () -> TypeBindings.create(clazz, null, names));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedNameArrayAndTypeArray() {
        Class<?> clazz = AbstractList.class;
        JavaType[] types = new JavaType[1];
        String[] names = new String[2];
        assertThrows(IllegalArgumentException.class, () -> TypeBindings.create(clazz, types, names));
    }

    @Test
    public void testCreateWithUnboundVariable() {
        TypeBindings bindings = TypeBindings.createIfNeeded(LinkedHashMap.class, JavaType.TYPE_INT_ARRAY).withUnboundVariable("E");
        assertEquals("<Integer[], E>", bindings.toString());
        assertEquals(1, bindings.size());
        assertEquals(JavaType.TYPE_INT_ARRAY, bindings.findBoundType(""));
        assertNull(bindings.findBoundType("E"));
    }

    @Test
    public void testToString() {
        TypeBindings bindings = TypeBindings.create(ArrayList.class, JavaType.TYPE_INT_ARRAY);
        assertEquals("<Integer[]>", bindings.toString());
        bindings = TypeBindings.createIfNeeded(LinkedHashMap.class, JavaType.TYPE_INT_ARRAY).withUnboundVariable("E");
        assertEquals("<Integer[], E>", bindings.toString());
    }
}