import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals("", bindings.toString());
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testCreateWithOneTypeArgument() {
        Class<List<String>> erasedType = java.util.List.class;
        JavaType typeArg = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg);
        assertEquals("<String>", bindings.toString());
        assertEquals(1, bindings.size());
        assertEquals("String", bindings.getBoundName(0));
        assertEquals(typeArg, bindings.getBoundType(0));
        assertNull(bindings.findBoundType("Unknown"));
    }

    @Test
    public void testCreateWithTwoTypeArguments() {
        Class<Map<String, List<Integer>>> erasedType = java.util.Map.class;
        JavaType keyTypeArg = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueTypeArg = TypeFactory.defaultInstance().constructType(List.class);
        JavaType valueTypeArgElementType = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(erasedType, keyTypeArg, valueTypeArg);
        assertEquals("<String, List<Integer>>", bindings.toString());
        assertEquals(2, bindings.size());
        assertEquals("String", bindings.getBoundName(0));
        assertEquals(keyTypeArg, bindings.getBoundType(0));
        assertEquals("List<Integer>", bindings.getBoundName(1));
        assertEquals(valueTypeArg, bindings.getBoundType(1));
        assertEquals("Integer", bindings.getBoundType(1).getRawType().getName());
        assertEquals(valueTypeArgElementType, bindings.getBoundType(1).getContentType());
        assertNull(bindings.findBoundType("Unknown"));
    }

    @Test
    public void testCreateIfNeededWithOneTypeArgument() {
        Class<ArrayList<String>> erasedType = java.util.ArrayList.class;
        JavaType typeArg = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, typeArg);
        assertEquals("<String>", bindings.toString());
        assertEquals(1, bindings.size());
        assertEquals("String", bindings.getBoundName(0));
        assertEquals(typeArg, bindings.getBoundType(0));
        assertNull(bindings.findBoundType("Unknown"));
    }

    @Test
    public void testCreateIfNeededWithTwoTypeArguments() {
        Class<HashMap<String, ArrayList<Integer>>> erasedType = java.util.HashMap.class;
        JavaType keyTypeArg = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueTypeArg = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType valueTypeArgElementType = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, keyTypeArg, valueTypeArg);
        assertEquals("<String, ArrayList<Integer>>", bindings.toString());
        assertEquals(2, bindings.size());
        assertEquals("String", bindings.getBoundName(0));
        assertEquals(keyTypeArg, bindings.getBoundType(0));
        assertEquals("ArrayList<Integer>", bindings.getBoundName(1));
        assertEquals(valueTypeArg, bindings.getBoundType(1));
        assertEquals("Integer", bindings.getBoundType(1).getRawType().getName());
        assertEquals(valueTypeArgElementType, bindings.getBoundType(1).getContentType());
        assertNull(bindings.findBoundType("Unknown"));
    }

    @Test
    public void testCreateWithUnboundVariable() {
        Class<LinkedHashMap<String, ArrayList<List<Integer>>>> erasedType = java.util.LinkedHashMap.class;
        JavaType keyTypeArg = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueTypeArg = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType valueTypeArgElementType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType valueTypeArgListElementType = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(erasedType, keyTypeArg, valueTypeArg);
        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("X");
        assertEquals("<String, ArrayList<List<X>>>", bindingsWithUnboundVariable.toString());
        assertEquals(3, bindingsWithUnboundVariable.size());
        assertEquals("String", bindingsWithUnboundVariable.getBoundName(0));
        assertEquals(keyTypeArg, bindingsWithUnboundVariable.getBoundType(0));
        assertEquals("ArrayList<List<X>>", bindingsWithUnboundVariable.getBoundName(1));
        assertEquals(valueTypeArg, bindingsWithUnboundVariable.getBoundType(1));
        assertEquals("List<X>", bindingsWithUnboundVariable.getBoundType(1).getContentType().getGenericType());
        assertEquals("X", bindingsWithUnboundVariable.getBoundType(1).getContentType().getRawTypes()[1].getName());
        assertEquals("X", bindingsWithUnboundVariable.findBoundType("X").getRawType().getName());
        assertNull(bindingsWithUnboundVariable.findBoundType("Unknown"));
    }
}