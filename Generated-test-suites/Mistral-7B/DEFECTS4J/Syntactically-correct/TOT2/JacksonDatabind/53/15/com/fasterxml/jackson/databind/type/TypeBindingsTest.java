import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeVariableIdentifier;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertNotNull(bindings);
        assertEquals("", bindings.toString());
        assertNull(bindings.findBoundType("T"));
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateBindingsWithNoTypeArguments() {
        Class<?> erasedType = List.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, null);
        assertNotNull(bindings);
        assertEquals("<>", bindings.toString());
        assertNull(bindings.findBoundType("T"));
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateBindingsWithOneTypeArgument() {
        Class<?> erasedType = List.class;
        JavaType typeArg = JavaType.constructParametricType(ArrayList.class, String.class);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg);
        assertNotNull(bindings);
        assertEquals("<T>", bindings.toString());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(typeArg, bindings.getBoundType(0));
        assertEquals(1, bindings.size());
    }

    @Test
    public void testCreateBindingsWithTwoTypeArguments() {
        Class<?> erasedType = Map.class;
        JavaType keyTypeArg = JavaType.constructParametricType(String.class, Object.class);
        JavaType valueTypeArg = JavaType.constructParametricType(Integer.class, Object.class);
        TypeBindings bindings = TypeBindings.create(erasedType, keyTypeArg, valueTypeArg);
        assertNotNull(bindings);
        assertEquals("<K, V>", bindings.toString());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(keyTypeArg, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(valueTypeArg, bindings.getBoundType(1));
        assertEquals(2, bindings.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsWithMismatchedNumberOfTypeArguments() {
        Class<?> erasedType = Map.class;
        JavaType[] types = new JavaType[] { JavaType.constructParametricType(String.class, Object.class), JavaType.constructParametricType(Integer.class) };
        TypeBindings bindings = TypeBindings.create(erasedType, types);
    }

    @Test
    public void testCreateBindingsWithExtraTypeArgument() {
        Class<?> erasedType = Map.class;
        JavaType keyTypeArg = JavaType.constructParametricType(String.class, Object.class);
        JavaType valueTypeArg = JavaType.constructParametricType(Integer.class, Object.class);
        JavaType extraTypeArg = JavaType.constructParametricType(Boolean.class, Object.class);
        TypeBindings bindings = TypeBindings.create(erasedType, keyTypeArg, valueTypeArg, extraTypeArg);
        assertEquals(3, bindings.size());
    }

    @Test
    public void testFindBoundType() {
        Class<?> erasedType = Map.class;
        JavaType keyTypeArg = JavaType.constructParametricType(String.class, Object.class);
        JavaType valueTypeArg = JavaType.constructParametricType(Integer.class, Object.class);
        TypeBindings bindings = TypeBindings.create(erasedType, keyTypeArg, valueTypeArg);
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(keyTypeArg, bindings.findBoundType("K"));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(valueTypeArg, bindings.findBoundType("V"));
        assertNull(bindings.findBoundType("X"));
    }

    @Test
    public void testCreateBindingsWithSelfReference() {
        Class<?> erasedType = List.class;
        JavaType elementTypeArg = JavaType.constructParametricType(List.class, Object.class);
        TypeBindings parentBindings = TypeBindings.create(erasedType, elementTypeArg);
        JavaType selfTypeArg = JavaType.constructParametricType(List.class, TypeBindings.class);
        TypeBindings bindings = parentBindings.withUnboundVariable("T").withUnboundVariable("selfType");
        assertEquals(3, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(elementTypeArg, bindings.getBoundType(0));
        assertEquals("selfType", bindings.getBoundName(1));
        assertEquals(selfTypeArg, bindings.getBoundType(1));
    }

    @Test
    public void testCreateBindingsWithUnboundVariable() {
        Class<?> erasedType = List.class;
        JavaType elementTypeArg = JavaType.constructParametricType(List.class, Object.class);
        TypeBindings parentBindings = TypeBindings.create(erasedType, elementTypeArg);
        TypeBindings bindings = parentBindings.withUnboundVariable("T");
        assertEquals(1, bindings.size());
        assertNull(bindings.findBoundType("T"));
    }
}