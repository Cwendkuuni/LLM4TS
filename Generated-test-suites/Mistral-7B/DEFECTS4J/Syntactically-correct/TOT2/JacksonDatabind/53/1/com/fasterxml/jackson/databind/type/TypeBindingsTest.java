import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
        assertEquals("[]", empty.toString());
    }

    @Test
    public void testCreate() {
        Class<List<String>> listOfStrings = List.class;
        TypeBindings listBindings = TypeBindings.create(listOfStrings, JavaType.constructCollectionType(List.class, String.class));
        assertEquals(1, listBindings.size());
        assertEquals("List<java.lang.String>", listBindings.toString());
        assertEquals("java.lang.String", listBindings.getBoundName(0));
        assertEquals(JavaType.constructCollectionType(List.class, String.class), listBindings.getBoundType(0));

        assertNull(listBindings.findBoundType("UnknownType"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidNumberOfTypeParams() {
        Class<List<String>> listOfStrings = List.class;
        JavaType[] types = new JavaType[3];
        TypeBindings bindings = TypeBindings.create(listOfStrings, types);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedTypeParams() {
        Class<Map<String, String>> mapOfStrings = Map.class;
        JavaType[] types = new JavaType[] { JavaType.constructTypeReference(int.class), JavaType.constructCollectionType(List.class, String.class) };
        TypeBindings bindings = TypeBindings.create(mapOfStrings, types);
    }

    @Test
    public void testCreateWithUnboundVariables() {
        Class<Map<String, Integer>> mapOfStringInt = Map.class;
        JavaType mapType = JavaType.constructTypeReference(mapOfStringInt, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(mapOfStringInt, mapType);
        String[] unboundVariables = bindings.getUnboundVariables();
        assertEquals(1, unboundVariables.length);
        assertEquals("K", unboundVariables[0]);
    }

    @Test
    public void testWithUnboundVariable() {
        Class<Map<String, Integer>> mapOfStringInt = Map.class;
        TypeBindings bindings = TypeBindings.create(mapOfStringInt, JavaType.constructTypeReference(mapOfStringInt, String.class, Integer.class));
        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("V");
        assertEquals(2, bindingsWithUnboundVariable.size());
        assertEquals("Map<java.lang.String,V>", bindingsWithUnboundVariable.toString());
    }
}