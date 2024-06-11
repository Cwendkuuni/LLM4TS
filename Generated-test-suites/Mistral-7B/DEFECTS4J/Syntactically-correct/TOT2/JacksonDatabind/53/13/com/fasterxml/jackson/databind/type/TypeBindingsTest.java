import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testCreateEmpty() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertFalse(bindings.isEmpty());
        assertEquals("", bindings.toString());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateWithNull() {
        try {
            TypeBindings bindings = TypeBindings.create(null, NO_TYPES);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateWithEmptyList() {
        TypeBindings bindings = TypeBindings.create(MyList.class, NO_TYPES);
        assertEquals(MyList.class.getName(), bindings.getBoundName(0));
        assertEquals(JavaType.getCollectionType(MyList.class, JavaType.getTypeFactory().getRawClass(Integer.class)), bindings.getBoundType(0));
        assertEquals(1, bindings.size());
    }

    @Test
    public void testCreateWithOneTypeArgument() {
        TypeBindings bindings = TypeBindings.create(MyClassWithOneTypeArg.class, JavaType.getTypeFactory().constructType(Integer.class));
        assertEquals(MyClassWithOneTypeArg.class.getName(), bindings.getBoundName(0));
        assertEquals(JavaType.getTypeFactory().constructType(Integer.class), bindings.getBoundType(0));
        assertEquals(1, bindings.size());
    }

    @Test
    public void testCreateWithTwoTypeArguments() {
        TypeBindings bindings = TypeBindings.create(MyClassWithTwoTypeArgs.class, JavaType.getTypeFactory().constructType(String.class), JavaType.getTypeFactory().constructType(Integer.class));
        assertEquals(MyClassWithTwoTypeArgs.class.getName(), bindings.getBoundName(0));
        assertEquals(JavaType.getTypeFactory().constructType(String.class), bindings.getBoundType(0));
        assertEquals("T", bindings.getBoundName(1));
        assertEquals(JavaType.getTypeFactory().constructType(Integer.class), bindings.getBoundType(1));
        assertEquals(2, bindings.size());
    }

    @Test
    public void testCreateIfNeededWithNull() {
        try {
            TypeBindings bindings = TypeBindings.createIfNeeded(MyClassWithoutTypeArgs.class, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateIfNeededWithEmptyList() {
        TypeBindings bindings = TypeBindings.createIfNeeded(MyClassWithoutTypeArgs.class, NO_TYPES);
        assertNotNull(bindings);
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateIfNeededWithOneTypeArgument() {
        TypeBindings bindings = TypeBindings.createIfNeeded(MyClassWithOneTypeArg.class, JavaType.getTypeFactory().constructType(Integer.class));
        assertEquals(MyClassWithOneTypeArg.class.getName(), bindings.getBoundName(0));
        assertEquals(JavaType.getTypeFactory().constructType(Integer.class), bindings.getBoundType(0));
        assertEquals(1, bindings.size());
    }

    @Test
    public void testCreateIfNeededWithTwoTypeArguments() {
        TypeBindings bindings = TypeBindings.createIfNeeded(MyClassWithTwoTypeArgs.class, JavaType.getTypeFactory().constructType(String.class), JavaType.getTypeFactory().constructType(Integer.class));
        assertEquals(MyClassWithTwoTypeArgs.class.getName(), bindings.getBoundName(0));
        assertEquals(JavaType.getTypeFactory().constructType(String.class), bindings.getBoundType(0));
        assertEquals("T", bindings.getBoundName(1));
        assertEquals(JavaType.getTypeFactory().constructType(Integer.class), bindings.getBoundType(1));
        assertEquals(2, bindings.size());
    }

    @Test
    public void testFindBoundType() {
        TypeBindings bindings = TypeBindings.create(MyClassWithTwoTypeArgs.class, JavaType.getTypeFactory().constructType(String.class), JavaType.getTypeFactory().constructType(Integer.class));
        assertEquals(JavaType.getTypeFactory().constructType(String.class), bindings.findBoundType("T"));
        assertNull(bindings.findBoundType("Unknown"));
    }

    @Test   
    public void testWithUnboundVariable() {
        TypeBindings bindings = TypeBindings.create(MyClassWithRecursiveType.class, JavaType.getTypeFactory().constructType(MyClassWithRecursiveType.class));
        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("X");
        assertEquals(bindings._names, bindingsWithUnboundVariable._names);
        assertEquals(bindings._types, bindingsWithUnboundVariable._types);
        assertArrayEquals(new String[]{"X"}, bindingsWithUnboundVariable._unboundVariables);
    }

    private static class MyClassWithoutTypeArgs {
    }

    private static class MyClassWithOneTypeArg<T> {
        private T value;

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }

    private static class MyClassWithTwoTypeArgs<T, U> {
        private T value1;
        private U value2;

        public T getValue1() {
            return value1;
        }

        public void setValue1(T value1) {
            this.value1 = value1;
        }

        public U getValue2() {
            return value2;
        }

        public void setValue2(U value2) {
            this.value2 = value2;
        }
    }

    private static class MyList<T> extends AbstractList<T> {
    }
}