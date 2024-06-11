import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
        assertEquals(0, empty.size());
        assertNull(empty.findBoundType("NonexistentType"));
    }

    @Test
    public void testSingleTypeBinding() {
        JavaType javaType = JavaType.buildArrayType(int[].class);
        TypeBindings singleTypeBinding = TypeBindings.create(int[].class, javaType);
        assertFalse(singleTypeBinding.isEmpty());
        assertEquals(1, singleTypeBinding.size());
        assertEquals("int", singleTypeBinding.getBoundName(0));
        assertEquals(int[].class, singleTypeBinding.getBoundType(0));
        assertNull(singleTypeBinding.findBoundType("NonexistentType"));
    }

    @Test
    public void testMultipleTypeBinding() {
        JavaType javaType1 = JavaType.constructType(List.class, int.class);
        JavaType javaType2 = JavaType.constructType(List.class, String.class);
        TypeBindings multipleTypeBinding = TypeBindings.create(List.class, javaType1, javaType2);
        assertFalse(multipleTypeBinding.isEmpty());
        assertEquals(2, multipleTypeBinding.size());
        assertEquals("int", multipleTypeBinding.getBoundName(0));
        assertEquals(int.class, multipleTypeBinding.getBoundType(0));
        assertEquals("java.lang.String", multipleTypeBinding.getBoundName(1));
        assertEquals(String.class, multipleTypeBinding.getBoundType(1));
        assertNull(multipleTypeBinding.findBoundType("NonexistentType"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumberOfTypeArguments() {
        JavaType javaType = JavaType.constructType(List.class, int.class, String.class);
        TypeBindings multipleTypeBinding = TypeBindings.create(List.class, javaType);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testCreateIfNeeded() {
        JavaType javaType = JavaType.constructType(List.class, int.class);
        TypeBindings multipleTypeBinding = TypeBindings.createIfNeeded(List.class, javaType);
        assertFalse(multipleTypeBinding.isEmpty());
        assertEquals(1, multipleTypeBinding.size());
        assertEquals("int", multipleTypeBinding.getBoundName(0));
        assertEquals(int.class, multipleTypeBinding.getBoundType(0));
        assertNull(multipleTypeBinding.findBoundType("NonexistentType"));
    }

    @Test
    public void testCreateIfNeededWithEmptyTypeArguments() {
        TypeBindings empty = TypeBindings.createIfNeeded(List.class, null);
        assertTrue(empty.isEmpty());
        assertEquals(0, empty.size());
        assertNull(empty.findBoundType("NonexistentType"));
    }

    @Test
    public void testWithUnboundVariable() {
        JavaType javaType = JavaType.constructType(List.class, int.class, String.class);
        TypeBindings multipleTypeBinding = TypeBindings.create(List.class, javaType);
        TypeBindings withUnboundVariable = multipleTypeBinding.withUnboundVariable("UnboundVariable");
        assertFalse(withUnboundVariable.isEmpty());
        assertEquals(3, withUnboundVariable.size());
        assertEquals("int", withUnboundVariable.getBoundName(0));
        assertEquals(int.class, withUnboundVariable.getBoundType(0));
        assertEquals("java.lang.String", withUnboundVariable.getBoundName(1));
        assertEquals(String.class, withUnboundVariable.getBoundType(1));
        assertEquals("UnboundVariable", withUnboundVariable.getBoundName(2));
        assertNull(withUnboundVariable.findBoundType("NonexistentType"));
    }

    @Test
    public void testFindBoundType() {
        JavaType javaType1 = JavaType.constructType(List.class, int.class);
        JavaType javaType2 = JavaType.constructType(List.class, String.class);
        TypeBindings multipleTypeBinding = TypeBindings.create(List.class, javaType1, javaType2);
        assertEquals(int[].class, multipleTypeBinding.findBoundType("int"));
        assertEquals(String[].class, multipleTypeBinding.findBoundType("java.lang.String"));
        assertNull(multipleTypeBinding.findBoundType("java.lang.Boolean"));
    }
}