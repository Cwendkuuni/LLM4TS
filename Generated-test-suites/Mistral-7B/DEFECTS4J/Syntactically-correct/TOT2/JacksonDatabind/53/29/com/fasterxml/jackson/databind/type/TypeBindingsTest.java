import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testSingleTypeBinding() {
        Class<List<String>> listOfStrings = List.class;
        TypeVariable<?>[] variables = listOfStrings.getTypeParameters();
        String variableName = variables[0].getName();
        JavaType javaType = JavaType.constructParametricType(List.class, String.class);

        TypeBindings bindings = TypeBindings.create(listOfStrings, javaType);
        assertEquals(1, bindings.size());
        assertEquals(variableName, bindings.getBoundName(0));
        assertEquals(javaType, bindings.getBoundType(0));
        assertEquals(javaType, bindings.findBoundType(variableName));
    }

    @Test
    public void testMultipleTypeBinding() {
        Class<Map<String, Integer>> mapOfStringToInteger = Map.class;
        TypeVariable<?>[] variables = mapOfStringToInteger.getTypeParameters();
        String keyVariableName = variables[0].getName();
        String valueVariableName = variables[1].getName();
        JavaType keyJavaType = JavaType.constructParametricType(String.class, Object.class);
        JavaType valueJavaType = JavaType.constructParametricType(Integer.class, Object.class);

        TypeBindings bindings = TypeBindings.create(mapOfStringToInteger, keyJavaType, valueJavaType);
        assertEquals(2, bindings.size());
        assertEquals(keyVariableName, bindings.getBoundName(0));
        assertEquals(keyJavaType, bindings.getBoundType(0));
        assertEquals(valueVariableName, bindings.getBoundName(1));
        assertEquals(valueJavaType, bindings.getBoundType(1));
        assertEquals(keyJavaType, bindings.findBoundType(keyVariableName));
        assertEquals(valueJavaType, bindings.findBoundType(valueVariableName));
    }

    @Test
    public void testUnboundVariable() {
        Class<Map<String, Integer>> mapOfStringToInteger = Map.class;
        JavaType[] types = new JavaType[1];
        TypeBindings bindings = new TypeBindings(new String[]{}, types, null);

        TypeBindings newBindings = bindings.withUnboundVariable("K");
        String unboundVariableName = "K";
        JavaType keyJavaType = JavaType.constructRawType(String.class);

        assertEquals(1, newBindings.size() + 1);
        assertEquals(unboundVariableName, newBindings.getBoundName(newBindings.size() - 1));
        assertNull(newBindings.getBoundType(newBindings.size() - 1));
        assertEquals(keyJavaType, newBindings.findBoundType(unboundVariableName));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTypeBindingCreation() {
        Class<List<String>> listOfStrings = List.class;
        JavaType[] types = new JavaType[1];
        TypeBindings bindings = new TypeBindings(new String[]{}, types, null);

        TypeBindings newBindings = bindings.withUnboundVariable("K");
        String unboundVariableName = "K";
        JavaType keyJavaType = JavaType.constructParametricType(String.class, Object.class);

        try {
            TypeBindings bindingsWithTwoUnboundVariables = newBindings.withUnboundVariable("V");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            TypeBindings bindingsWithTwoTypes = TypeBindings.create(listOfStrings, keyJavaType, keyJavaType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            TypeBindings bindingsWithMismatchedNames = TypeBindings.create(listOfStrings, keyJavaType, JavaType.constructParametricType(String.class, String.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}