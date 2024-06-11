import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertEquals(0, empty.size());
        assertEquals("<>", empty.toString());
    }

    @Test
    public void testCreateWithNoTypeArguments() {
        Class<List> listClass = List.class;
        TypeBindings bindings = TypeBindings.create(listClass, null);
        assertEquals(0, bindings.size());
        assertEquals("<>", bindings.toString());
    }

    @Test
    public void testCreateWithSingleTypeArgument() {
        Class<List<Integer>> listClass = List.class;
        TypeBindings bindings = TypeBindings.create(listClass, JavaType.constructCollectionType(List.class, Integer.class));
        assertEquals(1, bindings.size());
        assertEquals("java.util.List<Integer>", bindings.toString());
        assertEquals("Integer", bindings.getBoundName(0));
        assertEquals(JavaType.constructCollectionType(List.class, Integer.class), bindings.getBoundType(0));
    }

    @Test
    public void testCreateWithTwoTypeArguments() {
        Class<Map<String, List<Integer>>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.create(mapClass, JavaType.constructTypeVariableType(String.class), JavaType.constructTypeVariableType(List.class, Integer.class));
        assertEquals(2, bindings.size());
        assertEquals("java.util.Map<String, java.util.List<Integer>>", bindings.toString());
        assertEquals("String", bindings.getBoundName(0));
        assertEquals(JavaType.constructTypeVariableType(String.class), bindings.getBoundType(0));
        assertEquals("java.util.List<Integer>", bindings.getBoundName(1));
        assertEquals(JavaType.constructTypeVariableType(List.class, Integer.class), bindings.getBoundType(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedNumberOfTypeArguments() {
        Class<Map<String, List<Integer>>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.create(mapClass, JavaType.constructTypeVariableType(String.class), JavaType.constructCollectionType(List.class), JavaType.constructTypeVariableType(Integer.class));
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testCreateWithUnmatchedTypes() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.create(mapClass, JavaType.constructTypeVariableType(String.class), JavaType.constructTypeVariableType(Number.class));
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testCreateIfNeededWithNoTypeArguments() {
        TypeBindings bindings = TypeBindings.createIfNeeded(List.class, null);
        assertEquals(0, bindings.size());
        assertEquals("<>", bindings.toString());
    }

    @Test
    public void testCreateIfNeededWithSingleTypeArgument() {
        TypeBindings bindings = TypeBindings.createIfNeeded(List.class, JavaType.constructCollectionType(List.class, Integer.class));
        assertEquals(1, bindings.size());
        assertEquals("java.util.List<Integer>", bindings.toString());
    }

    @Test
    public void testCreateIfNeededWithTwoTypeArguments() {
        TypeBindings bindings = TypeBindings.createIfNeeded(Map.class, JavaType.constructTypeVariableType(String.class), JavaType.constructTypeVariableType(List.class, Integer.class));
        assertEquals(2, bindings.size());
        assertEquals("java.util.Map<String, java.util.List<Integer>>", bindings.toString());
    }

    @Test
    public void testFindBoundType() {
        TypeBindings bindings = TypeBindings.create(List.class, JavaType.constructCollectionType(List.class, Integer.class));
        assertEquals(JavaType.constructCollectionType(List.class, Integer.class), bindings.findBoundType("Integer"));
        assertEquals(null, bindings.findBoundType("Unknown"));
    }

    @Test
    public void testIsEmpty() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
        TypeBindings bindings = TypeBindings.create(List.class, JavaType.constructCollectionType(List.class, Integer.class));
        assertFalse(bindings.isEmpty());
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings bindings = TypeBindings.create(List.class, JavaType.constructCollectionType(List.class, Integer.class));
        TypeBindings bindingsWithVariable = bindings.withUnboundVariable("V");
        assertEquals("java.util.List<Integer>", bindings.toString());
        assertEquals("java.util.List<Integer>", bindingsWithVariable.toString());
        assertTrue(bindings.hasUnbound("Integer"));
        assertFalse(bindingsWithVariable.hasUnbound("Integer"));
        assertTrue(bindingsWithVariable.hasUnbound("V"));
    }
}