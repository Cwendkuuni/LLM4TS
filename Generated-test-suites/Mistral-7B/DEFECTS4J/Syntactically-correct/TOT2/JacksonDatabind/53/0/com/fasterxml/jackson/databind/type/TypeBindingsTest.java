import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertEquals(0, empty.size());
        assertNull(empty.findBoundType("EmptyTypeVariable"));
    }

    @Test
    public void testCreateWithOneTypeArgument() {
        TypeBindings oneArgBindings = TypeBindings.create(List.class, Integer.TYPE);
        assertEquals(1, oneArgBindings.size());
        assertEquals("java.lang.Integer", oneArgBindings.getBoundType(0).getClassName());
    }

    @Test
    public void testCreateWithTwoTypeArguments() {
        TypeBindings twoArgBindings = TypeBindings.create(Map.class, String.class, Integer.TYPE);
        assertEquals(2, twoArgBindings.size());
        assertEquals("java.lang.String", twoArgBindings.getBoundType(0).getClassName());
        assertEquals("java.lang.Integer", twoArgBindings.getBoundType(1).getClassName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedArguments() {
        TypeBindings twoArgBindings = TypeBindings.create(Map.class, String.class, Integer[].class);
        fail("Expected IllegalArgumentException but did not receive one.");
    }

    @Test
    public void testCreateIfNeededWithOneTypeArgument() {
        TypeBindings oneArgBindings = TypeBindings.createIfNeeded(List.class, Integer.TYPE);
        assertEquals(1, oneArgBindings.size());
        assertEquals("java.lang.Integer", oneArgBindings.getBoundType(0).getClassName());
    }

    @Test
    public void testCreateIfNeededWithNoTypeArguments() {
        TypeBindings noArgBindings = TypeBindings.createIfNeeded(HashMap.class, null);
        assertEquals(0, noArgBindings.size());
        assertNull(noArgBindings.findBoundType("EmptyTypeVariable"));
    }

    @Test
    public void testCreateIfNeededWithTwoTypeArguments() {
        TypeBindings twoArgBindings = TypeBindings.createIfNeeded(Map.class, String.class, Integer.TYPE);
        assertEquals(2, twoArgBindings.size());
        assertEquals("java.lang.String", twoArgBindings.getBoundType(0).getClassName());
        assertEquals("java.lang.Integer", twoArgBindings.getBoundType(1).getClassName());
    }

    @Test
    public void testFindBoundType() {
        TypeBindings twoArgBindings = TypeBindings.create(Map.class, String.class, Integer.TYPE);
        assertEquals("java.lang.String", twoArgBindings.findBoundType("KeyTypeVariable").getClassName());
        assertEquals("java.lang.Integer", twoArgBindings.findBoundType("ValueTypeVariable").getClassName());
        assertNull(twoArgBindings.findBoundType("UknownTypeVariable"));
    }

    @Test
    public void testIsEmpty() {
        TypeBindings emptyBindings = TypeBindings.emptyBindings();
        assertTrue(emptyBindings.isEmpty());

        TypeBindings nonEmptyBindings = TypeBindings.create(Map.class, String.class, Integer.TYPE);
        assertFalse(nonEmptyBindings.isEmpty());
    }

    @Test
    public void testHasUnbound() {
        String unboundVariable = "UnboundTypeVariable";

        TypeBindings emptyBindings = TypeBindings.emptyBindings();
        assertFalse(emptyBindings.hasUnbound(unboundVariable));

        TypeBindings withUnboundBindings = TypeBindings.create(Map.class, String.class, Integer.TYPE).withUnboundVariable(unboundVariable);
        assertTrue(withUnboundBindings.hasUnbound(unboundVariable));
    }

    @Test
    public void testToString() {
        TypeBindings oneArgBindings = TypeBindings.create(List.class, Integer.TYPE);
        assertEquals("<java.lang.Integer>", oneArgBindings.toString());

        TypeBindings twoArgBindings = TypeBindings.create(Map.class, String.class, Integer.TYPE);
        assertEquals("<java.lang.String, java.lang.Integer>", twoArgBindings.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        TypeBindings bindings1 = TypeBindings.create(List.class, Integer.TYPE);
        TypeBindings bindings2 = TypeBindings.create(List.class, Integer.TYPE);
        assertEquals(bindings1, bindings2);

        TypeBindings bindings3 = TypeBindings.create(ArrayList.class, Integer.TYPE);
        assertNotEquals(bindings1, bindings3);

        TypeBindings bindings4 = TypeBindings.create(List.class, String.TYPE);
        assertNotEquals(bindings1, bindings4);
    }
}