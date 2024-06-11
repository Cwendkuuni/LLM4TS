import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import org.junit.Test;
import org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
        assertEquals("<>", empty.toString());
    }

    @Test
    public void testCreateWithNoTypeArgs() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = TypeBindings.create(erasedType, null);
        assertTrue(bindings.isEmpty());
        assertEquals("<>", bindings.toString());
    }

    @Test
    public void testCreateWithOneTypeArg() {
        Class<List<String>> erasedType = ArrayList.class;
        List<JavaType> typeArgs = Arrays.asList(JavaType.getTypeFactory().constructParametricType(List.class, String.class));
        TypeBindings bindings = TypeBindings.create(erasedType, typeArgs);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(JavaType.constructType(String.class), bindings.getBoundType(0));
        assertEquals("[Ljava.lang.String;", bindings.toString());
    }

    @Test
    public void testCreateWithTwoTypeArgs() {
        Class<Map<String, List<Integer>>> erasedType = HashMap.class;
        List<JavaType> typeArgs = Arrays.asList(JavaType.getTypeFactory().constructParametricType(Map.class, String.class, JavaType.constructType(List.class, Integer.class)));
        TypeBindings bindings = TypeBindings.create(erasedType, typeArgs);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(JavaType.constructType(String.class), bindings.getBoundType(0));
        assertEquals("java.util.List", bindings.getBoundName(1));
        assertEquals(JavaType.constructType(List.class), bindings.getBoundType(1));
        assertEquals("{Ljava.lang.String;Ljava.util.List;}", bindings.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithIncorrectNumberOfTypeArgs() {
        Class<Map<String, List<Integer>>> erasedType = HashMap.class;
        List<JavaType> typeArgs = Arrays.asList(JavaType.getTypeFactory().constructParametricType(Map.class, String.class, JavaType.constructType(List.class, Integer.class)),
                JavaType.constructType(String.class));
        TypeBindings bindings = TypeBindings.create(erasedType, typeArgs);
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testCreateIfNeededWithNoTypeArgs() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, null);
        assertTrue(bindings.isEmpty());
        assertEquals("<>", bindings.toString());
    }

    @Test
    public void testCreateIfNeededWithOneTypeArg() {
        Class<List<String>> erasedType = ArrayList.class;
        JavaType typeArg = JavaType.getTypeFactory().constructParametricType(List.class, String.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, typeArg);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(typeArg, bindings.getBoundType(0));
        assertEquals("[Ljava.lang.String;", bindings.toString());
    }

    @Test
    public void testCreateIfNeededWithTwoTypeArgs() {
        Class<Map<String, List<Integer>>> erasedType = HashMap.class;
        JavaType keyType = JavaType.getTypeFactory().constructParametricType(Map.class, String.class);
        JavaType valueType = JavaType.constructType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, keyType, valueType);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(keyType, bindings.getBoundType(0));
        assertEquals("java.util.List", bindings.getBoundName(1));
        assertEquals(valueType, bindings.getBoundType(1));
        assertEquals("{Ljava.lang.String;Ljava.util.List;}", bindings.toString());
    }

    @Test
    public void testFindBoundType() {
        Class<Map<String, List<Integer>>> erasedType = HashMap.class;
        JavaType keyType = JavaType.getTypeFactory().constructParametricType(Map.class, String.class);
        JavaType valueType = JavaType.constructType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(erasedType, keyType, valueType);
        assertEquals(keyType, bindings.findBoundType("java.lang.String"));
        assertEquals(valueType, bindings.findBoundType("java.util.List"));
        assertNull(bindings.findBoundType("java.lang.Integer"));
    }
}