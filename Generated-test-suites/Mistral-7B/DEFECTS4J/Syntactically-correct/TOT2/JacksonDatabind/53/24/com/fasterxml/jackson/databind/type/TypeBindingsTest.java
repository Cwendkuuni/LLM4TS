import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertNotNull(bindings);
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType("Unknown"));
    }

    @Test
    public void testOneArgumentTypeBinding() {
        Class<List<Integer>> argType = new ArrayList<Integer>().getClass();
        JavaType expectedType = JavaType.constructParametricType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(argType, expectedType);
        assertEquals(1, bindings.size());
        assertEquals("java.util.List", bindings.getBoundName(0));
        assertEquals(expectedType, bindings.getBoundType(0));
        assertEquals("java.util.List", bindings.toString());
    }

    @Test
    public void testTwoArgumentsTypeBinding() {
        Class<Map<String, Integer>> argType = new HashMap<String, Integer>().getClass();
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        JavaType expectedType = JavaType.constructParametricType(Map.class, keyType, valueType);
        TypeBindings bindings = TypeBindings.create(argType, keyType, valueType);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(keyType, bindings.getBoundType(0));
        assertEquals("java.lang.Integer", bindings.getBoundName(1));
        assertEquals(valueType, bindings.getBoundType(1));
        assertEquals("java.util.Map", bindings.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumberOfArgumentsTypeBinding() {
        Class<List<Integer>> argType = new ArrayList<Integer>().getClass();
        JavaType expectedType = JavaType.constructParametricType(List.class, Integer.class);
        TypeBindings.create(argType, expectedType, "Third argument");
    }

    @Test
    public void testUnboundVariableTypeBinding() {
        Class<List<String>> argType = new ArrayList<String>().getClass();
        JavaType expectedType = JavaType.constructParametricType(List.class, String.class);
        TypeBindings bindings = TypeBindings.create(argType);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertNull(bindings.getBoundType(0));
        assertEquals("java.util.List<T>", bindings.toString());

        TypeBindings unboundBindings = bindings.withUnboundVariable("T");
        assertEquals("java.util.List<T>", unboundBindings.toString());
        JavaType unboundType = unboundBindings.findBoundType("T");
        assertNotNull(unboundType);
        assertEquals(expectedType, unboundType);
    }
}